/*
* Adama Platform and Language
* Copyright (C) 2021 - 2023 by Adama Platform Initiative, LLC
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

import org.adamalang.common.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DefaultPolicy {
  public static ObjectNode make() {
    ObjectNode policy = Json.newJsonObject();
    ObjectNode child;
    child = policy.putObject("init/setup-account");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("init/convert-google-user");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("init/complete-account");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("deinit");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("account/set-password");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("account/get-payment-plan");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("account/login");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("probe");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("stats");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("identity/hash");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("identity/stash");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("authority/create");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("authority/set");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("authority/get");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("authority/list");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("authority/destroy");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/create");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/generate-key");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/get");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/set");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/redeploy-kick");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/set-rxhtml");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/get-rxhtml");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/set-policy");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("policy/generate-default");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/get-policy");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/metrics");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/delete");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/set-role");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/list-developers");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/reflect");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("space/list");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("push/register");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/map");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/claim-apex");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/redirect");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/configure");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/reflect");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/map-document");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/list");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/list-by-space");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/get-vapid-public-key");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/unmap");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("domain/get");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/download-archive");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/list-backups");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/download-backup");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/list-push-tokens");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorization");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorization-domain");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorize");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorize-domain");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorize-with-reset");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/authorize-domain-with-reset");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/create");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/delete");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("document/list");
    child.put("developers",true);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("message/direct-send");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("message/direct-send-once");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("connection/create");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("connection/create-via-domain");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("documents/hash-password");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("billing-connection/create");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("attachment/start");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    child = policy.putObject("attachment/start-by-domain");
    child.put("developers",false);
    child.putArray("allowed-authorities");
    child.putArray("allowed-documents");
    child.putArray("allowed-document-spaces");
    return policy;
  }
}