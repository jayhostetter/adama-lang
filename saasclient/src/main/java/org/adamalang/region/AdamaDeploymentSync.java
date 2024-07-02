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
package org.adamalang.region;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.api.*;
import org.adamalang.common.*;
import org.adamalang.common.metrics.StreamMonitor;
import org.adamalang.runtime.deploy.Deploy;
import org.adamalang.runtime.deploy.DeploySync;
import org.adamalang.runtime.deploy.Undeploy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;

/** connect to ide/$space to learn when deployments happen */
public class AdamaDeploymentSync implements DeploySync {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdamaDeploymentSync.class);
  private final AdamaDeploymentSyncMetrics metrics;
  private final SelfClient client;
  private final String identity;
  private final SimpleExecutor executor;
  private final HashMap<String, SM> stateMachines;
  private final Deploy event;
  private final Undeploy undeploy;

  public AdamaDeploymentSync(AdamaDeploymentSyncMetrics metrics, SelfClient client, SimpleExecutor executor, String identity, Deploy event, Undeploy undeploy) {
    this.metrics = metrics;
    this.client = client;
    this.executor = executor;
    this.identity = identity;
    this.event = event;
    this.stateMachines = new HashMap<>();
    this.undeploy = undeploy;
  }

  public class SM {
    private final ClientConnectionCreateRequest request;
    private SelfClient.DocumentStreamHandler handler;
    private boolean alive;
    private final Callback<SelfClient.DocumentStreamHandler> callbackWrite;
    private final Stream<ClientDataResponse> callbackRead;
    private StreamMonitor.StreamMonitorInstance monitor;

    private SM(String space) {
      request = new ClientConnectionCreateRequest();
      request.identity = identity;
      request.space = "ide";
      request.key = space;
      request.viewerState = Json.newJsonObject();
      alive = true;
      handler = null;

      callbackWrite = metrics.adamasync_connected.wrap(new Callback<SelfClient.DocumentStreamHandler>() {
        @Override
        public void success(SelfClient.DocumentStreamHandler value) {
          monitor.progress();
          executor.execute(new NamedRunnable("set-handler") {
            @Override
            public void execute() throws Exception {
              handler = value;
              if (!alive) {
                sendEnd();
              }
            }
          });
        }

        @Override
        public void failure(ErrorCodeException ex) {
          if (!StartUp.hasRecentlyStartedUp()) {
            LOGGER.error("failed-watching:" + space + ":" + ex.code);
          }
        }
      });

      callbackRead = new Stream<ClientDataResponse>() {
        int backoff = 500;
        int deploymentAt = -1;
        @Override
        public void next(ClientDataResponse value) {
          monitor.progress();
          if (value.delta.has("data")) {
            ObjectNode data = (ObjectNode) value.delta.get("data");
            if (data.has("deployments")) {
              int deploymentsValue = data.get("deployments").intValue();
              if (deploymentsValue > deploymentAt) {
                LOGGER.error("deploying:" + space + "@" + deploymentsValue);
                event.deploy(space, Callback.DONT_CARE_VOID);
              }
              deploymentAt = deploymentsValue;
            }
          }
        }

        @Override
        public void complete() {
          monitor.finish();
        }

        @Override
        public void failure(ErrorCodeException ex) {
          if (ex.code == 625676) {
            undeploy.undeploy(space);
            unwatch(space);
            return;
          }
          monitor.failure(ex.code);
          backoff = (int) Math.min(5000, backoff * (1 + Math.random()));
          executor.execute(new NamedRunnable("deploymentsync-failed") {
            @Override
            public void execute() throws Exception {
              handler = null;
              if (alive) {
                executor.schedule(new NamedRunnable("deploymentsync-retry") {
                  @Override
                  public void execute() throws Exception {
                    retry();
                  }
                }, backoff);
              }
            }
          });
        }
      };
    }

    private void sendEnd() {
      handler.end(new ClientConnectionEndRequest(), new Callback<>() {
        @Override
        public void success(ClientSimpleResponse value) {}

        @Override
        public void failure(ErrorCodeException ex) {}
      });
    }

    public void retry() {
      monitor = metrics.adamasync_streaming_update.start();
      client.connectionCreate(request, callbackWrite, callbackRead);
    }

    public void stop() {
      executor.execute(new NamedRunnable("deploymentsync-stop") {
        @Override
        public void execute() throws Exception {
          alive = false;
          if (handler != null) {
            sendEnd();
          }
        }
      });
    }
  }

  @Override
  public void watch(String space) {
    executor.execute(new NamedRunnable("deploymentsync-watch") {
      @Override
      public void execute() throws Exception {
        if (!stateMachines.containsKey(space)) {
          LOGGER.error("start-watching:" + space);
          metrics.adamasync_watching.up();
          SM sm = new SM(space);
          stateMachines.put(space, sm);
          sm.retry();
        }
      }
    });
  }

  @Override
  public void unwatch(String space) {
    executor.execute(new NamedRunnable("deploymentsync-unwatch") {
      @Override
      public void execute() throws Exception {
        SM sm = stateMachines.remove(space);
        if (sm != null) {
          LOGGER.error("stop-watching:" + space);
          metrics.adamasync_watching.down();
          sm.stop();
        }
      }
    });
  }

  public void shutdown() {
    executor.execute(new NamedRunnable("deploymentsync-shutdown") {
      @Override
      public void execute() throws Exception {
        Iterator<SM> it = stateMachines.values().iterator();
        while (it.hasNext()) {
          it.next().stop();
          it.remove();
        }
      }
    });
  }
}
