package com.pineconeindustries.shared.log;

public class Log {

	public static boolean MONITOR_NET_TRAFFIC = false;
	public static boolean DEBUG_MODE = true;
	public static boolean DATABASE_LOG = true;
	public static boolean CONNECTION_STATUS = true;

	public static void print(String out) {
		System.out.println("Console: [" + out + "]");
	}

	public static void debug(String out) {
		if (DEBUG_MODE)
			System.out.println("Debug : [" + out + "]");
	}

	public static void netTraffic(String out, String type) {
		if (MONITOR_NET_TRAFFIC)
			System.out.println("Net Traffic : (" + type + ") ==== [" + out + "]");

	}

	public static void connection(String out) {
		if (CONNECTION_STATUS) {
			System.out.println("Connetion : [" + out + "]");
		}
	}

	public static void serverLog(String out) {
		if (DEBUG_MODE) {
			System.out.println("Server : [" + out + "]");
		}
	}

	public static void dbLog(String out) {
		if (DATABASE_LOG) {
			System.out.println("Database : [" + out + "]");
		}
	}

}
