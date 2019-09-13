package com.pineconeindustries.shared.data;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Global {

	public static final int TEST_PLAYER_ID = 2;
	public static final String localHost = "127.0.0.1";
	public static final String remoteHost = "73.230.126.75";

	public static Globals global = JsePlatform.standardGlobals();

	public static String LOGIN_SERVER_IP = remoteHost;
	public static String GAME_SERVER_IP = remoteHost;

	public static RUN_TYPE runType;

	public static enum RUN_TYPE {
		server, client, headless_server
	}

	// Globals globals = JsePlatform.standardGlobals();
	public static Globals getGlobals() {
		return global;
	}

	public static void setRunType(RUN_TYPE type) {
		runType = type;
	}

	public static boolean isServer() {
		if (runType == RUN_TYPE.server || runType == RUN_TYPE.headless_server) {
			return true;
		}
		return false;
	}

	public static boolean isHeadlessServer() {

		if (runType == RUN_TYPE.headless_server) {
			return true;
		}
		return false;
	}

	public static boolean isClient() {
		return !isServer();
	}

	public static boolean useDatabase = true;

}
