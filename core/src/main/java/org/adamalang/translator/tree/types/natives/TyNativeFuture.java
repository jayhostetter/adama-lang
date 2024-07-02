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
package org.adamalang.translator.tree.types.natives;

import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.adamalang.translator.parser.Formatter;
import org.adamalang.translator.tree.common.TokenizedItem;
import org.adamalang.translator.tree.types.ReflectionSource;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.functions.FunctionOverloadInstance;
import org.adamalang.translator.tree.types.natives.functions.FunctionPaint;
import org.adamalang.translator.tree.types.natives.functions.FunctionStyleJava;
import org.adamalang.translator.tree.types.traits.DetailNeverPublic;
import org.adamalang.translator.tree.types.traits.assign.AssignmentViaNative;
import org.adamalang.translator.tree.types.traits.details.DetailContainsAnEmbeddedType;
import org.adamalang.translator.tree.types.traits.details.DetailTypeHasMethods;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TyNativeFuture extends TyType implements //
    DetailContainsAnEmbeddedType, //
    DetailNeverPublic, //
    DetailTypeHasMethods, //
    AssignmentViaNative {
  public final Token futureToken;
  public final Token readonlyToken;
  public final TyType resultType;
  public final TokenizedItem<TyType> tokenResultType;

  public TyNativeFuture(final TypeBehavior behavior, final Token readonlyToken, final Token futureToken, final TokenizedItem<TyType> tokenResultType) {
    super(behavior);
    this.readonlyToken = readonlyToken;
    this.futureToken = futureToken;
    resultType = tokenResultType.item;
    this.tokenResultType = tokenResultType;
    ingest(futureToken);
    ingest(resultType);
  }

  @Override
  public void emitInternal(final Consumer<Token> yielder) {
    if (readonlyToken != null) {
      yielder.accept(readonlyToken);
    }
    yielder.accept(futureToken);
    tokenResultType.emitBefore(yielder);
    tokenResultType.item.emit(yielder);
    tokenResultType.emitAfter(yielder);
  }

  @Override
  public void format(Formatter formatter) {
    tokenResultType.item.format(formatter);
  }

  @Override
  public String getAdamaType() {
    return String.format("future<%s>", resultType.getAdamaType());
  }

  @Override
  public String getJavaBoxType(final Environment environment) {
    return String.format("SimpleFuture<%s>", getEmbeddedType(environment).getJavaBoxType(environment));
  }

  @Override
  public String getJavaConcreteType(final Environment environment) {
    return getJavaBoxType(environment);
  }

  @Override
  public TyType getEmbeddedType(final Environment environment) {
    return environment.rules.Resolve(resultType, false);
  }

  @Override
  public TyType makeCopyWithNewPositionInternal(final DocumentPosition position, final TypeBehavior newBehavior) {
    return new TyNativeFuture(newBehavior, readonlyToken, futureToken, new TokenizedItem<>(resultType.makeCopyWithNewPosition(position, newBehavior))).withPosition(position);
  }

  @Override
  public void typing(final Environment environment) {
    tokenResultType.item.typing(environment);
  }

  @Override
  public void writeTypeReflectionJson(JsonStreamWriter writer, ReflectionSource source) {
    writer.beginObject();
    writer.writeObjectFieldIntro("nature");
    writer.writeString("native_future");
    writeAnnotations(writer);
    writer.endObject();
  }

  @Override
  public TyNativeFunctional lookupMethod(final String name, final Environment environment) {
    if ("await".equals(name)) {
      var returnType = environment.rules.Resolve(resultType, false);
      returnType = returnType.makeCopyWithNewPosition(this, TypeBehavior.ReadOnlyNativeValue);
      return new TyNativeFunctional("await", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("await", returnType, new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    return null;
  }
}
