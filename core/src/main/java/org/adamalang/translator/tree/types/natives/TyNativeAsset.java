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
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.expressions.constants.NothingAssetConstant;
import org.adamalang.translator.tree.types.ReflectionSource;
import org.adamalang.translator.tree.types.TySimpleNative;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.functions.FunctionOverloadInstance;
import org.adamalang.translator.tree.types.natives.functions.FunctionPaint;
import org.adamalang.translator.tree.types.natives.functions.FunctionStyleJava;
import org.adamalang.translator.tree.types.traits.DetailCanExtractForUnique;
import org.adamalang.translator.tree.types.traits.assign.AssignmentViaNative;
import org.adamalang.translator.tree.types.traits.details.DetailHasDeltaType;
import org.adamalang.translator.tree.types.traits.details.DetailTypeHasMethods;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TyNativeAsset extends TySimpleNative implements //
    DetailHasDeltaType, //
    DetailCanExtractForUnique, //
    DetailTypeHasMethods, //
    AssignmentViaNative //
{
  public final Token readonlyToken;
  public final Token token;

  public TyNativeAsset(final TypeBehavior behavior, final Token readonlyToken, final Token token) {
    super(behavior, "NtAsset", "NtAsset", -1);
    this.readonlyToken = readonlyToken;
    this.token = token;
    ingest(token);
  }

  @Override
  public void emitInternal(final Consumer<Token> yielder) {
    if (readonlyToken != null) {
      yielder.accept(readonlyToken);
    }
    yielder.accept(token);
  }

  @Override
  public void format(Formatter formatter) {
  }

  @Override
  public String getAdamaType() {
    return "asset";
  }

  @Override
  public TyType makeCopyWithNewPositionInternal(final DocumentPosition position, final TypeBehavior newBehavior) {
    return new TyNativeAsset(newBehavior, readonlyToken, token).withPosition(position);
  }

  @Override
  public void writeTypeReflectionJson(JsonStreamWriter writer, ReflectionSource source) {
    writer.beginObject();
    writer.writeObjectFieldIntro("nature");
    writer.writeString("native_value");
    writeAnnotations(writer);
    writer.writeObjectFieldIntro("type");
    writer.writeString("asset");
    writer.endObject();
  }

  @Override
  public String getDeltaType(final Environment environment) {
    return "DAsset";
  }

  @Override
  public Expression inventDefaultValueExpression(final DocumentPosition forWhatExpression) {
    return new NothingAssetConstant(Token.WRAP("@nothing")).withPosition(forWhatExpression);
  }

  @Override
  public TyNativeFunctional lookupMethod(String name, Environment environment) {
    if ("name".equals(name)) {
      return new TyNativeFunctional("name", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("name", new TyNativeString(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("type".equals(name)) {
      return new TyNativeFunctional("type", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("type", new TyNativeString(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("valid".equals(name)) {
      return new TyNativeFunctional("valid", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("valid", new TyNativeBoolean(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("size".equals(name)) {
      return new TyNativeFunctional("size", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("size", new TyNativeLong(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("md5".equals(name)) {
      return new TyNativeFunctional("md5", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("md5", new TyNativeString(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("sha384".equals(name)) {
      return new TyNativeFunctional("sha384", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("sha384", new TyNativeString(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    if ("id".equals(name)) {
      return new TyNativeFunctional("id", FunctionOverloadInstance.WRAP(new FunctionOverloadInstance("id", new TyNativeString(TypeBehavior.ReadOnlyNativeValue, null, token).withPosition(this), new ArrayList<>(), FunctionPaint.READONLY_NORMAL)), FunctionStyleJava.ExpressionThenArgs);
    }
    return environment.state.globals.findExtension(this, name);
  }
}
