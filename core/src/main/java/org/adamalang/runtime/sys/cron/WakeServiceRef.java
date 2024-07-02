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
package org.adamalang.runtime.sys.cron;

import org.adamalang.common.Callback;
import org.adamalang.runtime.data.Key;

/** introduce a cyclyc connection between a WakeService and KeyAlarm (i.e. CoreService) */
public class WakeServiceRef implements WakeService {
  private WakeService ref;

  public WakeServiceRef() {
    this.ref = null;
  }

  public void set(WakeService ref) {
    this.ref = ref;
  }

  @Override
  public void wakeIn(Key key, long when, Callback<Void> callback) {
    ref.wakeIn(key, when, callback);
  }
}
