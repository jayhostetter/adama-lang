/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.transforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.adamalang.ErrorCodes;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.Json;
import org.adamalang.frontend.Session;
import org.adamalang.contracts.data.AuthenticatedUser;
import org.adamalang.web.io.ConnectionContext;

import java.security.KeyPair;
import java.util.Base64;
import java.util.regex.Pattern;

/** This is a per session Authenticator. This is in 1:1 correspondence to a session/connection */
public abstract class PerSessionAuthenticator {
  protected ConnectionContext defaultContext;

  public PerSessionAuthenticator(ConnectionContext defaultContext) {
    this.defaultContext = defaultContext;
  }

  public abstract ConnectionContext getDefaultContext();

  /** update the default asset key within the default context */
  @Deprecated
  public void updateAssetKey(String assetKey) {
    this.defaultContext = new ConnectionContext(defaultContext.origin, defaultContext.remoteIp, defaultContext.userAgent, assetKey);
  }

  /** get the asset key for the default context. If the session's connection is a user, then this is the user's asset key. */
  @Deprecated
  public String assetKey() {
    return defaultContext.assetKey;
  }

  /** log the user details into */
  public static void logInto(AuthenticatedUser user, ObjectNode node) {
    if (user != null) {
      if ("adama".equals(user.who.agent)) {
        node.put("user-id", user.id);
      }
      if (user.who != null) {
        node.put("principal-agent", user.who.agent);
        node.put("principal-authority", user.who.authority);
      }
      node.put("user-ip", user.context.remoteIp);
      node.put("user-origin", user.context.origin);
      node.put("user-agent", user.context.userAgent);
    }
  }

  public abstract void execute(Session session, String identity, Callback<AuthenticatedUser> callback);

  /** a pre-validated parsed token; we parse to find which keys to look up */
  public static class ParsedToken {
    public final String iss;
    public final String sub;
    public final int key_id;
    public final int proxy_user_id;
    public final String proxy_authority;
    public final String proxy_origin;
    public final String proxy_ip;
    public final String proxy_asset_key;
    public final String proxy_useragent;

    public ParsedToken(String token) throws ErrorCodeException {
      String[] parts = token.split(Pattern.quote("."));
      if (parts.length == 3) {
        try {
          String middle = new String(Base64.getDecoder().decode(parts[1]));
          JsonMapper mapper = new JsonMapper();
          JsonNode treeRaw = mapper.readTree(middle);
          if (treeRaw != null && treeRaw.isObject()) {
            ObjectNode tree = (ObjectNode) treeRaw;
            JsonNode _iss = tree.get("iss");
            JsonNode _sub = tree.get("sub");
            JsonNode _key_id = tree.get("kid");
            if (_key_id != null && _key_id.isIntegralNumber()) {
              this.key_id = _key_id.asInt();
              this.proxy_user_id = tree.get("puid").asInt();
              this.proxy_authority = Json.readString(tree, "pa");
              this.proxy_origin = Json.readString(tree, "po");
              this.proxy_ip = Json.readString(tree, "pip");
              this.proxy_asset_key = Json.readString(tree, "pak");
              this.proxy_useragent = Json.readString(tree, "pua");
            } else {
              this.key_id = -1;
              this.proxy_user_id = 0;
              this.proxy_authority = null;
              this.proxy_origin = null;
              this.proxy_ip = null;
              this.proxy_asset_key = null;
              this.proxy_useragent = null;
            }
            if (_iss != null && _iss.isTextual() && _sub != null && _sub.isTextual()) {
              this.iss = _iss.textValue();
              this.sub = _sub.textValue();
              return;
            }
          }
          throw new ErrorCodeException(ErrorCodes.AUTH_INVALID_TOKEN_JSON_COMPLETE);
        } catch (Exception ex) {
          throw new ErrorCodeException(ErrorCodes.AUTH_INVALID_TOKEN_JSON, ex);
        }
      } else {
        throw new ErrorCodeException(ErrorCodes.AUTH_INVALID_TOKEN_LAYOUT);
      }
    }
  }
}
