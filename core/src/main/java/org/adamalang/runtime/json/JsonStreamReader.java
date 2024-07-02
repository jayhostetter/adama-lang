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
package org.adamalang.runtime.json;

import org.adamalang.runtime.json.token.JsonToken;
import org.adamalang.runtime.json.token.JsonTokenType;
import org.adamalang.runtime.natives.*;
import org.adamalang.translator.parser.token.Token;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class JsonStreamReader {
  private final String json;
  private final int n;
  private final HashMap<String, String> dedupeStrings;
  private final HashMap<NtPrincipal, NtPrincipal> dedupeClients;
  ArrayDeque<JsonToken> tokens;
  private int index;

  public JsonStreamReader(final String json) {
    this.json = json;
    n = json.length();
    tokens = new ArrayDeque<>();
    this.dedupeStrings = new HashMap<>();
    this.dedupeClients = new HashMap<>();
    this.dedupeClients.put(NtPrincipal.NO_ONE, NtPrincipal.NO_ONE);
  }

  public void ingestDedupe(Set<String> strs) {
    for (String str : strs) {
      dedupeStrings.put(str, str);
    }
  }

  public boolean end() {
    return index >= n;
  }

  private void ensureQueueHappy(final int needs) {
    if (tokens.size() > needs) {
      return;
    }
    while (tokens.size() < 10 + needs) {
      if (index < n) {
        readToken();
      } else {
        if (tokens.size() < needs) {
          throw new RuntimeException("Unable to satisfy minimum limit");
        }
        return;
      }
    }
  }

  public String fieldName() {
    ensureQueueHappy(1);
    return tokens.removeFirst().data;
  }

  public boolean notEndOfArray() {
    ensureQueueHappy(1);
    final var first = tokens.peekFirst();
    if (first.type == JsonTokenType.EndArray) {
      tokens.removeFirst();
      return false;
    }
    return true;
  }

  public boolean notEndOfObject() {
    ensureQueueHappy(1);
    final var first = tokens.peekFirst();
    if (first.type == JsonTokenType.EndObject) {
      tokens.removeFirst();
      return false;
    }
    return true;
  }

  public boolean readBoolean() {
    ensureQueueHappy(1);
    JsonToken token = tokens.removeFirst();
    if (token.type == JsonTokenType.True) {
      return true;
    }
    if (token.type == JsonTokenType.StringLiteral) {
      return token.data.equals("true");
    }
    return false;
  }

  private String readValueWithDefaultZeros() {
    JsonToken token = readValueToken();
    if (token == null) {
      return "0";
    }
    if (token.type == JsonTokenType.True) {
      return "1";
    }
    if (token == null || token.type == JsonTokenType.Null || token.type == JsonTokenType.False || token.data == null || token.data.equals("")) {
      return "0";
    }
    return token.data;
  }

  public double readDouble() {
    ensureQueueHappy(1);
    String toParse = readValueWithDefaultZeros();
    try {
      return Double.parseDouble(toParse);
    } catch (NumberFormatException nfe) {
      return 0.0;
    }
  }

  private int parseInt(String toParse) {
    try {
      return Integer.parseInt(toParse);
    } catch (NumberFormatException nfe1) {
      try {
        return (int) Double.parseDouble(toParse);
      } catch (NumberFormatException nfe2) {
        return 0;
      }
    }
  }

  public int readInteger() {
    ensureQueueHappy(1);
    return parseInt(readValueWithDefaultZeros());
  }

  public long readLong() {
    ensureQueueHappy(1);
    String toParse = readValueWithDefaultZeros();
    try {
      return Long.parseLong(toParse);
    } catch (NumberFormatException nfe) {
      return parseInt(toParse);
    }
  }

  public NtPrincipal readNtPrincipal() {
    var agent = "?";
    var authority = "?";
    if (startObject()) {
      while (notEndOfObject()) {
        switch (fieldName()) {
          case "agent":
            agent = readString();
            break;
          case "authority":
            authority = readString();
            break;
          default:
            skipValue();
        }
      }
    } else {
      skipValue();
      return NtPrincipal.NO_ONE;
    }

    NtPrincipal lookup = new NtPrincipal(agent, authority);
    NtPrincipal test = dedupeClients.get(lookup);
    if (test == null) {
      dedupeClients.put(lookup, lookup);
      return lookup;
    } else {
      return test;
    }
  }

  public NtDate readNtDate() {
    return NtDate.parse(readString());
  }

  public NtDateTime readNtDateTime() {
    return NtDateTime.parse(readString());
  }

  public NtTime readNtTime() {
    return NtTime.parse(readString());
  }

  public NtTimeSpan readNtTimeSpan() {
    return new NtTimeSpan(readDouble());
  }

  public NtComplex readNtComplex() {
    double re = 0.0;
    double im = 0.0;
    if (startObject()) {
      while (notEndOfObject()) {
        switch (fieldName()) {
          case "r":
            re = readDouble();
            break;
          case "i":
            im = readDouble();
            break;
        }
      }
    } else {
      skipValue();
    }
    return new NtComplex(re, im);
  }

  public NtJson readNtJson() {
    return readNtDynamic().to_json();
  }

  public NtAsset readNtAsset() {
    String id = "";
    String name = "";
    long size = 0;
    String contentType = "";
    String md5 = "";
    String sha384 = "";
    if (startObject()) {
      while (notEndOfObject()) {
        switch (fieldName()) {
          case "@gc":
            readString();
            break;
          case "id":
            id = readString();
            break;
          case "size":
            size = readLong();
            break;
          case "name":
            name = readString();
            break;
          case "type":
            contentType = readString();
            break;
          case "md5":
            md5 = readString();
            break;
          case "sha384":
            sha384 = readString();
            break;
        }
      }
    } else {
      skipValue();
    }
    return new NtAsset(id, name, contentType, size, md5, sha384);
  }

  private JsonToken readValueToken() {
    if (startObject()) {
      while (notEndOfObject()) {
        fieldName();
        skipValue();
      }
      return null;
    } else if (startArray()) {
      while (notEndOfArray()) {
        skipValue();
      }
      return null;
    } else {
      ensureQueueHappy(1);
      return tokens.removeFirst();
    }
  }

  public String readString() {
    ensureQueueHappy(1);
    JsonToken next = readValueToken();
    if (next == null) {
      return "";
    }
    String lookup = next.data;
    if (lookup == null) {
      return "";
    }
    String test = dedupeStrings.get(lookup);
    if (test == null) {
      dedupeStrings.put(lookup, lookup);
      return lookup;
    } else {
      return test;
    }
  }

  public Object readJavaTree() {
    if (startObject()) {
      LinkedHashMap<String, Object> obj = new LinkedHashMap<>();
      while (notEndOfObject()) {
        String fieldName = fieldName();
        obj.put(fieldName, readJavaTree());
      }
      return obj;
    } else if (startArray()) {
      ArrayList<Object> arr = new ArrayList<>();
      while (notEndOfArray()) {
        arr.add(readJavaTree());
      }
      return arr;
    } else {
      ensureQueueHappy(1);
      JsonToken token = tokens.removeFirst();
      switch (token.type) {
        case Null:
          return null;
        case False:
          return false;
        case True:
          return true;
        case NumberLiteralDouble:
          return Double.parseDouble(token.data);
        case NumberLiteralInteger:
          long val = Long.parseLong(token.data);
          if (Integer.MIN_VALUE <= val && val <= Integer.MAX_VALUE) {
            return (int) val;
          }
          return val;
        case StringLiteral:
          return token.data;
        default:
          throw new RuntimeException("unexpected token: " + token);
      }
    }
  }

  private void readToken() {
    if (index >= json.length()) {
      return;
    }
    final var start = json.charAt(index);
    switch (start) {
      case ' ':
      case '\n':
      case '\r':
      case '\t':
      case ',':
      case ':':
        index++;
        readToken();
        return;
      case '{':
        index++;
        tokens.addLast(new JsonToken(JsonTokenType.StartObject, null));
        return;
      case '}':
        index++;
        tokens.addLast(new JsonToken(JsonTokenType.EndObject, null));
        return;
      case '[':
        index++;
        tokens.addLast(new JsonToken(JsonTokenType.StartArray, null));
        return;
      case ']':
        index++;
        tokens.addLast(new JsonToken(JsonTokenType.EndArray, null));
        return;
      case '\"':
        StringBuilder sb = null;
        for (var j = index + 1; j < n; j++) {
          var ch = json.charAt(j);
          if (ch == '\\') {
            if (sb == null) {
              sb = new StringBuilder();
              sb.append(json, index + 1, j);
            }
            j++;
            ch = json.charAt(j);
            switch (ch) {
              case 'n':
                sb.append('\n');
                break;
              case 't':
                sb.append('\t');
                break;
              case 'r':
                sb.append('\r');
                break;
              case 'f':
                sb.append('\f');
                break;
              case 'b':
                sb.append('\b');
                break;
              case '\\':
                sb.append('\\');
                break;
              case '"':
                sb.append('\"');
                break;
              case 'u':
                sb.append(Character.toString(Integer.parseInt(json.substring(j + 1, j + 5), 16)));
                j += 4;
            }
          } else if (ch == '"') {
            if (sb != null) {
              tokens.addLast(new JsonToken(JsonTokenType.StringLiteral, sb.toString()));
            } else {
              tokens.addLast(new JsonToken(JsonTokenType.StringLiteral, json.substring(index + 1, j)));
            }
            index = j + 1;
            return;
          } else {
            if (sb != null) {
              sb.append(ch);
            }
          }
        }
        throw new UnsupportedOperationException();
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case '-':
      case '+': {
        boolean isDouble = false;
        for (var j = index + 1; j < n; j++) {
          final var ch2 = json.charAt(j);
          switch (ch2) {
            case 'E':
            case 'e':
            case '.':
            case '-':
            case '+':
              isDouble = true;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
              break;
            default:
              tokens.addLast(new JsonToken(isDouble ? JsonTokenType.NumberLiteralDouble : JsonTokenType.NumberLiteralInteger, json.substring(index, j)));
              index = j;
              return;
          }
        }
        tokens.addLast(new JsonToken(isDouble ? JsonTokenType.NumberLiteralDouble : JsonTokenType.NumberLiteralInteger, json.substring(index)));
        index = n;
        return;
      }
      case 'n':
        index += 4;
        tokens.addLast(new JsonToken(JsonTokenType.Null, null));
        return;
      case 't':
        index += 4;
        tokens.addLast(new JsonToken(JsonTokenType.True, null));
        return;
      case 'f':
        index += 5;
        tokens.addLast(new JsonToken(JsonTokenType.False, null));
        return;
      default:
        throw new UnsupportedOperationException();
    }
  }

  public void skipValue() {
    if (startObject()) {
      while (notEndOfObject()) {
        fieldName();
        skipValue();
      }
    } else if (startArray()) {
      while (notEndOfArray()) {
        skipValue();
      }
    } else {
      ensureQueueHappy(1);
      tokens.removeFirst();
    }
  }

  public void mustSkipObject() {
    if (startObject()) {
      while (notEndOfObject()) {
        fieldName();
        skipValue();
      }
    } else {
      throw new RuntimeException("Required an object to skip");
    }
  }

  public void mustSkipArray() {
    if (startArray()) {
      while (notEndOfArray()) {
        skipValue();
      }
    } else {
      throw new RuntimeException("Required an array to skip");
    }
  }

  /** scan the JSON tree and find unique asset ids */
  public void populateGarbageCollectedIds(HashSet<String> ids) {
    if (startObject()) {
      String id = null;
      boolean gc = false;
      while (notEndOfObject()) {
        switch (fieldName()) {
          case "id": {
            Object testId = readJavaTree();
            if (testId instanceof String) {
              id = (String) testId;
            }
            break;
          }
          case "@gc":
            gc = true;
            skipValue();
            break;
          default:
            populateGarbageCollectedIds(ids);
        }
        if (gc && id != null) {
          ids.add(id);
        }
      }

    } else if (startArray()) {
      while (notEndOfArray()) {
        populateGarbageCollectedIds(ids);
      }
    } else {
      ensureQueueHappy(1);
      tokens.removeFirst();
    }
  }

  public String skipValueIntoJson() {
    JsonStreamWriter writer = new JsonStreamWriter();
    skipValue(writer);
    return writer.toString();
  }

  public NtDynamic readNtDynamic() {
    return new NtDynamic(skipValueIntoJson());
  }

  public void skipValue(final JsonStreamWriter writer) {
    if (startObject()) {
      writer.beginObject();
      while (notEndOfObject()) {
        writer.writeObjectFieldIntro(fieldName());
        skipValue(writer);
      }
      writer.endObject();
    } else if (startArray()) {
      writer.beginArray();
      while (notEndOfArray()) {
        skipValue(writer);
      }
      writer.endArray();
    } else {
      ensureQueueHappy(1);
      final var token = tokens.removeFirst();
      if (token.type == JsonTokenType.NumberLiteralInteger || token.type == JsonTokenType.NumberLiteralDouble) {
        writer.injectJson(token.data);
      } else if (token.type == JsonTokenType.StringLiteral) {
        writer.writeString(token.data);
      } else if (token.type == JsonTokenType.Null) {
        writer.writeNull();
      } else if (token.type == JsonTokenType.True) {
        writer.writeBoolean(true);
      } else if (token.type == JsonTokenType.False) {
        writer.writeBoolean(false);
      }
    }
  }

  public boolean startArray() {
    ensureQueueHappy(1);
    final var first = tokens.peekFirst();
    if (first.type == JsonTokenType.StartArray) {
      tokens.removeFirst();
      return true;
    }
    return false;
  }

  public void mustStartArray() {
    if (!startArray()) {
      throw new RuntimeException("Required an array");
    }
  }

  public boolean startObject() {
    ensureQueueHappy(1);
    final var first = tokens.peekFirst();
    if (first.type == JsonTokenType.StartObject) {
      tokens.removeFirst();
      return true;
    }
    return false;
  }

  public void mustStartObject() {
    if (!startObject()) {
      throw new RuntimeException("Required an object");
    }
  }

  public boolean testLackOfNull() {
    ensureQueueHappy(1);
    final var first = tokens.peekFirst();
    if (first.type == JsonTokenType.Null) {
      tokens.removeFirst();
      return false;
    }
    return true;
  }
}
