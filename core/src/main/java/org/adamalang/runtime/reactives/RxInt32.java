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

import org.adamalang.runtime.contracts.CanGetAndSet;
import org.adamalang.runtime.contracts.RxParent;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;

/** a reactive 32-bit integer (int) */
public class RxInt32 extends RxIndexableBase implements Comparable<RxInt32>, CanGetAndSet<Integer> {
  protected int backup;
  protected int value;

  public RxInt32(final RxParent parent, final int value) {
    super(parent);
    backup = value;
    this.value = value;
  }

  @Override
  public void __commit(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    if (__isDirty()) {
      forwardDelta.writeObjectFieldIntro(name);
      forwardDelta.writeInteger(value);
      reverseDelta.writeObjectFieldIntro(name);
      reverseDelta.writeInteger(backup);
      backup = value;
      __lowerDirtyCommit();
    }
  }

  @Override
  public void __dump(final JsonStreamWriter writer) {
    writer.writeInteger(value);
  }

  @Override
  public void __insert(final JsonStreamReader reader) {
    backup = reader.readInteger();
    value = backup;
  }

  @Override
  public void __patch(JsonStreamReader reader) {
    set(reader.readInteger());
  }

  @Override
  public void __revert() {
    if (__isDirty()) {
      value = backup;
      __lowerDirtyRevert();
    }
  }

  @Override
  public long __memory() {
    return super.__memory() + 8;
  }

  public int bumpDownPost() {
    final var result = value--;
    __raiseDirty();
    return result;
  }

  public int bumpDownPre() {
    final var result = --value;
    __raiseDirty();
    return result;
  }

  public int bumpUpPost() {
    final var result = value++;
    __raiseDirty();
    return result;
  }

  // these make ZERO sense
  public int bumpUpPre() {
    final var result = ++value;
    __raiseDirty();
    return result;
  }

  @Override
  public int compareTo(final RxInt32 other) {
    return Integer.compare(value, other.value);
  }

  public void forceSet(final int id) {
    backup = id;
    value = id;
  }

  @Override
  public Integer get() {
    return value;
  }

  @Override
  public void set(final Integer value) {
    if (this.value != value) {
      trigger();
      this.value = value;
      trigger();
      __raiseDirty();
    }
  }

  @Override
  public int getIndexValue() {
    return value;
  }

  public int opAddTo(final int incoming) {
    if (incoming != 0) {
      trigger();
      value += incoming;
      trigger();
      __raiseDirty();
    }
    return value;
  }

  public int opMultBy(final int x) {
    if (x != 1) {
      trigger();
      value *= x;
      trigger();
      __raiseDirty();
    }
    return value;
  }
}
