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
package org.adamalang.web.client;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.metrics.Inflight;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class FileWriterHttpResponderTests {

  private final static Inflight alarm = new Inflight() {
    @Override
    public void up() {

    }

    @Override
    public void down() {

    }

    @Override
    public void set(int value) {

    }
  };

  @Test
  public void happy() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(200, Collections.emptyMap()));
    writer.bodyStart(3);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(0, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(1, callbackValue.get());
    Assert.assertEquals("XYZ", Files.readString(file.toPath()));
  }

  @Test
  public void happyNoLenCheck() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(200, Collections.emptyMap()));
    writer.bodyStart(-1);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(0, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(1, callbackValue.get());
    Assert.assertEquals("XYZ", Files.readString(file.toPath()));
  }

  @Test
  public void prematureEnd() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(200, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(0, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(986319, callbackValue.get());
  }

  @Test
  public void not200_500() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(500, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(903347, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(903347, callbackValue.get());
  }

  @Test
  public void not200_302() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(302, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(991218, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(991218, callbackValue.get());
  }

  @Test
  public void not200_404() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(404, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(986396, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(986396, callbackValue.get());
  }


  @Test
  public void not200_410() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.start(new SimpleHttpResponseHeader(410, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    Assert.assertEquals(984312, callbackValue.get());
    writer.bodyEnd();
    Assert.assertEquals(984312, callbackValue.get());
  }

  @Test
  public void failureProxy() throws Exception {
    AtomicInteger callbackValue = new AtomicInteger(0);
    File file = File.createTempFile("ADAMA_tempfile", "suffix");
    file.deleteOnExit();
    FileWriterHttpResponder writer = new FileWriterHttpResponder(file, alarm, new FileWriterHttpTimeoutTracker(), wrap(callbackValue));
    writer.failure(new ErrorCodeException(123));
    Assert.assertEquals(123, callbackValue.get());
    writer.start(new SimpleHttpResponseHeader(200, Collections.emptyMap()));
    writer.bodyStart(5);
    writer.bodyFragment("XYZ".getBytes(StandardCharsets.UTF_8), 0, 3);
    writer.bodyEnd();
    Assert.assertEquals(123, callbackValue.get());
  }

  @Test
  public void dumbCrash() throws Exception {
    try {
      new FileWriterHttpResponder(null, alarm, new FileWriterHttpTimeoutTracker(), null);
    } catch (ErrorCodeException ece) {
      Assert.assertEquals(928944, ece.code);
    }
  }

  @Test
  public void dumbCoverageFinish() {
    AtomicInteger callbackValue = new AtomicInteger(0);
    FileWriterHttpResponder.finish(null, wrap(callbackValue));
    Assert.assertEquals(993487, callbackValue.get());
  }

  @Test
  public void dumbCoverageWrite() {
    AtomicInteger callbackValue = new AtomicInteger(0);
    FileWriterHttpResponder.write(null, null, 0, 0, wrap(callbackValue));
    Assert.assertEquals(913615, callbackValue.get());
  }

  public Callback<Void> wrap(AtomicInteger value) {
    return new Callback<Void>() {
      @Override
      public void success(Void x) {
        value.set(1);
      }

      @Override
      public void failure(ErrorCodeException ex) {
        ex.printStackTrace();
        value.set(ex.code);
      }
    };
  }
}
