/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.cli.router;

import org.adamalang.cli.Util;
import org.adamalang.cli.Config;

public class Arguments {
	public static class SpaceCreateArgs {
		public Config config;
		public String space;
		public static SpaceCreateArgs from(String[] args, int start) {
			SpaceCreateArgs returnArgs = new SpaceCreateArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Creates a new space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space create", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class SpaceDeleteArgs {
		public Config config;
		public String space;
		public static SpaceDeleteArgs from(String[] args, int start) {
			SpaceDeleteArgs returnArgs = new SpaceDeleteArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Deletes an empty space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space delete", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class SpaceDeployArgs {
		public Config config;
		public String space;
		public String dumpTo = null;
		public String plan;
		public String file = null;
		public static SpaceDeployArgs from(String[] args, int start) {
			SpaceDeployArgs returnArgs = new SpaceDeployArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--plan", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-d":
					case "--dump-to": {
						if (k+1 < args.length) {
							returnArgs.dumpTo = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-p":
					case "--plan": {
						if (k+1 < args.length) {
							returnArgs.plan = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Deploy a plan to a space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space deploy", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-p, --plan", Util.ANSI.Green) + " " + Util.prefix("<plan>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-d, --dump-to", Util.ANSI.Green) + " " + Util.prefix("<dump-to>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
		}
	}
	public static class SpaceSetRxhtmlArgs {
		public Config config;
		public String space;
		public String file = null;
		public static SpaceSetRxhtmlArgs from(String[] args, int start) {
			SpaceSetRxhtmlArgs returnArgs = new SpaceSetRxhtmlArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Set the frontend RxHTML forest", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space set-rxhtml", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
		}
	}
	public static class SpaceGetRxhtmlArgs {
		public Config config;
		public String space;
		public static SpaceGetRxhtmlArgs from(String[] args, int start) {
			SpaceGetRxhtmlArgs returnArgs = new SpaceGetRxhtmlArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Get the frontend RxHTML forest", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space get-rxhtml", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class SpaceUploadArgs {
		public Config config;
		public String space;
		public String gc = "no";
		public String root = null;
		public String file;
		public static SpaceUploadArgs from(String[] args, int start) {
			SpaceUploadArgs returnArgs = new SpaceUploadArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--file", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-g":
					case "--gc": {
						if (k+1 < args.length) {
							returnArgs.gc = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-r":
					case "--root": {
						if (k+1 < args.length) {
							returnArgs.root = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Placeholder", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space upload", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-g, --gc", Util.ANSI.Green) + " " + Util.prefix("<gc>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-r, --root", Util.ANSI.Green) + " " + Util.prefix("<root>", Util.ANSI.White));
		}
	}
	public static class SpaceDownloadArgs {
		public Config config;
		public String space;
		public static SpaceDownloadArgs from(String[] args, int start) {
			SpaceDownloadArgs returnArgs = new SpaceDownloadArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Download a space's plan", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space download", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class SpaceListArgs {
		public Config config;
		public String marker = "";
		public String limit = "100";
		public static SpaceListArgs from(String[] args, int start) {
			SpaceListArgs returnArgs = new SpaceListArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-m":
					case "--marker": {
						if (k+1 < args.length) {
							returnArgs.marker = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-l":
					case "--limit": {
						if (k+1 < args.length) {
							returnArgs.limit = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("List spaces available to your account", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space list", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-m, --marker", Util.ANSI.Green) + " " + Util.prefix("<marker>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-l, --limit", Util.ANSI.Green) + " " + Util.prefix("<limit>", Util.ANSI.White));
		}
	}
	public static class SpaceUsageArgs {
		public Config config;
		public String space;
		public String limit = "336";
		public static SpaceUsageArgs from(String[] args, int start) {
			SpaceUsageArgs returnArgs = new SpaceUsageArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-l":
					case "--limit": {
						if (k+1 < args.length) {
							returnArgs.limit = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Iterate the billed usage", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space usage", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-l, --limit", Util.ANSI.Green) + " " + Util.prefix("<limit>", Util.ANSI.White));
		}
	}
	public static class SpaceReflectArgs {
		public Config config;
		public String space;
		public String marker;
		public String output;
		public String key;
		public String limit = "336";
		public static SpaceReflectArgs from(String[] args, int start) {
			SpaceReflectArgs returnArgs = new SpaceReflectArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--marker", "--output", "--key", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-m":
					case "--marker": {
						if (k+1 < args.length) {
							returnArgs.marker = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-o":
					case "--output": {
						if (k+1 < args.length) {
							returnArgs.output = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[3] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-l":
					case "--limit": {
						if (k+1 < args.length) {
							returnArgs.limit = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Get a file of the reflection of a space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space reflect", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-m, --marker", Util.ANSI.Green) + " " + Util.prefix("<marker>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-o, --output", Util.ANSI.Green) + " " + Util.prefix("<output>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-l, --limit", Util.ANSI.Green) + " " + Util.prefix("<limit>", Util.ANSI.White));
		}
	}
	public static class SpaceSetRoleArgs {
		public Config config;
		public String space;
		public String marker;
		public String email = "";
		public String role = "none";
		public static SpaceSetRoleArgs from(String[] args, int start) {
			SpaceSetRoleArgs returnArgs = new SpaceSetRoleArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--marker", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-m":
					case "--marker": {
						if (k+1 < args.length) {
							returnArgs.marker = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-e":
					case "--email": {
						if (k+1 < args.length) {
							returnArgs.email = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-r":
					case "--role": {
						if (k+1 < args.length) {
							returnArgs.role = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Get a file of the reflection of a space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space set-role", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-m, --marker", Util.ANSI.Green) + " " + Util.prefix("<marker>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-e, --email", Util.ANSI.Green) + " " + Util.prefix("<email>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-r, --role", Util.ANSI.Green) + " " + Util.prefix("<role>", Util.ANSI.White));
		}
	}
	public static class SpaceGenerateKeyArgs {
		public Config config;
		public String space;
		public static SpaceGenerateKeyArgs from(String[] args, int start) {
			SpaceGenerateKeyArgs returnArgs = new SpaceGenerateKeyArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Generate a server-side key to use for storing secrets", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space generate-key", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class SpaceEncryptSecretArgs {
		public Config config;
		public String space;
		public static SpaceEncryptSecretArgs from(String[] args, int start) {
			SpaceEncryptSecretArgs returnArgs = new SpaceEncryptSecretArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Encrypt a secret to store within code", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama space encrypt-secret", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
		}
	}
	public static class AuthorityCreateArgs {
		public Config config;
		public static AuthorityCreateArgs from(String[] args, int start) {
			AuthorityCreateArgs returnArgs = new AuthorityCreateArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Creates a new authority", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority create", Util.ANSI.Green));
		}
	}
	public static class AuthoritySetArgs {
		public Config config;
		public String authority;
		public String keystore;
		public static AuthoritySetArgs from(String[] args, int start) {
			AuthoritySetArgs returnArgs = new AuthoritySetArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--authority", "--keystore", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--authority": {
						if (k+1 < args.length) {
							returnArgs.authority = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--keystore": {
						if (k+1 < args.length) {
							returnArgs.keystore = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Set public keys to an authority", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority set", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --authority", Util.ANSI.Green) + " " + Util.prefix("<authority>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --keystore", Util.ANSI.Green) + " " + Util.prefix("<keystore>", Util.ANSI.White));
		}
	}
	public static class AuthorityGetArgs {
		public Config config;
		public String authority;
		public String keystore;
		public static AuthorityGetArgs from(String[] args, int start) {
			AuthorityGetArgs returnArgs = new AuthorityGetArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--authority", "--keystore", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--authority": {
						if (k+1 < args.length) {
							returnArgs.authority = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--keystore": {
						if (k+1 < args.length) {
							returnArgs.keystore = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Get released public keys for an authority", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority get", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --authority", Util.ANSI.Green) + " " + Util.prefix("<authority>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --keystore", Util.ANSI.Green) + " " + Util.prefix("<keystore>", Util.ANSI.White));
		}
	}
	public static class AuthorityDestroyArgs {
		public Config config;
		public String authority;
		public static AuthorityDestroyArgs from(String[] args, int start) {
			AuthorityDestroyArgs returnArgs = new AuthorityDestroyArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--authority", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--authority": {
						if (k+1 < args.length) {
							returnArgs.authority = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Destroy an authority", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority destroy", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --authority", Util.ANSI.Green) + " " + Util.prefix("<authority>", Util.ANSI.White));
		}
	}
	public static class AuthorityListArgs {
		public Config config;
		public static AuthorityListArgs from(String[] args, int start) {
			AuthorityListArgs returnArgs = new AuthorityListArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("List authorities this developer owns", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority list", Util.ANSI.Green));
		}
	}
	public static class AuthorityCreateLocalArgs {
		public Config config;
		public String authority;
		public String keystore;
		public String priv;
		public static AuthorityCreateLocalArgs from(String[] args, int start) {
			AuthorityCreateLocalArgs returnArgs = new AuthorityCreateLocalArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--authority", "--keystore", "--priv", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--authority": {
						if (k+1 < args.length) {
							returnArgs.authority = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--keystore": {
						if (k+1 < args.length) {
							returnArgs.keystore = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-p":
					case "--priv": {
						if (k+1 < args.length) {
							returnArgs.priv = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Make a new set of public keys", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority create-local", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --authority", Util.ANSI.Green) + " " + Util.prefix("<authority>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --keystore", Util.ANSI.Green) + " " + Util.prefix("<keystore>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-p, --priv", Util.ANSI.Green) + " " + Util.prefix("<priv>", Util.ANSI.White));
		}
	}
	public static class AuthorityAppendLocalArgs {
		public Config config;
		public String authority;
		public String keystore;
		public String priv;
		public static AuthorityAppendLocalArgs from(String[] args, int start) {
			AuthorityAppendLocalArgs returnArgs = new AuthorityAppendLocalArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--authority", "--keystore", "--priv", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--authority": {
						if (k+1 < args.length) {
							returnArgs.authority = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--keystore": {
						if (k+1 < args.length) {
							returnArgs.keystore = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-p":
					case "--priv": {
						if (k+1 < args.length) {
							returnArgs.priv = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Append a new public key to the public key file", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority append-local", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --authority", Util.ANSI.Green) + " " + Util.prefix("<authority>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --keystore", Util.ANSI.Green) + " " + Util.prefix("<keystore>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-p, --priv", Util.ANSI.Green) + " " + Util.prefix("<priv>", Util.ANSI.White));
		}
	}
	public static class AuthoritySignArgs {
		public Config config;
		public String key;
		public String agent;
		public String validate = null;
		public static AuthoritySignArgs from(String[] args, int start) {
			AuthoritySignArgs returnArgs = new AuthoritySignArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--key", "--agent", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-ag":
					case "--agent": {
						if (k+1 < args.length) {
							returnArgs.agent = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-v":
					case "--validate": {
						if (k+1 < args.length) {
							returnArgs.validate = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Sign an agent with a local private key", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama authority sign", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-ag, --agent", Util.ANSI.Green) + " " + Util.prefix("<agent>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-v, --validate", Util.ANSI.Green) + " " + Util.prefix("<validate>", Util.ANSI.White));
		}
	}
	public static class AccountSetPasswordArgs {
		public Config config;
		public static AccountSetPasswordArgs from(String[] args, int start) {
			AccountSetPasswordArgs returnArgs = new AccountSetPasswordArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Create a password to be used on web", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama account set-password", Util.ANSI.Green));
		}
	}
	public static class AccountTestGtokenArgs {
		public Config config;
		public static AccountTestGtokenArgs from(String[] args, int start) {
			AccountTestGtokenArgs returnArgs = new AccountTestGtokenArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Test a Google token converts to an email", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama account test-gtoken", Util.ANSI.Green));
		}
	}
	public static class AwsSetupArgs {
		public Config config;
		public static AwsSetupArgs from(String[] args, int start) {
			AwsSetupArgs returnArgs = new AwsSetupArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Interactive setup for the config", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws setup", Util.ANSI.Green));
		}
	}
	public static class AwsTestEmailArgs {
		public Config config;
		public static AwsTestEmailArgs from(String[] args, int start) {
			AwsTestEmailArgs returnArgs = new AwsTestEmailArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Test Email via AWS", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws test-email", Util.ANSI.Green));
		}
	}
	public static class AwsTestAssetListingArgs {
		public Config config;
		public String space;
		public String key;
		public static AwsTestAssetListingArgs from(String[] args, int start) {
			AwsTestAssetListingArgs returnArgs = new AwsTestAssetListingArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--key", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Placeholder", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws test-asset-listing", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
		}
	}
	public static class AwsTestEnqueueArgs {
		public Config config;
		public static AwsTestEnqueueArgs from(String[] args, int start) {
			AwsTestEnqueueArgs returnArgs = new AwsTestEnqueueArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Placeholder", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws test-enqueue", Util.ANSI.Green));
		}
	}
	public static class AwsDownloadArchiveArgs {
		public Config config;
		public String archive;
		public String space;
		public String key;
		public static AwsDownloadArchiveArgs from(String[] args, int start) {
			AwsDownloadArchiveArgs returnArgs = new AwsDownloadArchiveArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--archive", "--space", "--key", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-a":
					case "--archive": {
						if (k+1 < args.length) {
							returnArgs.archive = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Download (and validate) an archive", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws download-archive", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-a, --archive", Util.ANSI.Green) + " " + Util.prefix("<archive>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
		}
	}
	public static class AwsMemoryTestArgs {
		public Config config;
		public static AwsMemoryTestArgs from(String[] args, int start) {
			AwsMemoryTestArgs returnArgs = new AwsMemoryTestArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Crash by allocating memory", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama aws memory-test", Util.ANSI.Green));
		}
	}
	public static class BusinessAddBalanceArgs {
		public Config config;
		public String email;
		public String pennies;
		public static BusinessAddBalanceArgs from(String[] args, int start) {
			BusinessAddBalanceArgs returnArgs = new BusinessAddBalanceArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--email", "--pennies", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-e":
					case "--email": {
						if (k+1 < args.length) {
							returnArgs.email = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-p":
					case "--pennies": {
						if (k+1 < args.length) {
							returnArgs.pennies = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Interactive setup for the config", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama business add-balance", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-e, --email", Util.ANSI.Green) + " " + Util.prefix("<email>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-p, --pennies", Util.ANSI.Green) + " " + Util.prefix("<pennies>", Util.ANSI.White));
		}
	}
	public static class CodeLspArgs {
		public Config config;
		public String port = "2423";
		public static CodeLspArgs from(String[] args, int start) {
			CodeLspArgs returnArgs = new CodeLspArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-p":
					case "--port": {
						if (k+1 < args.length) {
							returnArgs.port = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Spin up a single threaded language service protocol server", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama code lsp", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-p, --port", Util.ANSI.Green) + " " + Util.prefix("<port>", Util.ANSI.White));
		}
	}
	public static class CodeValidatePlanArgs {
		public Config config;
		public String plan;
		public static CodeValidatePlanArgs from(String[] args, int start) {
			CodeValidatePlanArgs returnArgs = new CodeValidatePlanArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--plan", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-p":
					case "--plan": {
						if (k+1 < args.length) {
							returnArgs.plan = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Validates a deployment plan (locally) for speed", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama code validate-plan", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-p, --plan", Util.ANSI.Green) + " " + Util.prefix("<plan>", Util.ANSI.White));
		}
	}
	public static class CodeBundlePlanArgs {
		public Config config;
		public String output;
		public String main;
		public String imports;
		public static CodeBundlePlanArgs from(String[] args, int start) {
			CodeBundlePlanArgs returnArgs = new CodeBundlePlanArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--output", "--main", "--imports", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-o":
					case "--output": {
						if (k+1 < args.length) {
							returnArgs.output = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-m":
					case "--main": {
						if (k+1 < args.length) {
							returnArgs.main = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-i":
					case "--imports": {
						if (k+1 < args.length) {
							returnArgs.imports = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Placeholder", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama code bundle-plan", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-o, --output", Util.ANSI.Green) + " " + Util.prefix("<output>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-m, --main", Util.ANSI.Green) + " " + Util.prefix("<main>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-i, --imports", Util.ANSI.Green) + " " + Util.prefix("<imports>", Util.ANSI.White));
		}
	}
	public static class CodeCompileFileArgs {
		public Config config;
		public String file;
		public String dumpTo = null;
		public static CodeCompileFileArgs from(String[] args, int start) {
			CodeCompileFileArgs returnArgs = new CodeCompileFileArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--file", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-d":
					case "--dump-to": {
						if (k+1 < args.length) {
							returnArgs.dumpTo = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Compiles the adama file and shows any problems", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama code compile-file", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-d, --dump-to", Util.ANSI.Green) + " " + Util.prefix("<dump-to>", Util.ANSI.White));
		}
	}
	public static class CodeReflectDumpArgs {
		public Config config;
		public String file;
		public String dumpTo = null;
		public static CodeReflectDumpArgs from(String[] args, int start) {
			CodeReflectDumpArgs returnArgs = new CodeReflectDumpArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--file", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-d":
					case "--dump-to": {
						if (k+1 < args.length) {
							returnArgs.dumpTo = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Compiles the adama file and dumps the reflection json", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama code reflect-dump", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-d, --dump-to", Util.ANSI.Green) + " " + Util.prefix("<dump-to>", Util.ANSI.White));
		}
	}
	public static class ContribTestsAdamaArgs {
		public Config config;
		public String input = "./test_code";
		public String output = "./src/test/java/org/adamalang/translator";
		public String errors = "./error-messages.csv";
		public static ContribTestsAdamaArgs from(String[] args, int start) {
			ContribTestsAdamaArgs returnArgs = new ContribTestsAdamaArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-i":
					case "--input": {
						if (k+1 < args.length) {
							returnArgs.input = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-o":
					case "--output": {
						if (k+1 < args.length) {
							returnArgs.output = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-e":
					case "--errors": {
						if (k+1 < args.length) {
							returnArgs.errors = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Generate tests for Adama Language.", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib tests-adama", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-i, --input", Util.ANSI.Green) + " " + Util.prefix("<input>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-o, --output", Util.ANSI.Green) + " " + Util.prefix("<output>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-e, --errors", Util.ANSI.Green) + " " + Util.prefix("<errors>", Util.ANSI.White));
		}
	}
	public static class ContribTestsRxhtmlArgs {
		public Config config;
		public String input = "./test_templates";
		public String output = "./src/test/java/org/adamalang/rxhtml";
		public static ContribTestsRxhtmlArgs from(String[] args, int start) {
			ContribTestsRxhtmlArgs returnArgs = new ContribTestsRxhtmlArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-i":
					case "--input": {
						if (k+1 < args.length) {
							returnArgs.input = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-o":
					case "--output": {
						if (k+1 < args.length) {
							returnArgs.output = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Generate tests for RxHTML.", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib tests-rxhtml", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-i, --input", Util.ANSI.Green) + " " + Util.prefix("<input>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-o, --output", Util.ANSI.Green) + " " + Util.prefix("<output>", Util.ANSI.White));
		}
	}
	public static class ContribMakeCodecArgs {
		public Config config;
		public static ContribMakeCodecArgs from(String[] args, int start) {
			ContribMakeCodecArgs returnArgs = new ContribMakeCodecArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Generates the networking codec", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib make-codec", Util.ANSI.Green));
		}
	}
	public static class ContribMakeApiArgs {
		public Config config;
		public static ContribMakeApiArgs from(String[] args, int start) {
			ContribMakeApiArgs returnArgs = new ContribMakeApiArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Produces api files for SaaS and documentation for the WebSocket low level API.", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib make-api", Util.ANSI.Green));
		}
	}
	public static class ContribBundleJsArgs {
		public Config config;
		public static ContribBundleJsArgs from(String[] args, int start) {
			ContribBundleJsArgs returnArgs = new ContribBundleJsArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Bundles the libadama.js into the webserver", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib bundle-js", Util.ANSI.Green));
		}
	}
	public static class ContribMakeEtArgs {
		public Config config;
		public static ContribMakeEtArgs from(String[] args, int start) {
			ContribMakeEtArgs returnArgs = new ContribMakeEtArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Generates the error table which provides useful insight to issues", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib make-et", Util.ANSI.Green));
		}
	}
	public static class ContribCopyrightArgs {
		public Config config;
		public static ContribCopyrightArgs from(String[] args, int start) {
			ContribCopyrightArgs returnArgs = new ContribCopyrightArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Sprinkle Jeff's name everywhere.", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama contrib copyright", Util.ANSI.Green));
		}
	}
	public static class DatabaseConfigureArgs {
		public Config config;
		public static DatabaseConfigureArgs from(String[] args, int start) {
			DatabaseConfigureArgs returnArgs = new DatabaseConfigureArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Update the configuration", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama database configure", Util.ANSI.Green));
		}
	}
	public static class DatabaseInstallArgs {
		public Config config;
		public static DatabaseInstallArgs from(String[] args, int start) {
			DatabaseInstallArgs returnArgs = new DatabaseInstallArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Install the tables on a monolithic database", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama database install", Util.ANSI.Green));
		}
	}
	public static class DatabaseMigrateArgs {
		public Config config;
		public static DatabaseMigrateArgs from(String[] args, int start) {
			DatabaseMigrateArgs returnArgs = new DatabaseMigrateArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Migrate data from 'db' to 'nextdb'", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama database migrate", Util.ANSI.Green));
		}
	}
	public static class DebugArchiveArgs {
		public Config config;
		public String space;
		public String archive;
		public static DebugArchiveArgs from(String[] args, int start) {
			DebugArchiveArgs returnArgs = new DebugArchiveArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--archive", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-a":
					case "--archive": {
						if (k+1 < args.length) {
							returnArgs.archive = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Explain the data within an archive", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama debug archive", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-a, --archive", Util.ANSI.Green) + " " + Util.prefix("<archive>", Util.ANSI.White));
		}
	}
	public static class DocumentConnectArgs {
		public Config config;
		public String space;
		public String key;
		public static DocumentConnectArgs from(String[] args, int start) {
			DocumentConnectArgs returnArgs = new DocumentConnectArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--key", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Connect to a document", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama document connect", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
		}
	}
	public static class DocumentCreateArgs {
		public Config config;
		public String space;
		public String key;
		public String arg;
		public String entropy = null;
		public static DocumentCreateArgs from(String[] args, int start) {
			DocumentCreateArgs returnArgs = new DocumentCreateArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--key", "--arg", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-aa":
					case "--arg": {
						if (k+1 < args.length) {
							returnArgs.arg = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-e":
					case "--entropy": {
						if (k+1 < args.length) {
							returnArgs.entropy = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Create a document", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama document create", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-aa, --arg", Util.ANSI.Green) + " " + Util.prefix("<arg>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-e, --entropy", Util.ANSI.Green) + " " + Util.prefix("<entropy>", Util.ANSI.White));
		}
	}
	public static class DocumentDeleteArgs {
		public Config config;
		public String space;
		public String key;
		public static DocumentDeleteArgs from(String[] args, int start) {
			DocumentDeleteArgs returnArgs = new DocumentDeleteArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--key", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Delete a document", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama document delete", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
		}
	}
	public static class DocumentListArgs {
		public Config config;
		public String space;
		public String marker = null;
		public String limit = "1000";
		public static DocumentListArgs from(String[] args, int start) {
			DocumentListArgs returnArgs = new DocumentListArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-m":
					case "--marker": {
						if (k+1 < args.length) {
							returnArgs.marker = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-l":
					case "--limit": {
						if (k+1 < args.length) {
							returnArgs.limit = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("List documents", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama document list", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-m, --marker", Util.ANSI.Green) + " " + Util.prefix("<marker>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-l, --limit", Util.ANSI.Green) + " " + Util.prefix("<limit>", Util.ANSI.White));
		}
	}
	public static class DocumentAttachArgs {
		public Config config;
		public String space;
		public String key;
		public String file;
		public String name = null;
		public String type = null;
		public static DocumentAttachArgs from(String[] args, int start) {
			DocumentAttachArgs returnArgs = new DocumentAttachArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--space", "--key", "--file", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-f":
					case "--file": {
						if (k+1 < args.length) {
							returnArgs.file = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-n":
					case "--name": {
						if (k+1 < args.length) {
							returnArgs.name = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-t":
					case "--type": {
						if (k+1 < args.length) {
							returnArgs.type = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Attach an asset to a document", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama document attach", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-f, --file", Util.ANSI.Green) + " " + Util.prefix("<file>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-n, --name", Util.ANSI.Green) + " " + Util.prefix("<name>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-t, --type", Util.ANSI.Green) + " " + Util.prefix("<type>", Util.ANSI.White));
		}
	}
	public static class DomainMapArgs {
		public Config config;
		public String domain;
		public String space;
		public String cert;
		public String key = null;
		public String auto = "true";
		public static DomainMapArgs from(String[] args, int start) {
			DomainMapArgs returnArgs = new DomainMapArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--domain", "--space", "--cert", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-d":
					case "--domain": {
						if (k+1 < args.length) {
							returnArgs.domain = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-s":
					case "--space": {
						if (k+1 < args.length) {
							returnArgs.space = args[k+1];
							k++;
							missing[1] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-c":
					case "--cert": {
						if (k+1 < args.length) {
							returnArgs.cert = args[k+1];
							k++;
							missing[2] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-k":
					case "--key": {
						if (k+1 < args.length) {
							returnArgs.key = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
					case "-a":
					case "--auto": {
						if (k+1 < args.length) {
							returnArgs.auto = args[k+1];
							k++;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Map a domain to a space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama domain map", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-d, --domain", Util.ANSI.Green) + " " + Util.prefix("<domain>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-s, --space", Util.ANSI.Green) + " " + Util.prefix("<space>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-c, --cert", Util.ANSI.Green) + " " + Util.prefix("<cert>", Util.ANSI.White));
			System.out.println(Util.prefixBold("OPTIONAL FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-k, --key", Util.ANSI.Green) + " " + Util.prefix("<key>", Util.ANSI.White));
			System.out.println("    " + Util.prefix("-a, --auto", Util.ANSI.Green) + " " + Util.prefix("<auto>", Util.ANSI.White));
		}
	}
	public static class DomainListArgs {
		public Config config;
		public static DomainListArgs from(String[] args, int start) {
			DomainListArgs returnArgs = new DomainListArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("List domains", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama domain list", Util.ANSI.Green));
		}
	}
	public static class DomainUnmapArgs {
		public Config config;
		public String domain;
		public static DomainUnmapArgs from(String[] args, int start) {
			DomainUnmapArgs returnArgs = new DomainUnmapArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--domain", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-d":
					case "--domain": {
						if (k+1 < args.length) {
							returnArgs.domain = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Unmap a domain from a space", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama domain unmap", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-d, --domain", Util.ANSI.Green) + " " + Util.prefix("<domain>", Util.ANSI.White));
		}
	}
	public static class FrontendRxhtmlArgs {
		public Config config;
		public String output;
		public static FrontendRxhtmlArgs from(String[] args, int start) {
			FrontendRxhtmlArgs returnArgs = new FrontendRxhtmlArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--output", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-o":
					case "--output": {
						if (k+1 < args.length) {
							returnArgs.output = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Compile an rxhtml template set", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama frontend rxhtml", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-o, --output", Util.ANSI.Green) + " " + Util.prefix("<output>", Util.ANSI.White));
		}
	}
	public static class FrontendEdhtmlArgs {
		public Config config;
		public static FrontendEdhtmlArgs from(String[] args, int start) {
			FrontendEdhtmlArgs returnArgs = new FrontendEdhtmlArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Compile an edhtml build instruction file", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama frontend edhtml", Util.ANSI.Green));
		}
	}
	public static class FrontendDevServerArgs {
		public Config config;
		public static FrontendDevServerArgs from(String[] args, int start) {
			FrontendDevServerArgs returnArgs = new FrontendDevServerArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Host the working directory as a webserver", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama frontend dev-server", Util.ANSI.Green));
		}
	}
	public static class ServicesAutoArgs {
		public Config config;
		public static ServicesAutoArgs from(String[] args, int start) {
			ServicesAutoArgs returnArgs = new ServicesAutoArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("The config will decide the role", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services auto", Util.ANSI.Green));
		}
	}
	public static class ServicesBackendArgs {
		public Config config;
		public static ServicesBackendArgs from(String[] args, int start) {
			ServicesBackendArgs returnArgs = new ServicesBackendArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Spin up a gRPC back-end node", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services backend", Util.ANSI.Green));
		}
	}
	public static class ServicesFrontendArgs {
		public Config config;
		public static ServicesFrontendArgs from(String[] args, int start) {
			ServicesFrontendArgs returnArgs = new ServicesFrontendArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Spin up a WebSocket front-end node", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services frontend", Util.ANSI.Green));
		}
	}
	public static class ServicesOverlordArgs {
		public Config config;
		public static ServicesOverlordArgs from(String[] args, int start) {
			ServicesOverlordArgs returnArgs = new ServicesOverlordArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Spin up the cluster overlord", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services overlord", Util.ANSI.Green));
		}
	}
	public static class ServicesSoloArgs {
		public Config config;
		public static ServicesSoloArgs from(String[] args, int start) {
			ServicesSoloArgs returnArgs = new ServicesSoloArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Spin up a solo machine", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services solo", Util.ANSI.Green));
		}
	}
	public static class ServicesDashboardsArgs {
		public Config config;
		public static ServicesDashboardsArgs from(String[] args, int start) {
			ServicesDashboardsArgs returnArgs = new ServicesDashboardsArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Produce dashboards for prometheus.", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama services dashboards", Util.ANSI.Green));
		}
	}
	public static class InitArgs {
		public Config config;
		public static InitArgs from(String[] args, int start) {
			InitArgs returnArgs = new InitArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Initializes the config with a valid token", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama init", Util.ANSI.Green));
		}
	}
	public static class StressArgs {
		public Config config;
		public String scenario;
		public static StressArgs from(String[] args, int start) {
			StressArgs returnArgs = new StressArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			String[] missing = new String[]{"--scenario", };
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
					case "-s":
					case "--scenario": {
						if (k+1 < args.length) {
							returnArgs.scenario = args[k+1];
							k++;
							missing[0] = null;
						} else {
							System.err.println("Expected value for argument '" + args[k] + "'");
							return null;
						}
						break;
					}
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			boolean invalid = false;
			for (String misArg : missing) {
				if (misArg != null) {
					System.err.println("Expected argument '" + misArg + "'");
					invalid = true;
				}
			}
			return (invalid ? null : returnArgs);
		}
		public static void help() {
			System.out.println(Util.prefix("Stress test using the canary tool", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama stress", Util.ANSI.Green)+ " " + Util.prefix("[FLAGS]", Util.ANSI.Magenta));
			System.out.println(Util.prefixBold("FLAGS:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("-s, --scenario", Util.ANSI.Green) + " " + Util.prefix("<scenario>", Util.ANSI.White));
		}
	}
	public static class DumpenvArgs {
		public Config config;
		public static DumpenvArgs from(String[] args, int start) {
			DumpenvArgs returnArgs = new DumpenvArgs();
			try {
				returnArgs.config = new Config(args);
			} catch (Exception er) {
				System.out.println("Error creating default config file.");
			}
			for (int k = start; k < args.length; k++) {
				switch(args[k]) {
						case "--help":
						case "-h":
						case "help":
							if (k == start)
								return null;
						case "--config":
							k++;
						case "--json":
						case "--no-color":
							break;
						default:
							System.err.println("Unknown argument '" + args[k] + "'");
							return null;
				}
			}
			return returnArgs;
		}
		public static void help() {
			System.out.println(Util.prefix("Dump your environment variables", Util.ANSI.Green));
			System.out.println(Util.prefixBold("USAGE:", Util.ANSI.Yellow));
			System.out.println("    " + Util.prefix("adama dumpenv", Util.ANSI.Green));
		}
	}
}