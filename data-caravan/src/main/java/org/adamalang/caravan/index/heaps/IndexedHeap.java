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
package org.adamalang.caravan.index.heaps;

import io.netty.buffer.ByteBuf;
import org.adamalang.caravan.index.Heap;
import org.adamalang.caravan.index.Region;
import org.adamalang.caravan.index.Report;

import java.util.*;

public class IndexedHeap implements Heap {
  public final long maximumSize;

  private final HashMap<Long, FreeSpace> left;
  private final HashMap<Long, FreeSpace> right;
  private final TreeMap<Long, TreeMap<Long, FreeSpace>> sized;

  @Override
  public void report(Report report) {
    report.addTotal(maximumSize);
    for (FreeSpace fs : left.values()) {
      report.addFree(fs.size);
    }
  }

  /** construct the heap as empty with the given maximum size available */
  public IndexedHeap(long maximumSize) {
    this.maximumSize = maximumSize;
    this.left = new HashMap<>();
    this.right = new HashMap<>();
    this.sized = new TreeMap<>();
    FreeSpace head = new FreeSpace();
    head.start = 0;
    head.size = maximumSize;
    add(head);
  }

  private void add(FreeSpace space) {
    left.put(space.start, space);
    right.put(space.start + space.size, space);
    TreeMap<Long, FreeSpace> bucket = sized.get(space.size);
    if (bucket == null) {
      bucket = new TreeMap<>();
      sized.put(space.size, bucket);
    }
    bucket.put(space.start, space);
  }

  @Override
  public long available() {
    long avail = 0;
    for (FreeSpace free : left.values()) {
      avail += free.size;
    }
    return avail;
  }

  @Override
  public long max() {
    return maximumSize;
  }

  @Override
  public Region ask(int size) {
    Map.Entry<Long, TreeMap<Long, FreeSpace>> bucket = sized.ceilingEntry((long) size);
    if (bucket == null) {
      return null;
    }
    Map.Entry<Long, FreeSpace> first = bucket.getValue().firstEntry();
    FreeSpace space = first.getValue();
    remove(space);

    Region region = new Region(space.start, size);
    space.start += size;
    space.size -= size;
    add(space);
    return region;
  }

  private void remove(FreeSpace space) {
    left.remove(space.start);
    right.remove(space.start + space.size);
    TreeMap<Long, FreeSpace> bucket = sized.get(space.size);
    bucket.remove(space.start);
    if (bucket.size() == 0) {
      sized.remove(space.size);
    }
  }

  @Override
  public void free(Region region) {
    FreeSpace byLeft = left.get(region.position + region.size);
    if (byLeft != null) {
      remove(byLeft);
    }
    FreeSpace byRight = right.get(region.position);
    if (byRight != null) {
      remove(byRight);
    }
    if (byLeft != null && byRight != null) {
      // ByRight + Region + ByLeft
      FreeSpace newOne = new FreeSpace();
      newOne.start = byRight.start;
      newOne.size = byRight.size + region.size + byLeft.size;
      add(newOne);
    } else if (byLeft != null && byRight == null) {
      // __ + Region + ByLeft
      byLeft.size += region.size;
      byLeft.start -= region.size;
      add(byLeft);
    } else if (byLeft == null && byRight != null) {
      // ByRight + Region + __
      byRight.size += region.size;
      add(byRight);
    } else {
      // __ + Region + __
      FreeSpace newOne = new FreeSpace();
      newOne.start = region.position;
      newOne.size = region.size;
      add(newOne);
    }
  }

  @Override
  public void snapshot(ByteBuf buf) {
    for (FreeSpace current : left.values()) {
      buf.writeBoolean(true);
      buf.writeLongLE(current.start);
      buf.writeLongLE(current.size);
    }
    buf.writeBoolean(false);
  }

  @Override
  public void load(ByteBuf buf) {
    left.clear();
    right.clear();
    sized.clear();
    while (buf.readBoolean()) {
      FreeSpace current = new FreeSpace();
      current.start = buf.readLongLE();
      current.size = buf.readLongLE();
      add(current);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    TreeSet<FreeSpace> items = new TreeSet<>(left.values());
    for (FreeSpace current : items) {
      sb.append(current.toString());
    }
    return sb.toString();
  }

  /** a mapping of free space */
  private class FreeSpace implements Comparable<FreeSpace> {
    private long start;
    private long size;

    @Override
    public int hashCode() {
      return Objects.hash(start, size);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      FreeSpace freeSpace = (FreeSpace) o;
      return start == freeSpace.start && size == freeSpace.size;// && Objects.equals(prior, freeSpace.prior) && Objects.equals(next, freeSpace.next);
    }

    @Override
    public String toString() {
      return "[" + start + "," + (start + size) + ")";
    }

    @Override
    public int compareTo(FreeSpace o) {
      return Long.compare(start, o.start);
    }
  }
}
