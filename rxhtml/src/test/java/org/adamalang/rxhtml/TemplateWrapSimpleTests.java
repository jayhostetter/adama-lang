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

public class TemplateWrapSimpleTests extends BaseRxHtmlTest {
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
    gold.append("\n    // <div rx:wrap=\"custom_wrapper\">");
    gold.append("\n    var d=$.E('div');");
    gold.append("\n");
    gold.append("\n    // <div rx:wrap=\"custom_wrapper\">");
    gold.append("\n    var h=$.RX([]);");
    gold.append("\n    $.WP(d,a,'custom_wrapper',h,function(f,e,g) {");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"a\">");
    gold.append("\n      if (g == 'a') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' A '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"b\">");
    gold.append("\n      if (g == 'b') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' B '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"c\">");
    gold.append("\n      if (g == 'c') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' C '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"d\">");
    gold.append("\n      if (g == 'd') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' D '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n    });");
    gold.append("\n    h.__();");
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
    gold.append("\n    // <div rx:wrap=\"custom_wrapper\">");
    gold.append("\n    var d=$.E('div');");
    gold.append("\n");
    gold.append("\n    // <div rx:wrap=\"custom_wrapper\">");
    gold.append("\n    var h=$.RX([]);");
    gold.append("\n    $.WP(d,a,'custom_wrapper',h,function(f,e,g) {");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"a\">");
    gold.append("\n      if (g == 'a') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' A '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"b\">");
    gold.append("\n      if (g == 'b') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' B '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"c\">");
    gold.append("\n      if (g == 'c') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' C '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"d\">");
    gold.append("\n      if (g == 'd') {");
    gold.append("\n        var i=$.E('div');");
    gold.append("\n        i.append($.T(' D '));");
    gold.append("\n        f.append(i);");
    gold.append("\n      }");
    gold.append("\n    });");
    gold.append("\n    h.__();");
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
    source.append("\n        <div rx:wrap=\"custom_wrapper\">");
    source.append("\n            <div rx:case=\"a\">");
    source.append("\n                A");
    source.append("\n            </div>");
    source.append("\n            <div rx:case=\"b\">");
    source.append("\n                B");
    source.append("\n            </div>");
    source.append("\n            <div rx:case=\"c\">");
    source.append("\n                C");
    source.append("\n            </div>");
    source.append("\n            <div rx:case=\"d\">");
    source.append("\n                D");
    source.append("\n            </div>");
    source.append("\n        </div>");
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
