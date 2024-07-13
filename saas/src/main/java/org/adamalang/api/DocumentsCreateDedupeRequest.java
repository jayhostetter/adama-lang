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
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.NamedRunnable;
import org.adamalang.frontend.Session;
import org.adamalang.web.io.*;

/** Ask the server to create a dedupe token */
public class DocumentsCreateDedupeRequest {

  public DocumentsCreateDedupeRequest() {
  }

  public static void resolve(Session session, RegionConnectionNexus nexus, JsonRequest request, Callback<DocumentsCreateDedupeRequest> callback) {
    nexus.executor.execute(new NamedRunnable("documentscreatededupe-error") {
      @Override
        public void execute() throws Exception {
          callback.success(new DocumentsCreateDedupeRequest());
        }
      });
  }

  public void logInto(ObjectNode _node) {
  }
}
