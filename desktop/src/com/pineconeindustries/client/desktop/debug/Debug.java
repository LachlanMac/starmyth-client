package com.pineconeindustries.client.desktop.debug;

import java.util.Random;

import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;

public class Debug {

	public static boolean TEST_CLIENT_1 = false;
	public static boolean TEST_CLIENT_2 = false;
	public static int TEST_CLIENT_1_ID = 998;
	public static int TEST_CLIENT_2_ID = 998;
	
	

	public static String DEBUG_USER_1 = "1";
	public static String DEBUG_USER_2 = "2";

	public static LocalPlayerData getTestClient(int client) {

		LocalPlayerData data = new LocalPlayerData();

		switch (client) {
		
		case 1:
			data.setCharID(998);
			data.setId(1001);
			data.setModel("1");
			data.setSector(27422);
			data.setStatus("ok");
			data.setUser("testclient1");
			data.setX(0);
			data.setY(0);
			data.setLayer(1);
			break;

		case 2:
			data.setCharID(999);
			data.setId(1002);
			data.setModel("1");
			data.setSector(27422);
			data.setStatus("ok");
			data.setUser("testclient2");
			data.setX(0);
			data.setY(0);
			data.setLayer(1);
			break;

		case 3:

			data.setCharID(Global.TEST_PLAYER_ID);
			data.setId(1001);
			data.setModel("1");
			data.setSector(7780);
			data.setStatus("ok");
			data.setUser("testclient2");
			data.setX(4000);
			data.setY(4000);
			data.setLayer(1);
			break;
		default:
			Log.print("Error loading test client data");
			System.exit(0);
			break;
		}

		return data;
	}

}
