/*
* Adama Platform and Language
* Copyright (C) 2021 - 2024 by Adama Platform Engineering, LLC
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published
* by the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Affero General Public License for more details.
* 
* You should have received a copy of the GNU Affero General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.adamalang.net.mocks;

import org.adamalang.common.AwaitHelper;
import org.adamalang.common.NamedRunnable;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.common.SimpleExecutorFactory;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectiveExecutorFactory implements SimpleExecutorFactory, SimpleExecutor {
  private final String name;
  private final SimpleExecutor executor;
  private final ArrayList<Runnable> backlog;
  private String prefixBlocking;
  private boolean blocked;
  private CountDownLatch latch;
  private final AtomicInteger inflight;

  public SelectiveExecutorFactory(String name) {
    this.name = name;
    this.executor = SimpleExecutor.create("SelectiveExecutorFactory");
    this.backlog = new ArrayList<>();
    this.prefixBlocking = null;
    this.latch = null;
    this.blocked = false;
    this.inflight = new AtomicInteger(0);
  }

  public synchronized Runnable pauseOn(String prefix) {
    System.err.println("INSPECTING["+this.name+"]:" + prefix);
    this.prefixBlocking = prefix;
    this.latch = new CountDownLatch(1);
    return () -> {
      AwaitHelper.block(latch, 50000);
    };
  }

  public synchronized void unpause() {
    this.blocked = false;
    for (Runnable action : backlog) {
      action.run();
    }
    backlog.clear();
  }

  public void flush() throws Exception {
    boolean once = true;
    while (inflight.get() > 0 || once) {
      once = false;
      CountDownLatch latch = new CountDownLatch(1);
      executor.execute(new NamedRunnable("flush") {
        @Override
        public void execute() throws Exception {
          latch.countDown();
        }
      });
      Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS));
      if (inflight.get() > 0) {
        Thread.sleep(10);
      }
    }
  }

  public synchronized void once() {
    backlog.remove(0).run();
  }

  public synchronized Runnable extract() {
    return backlog.remove(0);
  }

  private void test(NamedRunnable command) {
    if (prefixBlocking != null) {
      if (command.__runnableName.startsWith(prefixBlocking)) {
        System.err.println("PAUSED["+this.name+"]:" + command.__runnableName);
        this.blocked = true;
        this.prefixBlocking = null;
        this.latch.countDown();
      } else {
        System.err.println("CHECKED["+this.name+"]:" + command.__runnableName);
      }
    } else {
      System.err.println("SKIP["+this.name+"]:" + command.__runnableName);
    }
  }

  @Override
  public synchronized void execute(NamedRunnable command) {
    test(command);
    if (blocked) {
      System.err.println("BACKLOG["+this.name+"]:" + command.__runnableName);
      backlog.add(() -> {
        System.err.println("EXECUTE["+this.name+"]:" + command.__runnableName);
        executor.execute(command);
      });
      return;
    }
    executor.execute(command);
  }

  @Override
  public synchronized Runnable schedule(NamedRunnable command, long milliseconds) {
    inflight.incrementAndGet();
    NamedRunnable tracked = new NamedRunnable(command.__runnableName) {
      @Override
      public void execute() throws Exception {
        try {
          command.execute();
        } finally {
          inflight.getAndDecrement();
        }
      }
    };
    test(tracked);
    if (blocked) {
      System.err.println("BACKLOG["+this.name+"]:" + tracked.__runnableName);
      backlog.add(() -> {
        System.err.println("EXECUTE["+this.name+"]:" + tracked.__runnableName);
        executor.schedule(tracked, milliseconds);
      });
      return () -> {};
    }
    return executor.schedule(tracked, milliseconds);
  }

  @Override
  public Runnable scheduleNano(NamedRunnable command, long nanoseconds) {
    return this.schedule(command, nanoseconds / 1000000);
  }

  @Override
  public CountDownLatch shutdown() {
    return executor.shutdown();
  }

  @Override
  public SimpleExecutor makeSingle(String name) {
    return new SimpleExecutor() {
      @Override
      public void execute(NamedRunnable command) {
        SelectiveExecutorFactory.this.execute(command);
      }

      @Override
      public Runnable schedule(NamedRunnable command, long milliseconds) {
        return SelectiveExecutorFactory.this.schedule(command, milliseconds);
      }

      @Override
      public Runnable scheduleNano(NamedRunnable command, long nanoseconds) {
        return this.schedule(command, nanoseconds);
      }

      @Override
      public CountDownLatch shutdown() {
        return new CountDownLatch(0);
      }
    };
  }

  @Override
  public SimpleExecutor[] makeMany(String name, int nThreads) {
    SimpleExecutor[] many = new SimpleExecutor[nThreads];
    for (int k = 0; k < nThreads; k++) {
      many[k] = makeSingle(null);
    }
    return many;
  }
}
