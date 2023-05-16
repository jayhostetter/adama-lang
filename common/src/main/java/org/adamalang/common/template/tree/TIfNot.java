/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common.template.tree;

import com.fasterxml.jackson.databind.JsonNode;
import org.adamalang.common.template.Settings;

/** if the given variable is false, then show the enclosed template */
public class TIfNot implements T {
  private final String variable;
  private final T child;

  public TIfNot(String variable, T child) {
    this.variable = variable;
    this.child = child;
  }

  @Override
  public void render(Settings settings, JsonNode node, StringBuilder output) {
    JsonNode test = node.get(variable);
    if (test != null && test.isBoolean() && !test.booleanValue()) {
      child.render(settings, node, output);
    }
  }
}