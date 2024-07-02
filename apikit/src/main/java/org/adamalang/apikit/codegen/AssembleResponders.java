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
package org.adamalang.apikit.codegen;

import org.adamalang.apikit.model.FieldDefinition;
import org.adamalang.apikit.model.Responder;
import org.adamalang.apikit.model.Type;

import java.util.HashMap;
import java.util.Map;

public class AssembleResponders {
  public static Map<String, String> make(String packageName, Map<String, Responder> responders) throws Exception {
    HashMap<String, String> files = new HashMap<>();
    for (Responder responder : responders.values()) {
      { // server
        StringBuilder java = new StringBuilder();
        java.append("package ").append(packageName).append(";\n\n");
        for (String imp : responder.imports()) {
          java.append("import ").append(imp).append(";\n");
        }
        java.append("\n");
        java.append("public class ").append(responder.camelName).append("Responder {\n");
        java.append("  public final JsonResponder responder;\n");
        java.append("\n");
        java.append("  public ").append(responder.camelName).append("Responder(JsonResponder responder) {\n");
        java.append("    this.responder = responder;\n");
        java.append("  }\n\n");
        String[] names = responder.stream ? new String[]{"next"} : new String[]{"complete"};
        for (String mName : names) {
          boolean terminal = !mName.equals("next");

          java.append("  public void ").append(mName).append("(");
          boolean first = true;
          for (FieldDefinition fd : responder.fields) {
            if (!first) {
              java.append(", ");
            }
            first = false;
            java.append(fd.type.javaType()).append(" ").append(fd.camelName);
          }
          java.append(") {\n");
          java.append("    ObjectNode _obj = new JsonMapper().createObjectNode();\n");
          for (FieldDefinition fd : responder.fields) {
            String ext = "";
            if (fd.type == Type.JsonObject || fd.type == Type.JsonObjectOrArray) {
              java.append(ext + "    _obj.set(\"").append(fd.camelName).append("\", ").append(fd.camelName).append(");\n");
            } else {
              java.append(ext + "    _obj.put(\"").append(fd.camelName).append("\", ").append(fd.camelName).append(");\n");
            }
          }
          if (terminal) {
            java.append("    responder.finish(_obj.toString());\n");
          } else {
            java.append("    responder.stream(_obj.toString());\n");
          }
          java.append("  }\n\n");
        }

        if (responder.stream && responder.fields.length > 0) {
          java.append("  public void finish() {\n");
          java.append("    responder.finish(null);\n");
          java.append("  }\n\n");
        }
        java.append("  public void error(ErrorCodeException ex) {\n");
        java.append("    responder.error(ex);\n");
        java.append("  }\n");

        java.append("}\n");
        files.put(responder.camelName + "Responder.java", java.toString());
      }
      { // client
        // TODO: build the code to make the Java Client
      }
    }
    return files;
  }
}
