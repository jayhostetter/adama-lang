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
package org.adamalang.rxhtml.atl;

import java.util.ArrayList;
import java.util.Iterator;

public class TokenStream {

  public static ArrayList<Token> tokenize(String text) throws ParseException {
    ArrayList<Token> tokens = new ArrayList<>();
    final StringBuilder currentText = new StringBuilder();
    ScanState state = ScanState.Text;
    Iterator<Integer> it = text.codePoints().iterator();
    ArrayList<String> operands = new ArrayList<>();

    Runnable cutText = () -> {
      String result = currentText.toString();
      if (result.length() > 0) {
        currentText.setLength(0);
        tokens.add(new Token(Type.Text, result));
      }
    };

    Runnable cutOperand = () -> {
      String result = currentText.toString().trim();
      currentText.setLength(0);
      operands.add(result);
    };

    while (it.hasNext()) {
      int cp = it.next();
      switch (state) {
        case Text:
          if (cp == '`') {
            if (it.hasNext()) {
              cp = it.next();
              if (cp == '`') {
                state = ScanState.Escape;
                break;
              }
            }
            transferCharacter(currentText, cp);
          } else if (cp == '[') {
            cutText.run();
            state = ScanState.Condition;
          } else if (cp == '{') {
            cutText.run();
            state = ScanState.Variable;
          } else {
            transferCharacter(currentText, cp);
          }
          break;
        case Escape:
          if (cp == state.end) {
            cutText.run();
            state = ScanState.Text;
          } else {
            transferCharacter(currentText, cp);
          }
          break;
        case Condition:
        case Variable:
          if (cp == state.end) {
            cutOperand.run();
            if (operands.size() > 0) {
              String base = operands.remove(0);
              tokens.add(new Token(state.type, base, operands.toArray(new String[operands.size()])));
              operands.clear();
            }
            state = ScanState.Text;
          } else if (cp == '|') {
            cutOperand.run();
          } else {
            transferCharacter(currentText, cp);
          }
      }
    }
    switch (state) {
      case Condition:
        throw new ParseException("condition token not closed");
      case Variable:
        throw new ParseException("variable token not closed");
      case Escape:
        throw new ParseException("big-escape (```) not closed off");
      default:
        cutText.run();
        return tokens;
    }
  }

  private static void transferCharacter(StringBuilder currentText, int cp) {
    currentText.append(Character.toString(cp));
    if (cp == '\\') {
      currentText.append(Character.toString(cp));
    }
  }

  private enum ScanState {
    Text(' ', Type.Text), //
    Variable('}', Type.Variable), //
    Condition(']', Type.Condition), //
    Escape('`', Type.Text); //

    public final char end;
    public final Type type;

    ScanState(char end, Type type) {
      this.end = end;
      this.type = type;
    }
  }

  public enum Type {
    Text, Variable, Condition
  }

  public enum Modifier {
    None, Not, Else, End,
  }

  public static class Token {
    public final Type type;
    public final Modifier mod;
    public final String base;
    public final String[] transforms;

    public Token(Type type, String base, String... transforms) {
      this.type = type;
      if (base.startsWith("#") && type == Type.Condition) {
        this.mod = Modifier.Else;
        this.base = base.substring(1).trim();
      } else if (base.startsWith("!")) {
        this.mod = Modifier.Not;
        this.base = base.substring(1).trim();
      } else if (base.startsWith("/") && type == Type.Condition) {
        this.mod = Modifier.End;
        this.base = base.substring(1).trim();
      } else {
        this.mod = Modifier.None;
        this.base = base;
      }
      this.transforms = transforms;
    }
  }
}
