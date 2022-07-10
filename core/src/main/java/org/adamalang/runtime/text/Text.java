/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.runtime.text;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.text.ot.Operand;
import org.adamalang.runtime.text.ot.Raw;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** the core Text class for representing large text which can undergo OT transformations */
public class Text {
  public final HashMap<String, String> fragments;
  public final HashMap<Integer, String> order;
  public final HashMap<Integer, String> changes;
  public final HashMap<Integer, String> uncommitedChanges;
  public int seq;
  public int gen;
  public boolean upgraded;
  private SeqString copy;

  /** fresh Text */
  public Text(int gen) {
    this.fragments = new HashMap<>();
    this.order = new HashMap<>();
    this.changes = new HashMap<>();
    this.uncommitedChanges = new HashMap<>();
    this.seq = 0;
    this.gen = gen;
    this.upgraded = false;
    this.copy = null;
  }

  /** make a value copy of the backup, and clean the backup */
  public Text(Text other) {
    this.fragments = new HashMap<>(other.fragments);
    this.order = new HashMap<>(other.order);
    this.changes = new HashMap<>(other.changes);
    this.uncommitedChanges = new HashMap<>(other.uncommitedChanges);
    this.seq = other.seq;
    this.upgraded = other.upgraded;
    this.gen = other.gen;
    this.copy = null;
    other.uncommitedChanges.clear();

  }

  /** read from JSON */
  public Text(JsonStreamReader reader, int gen) {
    this.fragments = new HashMap<>();
    this.order = new HashMap<>();
    this.changes = new HashMap<>();
    this.uncommitedChanges = new HashMap<>();
    this.seq = 0;
    this.upgraded = false;
    patch(reader, gen);
  }

  public void patch(JsonStreamReader reader, int gen) {
    this.copy = null;
    this.gen = gen;
    if (reader.startObject()) {
      while (reader.notEndOfObject()) {
        switch (reader.fieldName()) {
          case "fragments":
            if (reader.startObject()) {
              while (reader.notEndOfObject()) {
                String key = reader.fieldName();
                if (reader.testLackOfNull()) {
                  fragments.put(key, reader.readString());
                } else {
                  fragments.remove(key);
                }
              }
            } else {
              reader.skipValue();
            }
            break;
          case "order":
            if (reader.startObject()) {
              while (reader.notEndOfObject()) {
                int index = Integer.parseInt(reader.fieldName());
                if (reader.testLackOfNull()) {
                  order.put(index, reader.readString());
                } else {
                  order.remove(index);
                }
              }
            } else {
              reader.skipValue();
            }
            break;
          case "changes":
            if (reader.startObject()) {
              while (reader.notEndOfObject()) {
                int __seq = Integer.parseInt(reader.fieldName());
                if (reader.testLackOfNull()) {
                  changes.put(__seq, reader.skipValueIntoJson());
                } else {
                  changes.remove(__seq);
                }
              }
            } else {
              reader.skipValue();
            }
            break;
          case "seq":
            seq = reader.readInteger();
            break;
        }
      }
    } else {
      set(reader.readString(), gen);
      upgraded = true;
    }
  }

  public void commit() {
    changes.putAll(uncommitedChanges);
    uncommitedChanges.clear();
  }

  private void setBase(String str) {
    HashMap<String, String> inverse = new HashMap<>();
    for (Map.Entry<String, String> prior : fragments.entrySet()) {
      inverse.put(prior.getValue(), prior.getKey());
    }
    fragments.clear();
    order.clear();
    int at = 0;
    for (String ln : str.split(Pattern.quote("\n"), -1)) {
      String key = inverse.get(ln);
      if (key == null) {
        key = keyFor(ln, inverse);
        inverse.put(ln, key);
      }
      fragments.put(key, ln);
      order.put(at, key);
      at++;
    }
  }

  public void set(String str, int gen) {
    setBase(str);
    changes.clear();
    uncommitedChanges.clear();
    this.seq = 0;
    this.gen = gen;
    this.copy = new SeqString(seq, str);
  }

  public static String keyFor(String ln, HashMap<String, String> inverse) {
    while (true) {
      String candidate = Integer.toString(Math.abs(ln.hashCode()), 16) + "X" + Integer.toString(Math.abs(ln.hashCode()), 16) + "Y" + (long) (System.currentTimeMillis() * Math.random());
      for (int k = 2; k < candidate.length(); k++) {
        String test = candidate.substring(0, k);
        if (!inverse.containsKey(test)) {
          return test;
        }
      }
    }
  }

  /** write the text entirely to the given writer */
  public void write(JsonStreamWriter writer) {
    writer.beginObject();
    writer.writeObjectFieldIntro("fragments");
    writer.beginObject();
    for (Map.Entry<String, String> fragmentEntry : fragments.entrySet()) {
      writer.writeObjectFieldIntro(fragmentEntry.getKey());
      writer.writeString(fragmentEntry.getValue());
    }
    writer.endObject();
    writer.writeObjectFieldIntro("order");
    writer.beginObject();
    for (Map.Entry<Integer, String> orderEntry : order.entrySet()) {
      writer.writeObjectFieldIntro(orderEntry.getKey());
      writer.writeString(orderEntry.getValue());
    }
    writer.endObject();
    writer.writeObjectFieldIntro("changes");
    writer.beginObject();
    for (Map.Entry<Integer, String> changeEntry : changes.entrySet()) {
      writer.writeObjectFieldIntro(changeEntry.getKey());
      writer.injectJson(changeEntry.getValue());
    }
    for (Map.Entry<Integer, String> changeEntry : uncommitedChanges.entrySet()) {
      writer.writeObjectFieldIntro(changeEntry.getKey());
      writer.injectJson(changeEntry.getValue());
    }
    writer.endObject();
    writer.writeObjectFieldIntro("seq");
    writer.writeInteger(seq);
    writer.endObject();
  }

  public boolean append(int seq, String change) {
    boolean exists = this.changes.containsKey(seq) || uncommitedChanges.containsKey(seq);
    boolean priorExists = this.seq == seq || this.changes.containsKey(seq - 1) || this.uncommitedChanges.containsKey(seq - 1);
    if (exists || !priorExists) {
      return false;
    }

    JsonStreamReader reader = new JsonStreamReader(change);
    if (reader.startArray()) {
      int off = 0;
      Operand operand = copy != null ? new Raw(copy.value) : null;
      while (reader.notEndOfArray()) {
        String elementChange = reader.skipValueIntoJson();
        uncommitedChanges.put(seq + off, elementChange);
        if (operand != null) {
          operand = Operand.apply(operand, elementChange);
        }
        off++;
      }
      if (operand != null) {
        copy = new SeqString(seq + off, operand.get());
      }
    } else {
      uncommitedChanges.put(seq, change);
      if (copy != null) {
        copy = new SeqString(seq + 1, Operand.apply(new Raw(copy.value), change).get());
      }
    }
    return true;
  }

  public void compact(double ratio) {
    StringBuilder sb = new StringBuilder();
    for(int k = 0; k < order.size(); k++) {
      if (k > 0) {
        sb.append("\n");
      }
      sb.append(fragments.get(order.get(k)));
    }
    Operand result = new Raw(sb.toString());
    int toDo = (int) (ratio * (changes.size() + uncommitedChanges.size()));
    while (toDo-- > 0) {
      String change = changes.remove(seq);
      if (change == null) {
        change = uncommitedChanges.remove(seq);
      }
      if (change != null) {
        result = Operand.apply(result, change);
        seq++;
      } else {
        toDo = 0;
      }
    }
    setBase(result.get());
  }

  public SeqString get() {
    if (copy == null) {
      StringBuilder sb = new StringBuilder();
      for(int k = 0; k < order.size(); k++) {
        if (k > 0) {
          sb.append("\n");
        }
        sb.append(fragments.get(order.get(k)));
      }
      int at = seq;
      Operand result = new Raw(sb.toString());
      String change;
      while ((change = changes.get(at)) != null) {
        result = Operand.apply(result, change);
        at++;
      }
      while ((change = uncommitedChanges.get(at)) != null) {
        result = Operand.apply(result, change);
        at++;
      }
      copy = new SeqString(at, result.get());
    }
    return copy;
  }
}
