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
package org.adamalang;

import io.jsonwebtoken.Jwts;
import org.adamalang.common.Json;
import org.adamalang.runtime.security.Keystore;
import org.junit.Assert;
import org.junit.Test;

import java.security.PrivateKey;
import java.util.Iterator;
import java.util.regex.Pattern;

public class EndToEnd_AuthorityTests {
  @Test
  public void flow() throws Exception {
    try (TestFrontEnd fe = new TestFrontEnd()) {
      String devIdentity = fe.setupDevIdentity();
      Iterator<String> c3 =
          fe.execute("{\"id\":3,\"method\":\"authority/create\",\"identity\":\"" + devIdentity + "\"}");
      String authorityCreatedLog = c3.next();
      Assert.assertTrue(authorityCreatedLog.startsWith("FINISH:{\"authority\":\""));
      String authority = authorityCreatedLog.split(Pattern.quote("\""))[3];
      Iterator<String> c4 =
          fe.execute("{\"id\":4,\"method\":\"authority/list\",\"identity\":\"" + devIdentity + "\"}");
      Assert.assertEquals("STREAM:{\"authority\":\""+authority+"\"}", c4.next());
      Iterator<String> c5 =
          fe.execute("{\"id\":5,\"method\":\"authority/destroy\",\"authority\":\""+authority+"\",\"identity\":\"" + devIdentity + "\"}");
      Assert.assertEquals("FINISH:{}", c5.next());
      Iterator<String> c6 =
          fe.execute("{\"id\":6,\"method\":\"authority/create\",\"identity\":\"" + devIdentity + "\"}");
      authorityCreatedLog = c6.next();
      authority = authorityCreatedLog.split(Pattern.quote("\""))[3];
      Assert.assertTrue(authorityCreatedLog.startsWith("FINISH:{\"authority\":\""));
      Iterator<String> c7 = fe.execute("{\"id\":6,\"method\":\"authority/set\",\"identity\":\"" + devIdentity + "\",\"authority\":\"nope\",\"keystore\":\"\"}");
      Assert.assertEquals("ERROR:457743", c7.next());
      Iterator<String> c8 = fe.execute("{\"id\":6,\"method\":\"authority/set\",\"identity\":\"" + devIdentity + "\",\"authority\":\""+authority+"\",\"keystore\":\"{}\"}");
      Assert.assertEquals("ERROR:457743", c8.next());
      Iterator<String> c9 = fe.execute("{\"id\":6,\"method\":\"authority/get\",\"identity\":\"" + devIdentity + "\",\"authority\":\""+authority+"\"}");
      Assert.assertEquals("FINISH:{\"keystore\":{}}", c9.next());
      Keystore ks = Keystore.parse("{}");
      String privateKeyFile = ks.generate(authority);
      Iterator<String> c10 = fe.execute("{\"id\":6,\"method\":\"authority/set\",\"identity\":\"" + devIdentity + "\",\"authority\":\""+authority+"\",\"key-store\":"+ks.persist()+"}");
      Assert.assertEquals("FINISH:{}", c10.next());
      PrivateKey key = Keystore.parsePrivateKey(Json.parseJsonObject(privateKeyFile));
      String newIdentity = Jwts.builder().subject("me").issuer(authority).signWith(key).compact();
      Iterator<String> c11 = fe.execute("{\"id\":3,\"method\":\"authority/create\",\"identity\":\"" + newIdentity + "\"}");
      Assert.assertEquals("ERROR:990208", c11.next());
      Iterator<String> c12 = fe.execute("{\"id\":3,\"method\":\"authority/list\",\"identity\":\"" + newIdentity + "\"}");
      Assert.assertEquals("ERROR:904223", c12.next());
      Iterator<String> c13 = fe.execute("{\"id\":6,\"method\":\"authority/set\",\"identity\":\"" + newIdentity + "\",\"authority\":\""+authority+"\",\"key-store\":"+ks.persist()+"}");
      Assert.assertEquals("ERROR:970780", c13.next());
      Iterator<String> c14 = fe.execute("{\"id\":6,\"method\":\"authority/get\",\"identity\":\"" + newIdentity + "\",\"authority\":\""+authority+"\"}");
      Assert.assertEquals("ERROR:978992", c14.next());
      Iterator<String> c15 = fe.execute("{\"id\":6,\"method\":\"authority/destroy\",\"identity\":\"" + newIdentity + "\",\"authority\":\""+authority+"\"}");
      Assert.assertEquals("ERROR:901144", c15.next());
      Iterator<String> c16 = fe.execute("{\"id\":6,\"method\":\"account/set-password\",\"identity\":\"" + newIdentity + "\",\"password\":\"pw\"}");
      Assert.assertEquals("ERROR:983199", c16.next());
      Iterator<String> c17 = fe.execute("{\"id\":3,\"method\":\"account/get-payment-plan\",\"identity\":\"" + newIdentity + "\"}");
      Assert.assertEquals("ERROR:903375", c17.next());
      Iterator<String> c18 = fe.execute("{\"id\":1,\"identity\":\"" + devIdentity + "\",\"method\":\"deinit\"}");
      Assert.assertEquals("ERROR:918771", c18.next());
    }
  }
}
