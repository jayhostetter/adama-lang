/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.translator.jvm;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/** responsible for capturing results from the java compiler */
@SuppressWarnings("unchecked")
public class ByteArrayJavaFileManager extends ForwardingJavaFileManager {
  private class ClassByteArrayOutputBuffer extends SimpleJavaFileObject {
    private final String name;

    ClassByteArrayOutputBuffer(final String name) {
      super(URI.create(new StringBuilder().append("code:///").append(name).toString()), Kind.CLASS);
      this.name = name;
    }

    @Override
    public OutputStream openOutputStream() {
      return new FilterOutputStream(new ByteArrayOutputStream()) {
        @Override
        public void close() throws IOException {
          out.close();
          final var bos = (ByteArrayOutputStream) out;
          classes.put(name, bos.toByteArray());
        }
      };
    }
  }
  private static class SingleSourceJavaObject extends SimpleJavaFileObject {
    final String source;

    SingleSourceJavaObject(final String fileName, final String code) {
      super(URI.create(new StringBuilder().append("code:///").append(fileName).toString()), Kind.SOURCE);
      source = code;
    }

    @Override
    public CharBuffer getCharContent(final boolean ignoreEncodingErrors) {
      return CharBuffer.wrap(source);
    }
  }

  public static ArrayList<JavaFileObject> turnIntoCompUnits(final String fileName, final String code) {
    final var compUnits = new ArrayList<JavaFileObject>();
    compUnits.add(new SingleSourceJavaObject(fileName, code));
    return compUnits;
  }

  private Map<String, byte[]> classes;

  public ByteArrayJavaFileManager(final JavaFileManager fileManager) {
    super(fileManager);
    classes = new TreeMap<>();
  }

  @Override
  public void close() throws IOException {
    classes = null;
  }

  @Override
  public void flush() throws IOException {
  }

  public Map<String, byte[]> getClasses() {
    return classes;
  }

  @Override
  public JavaFileObject getJavaFileForOutput(final JavaFileManager.Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
    return new ClassByteArrayOutputBuffer(className);
  }
}
