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
package org.adamalang.common.pool;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.Living;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PoolCallbackWrapperTests {
  public class LivingString implements Living {
    public final String value;

    public LivingString(String value) {
      this.value = value;
    }

    @Override
    public boolean alive() {
      return true;
    }
  }
  @Test
  public void coverage() {
    ArrayList<String> events = new ArrayList<>();
    PoolItem<LivingString> item = new PoolItem<LivingString>() {
      @Override
      public LivingString item() {
        return new LivingString("xyz");
      }

      @Override
      public void signalFailure() {
        events.add("ITEM:FAILURE");
      }

      @Override
      public void returnToPool() {
        events.add("ITEM:SUCCESS->RETURN");
      }
    };
    Callback<String> cb = new Callback<String>() {
      @Override
      public void success(String value) {
        events.add("CB:SUCCESS==" + value);
      }

      @Override
      public void failure(ErrorCodeException ex) {
        events.add("CB:FAILURE:" + ex.code);
      }
    };
    PoolCallbackWrapper<String, LivingString> w = new PoolCallbackWrapper<>(cb, item);
    w.success("xyz");
    Assert.assertEquals(2, events.size());
    w.failure(new ErrorCodeException(123));
    Assert.assertEquals(4, events.size());
    Assert.assertEquals("CB:SUCCESS==xyz", events.get(0));
    Assert.assertEquals("ITEM:SUCCESS->RETURN", events.get(1));
    Assert.assertEquals("CB:FAILURE:123", events.get(2));
    Assert.assertEquals("ITEM:FAILURE", events.get(3));
  }
}
