package org.adamalang.frontend;

import org.adamalang.rxhtml.template.Escapes;
import java.io.File;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class EmbedTemplates {
  public static void main(String[] args) throws Exception {
    File javaSpaceTemplates = new File("./saas/src/main/java/org/adamalang/frontend/SpaceTemplates.java");
    String java = Files.readString(javaSpaceTemplates.toPath());

    String start = "// BEGIN-TEMPLATES-POPULATE";
    String end = "// END-TEMPLATES-POPULATE";

    String prefix = java.substring(0, java.indexOf(start) + start.length());
    String suffix = java.substring(java.indexOf(end));
    StringBuilder sb = new StringBuilder();

    for (File file : new File("./saas/templates").listFiles((dir, name) -> name.endsWith(".adama"))) {
      String name = file.getName().replaceAll(Pattern.quote(".adama"), "");
      String adama = Files.readString(file.toPath());
      String rxhtml = "<forest></forest>";
      File rxhtmlFile = new File("./saas/templates/" + name + ".rxhtml");
      if (rxhtmlFile.exists()) {
        rxhtml = Files.readString(rxhtmlFile.toPath());
      }
      sb.append("    templates.put(\"").append(name).append("\", new SpaceTemplate(\"").append(Escapes.escapeNewLine(Escapes.escape34(adama)).replaceAll("\r", "")).append("\",\"").append(Escapes.escapeNewLine(Escapes.escape34(rxhtml)).replaceAll("\r", "")).append("\"));\n");
    }
    Files.writeString(javaSpaceTemplates.toPath(), prefix + "\n" + sb + "    " + suffix);
  }
}
