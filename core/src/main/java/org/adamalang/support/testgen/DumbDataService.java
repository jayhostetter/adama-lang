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

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.Json;
import org.adamalang.runtime.contracts.DeleteTask;
import org.adamalang.runtime.data.*;
import org.adamalang.runtime.json.JsonAlgebra;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.PrivateView;
import org.adamalang.runtime.sys.DurableLivingDocument;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DumbDataService implements DataService {
  public static Callback<PrivateView> makePrinterPrivateView(String prefix, StringBuilder sb) {
    return new Callback<PrivateView>() {
      @Override
      public void success(PrivateView value) {
        sb.append(prefix + ": CREATED PRIVATE VIEW\n");
      }

      @Override
      public void failure(ErrorCodeException ex) {
        sb.append(prefix + ": FAILED PRIVATE VIEW DUE TO:" + ex.code + "\n");
      }
    };
  }
  public static Callback<Integer> makePrinterInt(String prefix, StringBuilder sb) {
    return new Callback<>() {
      @Override
      public void success(Integer value) {
        sb.append(prefix + "|SUCCESS:" + value + "\n");
      }

      @Override
      public void failure(ErrorCodeException ex) {
        sb.append(prefix + "|FAILURE:" + ex.code + "\n");
      }
    };
  }
  public final HashSet<Key> deleted;
  public boolean deletesWork = true;
  public boolean computesWork = true;
  public boolean dropPatches = false;
  private Object tree;
  private String data;
  private final Consumer<RemoteDocumentUpdate> updates;
  public long assetsSeen = 0L;

  public DumbDataService(Consumer<RemoteDocumentUpdate> updates) {
    this.tree = new HashMap<String, Object>();
    this.deleted = new HashSet<>();
    this.data = null;
    this.updates = updates;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public void get(Key key, Callback<LocalDocumentChange> callback) {
    if (data != null) {
      callback.success(new LocalDocumentChange(data, 1, 1));
    } else {
      callback.failure(new ErrorCodeException(0, new UnsupportedOperationException()));
    }
  }

  @Override
  public void initialize(Key key, RemoteDocumentUpdate patch, Callback<Void> callback) {
    patch(key, new RemoteDocumentUpdate[]{patch}, callback);
  }

  @Override
  public void patch(Key key, RemoteDocumentUpdate[] patches, Callback<Void> callback) {
    for (RemoteDocumentUpdate update : patches) {
      assetsSeen += update.assetBytes;
    }
    if (dropPatches) {
      return;
    }
    for (RemoteDocumentUpdate patch : patches) {
      Json.parseJsonObject(patch.redo);
      Json.parseJsonObject(patch.undo);
      updates.accept(patch);
      JsonStreamReader reader = new JsonStreamReader(patch.redo);
      tree = JsonAlgebra.merge(tree, reader.readJavaTree(), false);
    }
    callback.success(null);
  }

  @Override
  public void compute(Key key, ComputeMethod method, int seq, Callback<LocalDocumentChange> callback) {
    if (computesWork) {
      if (method == ComputeMethod.Rewind) {
        callback.success(new LocalDocumentChange("{\"x\":1000}", 1, 1));
      }
    } else {
      callback.failure(new ErrorCodeException(23456));
    }
  }

  @Override
  public void delete(Key key, DeleteTask task, Callback<Void> callback) {
    if (deletesWork) {
      deleted.add(key);
      task.executeAfterMark(callback);
    } else {
      callback.failure(new ErrorCodeException(1234567));
    }
  }

  @Override
  public void snapshot(Key key, DocumentSnapshot snapshot, Callback<Integer> callback) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void shed(Key key) {
  }

  @Override
  public void inventory(Callback<Set<Key>> callback) {
    callback.failure(new ErrorCodeException(-123));
  }

  @Override
  public void recover(Key key, DocumentRestore restore, Callback<Void> callback) {
    callback.failure(new ErrorCodeException(-42));
  }

  @Override
  public void close(Key key, Callback<Void> callback) {
    callback.success(null);
  }

  public static class DumbDurableLivingDocumentAcquire implements Callback<DurableLivingDocument> {
    private DurableLivingDocument value;

    public DumbDurableLivingDocumentAcquire() {
      this.value = null;
    }

    public DurableLivingDocument get() {
      if (value == null) {
        throw new NullPointerException();
      }
      return value;
    }

    @Override
    public void success(DurableLivingDocument value) {
      this.value = value;
    }

    @Override
    public void failure(ErrorCodeException ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
}
