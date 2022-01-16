/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.frontend;

import org.adamalang.api.ConnectionNexus;
import org.adamalang.api.ConnectionRouter;
import org.adamalang.api.Metrics;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.extern.ExternNexus;
import org.adamalang.transforms.Authenticator;
import org.adamalang.transforms.SpacePolicyLocator;
import org.adamalang.transforms.UserIdResolver;
import org.adamalang.web.contracts.HtmlHandler;
import org.adamalang.web.contracts.ServiceBase;
import org.adamalang.web.contracts.ServiceConnection;
import org.adamalang.web.io.ConnectionContext;
import org.adamalang.web.io.JsonRequest;
import org.adamalang.web.io.JsonResponder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BootstrapFrontend {

  // TODO: add config
  public static ServiceBase make(ExternNexus extern) throws Exception {

    // TODO: make multiple of these, pull nThreads from config
    ExecutorService executor = Executors.newSingleThreadExecutor();
    RootHandlerImpl handler = new RootHandlerImpl(extern);
    SpacePolicyLocator spacePolicyLocator = new SpacePolicyLocator(SimpleExecutor.create("space-policy-locator"), extern);
    UserIdResolver userIdResolver = new UserIdResolver(SimpleExecutor.create("user-id-resolver"), extern);
    Metrics metrics = new Metrics(extern.metricsFactory);


    return new ServiceBase() {
      @Override
      public ServiceConnection establish(ConnectionContext context) {
        return new ServiceConnection() {
          // TODO: pick an executor (randomly? pick two and do the faster of the two?)
          final ConnectionNexus nexus =
              new ConnectionNexus(
                  metrics, //
                  executor, //
                  userIdResolver, //
                  new Authenticator(extern), //
                  spacePolicyLocator); //
          final ConnectionRouter router = new ConnectionRouter(nexus, handler);

          @Override
          public void execute(JsonRequest request, JsonResponder responder) {
            router.route(request, responder);
          }

          @Override
          public boolean keepalive() {
            // TODO: check with the nexus for some activity
            // TODO: rule #1: there should be activity within the first 30 seconds of a connection
            // TODO: rule #2: there should be activity within the last 5 minutes
            return true;
          }

          @Override
          public void kill() {
            router.disconnect();
          }
        };
      }

      @Override
      public HtmlHandler html() {
        return HtmlHandler.NULL;
      }
    };
  }
}
