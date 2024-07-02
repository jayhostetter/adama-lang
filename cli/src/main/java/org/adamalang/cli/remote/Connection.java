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
package org.adamalang.cli.remote;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.Json;
import org.adamalang.web.client.socket.WebClientConnection;
import org.adamalang.web.contracts.WebJsonStream;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/** a single connection to a remote Web Proxy host */
public class Connection implements AutoCloseable {
  private final WebClientConnection connection;

  public Connection(WebClientConnection connection) {
    this.connection = connection;
  }

  public WebClientConnection raw() {
    return connection;
  }

  public void stream(ObjectNode request, BiConsumer<Integer, ObjectNode> stream) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Exception> failure = new AtomicReference<>(null);
    connection.execute(request, new WebJsonStream() {
      @Override
      public void data(int cId, ObjectNode node) {
        stream.accept(cId, node);
      }

      @Override
      public void complete() {
        latch.countDown();
      }

      @Override
      public void failure(int code) {
        failure.set(new ErrorCodeException(code));
        latch.countDown();
      }
    });
    latch.await(120000, TimeUnit.MILLISECONDS);
    if (failure.get() != null) {
      throw failure.get();
    }
  }

  /** An object that is either an ObjectNode or an Exception */
  public static class IdObject {
    public final int id;
    public final Object value;

    public IdObject(int id, Object value) {
      this.id = id;
      this.value = value;
    }

    public ObjectNode node() throws Exception {
      if (value instanceof ObjectNode) {
        return (ObjectNode) value;
      }
      throw (Exception) value;
    }
  }

  /** use a blocking queue to interact with a stream */
  public BlockingDeque<IdObject> stream_queue(ObjectNode request) {
    BlockingDeque<IdObject> queue = new LinkedBlockingDeque<>();
    connection.execute(request, new WebJsonStream() {
      @Override
      public void data(int cId, ObjectNode node) {
        queue.offer(new IdObject(cId, node));
      }

      @Override
      public void complete() {
      }

      @Override
      public void failure(int code) {
        queue.offer(new IdObject(-1, new ErrorCodeException(code)));
      }
    });
    return queue;
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }

  public ObjectNode execute(ObjectNode request) throws Exception {
    AtomicReference<Object> value = new AtomicReference<>(null);
    CountDownLatch latch = new CountDownLatch(1);
    connection.execute(request, new WebJsonStream() {
      @Override
      public void data(int cId, ObjectNode node) {
        value.set(node);
      }

      @Override
      public void complete() {
        latch.countDown();
      }

      @Override
      public void failure(int code) {
        value.set(new ErrorCodeException(code));
        latch.countDown();
      }
    });
    if (latch.await(10 * 60000, TimeUnit.MILLISECONDS)) {
      if (value.get() != null) {
        if (value.get() instanceof ObjectNode) {
          return (ObjectNode) value.get();
        } else {
          throw (ErrorCodeException) value.get();
        }
      }
      return Json.newJsonObject();
    } else {
      throw new Exception("timed out");
    }
  }
}
