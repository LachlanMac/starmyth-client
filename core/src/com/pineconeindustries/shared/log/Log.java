package com.pineconeindustries.shared.log;

public class Log {

	public static boolean MONITOR_NET_TRAFFIC = false;
	public static boolean DEBUG_MODE = true;

	public static void print(String out) {
		System.out.println("Console: [" + out + "]");
	}

	public static void debug(String out) {
		if (DEBUG_MODE)
			System.out.println("Debug : [" + out + "]");
	}

	public static void netTraffic(String out, String type) {
		if (MONITOR_NET_TRAFFIC)
			System.out.println("NET(" + type + ") ==== [" + out + "]");

	}

	public static void serverLog(String out) {
		if (DEBUG_MODE) {
			System.out.println("Server : [" + out + "]");
		}
	}

}
