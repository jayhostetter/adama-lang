/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.cli.router;

import org.adamalang.cli.router.Arguments.*;
import org.adamalang.cli.runtime.Output.*;

public interface AwsHandler {
  void setup(AwsSetupArgs args, YesOrError output) throws Exception;
  void testEmail(AwsTestEmailArgs args, YesOrError output) throws Exception;
  void testAssetListing(AwsTestAssetListingArgs args, YesOrError output) throws Exception;
  void testEnqueue(AwsTestEnqueueArgs args, YesOrError output) throws Exception;
  void downloadArchive(AwsDownloadArchiveArgs args, YesOrError output) throws Exception;
  void memoryTest(AwsMemoryTestArgs args, YesOrError output) throws Exception;
}