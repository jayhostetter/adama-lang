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
package org.adamalang.runtime.reactives;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.mocks.MockRxChild;
import org.adamalang.runtime.mocks.MockRxParent;
import org.junit.Assert;
import org.junit.Test;

public class RxInt32Tests {
  @Test
  public void memory() {
    final var i1 = new RxInt32(null, 1);
    final var i2 = new RxInt32(null, 2);
    Assert.assertEquals(48, i1.__memory());
    Assert.assertEquals(48, i2.__memory());
  }

  @Test
  public void compare() {
    final var i1 = new RxInt32(null, 1);
    final var i2 = new RxInt32(null, 2);
    Assert.assertEquals(-1, i1.compareTo(i2));
    Assert.assertEquals(1, i2.compareTo(i1));
  }

  @Test
  public void dump() {
    final var d = new RxInt32(null, 42);
    JsonStreamWriter writer = new JsonStreamWriter();
    d.__dump(writer);
    Assert.assertEquals("42", writer.toString());
  }

  @Test
  public void dirty_and_commit() {
    final var parent = new MockRxParent();
    final var i = new RxInt32(parent, 42);
    Assert.assertEquals(42, (int) i.get());
    i.set(50);
    parent.assertDirtyCount(1);
    i.set(60);
    parent.assertDirtyCount(1);
    final var writer = new JsonStreamWriter();
    final var reverse = new JsonStreamWriter();
    i.__commit("v", writer, reverse);
    Assert.assertEquals("\"v\":60", writer.toString());
    Assert.assertEquals("\"v\":42", reverse.toString());
    final var writerAgain = new JsonStreamWriter();
    final var reverseAgain = new JsonStreamWriter();
    i.__commit("v2", writerAgain, reverseAgain);
    Assert.assertEquals("", writerAgain.toString());
    Assert.assertEquals("", reverseAgain.toString());
  }

  @Test
  public void forceSet() {
    final var d = new RxInt32(null, 1);
    d.forceSet(42);
    Assert.assertEquals(42, (int) d.get());
  }

  @Test
  public void insert() {
    final var d = new RxInt32(null, 1);
    d.__insert(new JsonStreamReader("42"));
    Assert.assertEquals(42, (int) d.get());
  }

  @Test
  public void patch() {
    final var d = new RxInt32(null, 1);
    d.__patch(new JsonStreamReader("42"));
    Assert.assertEquals(42, (int) d.get());
  }

  @Test
  public void invalidate_and_revert() {
    final var i = new RxInt32(null, 42);
    final var child = new MockRxChild();
    i.__subscribe(child);
    i.set(50);
    child.assertInvalidateCount(1);
    i.set(50);
    child.assertInvalidateCount(1);
    i.set(55);
    child.assertInvalidateCount(1);
    Assert.assertEquals(55, i.getIndexValue());
    i.__revert();
    child.assertInvalidateCount(1);
    Assert.assertEquals(42, (int) i.get());
    i.__raiseDirty();
    i.__revert();
    i.__revert();
    child.assertInvalidateCount(2);
    i.__revert();
    child.assertInvalidateCount(2);
  }

  @Test
  public void ops() {
    final var i = new RxInt32(null, 1);
    i.bumpUpPre();
    Assert.assertEquals(2, (int) i.get());
    i.bumpUpPost();
    Assert.assertEquals(3, (int) i.get());
    i.bumpDownPre();
    Assert.assertEquals(2, (int) i.get());
    i.bumpDownPost();
    Assert.assertEquals(1, (int) i.get());
    i.opAddTo(10);
    Assert.assertEquals(11, (int) i.get());
    i.opMultBy(2);
    Assert.assertEquals(22, (int) i.get());
    i.opAddTo(-7);
    Assert.assertEquals(15, (int) i.get());
  }
}
