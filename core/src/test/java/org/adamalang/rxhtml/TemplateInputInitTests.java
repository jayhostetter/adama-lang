/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.rxhtml;

public class TemplateInputInitTests extends BaseRxHtmlTest {
  @Override
  public String issues() {
    StringBuilder issues = new StringBuilder();
    issues.append("");
    return issues.toString();
  }
  @Override
  public String gold() {
    StringBuilder gold = new StringBuilder();
    gold.append("(function($){");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    var c = $.E('input');");
    gold.append("\n    c.setAttribute('name','email');");
    gold.append("\n    {");
    gold.append("\n      var d = {};");
    gold.append("\n      d.__dom = c;");
    gold.append("\n      var e = (function() {");
    gold.append("\n        this.__dom.value=this['value'];");
    gold.append("\n      }).bind(d);");
    gold.append("\n      $.Y(a,d,'value',e);");
    gold.append("\n      e();");
    gold.append("\n    }");
    gold.append("\n    b.append(c);");
    gold.append("\n    var c = $.E('textarea');");
    gold.append("\n    c.setAttribute('name','email');");
    gold.append("\n    {");
    gold.append("\n      var d = {};");
    gold.append("\n      d.__dom = c;");
    gold.append("\n      var e = (function() {");
    gold.append("\n        this.__dom.value=this['value'];");
    gold.append("\n      }).bind(d);");
    gold.append("\n      $.Y(a,d,'value',e);");
    gold.append("\n      e();");
    gold.append("\n    }");
    gold.append("\n    b.append(c);");
    gold.append("\n    var c = $.E('select');");
    gold.append("\n    c.setAttribute('name','email');");
    gold.append("\n    {");
    gold.append("\n      var d = {};");
    gold.append("\n      d.__dom = c;");
    gold.append("\n      var e = (function() {");
    gold.append("\n        this.__dom.setAttribute('value',this['value']);");
    gold.append("\n      }).bind(d);");
    gold.append("\n      $.Y(a,d,'value',e);");
    gold.append("\n      e();");
    gold.append("\n    }");
    gold.append("\n    var d = $.E('option');");
    gold.append("\n    d.setAttribute('value','foo');");
    gold.append("\n    d.append($.T('FOOO'));");
    gold.append("\n    c.append(d);");
    gold.append("\n    b.append(c);");
    gold.append("\n    var c = $.E('input');");
    gold.append("\n    c.setAttribute('type','checkbox');");
    gold.append("\n    c.setAttribute('checked',true);");
    gold.append("\n    b.append(c);");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    return gold.toString();
  }
  @Override
  public String source() {
    StringBuilder source = new StringBuilder();
    source.append("<forest>");
    source.append("\n    <page uri=\"/\">");
    source.append("\n        <input name=\"email\" value=\"{value}\"/>");
    source.append("\n        <textarea name=\"email\" value=\"{value}\"></textarea>");
    source.append("\n        <select name=\"email\" value=\"{value}\">");
    source.append("\n            <option value=\"foo\">FOOO</option>");
    source.append("\n        </select>");
    source.append("\n        <input type=\"checkbox\" checked />");
    source.append("\n    </page>");
    source.append("\n</forest>");
    return source.toString();
  }
}