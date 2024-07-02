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
package org.adamalang.common;

import org.junit.Assert;
import org.junit.Test;

public class HMACSHA256Tests {
  @Test
  public void flow() {
    byte[] key = "AWS4BlahBlahBlah".getBytes();
    Assert.assertEquals("d26f8c02690af9c24c06c78f7e45dbd6c3467bb2db9be10b2507caad52ea9780", Hex.of(HMACSHA256.of(key, "OK")));
    try {
      HMACSHA256.of("".getBytes(), "OK");
      Assert.fail();
    } catch (IllegalArgumentException iae) {

    }
    try {
      HMACSHA256.of(null, "OK");
      Assert.fail();
    } catch (IllegalArgumentException iae) {

    }
  }

}
