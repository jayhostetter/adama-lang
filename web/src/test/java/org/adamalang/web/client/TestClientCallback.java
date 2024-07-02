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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.Json;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestClientCallback {
  private final CountDownLatch closeLatch;
  private final CountDownLatch pingLatch;
  private final CountDownLatch firstLatch;
  private final CountDownLatch failureLatch;
  private final CountDownLatch failedToConnectLatch;
  private final CountDownLatch disconnectLatch;
  private String data;
  private Throwable exception;
  private ArrayList<String> writes;
  private HashMap<Integer, Mailbox> mailboxes;
  public final HashMap<String, String> headers;

  public TestClientCallback() {
    this.closeLatch = new CountDownLatch(1);
    this.firstLatch = new CountDownLatch(1);
    this.failureLatch = new CountDownLatch(1);
    this.failedToConnectLatch = new CountDownLatch(1);
    this.pingLatch = new CountDownLatch(1);
    this.disconnectLatch = new CountDownLatch(1);
    this.data = "";
    this.writes = new ArrayList<>();
    this.mailboxes = new HashMap<>();
    this.headers = new HashMap<>();
  }

  public void awaitClosed() throws Exception {
    Assert.assertTrue(closeLatch.await(5000, TimeUnit.MILLISECONDS));
  }

  public void awaitPing() throws Exception {
    Assert.assertTrue(pingLatch.await(5000, TimeUnit.MILLISECONDS));
  }

  public void awaitFailure() throws Exception {
    Assert.assertTrue(failureLatch.await(5000, TimeUnit.MILLISECONDS));
  }

  public void awaitFailedToConnect() throws Exception {
    Assert.assertTrue(failedToConnectLatch.await(5000, TimeUnit.MILLISECONDS));
  }

  public void awaitFirst() throws Exception {
    Assert.assertTrue(firstLatch.await(25000, TimeUnit.MILLISECONDS));
  }

  public void assertData(String data) {
    Assert.assertEquals(data, this.data);
  }

  public void assertData(int write, String data) {
    Assert.assertEquals(data, this.writes.get(write));
  }

  public void assertDataPrefix(int write, String data) {
    Assert.assertTrue(this.writes.get(write).startsWith(data));
  }

  public void assertDataPrefix(String prefix) {
    Assert.assertTrue(this.data.startsWith(prefix));
  }

  public void closed() {
    closeLatch.countDown();
  }

  public void failed(Throwable exception) {
    this.exception = exception;
    failureLatch.countDown();
  }

  public void failedToConnect() {
    failedToConnectLatch.countDown();
  }

  public void awaitDisconnect() throws Exception {
    Assert.assertTrue(disconnectLatch.await(5000, TimeUnit.MILLISECONDS));
  }

  public boolean keepPings = false;

  public void successfulResponse(String data) {
    if (!data.contains("ping") || keepPings) {
      this.data += data;
    }
    writes.add(data);
    firstLatch.countDown();
    try {
      ObjectNode node = Json.parseJsonObject(data);

      if (node.has("ping")) {
        pingLatch.countDown();
      }

      if (node.has("status")) {
        if ("disconnected".equals(node.get("status").textValue())) {
          disconnectLatch.countDown();
        }
      }
      if (node.has("id")) {
        JsonNode idNode = node.get("id");
        if (idNode != null && idNode.isInt()) {
          getOrCreate(idNode.asInt()).deliver(data);
        }
      }
      if (node.has("failure")) {
        JsonNode idNode = node.get("failure");
        if (idNode != null && idNode.isInt()) {
          getOrCreate(idNode.asInt()).deliver(data);
        }
      }
      if (node.has("deliver")) {
        JsonNode idNode = node.get("deliver");
        if (idNode != null && idNode.isInt()) {
          getOrCreate(idNode.asInt()).deliver(data);
        }
      }
    } catch (Exception ex) {

    }
  }

  public synchronized Mailbox getOrCreate(int id) {
    Mailbox mailbox = mailboxes.get(id);
    if (mailbox == null) {
      mailbox = new Mailbox();
      mailboxes.put(id, mailbox);
    }
    return mailbox;
  }

  public class Mailbox {
    private final ArrayList<String> writes;
    private final HashSet<CountDownLatch> arrivals;
    private final CountDownLatch firstLatch;

    public Mailbox() {
      this.arrivals = new HashSet<>();
      writes = new ArrayList<>();
      this.firstLatch = new CountDownLatch(1);
    }

    public synchronized void deliver(String data) {
      firstLatch.countDown();
      writes.add(data);
      for (CountDownLatch latch : arrivals) {
        latch.countDown();
      }
    }

    public synchronized CountDownLatch latch(int c) {
      CountDownLatch latch = new CountDownLatch(c);
      arrivals.add(latch);
      return latch;
    }

    public void assertData(int at, String data) {
      Assert.assertEquals(data, writes.get(at));
    }

    public void awaitFirst() throws Exception {
      Assert.assertTrue(firstLatch.await(5000, TimeUnit.MILLISECONDS));
    }
  }
}
