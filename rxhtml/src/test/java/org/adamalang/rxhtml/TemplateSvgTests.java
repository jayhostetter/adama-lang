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
package org.adamalang.rxhtml;

public class TemplateSvgTests extends BaseRxHtmlTest {
  @Override
  public boolean dev() {
    return false;
  }
  @Override
  public String issues() {
    StringBuilder issues = new StringBuilder();
    issues.append("");
    return issues.toString();
  }
  @Override
  public String gold() {
    StringBuilder gold = new StringBuilder();
    gold.append("JavaScript:(function($){");
    gold.append("\n");
    gold.append("\n  // <page uri=\"/\">");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c=$.X();");
    gold.append("\n");
    gold.append("\n    // <svg height=\"210\" width=\"400\">");
    gold.append("\n    var d=$.E('svg', 'http://www.w3.org/2000/svg');");
    gold.append("\n    $.SA(d,'height',\"210\");");
    gold.append("\n    $.SA(d,'width',\"400\");");
    gold.append("\n");
    gold.append("\n    // <path d=\"M150 0 L75 200 L225 200 Z\">");
    gold.append("\n    var e=$.E('path', 'http://www.w3.org/2000/svg');");
    gold.append("\n    $.SA(e,'d',\"M150 0 L75 200 L225 200 Z\");");
    gold.append("\n    d.append(e);");
    gold.append("\n    d.append($.T(' Sorry, your browser does not support inline SVG. '));");
    gold.append("\n    b.append(d);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\nStyle:");
    gold.append("\nShell:<!DOCTYPE html>");
    gold.append("\n<html>");
    gold.append("\n<head><script src=\"/libadama.js/GENMODE.js\"></script><script>");
    gold.append("\n");
    gold.append("\n(function($){");
    gold.append("\n");
    gold.append("\n  // <page uri=\"/\">");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c=$.X();");
    gold.append("\n");
    gold.append("\n    // <svg height=\"210\" width=\"400\">");
    gold.append("\n    var d=$.E('svg', 'http://www.w3.org/2000/svg');");
    gold.append("\n    $.SA(d,'height',\"210\");");
    gold.append("\n    $.SA(d,'width',\"400\");");
    gold.append("\n");
    gold.append("\n    // <path d=\"M150 0 L75 200 L225 200 Z\">");
    gold.append("\n    var e=$.E('path', 'http://www.w3.org/2000/svg');");
    gold.append("\n    $.SA(e,'d',\"M150 0 L75 200 L225 200 Z\");");
    gold.append("\n    d.append(e);");
    gold.append("\n    d.append($.T(' Sorry, your browser does not support inline SVG. '));");
    gold.append("\n    b.append(d);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n</script><style>");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n");
    gold.append("\n</style></head><body></body><script>");
    gold.append("\n  RxHTML.init();");
    gold.append("\n</script></html>");
    return gold.toString();
  }
  @Override
  public String source() {
    StringBuilder source = new StringBuilder();
    source.append("<forest>");
    source.append("\n    <page uri=\"/\">");
    source.append("\n");
    source.append("\n        <svg height=\"210\" width=\"400\">");
    source.append("\n            <path d=\"M150 0 L75 200 L225 200 Z\" />");
    source.append("\n            Sorry, your browser does not support inline SVG.");
    source.append("\n        </svg>");
    source.append("\n");
    source.append("\n    </page>");
    source.append("\n</forest>");
    return source.toString();
  }
  @Override
  public String schema() {
    StringBuilder gold = new StringBuilder();
    gold.append("{");
    gold.append("\n  \"/\" : { }");
    gold.append("\n}");
    return gold.toString();
  }
}
