/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.rxhtml.template.elements;

import org.adamalang.rxhtml.Template;
import org.adamalang.rxhtml.template.Environment;

import java.util.HashMap;

public class Lookup {


  private static enum SubscribeMethod {
    Set,
    TurnIntoArray,
    Push
  }

  private static SubscribeMethod subscribeTest(String name, HashMap<String, Integer> subscriptionCounts) {
    Integer prior = subscriptionCounts.get(name);
    if (prior == null) {
      subscriptionCounts.put(name, 1);
      return SubscribeMethod.Set;
    } else if (prior == 1) {
      subscriptionCounts.put(name, 2);
      return SubscribeMethod.TurnIntoArray;
    } else {
      subscriptionCounts.put(name, prior + 1);
      return SubscribeMethod.Push;
    }
  }

  private static String wrapTransform(String expression, String transform) {
    if (transform == null || "".equals(transform)) {
      return expression;
    }
    if ("ntclient.agent".equalsIgnoreCase(transform)) {
      return expression + ".agent";
    }
    return expression;
  }

  public static void writeLookup(Environment env) {
    String eVar = env.pool.ask();
    String transform = env.element.attr("transform");
    env.writer.tab().append("{").tabUp().newline();
    env.writer.tab().append("var ").append(eVar).append(" = $.t(").append(wrapTransform(env.current != null ? (env.current + "." + env.name) : "''", transform)).append(");").newline();
    SubscribeMethod method = subscribeTest(env.name, env.subscriptionCounts);
    switch (method) {
      case Set:
        env.writer.tab().append("_.").append(env.name).append(" = ");
        break;
      case TurnIntoArray:
        env.writer.tab().append("_.").append(env.name).append(" = [_.").append(env.name).append(",");
        break;
      case Push:
        env.writer.tab().append("_.").append(env.name).append(".push(");
        break;
    }
    env.writer.append("function(x) {").newline().tabUp();
    env.writer.tab().append("$.s(").append(eVar).append(",").append(wrapTransform("x.value", transform)).append(");").newline().tabDown();
    env.writer.tab().append("}");
    switch (method) {
      case Set:
        env.writer.append(";");
        break;
      case TurnIntoArray:
        env.writer.append("];");
        break;
      case Push:
        env.writer.append(");");
        break;
    }
    env.writer.newline();
    env.writer.tab().append(env.parentVariable).append(".append(").append(eVar).append(");").newline();
    env.writer.tabDown().tab().append("}").newline();
    env.pool.give(eVar);
  }
}