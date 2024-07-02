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
package org.adamalang.mysql.mocks;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.junit.Assert;

public class SimpleMockCallback implements Callback<Void> {
  private boolean success;
  private int count;
  private int reason;

  public SimpleMockCallback() {
    this.success = false;
    this.count = 0;
    this.reason = 0;
  }

  @Override
  public void success(Void value) {
    count++;
    success = true;
  }

  @Override
  public void failure(ErrorCodeException ex) {
    ex.printStackTrace();
    count++;
    success = false;
    reason = ex.code;
  }

  public void assertSuccess() {
    Assert.assertEquals(1, count);
    Assert.assertTrue(success);
  }

  public void assertFailure(int code) {
    Assert.assertEquals(1, count);
    Assert.assertFalse(success);
    Assert.assertEquals(code, this.reason);
  }
}
