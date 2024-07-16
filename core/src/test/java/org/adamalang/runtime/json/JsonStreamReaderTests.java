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

import org.adamalang.runtime.natives.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class JsonStreamReaderTests {
  @Test
  public void bad_obj() {
    JsonStreamReader reader = new JsonStreamReader("[}");
    try {
      reader.readJavaTree();
      Assert.fail();
    } catch (RuntimeException re) {
      Assert.assertEquals(
          "unexpected token: JsonToken{data='null', type=EndObject}", re.getMessage());
    }
  }

  @Test
  public void obj_to_empty_str() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\":123},\"xyz\"");
    Assert.assertEquals("", reader.readString());
    Assert.assertEquals("xyz", reader.readString());
  }

  @Test
  public void skip_bad_principal() {
    JsonStreamReader reader = new JsonStreamReader("[]");
    Assert.assertEquals(NtPrincipal.NO_ONE, reader.readNtPrincipal());
  }

  @Test
  public void principal_skip_bad_data() {
    JsonStreamReader reader = new JsonStreamReader("{\"agent\":\"z\",\"x\":true}");
    Assert.assertEquals(new NtPrincipal("z", "?"), reader.readNtPrincipal());
  }

  @Test
  public void bad_long_skip() {
    JsonStreamReader reader = new JsonStreamReader("[]123");
    Assert.assertEquals(0, reader.readLong());
    Assert.assertEquals(123L, reader.readLong());
  }

  @Test
  public void bad_long() {
    JsonStreamReader reader = new JsonStreamReader("\"x\"");
    Assert.assertEquals(0, reader.readLong());
  }

  @Test
  public void bad_double() {
    JsonStreamReader reader = new JsonStreamReader("\"x\"");
    Assert.assertEquals(0, reader.readDouble(), 0.01);
  }

  @Test
  public void arr_to_empty_str() {
    JsonStreamReader reader = new JsonStreamReader("[123,\"\"],\"xyz\"");
    Assert.assertEquals("", reader.readString());
    Assert.assertEquals("xyz", reader.readString());
  }

  @Test
  public void obj_to_zero_int() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\":123},42");
    Assert.assertEquals(0, reader.readInteger());
    Assert.assertEquals(42, reader.readInteger());
  }

  @Test
  public void false_to_zero() {
    JsonStreamReader reader = new JsonStreamReader("false");
    Assert.assertEquals(0, reader.readInteger());
  }

  @Test
  public void true_to_one() {
    JsonStreamReader reader = new JsonStreamReader("true");
    Assert.assertEquals(1, reader.readInteger());
  }

  @Test
  public void eos_to_zero() {
    JsonStreamReader reader = new JsonStreamReader("[]");
    Assert.assertEquals(0, reader.readInteger());
  }

  @Test
  public void obj_to_zero_int_2() {
    JsonStreamReader reader = new JsonStreamReader("\"X\"");
    Assert.assertEquals(0, reader.readInteger());
  }

  @Test
  public void arr_to_zero_int() {
    JsonStreamReader reader = new JsonStreamReader("[123,\"\"],42");
    Assert.assertEquals(0, reader.readInteger());
    Assert.assertEquals(42, reader.readInteger());
  }

  @Test
  public void str_as_null() {
    JsonStreamReader reader = new JsonStreamReader("null");
    Assert.assertEquals("", reader.readString());
  }

  @Test
  public void complex_downpromote() {
    JsonStreamReader reader = new JsonStreamReader("\"x\"");
    Assert.assertEquals(0, (int) reader.readNtComplex().real);
  }

  @Test
  public void principal_downpromote() {
    JsonStreamReader reader = new JsonStreamReader("\"x\"");
    Assert.assertEquals("?", reader.readNtPrincipal().authority);
  }

  @Test
  public void assert_downpromote() {
    JsonStreamReader reader = new JsonStreamReader("\"x\"");
    Assert.assertEquals("", reader.readNtAsset().id);
  }

  @Test
  public void emptyIsZero() {
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":\"\"}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertEquals(0, reader.readInteger());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":\"\"}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertTrue(Math.abs(reader.readDouble()) < 0.0001);
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":\"\"}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertEquals(0, reader.readLong());
    }
  }

  @Test
  public void nullIsZero() {
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":null}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertEquals(0, reader.readInteger());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":null}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertTrue(Math.abs(reader.readDouble()) < 0.0001);
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":null}");
      Assert.assertTrue(reader.startObject());
      Assert.assertTrue(reader.notEndOfObject());
      Assert.assertEquals("x", reader.fieldName());
      Assert.assertEquals(0, reader.readLong());
    }
  }

  @Test
  public void coverage_mustSkipArray() {
    JsonStreamReader reader = new JsonStreamReader("[1, 2, 3]");
    reader.mustSkipArray();;
  }

  @Test
  public void coverage_mustSkipObject() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\":123}");
    reader.mustSkipObject();
  }

  @Test
  public void dupes() {
    JsonStreamReader reader = new JsonStreamReader("{}");
    HashSet<String> dupes = new HashSet<>();
    dupes.add("x");
    reader.ingestDedupe(dupes);
  }

  @Test
  public void dedupeClient() {
    JsonStreamWriter writer = new JsonStreamWriter();
    writer.beginArray();
    writer.writeNtPrincipal(new NtPrincipal("jack", "jill"));
    writer.writeNtPrincipal(new NtPrincipal("jack", "jill"));
    writer.writeNtPrincipal(new NtPrincipal("jack", "jill"));
    writer.endArray();
    JsonStreamReader reader = new JsonStreamReader(writer.toString());
    Assert.assertTrue(reader.startArray());
    Assert.assertTrue(reader.notEndOfArray());
    NtPrincipal A = reader.readNtPrincipal();
    NtPrincipal B = reader.readNtPrincipal();
    NtPrincipal C = reader.readNtPrincipal();
    Assert.assertFalse(reader.notEndOfArray());
    Assert.assertTrue(A == B && B == C);
  }

  @Test
  public void dupeString() {
    JsonStreamWriter writer = new JsonStreamWriter();
    writer.beginArray();
    writer.writeString("XYZ");
    writer.writeString("XYZ");
    writer.writeString("XYZ");
    writer.endArray();
    JsonStreamReader reader = new JsonStreamReader(writer.toString());
    Assert.assertTrue(reader.startArray());
    Assert.assertTrue(reader.notEndOfArray());
    String A = reader.readString();
    String B = reader.readString();
    String C = reader.readString();
    Assert.assertFalse(reader.notEndOfArray());
    Assert.assertTrue(A == B && B == C);
  }

  @Test
  public void crash() {
    JsonStreamReader reader = new JsonStreamReader("{}");
    reader.fieldName();
    reader.fieldName();
    try {
      reader.fieldName();
      Assert.fail();
    } catch (RuntimeException re) {
      Assert.assertEquals("Unable to satisfy minimum limit", re.getMessage());
    }
  }

  @Test
  public void asset() {
    JsonStreamReader reader =
        new JsonStreamReader(
            "{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\",\"@gc\":\"@yes\"}");
    NtAsset cmp = new NtAsset("123", "name", "png", 42, "hash", "sheesh");
    NtAsset tst = reader.readNtAsset();
    Assert.assertEquals(cmp, tst);
  }

  @Test
  public void complex() {
    JsonStreamReader reader = new JsonStreamReader("{\"r\":1.2,\"i\":2.4}");
    NtComplex cmp = new NtComplex(1.2, 2.4);
    NtComplex tst = reader.readNtComplex();
    Assert.assertEquals(cmp, tst);
  }

  @Test
  public void asset_ws() {
    JsonStreamReader reader =
        new JsonStreamReader(
            "   { \t \"id\"  \r :  \n  \"123\" \n , \n \"size\" \n : \n \"42\" \n , \n \"name\" \n : \n \"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\",\"@gc\":\n\"@yes\"}");
    NtAsset cmp = new NtAsset("123", "name", "png", 42, "hash", "sheesh");
    NtAsset tst = reader.readNtAsset();
    Assert.assertEquals(cmp, tst);
  }

  @Test
  public void tree_empty_obj() {
    JsonStreamReader reader = new JsonStreamReader("  {  }  ");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(obj instanceof HashMap);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_obj() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\"  :  123}");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(obj instanceof HashMap);
    Assert.assertTrue(123 == (int) (((HashMap<?, ?>) obj).get("x")));
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_empty_array() {
    JsonStreamReader reader = new JsonStreamReader("[]");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(obj instanceof ArrayList);
    Assert.assertTrue(reader.end());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void tree_array() {
    JsonStreamReader reader = new JsonStreamReader("[123]");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(obj instanceof ArrayList);
    Assert.assertTrue(123 == (int) (((ArrayList<Object>) obj).get(0)));
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_number_d() {
    JsonStreamReader reader = new JsonStreamReader("1.4");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(1.4 == (Double) obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_number_i() {
    JsonStreamReader reader = new JsonStreamReader("12");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(12 == (Integer) obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_number_l() {
    JsonStreamReader reader = new JsonStreamReader("4242424242");
    Object obj = reader.readJavaTree();
    Assert.assertTrue(4242424242L == (Long) obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_bool_true() {
    JsonStreamReader reader = new JsonStreamReader("true");
    Object obj = reader.readJavaTree();
    Assert.assertTrue((Boolean) obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_bool_true_as_str() {
    JsonStreamReader reader = new JsonStreamReader("\"true\"");
    Assert.assertTrue(reader.readBoolean());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_bool_false_as_str() {
    JsonStreamReader reader = new JsonStreamReader("\"false\"");
    Assert.assertFalse(reader.readBoolean());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_bool_false() {
    JsonStreamReader reader = new JsonStreamReader("false");
    Object obj = reader.readJavaTree();
    Assert.assertFalse((Boolean) obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void tree_null() {
    JsonStreamReader reader = new JsonStreamReader("null");
    Object obj = reader.readJavaTree();
    Assert.assertNull(obj);
    Assert.assertTrue(reader.end());
  }

  @Test
  public void scanOver() {
    JsonStreamReader reader = new JsonStreamReader("{}");
    if (reader.startObject()) {
      Assert.assertFalse(reader.notEndOfObject());
    }
  }
  @Test
  public void skipValueObject() {
    try {
      JsonStreamReader reader = new JsonStreamReader("42");
      reader.mustSkipObject();
      Assert.fail();
    } catch (RuntimeException re) {
    }
    try {
      JsonStreamReader reader = new JsonStreamReader("\"x\"");
      reader.mustSkipObject();
      Assert.fail();
    } catch (RuntimeException re) {
    }
    JsonStreamReader reader = new JsonStreamReader("{}");
    reader.mustSkipObject();
  }
  @Test
  public void skipValueArray() {
    try {
      JsonStreamReader reader = new JsonStreamReader("42");
      reader.mustSkipArray();
      Assert.fail();
    } catch (RuntimeException re) {
    }
    try {
      JsonStreamReader reader = new JsonStreamReader("\"x\"");
      reader.mustSkipArray();
      Assert.fail();
    } catch (RuntimeException re) {
    }
    JsonStreamReader reader = new JsonStreamReader("[]");
    reader.mustSkipArray();
  }
  @Test
  public void skipValue() {
    {
      JsonStreamReader reader = new JsonStreamReader("42");
      Assert.assertEquals("42", reader.readNtDynamic().json);
      Assert.assertTrue(reader.end());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("\"x\"");
      reader.skipValue();
      Assert.assertTrue(reader.end());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("[\"x\",42]");
      reader.skipValue();
      Assert.assertTrue(reader.end());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":42}");
      reader.skipValue();
      Assert.assertTrue(reader.end());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":13.13}");
      reader.skipValue();
      Assert.assertTrue(reader.end());
    }
    {
      JsonStreamReader reader = new JsonStreamReader("{\"x\":42}");
      Assert.assertEquals("{\"x\":42}", reader.readNtDynamic().json);
      Assert.assertTrue(reader.end());
    }
  }

  @Test
  public void mustStart() {
    try {
      JsonStreamReader reader = new JsonStreamReader("42");
      reader.mustStartObject();
      Assert.fail();
    } catch (RuntimeException re) {

    }
    try {
      JsonStreamReader reader = new JsonStreamReader("42");
      reader.mustStartArray();
      Assert.fail();
    } catch (RuntimeException re) {

    }
  }

  @Test
  public void readObject1() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\":\"z\"}");
    Assert.assertTrue(reader.startObject());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("x", reader.fieldName());
    Assert.assertEquals("z", reader.readString());
    Assert.assertFalse(reader.notEndOfObject());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void readObject2() {
    JsonStreamReader reader = new JsonStreamReader("{\"x\":\"z\",\"z\":123}");
    reader.mustStartObject();
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("x", reader.fieldName());
    Assert.assertEquals("z", reader.readString());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("z", reader.fieldName());
    Assert.assertEquals(123, reader.readInteger());
    Assert.assertFalse(reader.notEndOfObject());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void readObject3() {
    JsonStreamReader reader =
        new JsonStreamReader(
            "{\"x\":\"z\",\"z\":123.4,\"t\":true,\"f\":false,\"n\":null,\"d\":3.14}");
    Assert.assertTrue(reader.testLackOfNull());
    ;
    Assert.assertTrue(reader.startObject());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("x", reader.fieldName());
    Assert.assertEquals("z", reader.readString());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("z", reader.fieldName());
    Assert.assertTrue(reader.testLackOfNull());
    ;
    Assert.assertEquals(123.4, reader.readDouble(), 0.01);
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("t", reader.fieldName());
    Assert.assertTrue(reader.testLackOfNull());
    ;
    Assert.assertEquals(true, reader.readBoolean());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("f", reader.fieldName());
    Assert.assertTrue(reader.testLackOfNull());
    ;
    Assert.assertEquals(false, reader.readBoolean());
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("n", reader.fieldName());
    Assert.assertFalse(reader.testLackOfNull());
    ;
    Assert.assertTrue(reader.notEndOfObject());
    Assert.assertEquals("d", reader.fieldName());
    Assert.assertTrue(3.14 == reader.readDouble());
    Assert.assertFalse(reader.notEndOfObject());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void readObject4DoubleAsInt() {
    JsonStreamReader reader = new JsonStreamReader("{\"dasi\":3.14}");
    Assert.assertTrue(reader.testLackOfNull());
    ;
    Assert.assertTrue(reader.startObject());
    Assert.assertEquals("dasi", reader.fieldName());
    Assert.assertEquals(3, reader.readInteger());
    Assert.assertFalse(reader.notEndOfObject());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void readArray() {
    JsonStreamReader reader = new JsonStreamReader("[\"x\",\"z\",123,\"4444444\"]");
    reader.mustStartArray();
    Assert.assertTrue(reader.notEndOfArray());
    Assert.assertEquals("x", reader.readString());
    Assert.assertTrue(reader.notEndOfArray());
    Assert.assertEquals("z", reader.readString());
    Assert.assertTrue(reader.notEndOfArray());
    Assert.assertEquals(123, reader.readInteger());
    Assert.assertTrue(reader.notEndOfArray());
    Assert.assertEquals(4444444L, reader.readLong());
    Assert.assertFalse(reader.notEndOfArray());
    Assert.assertTrue(reader.end());
  }

  @Test
  public void readNtPrincipal() {
    JsonStreamReader reader = new JsonStreamReader("{\"agent\":\"z\",\"authority\":\"g\"}");
    NtPrincipal c = reader.readNtPrincipal();
    Assert.assertEquals("z", c.agent);
    Assert.assertEquals("g", c.authority);
  }

  @Test
  public void echoBattery() {
    assertEcho("{}");
    assertEcho("{\"x\":123}");
    assertEcho("[1,3,true,null,{},[]]");
    assertEcho("[\"x\",{\"y\":123}]");
    assertEcho("\"\\n\\t\\r\\f\\b\\\\\\\"\"");
    assertEcho("\"cake\"");
    assertEcho("true");
    assertEcho("null");
    assertEcho("false");
    assertEcho("\"\\u733f\\u3082\\u6728\\u304b\\u3089\\u843d\\u3061\\u308b\"");
    assertEcho("\"cake\\n\\t\\r\\f\\b\\\\\\\"ninja\"");
  }

  private void assertEcho(String x) {
    JsonStreamReader reader = new JsonStreamReader(x);
    JsonStreamWriter writer = new JsonStreamWriter();
    reader.skipValue(writer);
    Assert.assertEquals(x, writer.toString());
  }

  @Test
  public void longString() {
    JsonStreamReader reader = new JsonStreamReader("\"1234zenninja\"");
    Assert.assertEquals("1234zenninja", reader.readString());
  }

  @Test
  public void readBad() {
    try {
      JsonStreamReader reader = new JsonStreamReader("cake");
      reader.skipValue();
      Assert.fail();
    } catch (UnsupportedOperationException use) {

    }
  }

  @Test
  public void readBadString() {
    try {
      JsonStreamReader reader = new JsonStreamReader("\"cake");
      reader.skipValue();
      Assert.fail();
    } catch (UnsupportedOperationException use) {

    }
  }

  @Test
  public void gcScan() {
    JsonStreamWriter writer = new JsonStreamWriter();
    writer.beginArray();
    writer.beginObject();
    writer.writeObjectFieldIntro("x");
    writer.writeNtAsset(new NtAsset("id-xyz", "name", "type", 42, "md5", "sha"));
    writer.writeObjectFieldIntro("y");
    writer.writeNtAsset(new NtAsset("id-abc", "name", "type", 42, "md5", "sha"));
    writer.writeObjectFieldIntro("dup-y");
    writer.writeNtAsset(new NtAsset("id-abc", "name", "type", 42, "md5", "sha"));
    writer.writeObjectFieldIntro("dup-x");
    writer.writeNtAsset(new NtAsset("id-xyz", "name", "type", 42, "md5", "sha"));
    writer.endObject();
    writer.writeString("nope");
    writer.endArray();
    JsonStreamReader reader = new JsonStreamReader(writer.toString());
    HashSet<String> ids = new HashSet<>();
    reader.populateGarbageCollectedIds(ids);
    Assert.assertEquals(2, ids.size());
    Assert.assertTrue(ids.contains("id-xyz"));
    Assert.assertTrue(ids.contains("id-abc"));
  }

  @Test
  public void readDate1() {
    JsonStreamReader reader = new JsonStreamReader("\"1970/1/23\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1970, date.year);
    Assert.assertEquals(1, date.month);
    Assert.assertEquals(23, date.day);
  }

  @Test
  public void badDateTime1() {
    JsonStreamReader reader = new JsonStreamReader("\"1970/1/23\"");
    NtDateTime date = reader.readNtDateTime();
    Assert.assertEquals(1, date.dateTime.getYear());
    Assert.assertEquals(1, date.dateTime.getMonthValue());
    Assert.assertEquals(1, date.dateTime.getDayOfMonth());
  }

  @Test
  public void badDateTime2() {
    JsonStreamReader reader = new JsonStreamReader("\"10:20\"");
    NtDateTime date = reader.readNtDateTime();
    Assert.assertEquals(1, date.dateTime.getYear());
    Assert.assertEquals(1, date.dateTime.getMonthValue());
    Assert.assertEquals(1, date.dateTime.getDayOfMonth());
  }

  @Test
  public void readDate1alt() {
    JsonStreamReader reader = new JsonStreamReader("\"1970-1-23\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1970, date.year);
    Assert.assertEquals(1, date.month);
    Assert.assertEquals(23, date.day);
  }

  @Test
  public void readDate1altFull() {
    JsonStreamReader reader = new JsonStreamReader("\"1970-01-23\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1970, date.year);
    Assert.assertEquals(1, date.month);
    Assert.assertEquals(23, date.day);
  }

  @Test
  public void readDate2() {
    JsonStreamReader reader = new JsonStreamReader("\"1970/07/31\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1970, date.year);
    Assert.assertEquals(7, date.month);
    Assert.assertEquals(31, date.day);
  }

  @Test
  public void readDate3() {
    JsonStreamReader reader = new JsonStreamReader("\"1970\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1970, date.year);
    Assert.assertEquals(1, date.month);
    Assert.assertEquals(1, date.day);
  }

  @Test
  public void readDate4() {
    JsonStreamReader reader = new JsonStreamReader("\"1980/11\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1980, date.year);
    Assert.assertEquals(11, date.month);
    Assert.assertEquals(1, date.day);
  }

  @Test
  public void readDateBad() {
    JsonStreamReader reader = new JsonStreamReader("\"x/11\"");
    NtDate date = reader.readNtDate();
    Assert.assertEquals(1, date.year);
    Assert.assertEquals(1, date.month);
    Assert.assertEquals(1, date.day);
  }

  @Test
  public void readTime() {
    JsonStreamReader reader = new JsonStreamReader("\"14:37\"");
    NtTime time = reader.readNtTime();
    Assert.assertEquals(37, time.minute);
    Assert.assertEquals(14, time.hour);
  }


  @Test
  public void readTimeBad() {
    JsonStreamReader reader = new JsonStreamReader("\"x:37\"");
    NtTime time = reader.readNtTime();
    Assert.assertEquals(0, time.minute);
    Assert.assertEquals(0, time.hour);
  }

  @Test
  public void readTimeSpan() {
    JsonStreamReader reader = new JsonStreamReader("1235");
    Assert.assertEquals(1235, reader.readNtTimeSpan().seconds, 0.01);
  }

  @Test
  public void readDatetime1() {
    JsonStreamReader reader = new JsonStreamReader("\"2023-04-24T17:57:19.802528800-05:00[America/Chicago]\"");
    Assert.assertEquals(2023, reader.readNtDateTime().dateTime.getYear());
  }

  @Test
  public void readDatetime2() {
    JsonStreamReader reader = new JsonStreamReader("\"2021-04-24T17:57:19.802528800-05:00\"");
    Assert.assertEquals(2021, reader.readNtDateTime().dateTime.getYear());
  }

  @Test
  public void readNtJson() {
    JsonStreamReader reader = new JsonStreamReader("[{}]");
    Assert.assertEquals("[{}]", reader.readNtJson().to_dynamic().json);
  }
}
