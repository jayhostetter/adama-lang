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
package org.adamalang.common.keys;

import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/** a simple factory for creating public/private keys for VAPID */
public class VAPIDFactory {
  private SecureRandom random;

  public VAPIDFactory(SecureRandom random) {
    this.random = random;
  }

  public VAPIDPublicPrivateKeyPair generateKeyPair() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
    generator.initialize(spec, this.random);
    KeyPair keyPair = generator.generateKeyPair();
    ECPublicKey publicKey = (java.security.interfaces.ECPublicKey) keyPair.getPublic();
    String publicKey64 = Base64.getEncoder().encodeToString(ECPublicKeyCodec.encode(publicKey));
    String privateKey64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    return new VAPIDPublicPrivateKeyPair(publicKey64, privateKey64);
  }
}
