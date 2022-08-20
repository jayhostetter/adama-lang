/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common;

/** Hex encodings */
public class Hex {
  private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  public static String of(final byte[] bytes) {
    final int n = bytes.length;
    final char[] encoded = new char[n * 2];
    int j = 0;
    for (int i = 0; i < n; i++) {
      encoded[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
      encoded[j++] = HEX[0x0F & bytes[i]];
    }
    return new String(encoded);
  }
}