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
package org.adamalang.translator.tree.expressions;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.parser.Formatter;
import org.adamalang.translator.tree.common.TokenizedItem;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.*;
import org.adamalang.translator.tree.types.natives.functions.TyNativeAggregateFunctional;
import org.adamalang.translator.tree.types.natives.functions.TyNativeFunctionInternalFieldReplacement;
import org.adamalang.translator.tree.types.reactive.TyReactiveRecord;
import org.adamalang.translator.tree.types.traits.IsStructure;
import org.adamalang.translator.tree.types.traits.details.DetailComputeRequiresGet;
import org.adamalang.translator.tree.types.traits.details.DetailContainsAnEmbeddedType;
import org.adamalang.translator.tree.types.traits.details.DetailTypeHasMethods;

import java.util.function.Consumer;

/** field/method look up ($e.field) */
public class FieldLookup extends Expression {
  public final Token dotToken;
  public final Expression expression;
  public final String fieldName;
  public final Token fieldNameToken;
  private boolean addGet;
  private TyType aggregateType;
  private boolean isGlobalObject;
  private boolean makeList;
  private boolean onlyExpression;
  private String overrideFieldName;
  private boolean requiresMaybeUnpack;
  private boolean doubleMaybeUnpack;
  private String maybeCastType;
  private TyType elementTypeIfList;
  private boolean jsonDeref;

  /**
   * @param expression the expression to evaluate
   * @param fieldNameToken the field to look up
   */
  public FieldLookup(final Expression expression, final Token dotToken, final Token fieldNameToken) {
    this.expression = expression;
    this.dotToken = dotToken;
    this.fieldNameToken = fieldNameToken;
    fieldName = fieldNameToken.text;
    this.ingest(expression);
    this.ingest(fieldNameToken);
    addGet = false;
    makeList = false;
    overrideFieldName = null;
    isGlobalObject = false;
    requiresMaybeUnpack = false;
    maybeCastType = null;
    elementTypeIfList = null;
    jsonDeref = false;
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    expression.emit(yielder);
    yielder.accept(dotToken);
    yielder.accept(fieldNameToken);
  }

  @Override
  public void format(Formatter formatter) {
    expression.format(formatter);
  }

  private void enforceSpecialIDReadonly(Environment environment) {
    if ("id".equals(fieldName) && environment.state.isContextAssignment()) {
      environment.document.createError(this, "'id' is a special readonly field");
    }
  }

  @Override
  protected TyType typingInternal(final Environment environment, final TyType suggestion) {
    var eType = expression.typing(environment, null);
    eType = environment.rules.ResolvePtr(eType, false);

    if (eType != null) {
      // global object, find the function
      if (eType instanceof TyNativeGlobalObject) {
        isGlobalObject = true;
        final var func = ((TyNativeGlobalObject) eType).lookupMethod(fieldName, environment);
        if (func == null) {
          environment.document.createError(this, String.format("Global '%s' lacks '%s'", ((TyNativeGlobalObject) eType).globalName, fieldName));
          return null;
        }
      }
      if (eType instanceof TyNativePair) {
        TyNativePair ePair = (TyNativePair) eType;
        TyType resultType;
        if ("key".equals(fieldName)) {
          resultType = ePair.domainType;
        } else if ("value".equals(fieldName)) {
          resultType = ePair.rangeType;
        } else {
          environment.document.createError(this, String.format("Pair '%s' does not have '%s' field, and only supports 'key' and 'value'", eType.getAdamaType(), fieldName));
          return null;
        }
        resultType = environment.rules.Resolve(resultType, false);
        if (resultType instanceof DetailComputeRequiresGet && environment.state.isContextComputation()) {
          resultType = ((DetailComputeRequiresGet) resultType).typeAfterGet(environment);
          addGet = true;
        }
        return resultType;
      }
      if (eType instanceof TyInternalReadonlyClass) {
        return ((TyInternalReadonlyClass) eType).getLookupType(environment, fieldName);
      }
      if (eType instanceof TyNativeList) {
        elementTypeIfList = environment.rules.ResolvePtr(((TyNativeList) eType).getEmbeddedType(environment), false);
        if (elementTypeIfList != null && elementTypeIfList instanceof IsStructure) {
          final var fd = ((IsStructure) elementTypeIfList).storage().fields.get(fieldName);
          if (fd != null) {
            enforceSpecialIDReadonly(environment);
            aggregateType = fd.type;
            makeList = true;
            if (aggregateType instanceof DetailComputeRequiresGet && environment.state.isContextComputation()) {
              addGet = true;
              aggregateType = ((DetailComputeRequiresGet) aggregateType).typeAfterGet(environment);
            }
            if (aggregateType != null) {
              return new TyNativeList(TypeBehavior.ReadWriteWithSetGet, null, null, new TokenizedItem<>(aggregateType.makeCopyWithNewPosition(this, aggregateType.behavior))).withPosition(this);
            }
          }
        }
      }
      if (eType instanceof DetailTypeHasMethods) {
        final var functional = ((DetailTypeHasMethods) eType).lookupMethod(fieldName, environment);
        if (functional != null) {
          onlyExpression = functional instanceof TyNativeAggregateFunctional || functional.style.useOnlyExpressionInLookup;
          if (functional instanceof TyNativeFunctionInternalFieldReplacement) {
            overrideFieldName = functional.name;
          }
          return functional;
        }
      }
      TyType originalType = eType;
      if (environment.rules.IsMaybe(eType, true)) {
        requiresMaybeUnpack = true;
        eType = ((DetailContainsAnEmbeddedType) eType).getEmbeddedType(environment);
      }
      if (eType instanceof TyNativeJson) {
        maybeCastType = "NtJson";
        jsonDeref = true;
        return originalType;
      }
      if (eType instanceof IsStructure) {
        if (!environment.state.isContextComputation() && eType.behavior == TypeBehavior.ReadOnlyNativeValue) {
          environment.document.createError(this, String.format("The field '%s' is on a readonly message", fieldName));
        }
        if (eType instanceof TyReactiveRecord) {
          enforceSpecialIDReadonly(environment);
        }
        final var hrs = (IsStructure) eType;
        final var fd = hrs.storage().fields.get(fieldName);
        if (fd != null) {
          if ("__ViewerType".equals(((IsStructure) eType).storage().name.text)) {
            environment.registerViewerField(fieldName);
          }
          TyType actualType = environment.rules.Resolve(fd.type, false);
          if (environment.rules.IsMaybe(actualType, true) && requiresMaybeUnpack) {
            doubleMaybeUnpack = true;
            actualType = ((DetailContainsAnEmbeddedType) actualType).getEmbeddedType(environment);
          }
          if (actualType != null) {
            TyType typeToReturn;
            if (actualType instanceof DetailComputeRequiresGet && environment.state.isContextComputation()) {
              addGet = true;
              typeToReturn = ((DetailComputeRequiresGet) actualType).typeAfterGet(environment);
            } else {
              typeToReturn = actualType.makeCopyWithNewPosition(this, actualType.behavior);
            }
            if (requiresMaybeUnpack) {
              maybeCastType = eType.getJavaBoxType(environment);
              return new TyNativeMaybe(TypeBehavior.ReadOnlyNativeValue, null, Token.WRAP("MAYBE"), new TokenizedItem<>(typeToReturn));
            } else {
              return typeToReturn;
            }
          }
        }
      }
      environment.document.createError(this, String.format("Type '%s' lacks field '%s'", eType.getAdamaType(), fieldName));
    }
    return null;
  }

  @Override
  public void writeJava(final StringBuilder sb, final Environment environment) {
    if (passedTypeChecking() && !isGlobalObject) {
      expression.writeJava(sb, environment);
      if (onlyExpression) {
        return;
      }
      sb.append(".");
      var fieldNameToUse = fieldName;
      if (overrideFieldName != null) {
        fieldNameToUse = overrideFieldName;
      }
      String finalNameToUse = fieldNameToUse;
      Runnable injectName = () -> {
        if (jsonDeref) {
          sb.append("deref(\"");
        }
        sb.append(finalNameToUse);
        if (jsonDeref) {
          sb.append("\")");
        }
      };
      if (requiresMaybeUnpack) {
        sb.append("unpack").append(doubleMaybeUnpack ? "Transfer" : "").append("((__item) -> ((").append(maybeCastType).append(")").append(" __item).");
        injectName.run();
        if (addGet) {
          sb.append(".get()");
        }
        sb.append(")");
      } else if (makeList && aggregateType != null) {
        sb.append("transform((").append(elementTypeIfList.getJavaBoxType(environment)).append(" __item) -> (").append(aggregateType.getJavaBoxType(environment)).append(") (__item.");
        injectName.run();
        if (addGet) {
          sb.append(".get()");
        }
        sb.append("))");
      } else {
        injectName.run();
        if (addGet) {
          sb.append(".get()");
        }
      }
    }
  }

  @Override
  public void free(FreeEnvironment environment) {
    environment.require("::" + fieldName);
    expression.free(environment);
  }
}
