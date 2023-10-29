/*
* Adama Platform and Language
* Copyright (C) 2021 - 2023 by Adama Platform Initiative, LLC
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
package org.adamalang.translator.tree.types.structures;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.definitions.DefineAssoc;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.Watcher;
import org.adamalang.translator.tree.types.natives.TyNativeInteger;
import org.adamalang.translator.tree.types.reactive.TyReactiveRecord;
import org.adamalang.translator.tree.types.reactive.TyReactiveTable;

import java.util.LinkedHashSet;
import java.util.function.Consumer;

public class JoinAssoc extends StructureComponent {
  public final Token joinToken;
  public final Token assoc;
  public final Token via;
  public final Token tableName;

  public final Token brackOpen;
  public final Token itemVar;
  public final Token brackClose;

  public final Token fromLabel;
  public final Expression fromExpr;
  public final Token toLabel;
  public final Expression toExpr;
  public final Token semicolon;

  public final LinkedHashSet<String> variablesToWatch;
  public final LinkedHashSet<String> servicesToWatch;
  private DefineAssoc foundAssoc;

  public JoinAssoc(Token joinToken, Token assoc, Token via, Token tableName, Token brackOpen, Token itemVar, Token brackClose, Token fromLabel, Expression fromExpr, Token toLabel, Expression toExpr, Token semicolon) {
    this.joinToken = joinToken;
    this.assoc = assoc;
    this.via = via;
    this.tableName = tableName;
    this.brackOpen = brackOpen;
    this.itemVar = itemVar;
    this.brackClose = brackClose;
    this.fromLabel = fromLabel;
    this.fromExpr = fromExpr;
    this.toLabel = toLabel;
    this.toExpr = toExpr;
    this.semicolon = semicolon;
    this.variablesToWatch = new LinkedHashSet<>();
    this.servicesToWatch = new LinkedHashSet<>();
    ingest(joinToken);
    ingest(semicolon);
  }

  @Override
  public void emit(Consumer<Token> yielder) {
    yielder.accept(joinToken);
    yielder.accept(assoc);
    yielder.accept(via);
    yielder.accept(tableName);
    yielder.accept(brackOpen);
    yielder.accept(itemVar);
    yielder.accept(brackClose);
    yielder.accept(fromLabel);
    fromExpr.emit(yielder);
    yielder.accept(toLabel);
    toExpr.emit(yielder);
    yielder.accept(semicolon);
  }

  public void typing(final Environment environment, StructureStorage owningStructureStorage) {
    Environment next = environment.watch(Watcher.makeAuto(environment, variablesToWatch, variablesToWatch, servicesToWatch));
    FieldDefinition fd = owningStructureStorage.fields.get(tableName.text);
    if (fd == null) {
      environment.document.createError(this, "The table '" + tableName.text + "' was not found within the record.");
    } else {
      if (fd.type instanceof TyReactiveTable) {
        TyReactiveTable rxTable = (TyReactiveTable) fd.type;
        TyType elementType = environment.document.types.get(rxTable.recordName);
        if (elementType != null) {
          TyType suggestion = new TyNativeInteger(TypeBehavior.ReadOnlyNativeValue, null, null).withPosition(this);
          Environment itemEnv = next.scopeAsReadOnlyBoundary().scopeWithComputeContext(ComputeContext.Computation);
          itemEnv.define(itemVar.text, elementType, true, this);
          TyType fromType = environment.rules.Resolve(fromExpr.typing(itemEnv, suggestion), false);
          TyType toType = environment.rules.Resolve(toExpr.typing(itemEnv, suggestion), false);
          environment.rules.IsInteger(fromType, false);
          environment.rules.IsInteger(toType, false);
        }
      } else {
        environment.document.createError(this, "'" + tableName.text + "' was not a table");
      }
    }
    foundAssoc = environment.document.assocs.get(assoc.text);
    if (foundAssoc == null) {
      environment.document.createError(this, "The assoc '" + assoc.text + "' was not found in the document.");
    }
  }
}