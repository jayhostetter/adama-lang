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
package org.adamalang.web.assets.cache;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.common.TimeSource;
import org.adamalang.common.cache.AsyncSharedLRUCache;
import org.adamalang.common.cache.SyncCacheLRU;
import org.adamalang.runtime.natives.NtAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/** the asset cache for the web handler */
public class WebHandlerAssetCache {
  private final SimpleExecutor memoryExecutor;
  private final SimpleExecutor fileExecutor;
  private final SyncCacheLRU<NtAsset, CachedAsset> memoryCache;
  private final AsyncSharedLRUCache<NtAsset, CachedAsset> memoryAsync;
  private final SyncCacheLRU<NtAsset, CachedAsset> fileCache;
  private final AsyncSharedLRUCache<NtAsset, CachedAsset> fileAsync;
  private final AtomicLong localId;
  private final AtomicBoolean alive;

  public WebHandlerAssetCache(TimeSource time, File cacheRoot) {
    this.alive = new AtomicBoolean(true);
    { // memory cache
      this.memoryExecutor = SimpleExecutor.create("webhandle-memcache");
      this.memoryCache = new SyncCacheLRU<>(time, 10, 2000, 64 * 1024 * 1024L, 10 * 60000, (key, mem) -> {
        mem.evict();
      });
      this.memoryAsync = new AsyncSharedLRUCache<>(memoryExecutor, memoryCache, (asset, cb) -> {
        cb.success(new MemoryCacheAsset(asset, memoryExecutor));
      });
      this.memoryAsync.startSweeping(alive, 45000, 90000);
    }
    this.localId = new AtomicLong(0);
    for (File prior : cacheRoot.listFiles((dir, name) -> name.endsWith(".cache") && name.startsWith("asset."))) {
      prior.delete();
    }
    { // file cache
      this.fileExecutor = SimpleExecutor.create("webhandle-file");
      this.fileCache = new SyncCacheLRU<>(time, 25, 500, 1024 * 1024 * 1024L, 20 * 60000, (key, file) -> {
        file.evict();
      });
      this.fileAsync = new AsyncSharedLRUCache<>(fileExecutor, fileCache, (asset, cb) -> {
        try {
          cb.success(new FileCacheAsset(localId.getAndIncrement(), cacheRoot, asset, fileExecutor));
        } catch (ErrorCodeException ece) {
          cb.failure(ece);
        }
      });
      this.fileAsync.startSweeping(alive, 45000, 90000);
    }
  }

  public static boolean policyCacheMemory(NtAsset asset) {
    // TODO: expand policy
    boolean isHtml = asset.contentType.equals("text/html");
    boolean isCSS = asset.contentType.equals("text/css");
    boolean isJavaScript = asset.contentType.equals("text/javascript");
    if (asset.size < 196 * 1024 && (isHtml || isCSS || isJavaScript)) {
      return true;
    }
    return false;
  }

  public static boolean policyCacheDisk(NtAsset asset) {
    return asset.size < 16 * 1024 * 1024;
  }

  public static boolean canCache(NtAsset asset) {
    return policyCacheMemory(asset) || policyCacheDisk(asset);
  }

  public void get(NtAsset asset, Callback<CachedAsset> callback) {
    if (policyCacheMemory(asset)) {
      memoryAsync.get(asset, callback);
    } else {
      fileAsync.get(asset, callback);
    }
  }

  public void failure(NtAsset asset) {
    memoryAsync.forceEvictionFromCacheNoDownstreamEviction(asset);
    fileAsync.forceEvictionFromCacheNoDownstreamEviction(asset);
  }

  public void shutdown() {
    alive.set(false);
    try {
      memoryExecutor.shutdown();
    } catch (Exception ex) {
    }
    try {
      fileExecutor.shutdown();
    } catch (Exception ex) {
    }
  }
}
