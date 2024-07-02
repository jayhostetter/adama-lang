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
import org.adamalang.runtime.natives.NtDynamic;
import org.junit.Assert;
import org.junit.Test;

public class RxDynamicTests {
  private static final NtDynamic A = new NtDynamic("true");
  private static final NtDynamic B = new NtDynamic("false");

  @Test
  public void memory() {
    final var a = new RxDynamic(null, A);
    final var b = new RxDynamic(null, B);
    Assert.assertEquals(72, a.__memory());
    Assert.assertEquals(76, b.__memory());
  }

  @Test
  public void compare() {
    final var a = new RxDynamic(null, A);
    final var b = new RxDynamic(null, B);
    Assert.assertEquals(14, a.compareTo(b));
    Assert.assertEquals(-14, b.compareTo(a));
  }

  @Test
  public void dirty_and_commit() {
    final var parent = new MockRxParent();
    final var c = new RxDynamic(parent, NtDynamic.NULL);
    parent.assertDirtyCount(0);
    c.set(A);
    parent.assertDirtyCount(1);
    c.set(A);
    parent.assertDirtyCount(1);
    final var writer = new JsonStreamWriter();
    final var reverse = new JsonStreamWriter();
    c.__commit("v", writer, reverse);
    Assert.assertEquals("\"v\":true", writer.toString());
    Assert.assertEquals("\"v\":null", reverse.toString());
    final var writerAgain = new JsonStreamWriter();
    final var reverseAgain = new JsonStreamWriter();
    c.__commit("v2", writerAgain, reverseAgain);
    Assert.assertEquals("", writerAgain.toString());
    Assert.assertEquals("", reverseAgain.toString());
  }

  @Test
  public void dumpA() {
    final var d = new RxDynamic(null, A);
    final var writer = new JsonStreamWriter();
    d.__dump(writer);
    Assert.assertEquals("true", writer.toString());
  }

  @Test
  public void dumpB() {
    final var d = new RxDynamic(null, B);
    final var writer = new JsonStreamWriter();
    d.__dump(writer);
    Assert.assertEquals("false", writer.toString());
  }

  @Test
  public void insert() {
    final var c = new RxDynamic(null, NtDynamic.NULL);
    c.__insert(new JsonStreamReader("{\"x\":\"foo\"}"));
    final var g = c.get();
    Assert.assertEquals("{\"x\":\"foo\"}", g.json);
  }

  @Test
  public void patch() {
    final var c = new RxDynamic(null, NtDynamic.NULL);
    c.__patch(new JsonStreamReader("{\"x\":\"foo\"}"));
    final var g = c.get();
    Assert.assertEquals("{\"x\":\"foo\"}", g.json);
  }

  @Test
  public void invalidate_and_revert() {
    final var c = new RxDynamic(null, NtDynamic.NULL);
    final var child = new MockRxChild();
    c.__subscribe(child);
    child.assertInvalidateCount(0);
    c.set(A);
    child.assertInvalidateCount(1);
    c.set(A);
    Assert.assertEquals(A, c.get());
    child.assertInvalidateCount(1);
    c.set(B);
    Assert.assertEquals(B, c.get());
    child.assertInvalidateCount(1);
    c.__revert();
    child.assertInvalidateCount(1);
    Assert.assertEquals(NtDynamic.NULL, c.get());
    c.__raiseDirty();
    c.__revert();
    c.__revert();
    child.assertInvalidateCount(2);
  }

  @Test
  public void seq() {
    final var c = new RxDynamic(null, NtDynamic.NULL);
    c.set(new NtDynamic("{\"x\":123,\"y\":42}"));
    {
      JsonStreamWriter redo = new JsonStreamWriter();
      JsonStreamWriter undo = new JsonStreamWriter();
      c.__commit("x", redo, undo);
      Assert.assertEquals("\"x\":{\"x\":123,\"y\":42}", redo.toString());
      Assert.assertEquals("\"x\":null", undo.toString());
    }
    c.set(new NtDynamic("{\"x\":42,\"z\":42}"));
    {
      JsonStreamWriter redo = new JsonStreamWriter();
      JsonStreamWriter undo = new JsonStreamWriter();
      c.__commit("x", redo, undo);
      Assert.assertEquals("\"x\":{\"x\":42,\"z\":42,\"y\":null}", redo.toString());
      Assert.assertEquals("\"x\":{\"x\":123,\"y\":42,\"z\":null}", undo.toString());
    }


  }
}
