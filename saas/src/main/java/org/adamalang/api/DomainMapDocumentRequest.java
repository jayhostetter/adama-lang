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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.auth.AuthenticatedUser;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.NamedRunnable;
import org.adamalang.contracts.data.DomainWithPolicy;
import org.adamalang.contracts.data.SpacePolicy;
import org.adamalang.frontend.Session;
import org.adamalang.validators.ValidateKey;
import org.adamalang.validators.ValidateSpace;
import org.adamalang.web.io.*;

/** Map a domain to a space. */
public class DomainMapDocumentRequest {
  public final String identity;
  public final AuthenticatedUser who;
  public final String domain;
  public final DomainWithPolicy resolvedDomain;
  public final String space;
  public final SpacePolicy policy;
  public final String key;
  public final Boolean route;
  public final String certificate;

  public DomainMapDocumentRequest(final String identity, final AuthenticatedUser who, final String domain, final DomainWithPolicy resolvedDomain, final String space, final SpacePolicy policy, final String key, final Boolean route, final String certificate) {
    this.identity = identity;
    this.who = who;
    this.domain = domain;
    this.resolvedDomain = resolvedDomain;
    this.space = space;
    this.policy = policy;
    this.key = key;
    this.route = route;
    this.certificate = certificate;
  }

  public static void resolve(Session session, GlobalConnectionNexus nexus, JsonRequest request, Callback<DomainMapDocumentRequest> callback) {
    try {
      final BulkLatch<DomainMapDocumentRequest> _latch = new BulkLatch<>(nexus.executor, 3, callback);
      final String identity = request.getString("identity", true, 458759);
      final LatchRefCallback<AuthenticatedUser> who = new LatchRefCallback<>(_latch);
      final String domain = request.getStringNormalize("domain", true, 488444);
      final LatchRefCallback<DomainWithPolicy> resolvedDomain = new LatchRefCallback<>(_latch);
      final String space = request.getStringNormalize("space", true, 461828);
      ValidateSpace.validate(space);
      final LatchRefCallback<SpacePolicy> policy = new LatchRefCallback<>(_latch);
      final String key = request.getString("key", true, 466947);
      ValidateKey.validate(key);
      final Boolean route = request.getBoolean("route", false, 0);
      final String certificate = request.getString("certificate", false, 0);
      _latch.with(() -> new DomainMapDocumentRequest(identity, who.get(), domain, resolvedDomain.get(), space, policy.get(), key, route, certificate));
      nexus.identityService.execute(session, identity, who);
      nexus.domainService.execute(session, domain, resolvedDomain);
      nexus.spaceService.execute(session, space, policy);
    } catch (ErrorCodeException ece) {
      nexus.executor.execute(new NamedRunnable("domainmapdocument-error") {
        @Override
        public void execute() throws Exception {
          callback.failure(ece);
        }
      });
    }
  }

  public void logInto(ObjectNode _node) {
    org.adamalang.transforms.PerSessionAuthenticator.logInto(who, _node);
    _node.put("domain", domain);
    org.adamalang.contracts.DomainWithPolicyResolver.logInto(resolvedDomain, _node);
    _node.put("space", space);
    org.adamalang.contracts.SpacePolicyLocator.logInto(policy, _node);
    _node.put("key", key);
    _node.put("route", route);
  }
}
