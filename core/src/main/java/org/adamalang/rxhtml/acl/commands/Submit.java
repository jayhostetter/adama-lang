/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.rxhtml.acl.commands;

import org.adamalang.rxhtml.template.Environment;

/** search for a parent form and then submit it */
public class Submit implements Command {
  public Submit() {
  }

  @Override
  public void write(Environment env, String type, String eVar) {
    env.writer.tab().append("$.oSBMT(").append(eVar).append(",'").append(type).append("',").append(env.stateVar).append(");").newline();
  }
}