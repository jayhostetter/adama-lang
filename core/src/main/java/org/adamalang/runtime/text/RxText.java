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
package org.adamalang.runtime.text;

import org.adamalang.runtime.contracts.RxParent;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.natives.NtDynamic;
import org.adamalang.runtime.reactives.RxBase;
import org.adamalang.runtime.reactives.RxInt32;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/** a text field within a document */
public class RxText extends RxBase {
  private final RxInt32 gen;
  private Text backup;
  private Text value;

  public RxText(final RxParent parent, RxInt32 gen) {
    super(parent);
    this.gen = gen;
    this.backup = new Text(gen.bumpUpPost());
    this.value = this.backup;
  }

  public Text current() {
    return value;
  }

  @Override
  public void __commit(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    if (__isDirty()) {
      if (backup == value && value.uncommitedChanges.size() > 0 && !value.upgraded) {
        __commitJustChanges(name, forwardDelta, reverseDelta);
      } else {
        __commitFullDiff(name, forwardDelta, reverseDelta);
      }
      backup = value;
      __lowerDirtyCommit();
    }
  }

  private void __commitJustChanges(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    forwardDelta.writeObjectFieldIntro(name);
    forwardDelta.beginObject();
    forwardDelta.writeObjectFieldIntro("changes");
    forwardDelta.beginObject();
    reverseDelta.writeObjectFieldIntro(name);
    reverseDelta.beginObject();
    reverseDelta.writeObjectFieldIntro("changes");
    reverseDelta.beginObject();
    for (Map.Entry<Integer, String> change : value.uncommitedChanges.entrySet()) {
      forwardDelta.writeObjectFieldIntro(change.getKey());
      forwardDelta.injectJson(change.getValue());
      reverseDelta.writeObjectFieldIntro(change.getKey());
      reverseDelta.writeNull();
    }
    value.commit();
    forwardDelta.endObject();
    forwardDelta.endObject();
    reverseDelta.endObject();
    reverseDelta.endObject();
  }

  private void __commitFullDiff(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    forwardDelta.writeObjectFieldIntro(name);
    forwardDelta.beginObject();
    reverseDelta.writeObjectFieldIntro(name);
    reverseDelta.beginObject();

    forwardDelta.writeObjectFieldIntro("fragments");
    reverseDelta.writeObjectFieldIntro("fragments");
    forwardDelta.beginObject();
    reverseDelta.beginObject();
    __commitDiffMap(backup.fragments, value.fragments, forwardDelta, reverseDelta);
    forwardDelta.endObject();
    reverseDelta.endObject();

    forwardDelta.writeObjectFieldIntro("order");
    reverseDelta.writeObjectFieldIntro("order");
    forwardDelta.beginObject();
    reverseDelta.beginObject();
    __commitDiffMap(backup.order, value.order, forwardDelta, reverseDelta);
    forwardDelta.endObject();
    reverseDelta.endObject();

    value.commit();

    forwardDelta.writeObjectFieldIntro("changes");
    reverseDelta.writeObjectFieldIntro("changes");
    forwardDelta.beginObject();
    reverseDelta.beginObject();
    __commitDiffMap(backup.changes, value.changes, forwardDelta, reverseDelta);
    forwardDelta.endObject();
    reverseDelta.endObject();

    if (backup.seq != value.seq) {
      forwardDelta.writeObjectFieldIntro("seq");
      forwardDelta.writeInteger(value.seq);
      reverseDelta.writeObjectFieldIntro("seq");
      reverseDelta.writeInteger(backup.seq);
    }

    if (backup.gen != value.gen) {
      forwardDelta.writeObjectFieldIntro("gen");
      forwardDelta.writeInteger(value.gen);
      reverseDelta.writeObjectFieldIntro("gen");
      reverseDelta.writeInteger(backup.gen);
    }

    forwardDelta.endObject();
    reverseDelta.endObject();

    backup = value;
  }

  private static <T> void __commitDiffMap(HashMap<T, String> prior, HashMap<T, String> next, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    HashSet<T> nuke = new HashSet<>(prior.keySet());
    for (Map.Entry<T, String> entryNew : next.entrySet()) {
      nuke.remove(entryNew.getKey());
      String old = prior.get(entryNew.getKey());
      if (!entryNew.getValue().equals(old)) {
        forwardDelta.writeObjectFieldIntro(entryNew.getKey());
        forwardDelta.writeString(entryNew.getValue());
        reverseDelta.writeObjectFieldIntro(entryNew.getKey());
        if (old == null) {
          reverseDelta.writeNull();
        } else {
          reverseDelta.writeString(old);
        }
      }
    }
    for (T nukeKey : nuke) {
      forwardDelta.writeObjectFieldIntro(nukeKey);
      forwardDelta.writeNull();
      reverseDelta.writeObjectFieldIntro(nukeKey);
      reverseDelta.writeString(prior.get(nukeKey));
    }
  }

  @Override
  public void __dump(JsonStreamWriter writer) {
    value.write(writer);
  }

  @Override
  public void __insert(JsonStreamReader reader) {
    backup = new Text(reader, gen.bumpUpPost());
    value = backup;
  }

  @Override
  public void __patch(JsonStreamReader reader) {
    if (value == backup) {
      value = backup.forkValue();
    }
    value.patch(reader, gen.bumpUpPost());
    __raiseDirty();
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
    if (backup == value) {
      return super.__memory() + value.memory();
    } else {
      return super.__memory() + value.memory() + backup.memory();
    }
  }

  public boolean append(int seq, NtDynamic changes) {
    if (value.append(seq, changes.json)) {
      __raiseDirty();
      return true;
    }
    return false;
  }

  public void set(String str) {
    if (value == backup) {
      value = backup.forkValue();
    }
    this.value.set(str, gen.bumpUpPost());
    __raiseDirty();
  }

  public void compact(double ratio) {
    if (value == backup) {
      value = backup.forkValue();
    }
    value.compact(ratio);
    __raiseDirty();
  }

  public String get() {
    return value.get().value;
  }
}
