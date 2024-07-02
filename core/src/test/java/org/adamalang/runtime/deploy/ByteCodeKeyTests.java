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
package org.adamalang.runtime.deploy;

import org.junit.Assert;
import org.junit.Test;

public class ByteCodeKeyTests {
  @Test
  public void sanity() {
    ByteCodeKey a = new ByteCodeKey("a", "b", "c", "d");
    Assert.assertEquals(a, a);
    Assert.assertEquals(a, new ByteCodeKey("a", "b", "c", "d"));
    Assert.assertNotEquals(a, "");
    a.hashCode();
    Assert.assertEquals(0, a.compareTo(a));
  }
}
