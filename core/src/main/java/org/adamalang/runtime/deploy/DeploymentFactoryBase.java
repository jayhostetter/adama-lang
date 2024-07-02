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
package org.adamalang.runtime.deploy;

import org.adamalang.ErrorCodes;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.keys.PrivateKeyBundle;
import org.adamalang.runtime.contracts.LivingDocumentFactoryFactory;
import org.adamalang.runtime.data.Key;
import org.adamalang.runtime.natives.NtPrincipal;
import org.adamalang.runtime.remote.Deliverer;
import org.adamalang.runtime.remote.RemoteResult;
import org.adamalang.runtime.sys.PerfTracker;
import org.adamalang.runtime.sys.PredictiveInventory;
import org.adamalang.translator.env.RuntimeEnvironment;
import org.adamalang.translator.jvm.LivingDocumentFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** this is the base for all spaces to resolve against */
public class DeploymentFactoryBase implements LivingDocumentFactoryFactory, Deliverer, Undeploy {
  private final AsyncByteCodeCache cache;
  private final ConcurrentHashMap<String, DeploymentFactory> spaces;
  private final RuntimeEnvironment runtime;
  private Deliverer deliverer;

  public DeploymentFactoryBase(AsyncByteCodeCache cache, RuntimeEnvironment runtime) {
    this.cache = cache;
    this.spaces = new ConcurrentHashMap<>();
    this.deliverer = Deliverer.FAILURE;
    this.runtime = runtime;
  }

  public void attachDeliverer(Deliverer deliverer) {
    this.deliverer = deliverer;
  }

  public String hashOf(String space) {
    DeploymentFactory factory = this.spaces.get(space);
    if (factory != null) {
      return factory.plan.hash;
    }
    return null;
  }

  @Override
  public void deliver(NtPrincipal agent, Key key, int id, RemoteResult result, boolean firstParty, Callback<Integer> callback) {
    deliverer.deliver(agent, key, id, result, firstParty, callback);
  }

  public void deploy(String space, DeploymentPlan plan, TreeMap<Integer, PrivateKeyBundle> keys, Callback<Void> callback){
    long started = System.currentTimeMillis();
    AsyncCompiler.forge(runtime, space, spaces.get(space), plan, this, keys, cache, new Callback<DeploymentFactory>() {
      @Override
      public void success(DeploymentFactory factory) {
        spaces.put(space, factory);
        PerfTracker.writeDeploymentTime(space, System.currentTimeMillis() - started, true);
        callback.success(null);
      }

      @Override
      public void failure(ErrorCodeException ex) {
        PerfTracker.writeDeploymentTime(space, System.currentTimeMillis() - started, false);
        callback.failure(ex);
      }
    });
  }

  public boolean contains(String space) {
    return spaces.containsKey(space);
  }

  @Override
  public void undeploy(String space) {
    spaces.remove(space);
  }

  @Override
  public void fetch(Key key, Callback<LivingDocumentFactory> callback) {
    DeploymentFactory factory = spaces.get(key.space);
    if (factory == null) {
      callback.failure(new ErrorCodeException(ErrorCodes.DEPLOYMENT_FACTORY_CANT_FIND_SPACE));
      return;
    }
    factory.fetch(key, callback);
  }

  @Override
  public void account(HashMap<String, PredictiveInventory.MeteringSample> sample) {
    for (DeploymentFactory factory : spaces.values()) {
      factory.account(sample);
    }
  }

  @Override
  public Collection<String> spacesAvailable() {
    return spaces.keySet();
  }
}
