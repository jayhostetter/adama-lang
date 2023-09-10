/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.rxhtml;

public class TemplateBadDelayTimeTests extends BaseRxHtmlTest {
  @Override
  public boolean dev() {
    return false;
  }
  @Override
  public String issues() {
    StringBuilder issues = new StringBuilder();
    issues.append("WARNING:/:delay 'delayx' is not a well formed delay");
    issues.append("\nWARNING:/:delay 'delay:x' is not a well formed delay (failed to parse x as int)");
    return issues.toString();
  }
  @Override
  public String gold() {
    StringBuilder gold = new StringBuilder();
    gold.append("JavaScript:(function($){");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c = $.E('form');");
    gold.append("\n    $.aSD(c,a,'foo');");
    gold.append("\n    $.onS(c,'success',$.pV(a),'send_failed',false);");
    gold.append("\n    $.onS(c,'failure',$.pV(a),'send_failed',true);");
    gold.append("\n    var f = $.E('div');");
    gold.append("\n    $.oSBMT(f,'delayx',a);");
    gold.append("\n    c.append(f);");
    gold.append("\n    b.append(c);");
    gold.append("\n    var c = $.E('form');");
    gold.append("\n    $.aSD(c,a,'foo');");
    gold.append("\n    $.onS(c,'success',$.pV(a),'send_failed',false);");
    gold.append("\n    $.onS(c,'failure',$.pV(a),'send_failed',true);");
    gold.append("\n    var i = $.E('div');");
    gold.append("\n    $.oSBMT(i,'delay:x',a);");
    gold.append("\n    c.append(i);");
    gold.append("\n    b.append(c);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\nStyle:");
    gold.append("\nShell:<!DOCTYPE html>");
    gold.append("\n<html>");
    gold.append("\n<head><script src=\"https://aws-us-east-2.adama-platform.com/libadama.js\"></script><script>");
    gold.append("\n");
    gold.append("\n(function($){");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c = $.E('form');");
    gold.append("\n    $.aSD(c,a,'foo');");
    gold.append("\n    $.onS(c,'success',$.pV(a),'send_failed',false);");
    gold.append("\n    $.onS(c,'failure',$.pV(a),'send_failed',true);");
    gold.append("\n    var f = $.E('div');");
    gold.append("\n    $.oSBMT(f,'delayx',a);");
    gold.append("\n    c.append(f);");
    gold.append("\n    b.append(c);");
    gold.append("\n    var c = $.E('form');");
    gold.append("\n    $.aSD(c,a,'foo');");
    gold.append("\n    $.onS(c,'success',$.pV(a),'send_failed',false);");
    gold.append("\n    $.onS(c,'failure',$.pV(a),'send_failed',true);");
    gold.append("\n    var i = $.E('div');");
    gold.append("\n    $.oSBMT(i,'delay:x',a);");
    gold.append("\n    c.append(i);");
    gold.append("\n    b.append(c);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n</script><style>");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n</style></head><body></body><script>RxHTML.init();</script></html>");
    return gold.toString();
  }
  @Override
  public String source() {
    StringBuilder source = new StringBuilder();
    source.append("<forest>");
    source.append("\n    <page uri=\"/\">");
    source.append("\n        <form rx:action=\"send:foo\">");
    source.append("\n            <div rx:delayX=\"submit\"></div>");
    source.append("\n        </form>");
    source.append("\n        <form rx:action=\"send:foo\">");
    source.append("\n            <div rx:delay:X=\"submit\"></div>");
    source.append("\n        </form>");
    source.append("\n    </page>");
    source.append("\n</forest>");
    return source.toString();
  }
}