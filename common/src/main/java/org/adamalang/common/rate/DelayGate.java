/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common.rate;

import org.adamalang.common.TimeSource;

public class DelayGate {
  private final TimeSource time;
  private final long periodMilliseconds;
  private long last;

  public DelayGate(TimeSource time, long periodMilliseconds) {
    this.time = time;
    this.periodMilliseconds = periodMilliseconds;
    this.last = 0;
  }

  public boolean test() {
    long now = time.nowMilliseconds();
    if (now - last >= periodMilliseconds) {
      last = now;
      return true;
    }
    return false;
  }

}