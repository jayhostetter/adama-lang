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
package org.adamalang.translator.tree.expressions.linq;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.parser.Formatter;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.TyNativeFunctional;
import org.adamalang.translator.tree.types.natives.TyNativeMap;
import org.adamalang.translator.tree.types.natives.functions.FunctionOverloadInstance;
import org.adamalang.translator.tree.types.natives.functions.FunctionPaint;
import org.adamalang.translator.tree.types.natives.functions.FunctionStyleJava;
import org.adamalang.translator.tree.types.traits.IsStructure;
import org.adamalang.translator.tree.types.traits.details.DetailComputeRequiresGet;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Reduce extends LinqExpression {
  public final Token fieldToken;
  public final Expression functionToReduceWith;
  public final Token onToken;
  public final Token reduceToken;
  public final Token viaToken;
  private FunctionOverloadInstance functionInstance;
  private TyNativeFunctional functionalType;
  private boolean requireGet;

  public Reduce(final Expression sql, final Token reduceToken, final Token onToken, final Token fieldToken, final Token viaToken, final Expression functionToReduceWith) {
    super(sql);
    this.reduceToken = reduceToken;
    this.onToken = onToken;
    this.fieldToken = fieldToken;
    this.viaToken = viaToken;
    this.functionToReduceWith = functionToReduceWith;
    functionalType = null;
    functionInstance = null;
    requireGet = false;
    ingest(sql);
    ingest(fieldToken);
    ingest(functionToReduceWith);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    sql.emit(yielder);
    yielder.accept(reduceToken);
    if (onToken != null) {
      yielder.accept(onToken);
    }
    yielder.accept(fieldToken);
    if (viaToken != null) {
      yielder.accept(viaToken);
      functionToReduceWith.emit(yielder);
    }
  }

  @Override
  public void format(Formatter formatter) {
    sql.format(formatter);
    if (viaToken != null) {
      functionToReduceWith.format(formatter);
    }
  }

  @Override
  protected TyType typingInternal(final Environment environment, final TyType suggestion) {
    final var typeSql = sql.typing(environment, null);
    final var isGoodSql = environment.rules.IsNativeListOfStructure(typeSql, false);
    // validate the function
    if (isGoodSql && viaToken != null) {
      ArrayList<TyType> guessInputTypes = new ArrayList<>();
      guessInputTypes.add(typeSql);
      FunctionOverloadInstance guess = new FunctionOverloadInstance("unknown", null, guessInputTypes, FunctionPaint.READONLY_NORMAL);
      TyType guessType = new TyNativeFunctional("unknown", FunctionOverloadInstance.WRAP(guess), FunctionStyleJava.None);
      TyType funcType = functionToReduceWith.typing(environment, guessType);
      if (environment.rules.IsFunction(funcType, false)) {
        functionalType = (TyNativeFunctional) funcType;
        final var expectedArgs = new ArrayList<TyType>();
        expectedArgs.add(typeSql);
        functionInstance = functionalType.find(this, expectedArgs, environment);
        if (functionInstance != null) {
          if (!functionInstance.pure) {
            environment.document.createError(this, String.format("Function '%s' must be a pure function a value", funcType.getAdamaType()));
          }
          if (functionInstance.returnType == null) {
            environment.document.createError(this, String.format("Function '%s' must return value", funcType.getAdamaType()));
          }
        }
      }
    }
    if (isGoodSql) {
      final var elementType = (IsStructure) environment.rules.ExtractEmbeddedType(typeSql, false);
      final var fd = elementType.storage().fields.get(fieldToken.text);
      if (fd != null) {
        var fieldType = environment.rules.Resolve(fd.type, false);
        if (fieldType instanceof DetailComputeRequiresGet) {
          requireGet = true;
          fieldType = environment.rules.Resolve(((DetailComputeRequiresGet) fieldType).typeAfterGet(environment), false);
        }
        TyType resultType = null;
        if (functionInstance != null) {
          if (fieldType != null && functionInstance.returnType != null) {
            resultType = new TyNativeMap(TypeBehavior.ReadOnlyNativeValue, null, null, null, fieldType, null, functionInstance.returnType, null);
          }
        } else {
          resultType = new TyNativeMap(TypeBehavior.ReadOnlyNativeValue, null, null, null, fieldType, null, typeSql, null);
        }
        if (resultType != null) {
          resultType.typing(environment);
          return resultType;
        }
      } else {
        environment.document.createError(this, String.format("Field '%s' was not found for reduction", fieldToken.text));
      }
    }
    return null;
  }

  @Override
  public void writeJava(final StringBuilder sb, final Environment environment) {
    sql.writeJava(sb, environment);
    sb.append(".reduce((__item) -> __item.").append(fieldToken.text);
    if (requireGet) {
      sb.append(".get()");
    }
    sb.append(", ");
    if (functionalType != null) {
      switch (functionalType.style) {
        case ExpressionThenArgs:
        case ExpressionThenNameWithArgs:
          functionToReduceWith.writeJava(sb, environment);
          break;
        default:
          sb.append("(__list) -> ").append(functionInstance.javaFunction).append("(__list)");
          break;
      }
    } else {
      sb.append("(__list) -> (__list)");
    }
    sb.append(")");
  }

  @Override
  public void free(FreeEnvironment environment) {
    sql.free(environment);
    functionToReduceWith.free(environment);
  }
}
