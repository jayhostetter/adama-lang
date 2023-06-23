/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.Json;

/** generated class for the responder: claim-result */
public class ClientClaimResultResponse {
  public final ObjectNode _original;
  public final String authority;

  public ClientClaimResultResponse(ObjectNode response) {
    this._original = response;
    this.authority = Json.readString(response, "authority");
  }
}