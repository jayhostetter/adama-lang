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
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.ExceptionLogger;
import org.adamalang.common.Hashing;
import org.adamalang.runtime.json.JsonStreamReader;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/** parses a deployment plan and constructs a safe plan which has yet to be compiled */
public class DeploymentPlan {
  public final String hash;
  public final HashMap<String, DeployedVersion> versions;
  public final ArrayList<Stage> stages;
  public final String defaultVersion;
  public final boolean instrument;

  public DeploymentPlan(String json, ExceptionLogger logger) throws ErrorCodeException {
    try {
      MessageDigest digest = Hashing.md5();
      digest.update(json.getBytes(StandardCharsets.UTF_8));
      this.hash = Hashing.finishAndEncode(digest);
      JsonStreamReader reader = new JsonStreamReader(json);
      versions = new HashMap<>();
      stages = new ArrayList<>();
      String _defaultVersion = null;
      boolean _instrument = false;
      if (reader.startObject()) {
        while (reader.notEndOfObject()) {
          switch (reader.fieldName()) {
            case "instrument": {
              _instrument = reader.readBoolean();
            }
            break;
            case "versions": {
              if (reader.startObject()) {
                while (reader.notEndOfObject()) {
                  String version = reader.fieldName();
                  versions.put(version, new DeployedVersion(reader));
                }
              } else {
                throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_VERSIONS_MUST_BE_OBJECT);
              }
            }
            break;
            case "default": {
              _defaultVersion = reader.readString();
            }
            break;
            case "plan": {
              if (reader.startArray()) {
                while (reader.notEndOfArray()) {
                  if (reader.startObject()) {
                    String _seed = "";
                    String _prefix = "";
                    double _percent = 1.0;
                    String _version = null;
                    HashSet<String> _keys = null;
                    while (reader.notEndOfObject()) {
                      switch (reader.fieldName()) {
                        case "version":
                          _version = reader.readString();
                          if (!versions.containsKey(_version)) {
                            throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_VERSION_MUST_EXIST);
                          }
                          break;
                        case "keys": {
                          _keys = new HashSet<>();
                          if (reader.startArray()) {
                            while (reader.notEndOfArray()) {
                              _keys.add(reader.readString());
                            }
                          } else {
                            throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_KEYS_MUST_BE_ARRAY);
                          }
                        }
                        break;
                        case "prefix":
                          _prefix = reader.readString();
                          break;
                        case "seed":
                          _seed = reader.readString();
                          break;
                        case "percent":
                          _percent = reader.readDouble();
                          break;
                        default:
                          throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_UNKNOWN_FIELD_STAGE);
                      }
                    }
                    if (_version == null) {
                      throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_PLAN_NO_VERSION);
                    }
                    stages.add(new Stage(_version, _keys, _prefix, _seed, _percent));
                  } else {
                    throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_PLAN_ARRAY_ELEMENT_MUST_OBJECT);
                  }
                }
              } else {
                throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_PLAN_MUST_BE_ARRAY);
              }
            }
            break;
            default:
              throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_UNKNOWN_FIELD_ROOT);
          }
        }
        if (versions.size() == 0) {
          throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_NO_VERSIONS);
        }
        if (_defaultVersion == null) {
          throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_NO_DEFAULT);
        }
        if (!versions.containsKey(_defaultVersion)) {
          throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_MUST_HAVE_DEFAULT);
        }
        this.defaultVersion = _defaultVersion;
        this.instrument = _instrument;
      } else {
        throw new ErrorCodeException(ErrorCodes.DEPLOYMENT_PLAN_MUST_BE_ROOT_OBJECT);
      }

    } catch (Exception ex) {
      throw ErrorCodeException.detectOrWrap(ErrorCodes.DEPLOYMENT_UNKNOWN_EXCEPTION, ex, logger);
    }
  }

  public String pickVersion(String key) {
    for (Stage stage : stages) {
      if (stage.keys != null) {
        if (stage.keys.contains(key)) {
          return stage.version;
        }
      }
      if (key.startsWith(stage.prefix)) {
        if (stage.percent >= 100) {
          return stage.version;
        }
        double check = hash(stage.seed, key);
        if (check <= stage.percent) {
          return stage.version;
        }
      }
    }
    return defaultVersion;
  }

  public static double hash(String seed, String key) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(seed.getBytes(StandardCharsets.UTF_8));
      byte[] bytes = md.digest(key.getBytes(StandardCharsets.UTF_8));
      return (Math.abs(Arrays.hashCode(bytes)) % 1000000) / 10000.0;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static class Stage {
    public final String version;
    public final HashSet<String> keys;
    public final String prefix;
    public final String seed;
    public final double percent;

    public Stage(String version, HashSet<String> keys, String prefix, String seed, double percent) {
      this.version = version;
      this.keys = keys;
      this.prefix = prefix;
      this.seed = seed;
      this.percent = percent;
    }
  }
}
