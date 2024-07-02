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
package org.adamalang.canary;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.api.ClientConnectionCreateRequest;
import org.adamalang.api.ClientDataResponse;
import org.adamalang.api.SelfClient;
import org.adamalang.common.*;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CanaryEval {
  public final SimpleExecutor eval;
  private final SimpleExecutor executor;
  private final SelfClient client;
  public final CanaryMetricsRegister register;
  private final Consumer<String> output;

  public CanaryEval(SimpleExecutor eval, SimpleExecutor executor, SelfClient client, CanaryMetricsRegister register, Consumer<String> output) {
    this.eval = eval;
    this.executor = executor;
    this.client = client;
    this.register = register;
    this.output = output;
  }

  /** the central eval() function */
  public void eval(ObjectNode config) throws Exception {
    String command = Json.readString(config, "command");
    if (command == null) {
      return;
    }
    switch (command.trim().toLowerCase()) {
      case "spawn":
        spawn(config);
        return;
      case "hold-open-connection":
        hold_open_connection(config);
        return;
      default:
        output.accept("failed to interpret command '" + command + "'");
    }
  }

  /** spawn a bunch of instances of a child canary */
  private void spawn(ObjectNode config) throws Exception {
    Integer count = Json.readInteger(config, "count");
    if (count == null) {
      count = 1;
    }
    Integer delayBetweenEval = Json.readInteger(config, "delay");
    if (delayBetweenEval == null) {
      delayBetweenEval = 50;
    }
    ObjectNode child = Json.readObject(config, "child");
    for (int k = 0; k < count && child != null; k++) {
      eval(child);
      Thread.sleep(delayBetweenEval);
    }
  }

  /** simple canary: hold open a connection until failure */
  private void hold_open_connection(ObjectNode config) throws Exception {
    ClientConnectionCreateRequest connectionCreateRequest = new ClientConnectionCreateRequest();
    connectionCreateRequest.identity = Json.readString(config, "identity");
    connectionCreateRequest.space = Json.readString(config, "space");
    connectionCreateRequest.key = Json.readString(config, "key");
    connectionCreateRequest.viewerState = Json.readObject(config, "viewer-state");
    int backOffMinimum = Json.readInteger(config, "backoff-min", 100);
    int backOffMaximum = Json.readInteger(config, "backoff-max", 5000);
    final boolean retry = Json.readBool(config, "retry", false);
    final AtomicReference<Callback<SelfClient.DocumentStreamHandler>> connectResult = new AtomicReference<>();
    final AtomicReference<Stream<ClientDataResponse>> streamData = new AtomicReference<>();

    connectResult.set(new Callback<SelfClient.DocumentStreamHandler>() {
      @Override
      public void success(SelfClient.DocumentStreamHandler value) {
        register.success_connects.incrementAndGet();
      }

      @Override
      public void failure(ErrorCodeException ex) {
        register.failure_connects.incrementAndGet();
      }
    });

    streamData.set(new Stream<ClientDataResponse>() {
      int backoff = backOffMinimum;
      @Override
      public void next(ClientDataResponse value) {
        register.bandwidth.addAndGet(value.delta.toString().length());
      }

      @Override
      public void complete() {

      }

      @Override
      public void failure(ErrorCodeException ex) {
        if (retry) {
          output.accept("failed: " + ex.code + ", trying again");
          backoff = (int) Math.min(backOffMaximum, backoff * (1.0 + Math.random()) + 1);
          executor.execute(new NamedRunnable("retry") {
             @Override
             public void execute() throws Exception {
               client.connectionCreate(connectionCreateRequest, connectResult.get(), streamData.get());
             }
           });
        } else {
          output.accept("failed: " + ex.code + ", stopping");
        }
      }
    });
    client.connectionCreate(connectionCreateRequest, connectResult.get(), streamData.get());
  }

}
