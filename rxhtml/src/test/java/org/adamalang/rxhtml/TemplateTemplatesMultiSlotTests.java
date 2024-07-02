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

public class TemplateTemplatesMultiSlotTests extends BaseRxHtmlTest {
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
    gold.append("\n  // <template name=\"temp\">");
    gold.append("\n  $.TP('temp', function(a,b,c,d) {");
    gold.append("\n    var e=$.X();");
    gold.append("\n    a.append($.T(' This is a template with a fragment: '));");
    gold.append("\n    c(a,b,'');");
    gold.append("\n    a.append($.T(' And this is a slot '));");
    gold.append("\n    c(a,b,'foo');");
    gold.append("\n  });");
    gold.append("\n");
    gold.append("\n  // <page uri=\"/\">");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c=$.X();");
    gold.append("\n    b.append($.T(' This is a page which is going to use the template. '));");
    gold.append("\n");
    gold.append("\n    // <div rx:template=\"temp\">");
    gold.append("\n    var f=$.E('div');");
    gold.append("\n    $.UT(f,a,'temp', function(g,h,i) {");
    gold.append("\n      g.append($.T(' This is data within the template, and this is shown always. '));");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"foo\">");
    gold.append("\n      if (i == 'foo') {");
    gold.append("\n        var j=$.E('div');");
    gold.append("\n        j.append($.T(' This only shows up when the case is foo. '));");
    gold.append("\n        g.append(j);");
    gold.append("\n      }");
    gold.append("\n    },{});");
    gold.append("\n    b.append(f);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\nStyle:");
    gold.append("\nShell:<!DOCTYPE html>");
    gold.append("\n<html>");
    gold.append("\n<head><script src=\"/libadama.js/GENMODE.js\"></script><script>");
    gold.append("\n");
    gold.append("\n(function($){");
    gold.append("\n");
    gold.append("\n  // <template name=\"temp\">");
    gold.append("\n  $.TP('temp', function(a,b,c,d) {");
    gold.append("\n    var e=$.X();");
    gold.append("\n    a.append($.T(' This is a template with a fragment: '));");
    gold.append("\n    c(a,b,'');");
    gold.append("\n    a.append($.T(' And this is a slot '));");
    gold.append("\n    c(a,b,'foo');");
    gold.append("\n  });");
    gold.append("\n");
    gold.append("\n  // <page uri=\"/\">");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c=$.X();");
    gold.append("\n    b.append($.T(' This is a page which is going to use the template. '));");
    gold.append("\n");
    gold.append("\n    // <div rx:template=\"temp\">");
    gold.append("\n    var f=$.E('div');");
    gold.append("\n    $.UT(f,a,'temp', function(g,h,i) {");
    gold.append("\n      g.append($.T(' This is data within the template, and this is shown always. '));");
    gold.append("\n");
    gold.append("\n      // <div rx:case=\"foo\">");
    gold.append("\n      if (i == 'foo') {");
    gold.append("\n        var j=$.E('div');");
    gold.append("\n        j.append($.T(' This only shows up when the case is foo. '));");
    gold.append("\n        g.append(j);");
    gold.append("\n      }");
    gold.append("\n    },{});");
    gold.append("\n    b.append(f);");
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
    source.append("\n    <template name=\"temp\">");
    source.append("\n        This is a template with a fragment: <fragment />");
    source.append("\n        And this is a slot <fragment case=\"foo\" />");
    source.append("\n    </template>");
    source.append("\n    <page uri=\"/\">");
    source.append("\n        This is a page which is going to use the template.");
    source.append("\n        <div rx:template=\"temp\">");
    source.append("\n            This is data within the template, and this is shown always.");
    source.append("\n            <div rx:case=\"foo\">");
    source.append("\n                This only shows up when the case is foo.");
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
