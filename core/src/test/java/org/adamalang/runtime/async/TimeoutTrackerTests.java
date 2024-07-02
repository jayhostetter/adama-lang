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
package org.adamalang.runtime.async;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.reactives.RxInt64;
import org.junit.Assert;
import org.junit.Test;

public class TimeoutTrackerTests {

  @Test
  public void save_load() {
    RxInt64 time = new RxInt64(null, 0);
    TimeoutTracker tracker = new TimeoutTracker(time);
    {
      JsonStreamWriter writer = new JsonStreamWriter();
      tracker.dump(writer);
      Assert.assertEquals("", writer.toString());
    }
    {
      tracker.timeouts.put(42, new Timeout(124, 52.4));
      JsonStreamWriter writer = new JsonStreamWriter();
      tracker.dump(writer);
      Assert.assertEquals("\"__timeouts\":{\"42\":{\"timestamp\":\"124\",\"timeout\":52.4}}", writer.toString());
    }
    {
      tracker.timeouts.put(100, new Timeout(1, 13.69));
      JsonStreamWriter writer = new JsonStreamWriter();
      tracker.dump(writer);
      Assert.assertEquals("\"__timeouts\":{\"100\":{\"timestamp\":\"1\",\"timeout\":13.69},\"42\":{\"timestamp\":\"124\",\"timeout\":52.4}}", writer.toString());
    }
    Assert.assertEquals(2, tracker.timeouts.size());
    tracker.hydrate(new JsonStreamReader("{\"100\":null}"));
    Assert.assertEquals(1, tracker.timeouts.size());
    Assert.assertFalse(tracker.timeouts.containsKey(100));
    tracker.hydrate(new JsonStreamReader("{\"1000\":{\"timestamp\":\"2\",\"timeout\":3.14}}"));
    Assert.assertEquals(2, tracker.timeouts.size());
    Assert.assertTrue(tracker.timeouts.containsKey(1000));
    Timeout to = tracker.timeouts.get(1000);
    Assert.assertEquals(2, to.timestamp);
    Assert.assertEquals(3.14, to.timeoutSeconds, 0.1);
  }

  @Test
  public void commit() {
    RxInt64 time = new RxInt64(null, 0);
    TimeoutTracker tracker = new TimeoutTracker(time);
    {
      Timeout a = tracker.create(42, 3.13);
      Timeout b = tracker.create(42, 3.13);
      Assert.assertTrue(a == b); // dedupes
      JsonStreamWriter forward = new JsonStreamWriter();
      JsonStreamWriter reverse = new JsonStreamWriter();
      tracker.commit(forward, reverse);
      Assert.assertEquals("\"__timeouts\":{\"42\":{\"timestamp\":\"0\",\"timeout\":3.13}}", forward.toString());
      Assert.assertEquals("\"__timeouts\":{\"42\":null}", reverse.toString());
    }
    {
      tracker.create(111, 4.2);
      JsonStreamWriter forward = new JsonStreamWriter();
      JsonStreamWriter reverse = new JsonStreamWriter();
      tracker.nuke(forward, reverse);
      Assert.assertEquals("\"__timeouts\":{\"42\":null,\"111\":null}", forward.toString());
      Assert.assertEquals("\"__timeouts\":{\"42\":{\"timestamp\":\"0\",\"timeout\":3.13},\"111\":{\"timestamp\":\"0\",\"timeout\":4.2}}", reverse.toString());
    }
    tracker.create(123, 6.9);
    tracker.revert();
  }

  @Test
  public void temporal_logic_first() {
    RxInt64 time = new RxInt64(null, 1000);
    TimeoutTracker tracker = new TimeoutTracker(time);
    RxInt64 next = new RxInt64(null, 0);
    Assert.assertFalse(tracker.needsInvalidationAndUpdateNext(next));
    tracker.create(12, 5.25);
    Assert.assertTrue(tracker.needsInvalidationAndUpdateNext(next));
    Assert.assertEquals(6250L, next.get().longValue());
  }

  @Test
  public void temporal_logic_closest() {
    RxInt64 time = new RxInt64(null, 1000);
    TimeoutTracker tracker = new TimeoutTracker(time);
    RxInt64 next = new RxInt64(null, 1750);
    Assert.assertFalse(tracker.needsInvalidationAndUpdateNext(next));
    tracker.create(12, 5.25);
    Assert.assertTrue(tracker.needsInvalidationAndUpdateNext(next));
    Assert.assertEquals(1750L, next.get().longValue());
  }

  @Test
  public void temporal_logic_first_pick_closest() {
    RxInt64 time = new RxInt64(null, 1000);
    TimeoutTracker tracker = new TimeoutTracker(time);
    RxInt64 next = new RxInt64(null, 0);
    Assert.assertFalse(tracker.needsInvalidationAndUpdateNext(next));
    tracker.create(12, 5.25);
    tracker.create(102, 1.25);
    Assert.assertTrue(tracker.needsInvalidationAndUpdateNext(next));
    Assert.assertEquals(2250L, next.get().longValue());
  }

  @Test
  public void clearNull() {
    RxInt64 time = new RxInt64(null, 1000);
    TimeoutTracker tracker = new TimeoutTracker(time);
    tracker.hydrate(new JsonStreamReader("null"));
  }
}
