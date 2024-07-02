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
package org.adamalang.translator.tree.types.checking;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.adamalang.translator.tree.definitions.DefineStateTransition;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.checking.properties.*;
import org.adamalang.translator.tree.types.checking.ruleset.*;
import org.adamalang.translator.tree.types.natives.TyNativeMessage;
import org.adamalang.translator.tree.types.traits.IsEnum;

public class Rules {
  private final Environment environment;

  public Rules(final Environment environment) {
    this.environment = environment;
  }

  /** FROM: RuleSetIngestion */
  public boolean CanAIngestB(final TyType tyTypeA, final TyType tyTypeB, final boolean silent) {
    return RuleSetIngestion.CanAIngestB(environment, tyTypeA, tyTypeB, silent);
  }

  /** FROM: RuleSetAssignment */
  public CanAssignResult CanAssignWithSet(final TyType tyTypeA, final TyType tyTypeB, final boolean silent) {
    return RuleSetAssignment.CanAssignWithSet(environment, tyTypeA, tyTypeB, silent);
  }

  /** FROM: RuleSetBump */
  public CanBumpResult CanBumpBool(final TyType tyType, final boolean silent) {
    return RuleSetBump.CanBumpBool(environment, tyType, silent);
  }

  public CanBumpResult CanBumpNumeric(final TyType tyType, final boolean silent) {
    return RuleSetBump.CanBumpNumeric(environment, tyType, silent);
  }

  /** FROM: RuleSetStructures */
  public boolean CanStructureAProjectIntoStructureB(final TyType tyTypeA, final TyType tyTypeB, final boolean silent) {
    return RuleSetStructures.CanStructureAProjectIntoStructureB(environment, tyTypeA, tyTypeB, silent);
  }

  public boolean CanTypeAStoreTypeB(final TyType tyTypeA, final TyType tyTypeB, final StorageTweak result, final boolean silent) {
    return RuleSetAssignment.CanTypeAStoreTypeB(environment, tyTypeA, tyTypeB, result, silent);
  }

  /** FROM: RuleSetCommon */
  public TyType EnsureRegisteredAndDedupe(final TyType tyType, final boolean silent) {
    return RuleSetCommon.EnsureRegisteredAndDedupe(environment, tyType, silent);
  }

  public TyType ExtractEmbeddedType(final TyType tyType, final boolean silent) {
    return RuleSetCommon.ExtractEmbeddedType(environment, tyType, silent);
  }

  /** FROM: RuleSetEnums */
  public IsEnum FindEnumType(final String search, final DocumentPosition position, final boolean silent) {
    return RuleSetEnums.FindEnumType(environment, search, position, silent);
  }

  /** FROM: RuleSetMessages */
  public TyNativeMessage FindMessageStructure(final String search, final DocumentPosition position, final boolean silent) {
    return RuleSetMessages.FindMessageStructure(environment, search, position, silent);
  }

  /** FROM: RuleSetStateMachine */
  public DefineStateTransition FindStateMachineStep(final String search, final DocumentPosition position, final boolean silent) {
    return RuleSetStateMachine.FindStateMachineStep(environment, search, position, silent);
  }

  public TyType GetMaxType(final TyType tyTypeA, final TyType tyTypeB, final boolean silent) {
    return RuleSetCommon.GetMaxType(environment, tyTypeA, tyTypeB, silent);
  }

  public WrapInstruction GetMaxTypeBasedWrappingInstruction(final TyType tyTypeA, final TyType tyTypeB) {
    return RuleSetCommon.GetMaxTypeBasedWrappingInstruction(environment, tyTypeA, tyTypeB);
  }

  public boolean IngestionLeftElementRequiresRecursion(final TyType tyType) {
    return RuleSetIngestion.IngestionLeftElementRequiresRecursion(environment, tyType);
  }

  public boolean IngestionLeftSideRequiresBridgeCreate(final TyType tyType) {
    return RuleSetIngestion.IngestionLeftSideRequiresBridgeCreate(environment, tyType);
  }

  public boolean IngestionRightSideRequiresIteration(final TyType tyType) {
    return RuleSetIngestion.IngestionRightSideRequiresIteration(environment, tyType);
  }

  /** FROM: RuleSetMath */
  public boolean IsBoolean(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsBoolean(environment, tyType, silent);
  }

  /** FROM: RuleSetFunctions */
  public boolean IsFunction(final TyType tyType, final boolean silent) {
    return RuleSetFunctions.IsFunction(environment, tyType, silent);
  }

  public boolean IsInteger(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsInteger(environment, tyType, silent);
  }

  public boolean IsLong(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsLong(environment, tyType, silent);
  }

  public boolean IsString(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsString(environment, tyType, silent);
  }

  /** FROM: RuleSetIterable */
  public boolean IsIterable(final TyType tyType, final boolean silent) {
    return RuleSetIterable.IsIterable(environment, tyType, silent);
  }

  /** FROM: RuleSetMap */
  public boolean IsMap(final TyType tyType) {
    return RuleSetMap.IsMap(environment, tyType);
  }

  /** FROM: RuleSetMaybe */
  public boolean IsMaybe(final TyType tyType, final boolean silent) {
    return RuleSetMaybe.IsMaybe(environment, tyType, silent);
  }

  /** FROM: RuleSetArray */
  public boolean IsNativeArray(final TyType tyType, final boolean silent) {
    return RuleSetArray.IsNativeArray(environment, tyType, silent);
  }

  public boolean IsNativeArrayOfStructure(final TyType tyType, final boolean silent) {
    return RuleSetArray.IsNativeArrayOfStructure(environment, tyType, silent);
  }

  /** FROM: RuleSetLists */
  public boolean IsNativeListOfStructure(final TyType tyType, final boolean silent) {
    return RuleSetLists.IsNativeListOfStructure(environment, tyType, silent);
  }

  public boolean IsNativeMessage(final TyType tyType, final boolean silent) {
    return RuleSetMessages.IsNativeMessage(environment, tyType, silent);
  }

  public boolean IsAsset(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsAsset(environment, tyType, silent);
  }

  public boolean IsNumeric(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsNumeric(environment, tyType, silent);
  }

  public boolean IsPrincipal(final TyType tyType, final boolean silent) {
    return RuleSetCommon.IsPrincipal(environment, tyType, silent);
  }

  public boolean IsStateMachineRef(final TyType tyType, final boolean silent) {
    return RuleSetStateMachine.IsStateMachineRef(environment, tyType, silent);
  }

  public boolean IsStructure(final TyType tyType, final boolean silent) {
    return RuleSetStructures.IsStructure(environment, tyType, silent);
  }


  public boolean IsRxStructure(final TyType tyType, final boolean silent) {
    return RuleSetStructures.IsRxStructure(environment, tyType, silent);
  }

  /** FROM: RuleSetTable */
  public boolean IsTable(final TyType tyType, final boolean silent) {
    return RuleSetTable.IsTable(environment, tyType, silent);
  }

  public TyType Resolve(final TyType tyType, final boolean silent) {
    return RuleSetCommon.Resolve(environment, tyType, silent);
  }

  public TyType ResolvePtr(final TyType tyType, final boolean silent) {
    return RuleSetCommon.ResolvePtr(environment, tyType, silent);
  }

  /** FROM: RuleSetConversion */
  public void SignalConversionIssue(final TyType tyType, final boolean silent) {
    RuleSetConversion.SignalConversionIssue(environment, tyType, silent);
  }
}
