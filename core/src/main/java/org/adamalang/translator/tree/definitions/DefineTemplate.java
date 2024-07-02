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
package org.adamalang.translator.tree.definitions;

import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.parser.Formatter;
import org.adamalang.translator.tree.expressions.constants.TemplateConstant;

import java.util.function.Consumer;

/** defines a template with a document */
public class DefineTemplate extends Definition {
  public final Token templateToken;
  public final Token nameToken;
  public final Token colonToken;
  public final TemplateConstant value;

  public DefineTemplate(Token templateToken, Token nameToken, Token colonToken, TemplateConstant value) {
    this.templateToken = templateToken;
    this.nameToken = nameToken;
    this.colonToken = colonToken;
    this.value = value;
    ingest(templateToken);
    ingest(value);
  }

  @Override
  public void emit(Consumer<Token> yielder) {
    yielder.accept(templateToken);
    yielder.accept(nameToken);
    yielder.accept(colonToken);
    value.emit(yielder);
  }

  @Override
  public void format(Formatter formatter) {
    formatter.startLine(templateToken);
    value.format(formatter);
  }
}
