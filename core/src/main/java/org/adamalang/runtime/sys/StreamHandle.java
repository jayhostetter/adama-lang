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
package org.adamalang.runtime.sys;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.PrivateView;

/** Since connections hold onto a stream, we need a way to swapping out the private view */
public class StreamHandle {
  private PrivateView current;

  public StreamHandle(PrivateView current) {
    this.current = current;
    current.link(this);
  }

  public void set(PrivateView view) {
    this.current = view;
  }

  public void ingestViewUpdate(JsonStreamReader reader) {
    current.ingestViewUpdate(reader);
  }

  public void triggerRefresh() {
    current.triggerRefresh();
  }

  public int getViewId() {
    return current.getViewId();
  }

  public void kill() {
    current.kill();
  }

  public void disconnect() {
    current.perspective.disconnect();
  }
}
