/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.translator.tree.definitions;

import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.env.Environment;
import org.junit.Test;

import java.util.function.Consumer;

public class DefinitionTests {
    @Test
    public void coverage() {
        Definition df = new Definition() {
            @Override
            public void emit(Consumer<Token> yielder) {
            }

            @Override
            public void typing(Environment environment) {
            }
        };
    }
}
