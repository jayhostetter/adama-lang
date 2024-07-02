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
package org.adamalang.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.Json;

/** generated class for the responder: reflection */
public class ClientReflectionResponse {
  public final ObjectNode _original;
  public final ObjectNode reflection;

  public ClientReflectionResponse(ObjectNode response) {
    this._original = response;
    this.reflection = Json.readObject(response, "reflection");
  }
  public String toInternalJson() {
    ObjectNode _next = Json.newJsonObject();
    _next.set("reflection", reflection);
    return _next.toString();
  }
}
