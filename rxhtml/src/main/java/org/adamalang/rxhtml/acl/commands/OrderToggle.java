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
package org.adamalang.rxhtml.acl.commands;

import org.adamalang.rxhtml.template.Environment;
import org.adamalang.rxhtml.template.StatePath;
import org.adamalang.rxhtml.typing.ViewScope;

/** Flip/flop (i.e. toggle) a boolean value between false and true */
public class OrderToggle implements Command {
  public final String path;
  public final String value;

  public OrderToggle(String path, String value) {
    if (path.startsWith("view:") | path.startsWith("data:")) {
      this.path = path;
    } else {
      this.path = "view:" + path;
    }
    this.value = value;
  }

  @Override
  public void writeTypes(ViewScope vs) {
    vs.write(this.path, "order-string", false);
  }

  @Override
  public void write(Environment env, String type, String eVar) {
    StatePath path = StatePath.resolve(this.path, env.stateVar);
    env.writer.tab().append("$.onOT(").append(eVar).append(",'").append(type).append("',").append(path.command).append(",'").append(path.name).append("','").append(value).append("');").newline();
  }
}
