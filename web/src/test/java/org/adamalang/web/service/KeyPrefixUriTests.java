/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.web.service;

import org.junit.Assert;
import org.junit.Test;

public class KeyPrefixUriTests {
  @Test
  public void full() {
    KeyPrefixUri t = KeyPrefixUri.fromCompleteUri("/key/uri");
    Assert.assertEquals("key", t.key);
    Assert.assertEquals("/uri", t.uri);
  }

  @Test
  public void root() {
    KeyPrefixUri t = KeyPrefixUri.fromCompleteUri("/key");
    Assert.assertEquals("key", t.key);
    Assert.assertEquals("/", t.uri);
  }
}