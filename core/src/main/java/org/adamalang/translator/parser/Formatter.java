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
package org.adamalang.translator.parser;

import org.adamalang.translator.parser.token.Token;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Formatter {
  private static String[] TAB_CACHE = makeTabCache();

  private static String[] makeTabCache() {
    String current = "";
    String[] cache = new String[40];
    for (int k = 0; k < cache.length; k++) {
      cache[k] = current;
      current += "  ";
    }
    return cache;
  }

  private int tab;
  private String tabCache;

  private void updateTab() {
    if (0 <= tab && tab < TAB_CACHE.length) {
      tabCache = TAB_CACHE[tab];
    } else {
      tabCache = "";
      for (int k = 0; k < tab; k++) {
        tabCache += "  ";
      }
    }
  }

  public Formatter() {
    this.tab = 0;
    updateTab();
  }

  public void tabUp() {
    this.tab++;
    updateTab();
  }

  public void startLine(Token t) {
    if (t.nonSemanticTokensPrior == null) {
      t.nonSemanticTokensPrior = new ArrayList<>();
      t.nonSemanticTokensPrior.add(Token.WRAP(tabCache));
    }
  }

  public void endLine(Token t) {
    if (t.nonSemanticTokensAfter == null) {
      t.nonSemanticTokensAfter = new ArrayList<>();
    }
    t.nonSemanticTokensAfter.add(Token.WS("\n"));
  }

  public void tabDown() {
    this.tab--;
    updateTab();
  }

  public static class FirstAndLastToken implements Consumer<Token> {
    public Token first;
    public Token last;

    public FirstAndLastToken() {
      this.first = null;
      this.last = null;
    }

    @Override
    public void accept(Token token) {
      if (first == null) {
        first = token;
      }
      last = token;
    }
  }

}
