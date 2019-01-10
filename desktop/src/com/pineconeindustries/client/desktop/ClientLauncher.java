package com.pineconeindustries.client.desktop;

import com.pineconeindustries.client.desktop.ecryption.LCrypto;
import com.pineconeindustries.client.desktop.login.LoginClient;

public class ClientLauncher {

	public static boolean debugmode = false;

	public static void main(String[] arg) {

		new LoginClient();

	}
}
