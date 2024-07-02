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
package org.adamalang.translator.tree.types.topo;

import org.adamalang.translator.env.*;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.Document;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.junit.Test;

import java.util.Collections;

public class TypeCheckerRootTests {
  @Test
  public void sanity() {
    TypeCheckerRoot tcr = new TypeCheckerRoot();
    tcr.issueError(DocumentPosition.ZERO, "message");
    tcr.define(Token.WRAP("x"), Collections.emptySet(), (env) -> {});
    tcr.register(Collections.singleton("x"), (env) -> {});
    tcr.alias("::x", "x");
    EnvironmentState es = new EnvironmentState(GlobalObjectPool.createPoolWithStdLib(RuntimeEnvironment.Beta), CompilerOptions.start().make());
    tcr.check(Environment.fresh(new Document(), es));
  }
}
