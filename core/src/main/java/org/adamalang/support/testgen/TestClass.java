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
package org.adamalang.support.testgen;

import org.adamalang.common.DefaultCopyright;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class TestClass {
  private static final String WTF = "" + (char) 92;
  private final String clazz;
  private final StringBuilder outputFile;
  private int testId;

  public TestClass(final String clazz) {
    testId = 0;
    this.clazz = clazz;
    outputFile = new StringBuilder();
    outputFile.append(DefaultCopyright.COPYRIGHT_FILE_PREFIX);
    outputFile.append("package org.adamalang.translator;\n\n");
    outputFile.append("import org.junit.Test;\n\n");
    outputFile.append(String.format("public class Generated%sTests extends GeneratedBase {\n", clazz));
  }

  public boolean addTest(final TestFile test) throws IOException {
    testId++;
    final var varName = test.name + "_" + testId;
    outputFile.append("  private String cached_" + varName + " = null;\n");
    outputFile.append("  private String get_" + varName + "() {\n");
    outputFile.append("    if (cached_" + varName + " != null) {\n");
    outputFile.append("      return cached_" + varName + ";\n");
    outputFile.append("    }\n");
    final var correctedPath = "./test_code/" + test.filename();
    final var correctedFile = new File(correctedPath);
    outputFile.append(String.format("    cached_" + varName + " = generateTestOutput(" + test.success + ", \"%s\", \"%s\");\n", varName, correctedPath));
    outputFile.append("    return cached_" + varName + ";\n");
    outputFile.append("  }\n\n");
    if (test.success) {
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "Emission() {\n");
      outputFile.append("    assertEmissionGood(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "Success() {\n");
      outputFile.append("    assertLivePass(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "NoFormatException() {\n");
      outputFile.append("    assertNoFormatException(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "GoodWillHappy() {\n");
      outputFile.append("    assertGoodWillHappy(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
    } else {
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "Failure() {\n");
      outputFile.append("    assertLiveFail(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
      outputFile.append("  @Test\n");
      outputFile.append("  public void test" + test.name + "NotTerribleLineNumbers() {\n");
      outputFile.append("    assertNotTerribleLineNumbers(get_" + varName + "());\n");
      outputFile.append("  }\n\n");
    }
    outputFile.append("  @Test\n");
    outputFile.append("  public void test" + test.name + "ExceptionFree() {\n");
    outputFile.append("    assertExceptionFree(get_" + varName + "());\n");
    outputFile.append("  }\n\n");
    outputFile.append("  @Test\n");
    outputFile.append("  public void test" + test.name + "TODOFree() {\n");
    outputFile.append("    assertTODOFree(get_" + varName + "());\n");
    outputFile.append("  }\n\n");
    outputFile.append("  @Test\n");
    outputFile.append("  public void stable_" + varName + "() {\n");
    outputFile.append("    String live = get_" + varName + "();\n");
    final var gold = TestForge.forge(test.success, varName, correctedFile.toPath(), correctedFile.getParentFile().toPath());
    writeStringBuilder(gold, outputFile, "gold");
    outputFile.append("    assertStable(live, gold);\n");
    outputFile.append("  }\n");
    return gold.endsWith("Success\n");
  }

  public static void writeStringBuilder(String str, StringBuilder outputFile, String variable) {
    outputFile.append("    StringBuilder ").append(variable).append(" = new StringBuilder();\n");
    final var lines = str.split("\n");
    for (var k = 0; k < lines.length; k++) {
      lines[k] = lines[k].stripTrailing();
    }
    if (lines.length > 0) {
      outputFile.append(String.format("    " + variable + ".append(\"%s\");\n", escapeLine(lines[0])));
      for (var k = 1; k < lines.length; k++) {
        outputFile.append(String.format("    " + variable + ".append(\"\\n%s\");\n", escapeLine(lines[k])));
      }
    }
  }

  public static String escapeLine(final String line) {
    return line //
                .replaceAll(Pattern.quote(WTF), WTF + WTF + WTF + WTF) //
                .replaceAll("\"", WTF + WTF + "\"") //
        ;
  }

  public void finish(final File root) throws IOException {
    outputFile.append("}\n");
    Files.writeString(new File(root, "Generated" + clazz + "Tests.java").toPath(), outputFile.toString());
  }
}
