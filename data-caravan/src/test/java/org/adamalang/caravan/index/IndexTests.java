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
package org.adamalang.caravan.index;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.adamalang.caravan.data.DiskMetrics;
import org.adamalang.caravan.index.heaps.IndexedHeap;
import org.adamalang.common.metrics.NoOpMetricsFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class IndexTests {
  public void assertEquals(String expected, Index index) {
    Assert.assertEquals(expected, index.toString());
    ByteBuf buf = Unpooled.buffer();
    index.snapshot(buf);
    Index index2 = new Index();
    index2.load(buf);
    Assert.assertEquals(expected, index2.toString());
  }

  private AnnotatedRegion wrap(Region r, AtomicInteger tracker) {
    return new AnnotatedRegion(r.position, r.size, tracker.getAndIncrement(), 0L);
  }

  @Test
  public void flow() {
    Index index = new Index();
    assertEquals("", index);
    Heap heap = new IndexedHeap(1024);
    AtomicInteger tracker = new AtomicInteger(0);

    index.append(1L, wrap(heap.ask(100), tracker));
    index.append(1L, wrap(heap.ask(100), tracker));
    assertEquals("1=[0,100=0][100,200=1];", index);
    index.append(2L, wrap(heap.ask(100), tracker));
    index.append(2L, wrap(heap.ask(100), tracker));
    assertEquals("1=[0,100=0][100,200=1];2=[200,300=2][300,400=3];", index);
    index.append(3L, wrap(heap.ask(100), tracker));
    index.append(3L, wrap(heap.ask(100), tracker));
    assertEquals("1=[0,100=0][100,200=1];2=[200,300=2][300,400=3];3=[400,500=4][500,600=5];", index);

    index.append(4L, wrap(heap.ask(1), tracker));
    index.append(4L, wrap(heap.ask(2), tracker));
    index.append(4L, wrap(heap.ask(3), tracker));
    index.append(4L, wrap(heap.ask(4), tracker));
    assertEquals("1=[0,100=0][100,200=1];2=[200,300=2][300,400=3];3=[400,500=4][500,600=5];4=[600,601=6][601,603=7][603,606=8][606,610=9];", index);

    for (Region region : index.trim(4L, 3)) {
      heap.free(region);
    }
    assertEquals("1=[0,100=0][100,200=1];2=[200,300=2][300,400=3];3=[400,500=4][500,600=5];4=[601,603=7][603,606=8][606,610=9];", index);

    Assert.assertTrue(index.exists(3));
    for (Region region : index.delete(3)) {
      heap.free(region);
    }
    Assert.assertFalse(index.exists(3));
    assertEquals("1=[0,100=0][100,200=1];2=[200,300=2][300,400=3];4=[601,603=7][603,606=8][606,610=9];", index);

    for (Region region : index.delete(4)) {
      heap.free(region);
    }
    for (Region region : index.delete(2)) {
      heap.free(region);
    }
    for (Region region : index.delete(1)) {
      heap.free(region);
    }
    assertEquals("", index);
    Assert.assertEquals("[0,1024)", heap.toString());
    Assert.assertNull(index.trim(500, 1));
  }

  @Test
  public void histogram() {
    Index index = new Index();
    long at = 0;
    int size = 1;
    for (int pow10 = 0; pow10 < 9; pow10++) {
      index.append(1, new AnnotatedRegion(at, size, 0, 0));
      at += size;
      size *= 10;
    }
    index.report(new DiskMetrics(new NoOpMetricsFactory()));
  }
}
