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
package org.adamalang.overlord.roles;

import org.adamalang.ErrorCodes;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.NamedRunnable;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.multiregion.MultiRegionClient;
import org.adamalang.mysql.DataBase;
import org.adamalang.mysql.data.DeletedSpace;
import org.adamalang.mysql.data.DocumentIndex;
import org.adamalang.mysql.model.Domains;
import org.adamalang.mysql.model.FinderOperations;
import org.adamalang.mysql.model.Sentinel;
import org.adamalang.mysql.model.Spaces;
import org.adamalang.overlord.OverlordMetrics;
import org.adamalang.runtime.natives.NtPrincipal;
import org.adamalang.auth.AuthenticatedUser;
import org.adamalang.web.io.ConnectionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/** a bot that wakes up, looks for deleted spaces, and then ensures the name is ready for usage */
public class GlobalSpaceDeleteBot {
  public static AuthenticatedUser OVERLORD = new AuthenticatedUser(
      -1,
      new NtPrincipal("overlord", "overlord"),
      new ConnectionContext("overlord", "0.0.0.0", "overlord", new TreeMap<>()));
  private static Logger LOG = LoggerFactory.getLogger(GlobalSpaceDeleteBot.class);
  private final OverlordMetrics metrics;
  private final DataBase dataBase;
  private final MultiRegionClient client; // TODO: convert to multi-region client

  private GlobalSpaceDeleteBot(OverlordMetrics metrics, DataBase dataBase, MultiRegionClient client) {
    this.metrics = metrics;
    this.dataBase = dataBase;
    this.client = client;
  }

  private void item(DeletedSpace ds) throws Exception {
    metrics.delete_bot_found.run();
    Domains.deleteSpace(dataBase, ds.name);
    ArrayList<DocumentIndex> found = FinderOperations.list(dataBase, ds.name, "", 100);
    LOG.error("cleaning-up:" + ds.name + ";size=" + found.size());
    if (found.size() == 0) {
      CountDownLatch latch = new CountDownLatch(1);
      client.delete(OVERLORD, "ide", ds.name, metrics.delete_bot_delete_ide.wrap(new Callback<Void>() {
        @Override
        public void success(Void value) {
          LOG.error("cleaned:" + ds.name);
          metrics.delete_bot_delete_space.run();
          try {
            Spaces.delete(dataBase, ds.id, 0);
          } catch (Exception ex) {
            LOG.error("failed-delete-space", ex);
          }
          latch.countDown();
        }

        @Override
        public void failure(ErrorCodeException ex) {
          if (ex.code == ErrorCodes.UNIVERSAL_LOOKUP_FAILED || ex.code == ErrorCodes.NET_FINDER_GAVE_UP || ex.code == ErrorCodes.NET_FINDER_FAILED_PICK_HOST) {
            success(null);
            return;
          }
          LOG.error("failed-delete-ide: " + ds.name, ex);
          latch.countDown();
        }
      }));
      latch.await(10000, TimeUnit.MILLISECONDS);
    } else {
      for (DocumentIndex doc : found) {
        client.delete(OVERLORD, ds.name, doc.key, metrics.delete_bot_delete_document.wrap(Callback.DONT_CARE_VOID));
      }
    }
  }

  public void round() throws Exception {
    metrics.delete_bot_wake.run();
    for (final DeletedSpace ds : Spaces.listDeletedSpaces(dataBase)) {
      try {
        item(ds);
      } catch (Exception ex) {
        LOG.error("failed-delete:" + ds.name, ex);
      }
    }
  }

  public static void kickOff(OverlordMetrics metrics, DataBase dataBase, MultiRegionClient client, AtomicBoolean alive) {
    SimpleExecutor executor = SimpleExecutor.create("space-delete-bot");
    GlobalSpaceDeleteBot bot = new GlobalSpaceDeleteBot(metrics, dataBase, client);
    executor.schedule(new NamedRunnable("space-delete-bot") {
      @Override
      public void execute() throws Exception {
        if (alive.get()) {
          try {
            bot.round();
            Sentinel.ping(dataBase, "space-delete-bot", System.currentTimeMillis());
          } finally {
            executor.schedule(this, (int) (30000 + Math.random() * 30000));
          }
        }
      }
    }, 1000);
  }
}
