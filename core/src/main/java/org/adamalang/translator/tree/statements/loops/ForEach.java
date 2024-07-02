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
package org.adamalang.translator.tree.statements.loops;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.parser.Formatter;
import org.adamalang.translator.tree.common.StringBuilderWithTabs;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.statements.Block;
import org.adamalang.translator.tree.statements.ControlFlow;
import org.adamalang.translator.tree.statements.Statement;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.traits.details.DetailContainsAnEmbeddedType;

import java.util.function.Consumer;

/** a modern and safe foreach(V in EXPR) code */
public class ForEach extends Statement {
  public final Block code;
  public final Token endParen;
  public final Token foreachToken;
  public final Token inToken;
  public final Expression iterable;
  public final Token openParen;
  public final String variable;
  public final Token variableToken;
  private TyType elementType;

  public ForEach(final Token foreachToken, final Token openParen, final Token variableToken, final Token inToken, final Expression iterable, final Token endParen, final Block code) {
    this.foreachToken = foreachToken;
    this.openParen = openParen;
    variable = variableToken.text;
    this.variableToken = variableToken;
    this.inToken = inToken;
    this.iterable = iterable;
    this.endParen = endParen;
    this.code = code;
    elementType = null;
    ingest(foreachToken);
    ingest(iterable);
    ingest(code);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    yielder.accept(foreachToken);
    yielder.accept(openParen);
    yielder.accept(variableToken);
    yielder.accept(inToken);
    iterable.emit(yielder);
    yielder.accept(endParen);
    code.emit(yielder);
  }

  @Override
  public void format(Formatter formatter) {
    iterable.format(formatter);
    code.format(formatter);
  }

  @Override
  public ControlFlow typing(final Environment environment) {
    if (environment.defined(variable)) {
      environment.document.createError(this, String.format("The variable '" + variable + "' is already defined"));
    }
    final var type = iterable.typing(environment.scopeWithComputeContext(ComputeContext.Computation), null /* we know nothing to suggest */);
    if (type != null) {
        if (environment.rules.IsIterable(type, false)) {
          elementType = ((DetailContainsAnEmbeddedType) type).getEmbeddedType(environment);
        }
      if (elementType != null) {
        final var next = environment.scopeWithComputeContext(ComputeContext.Computation);
        next.define(variable, elementType, false, elementType);
        code.typing(next);
      }
    }
    return ControlFlow.Open;
  }

  @Override
  public void writeJava(final StringBuilderWithTabs sb, final Environment environment) {
    if (elementType != null) {
      sb.append("for(").append(elementType.getJavaBoxType(environment)).append(" ").append(variable).append(" : ");
      iterable.writeJava(sb, environment.scopeWithComputeContext(ComputeContext.Computation));
      sb.append(") ");
      final var next = environment.scopeWithComputeContext(ComputeContext.Computation);
      next.define(variable, elementType, false, elementType);
      code.writeJava(sb, next);
    }
  }

  @Override
  public void free(FreeEnvironment environment) {
    iterable.free(environment);
    code.free(environment.push());
  }
}
