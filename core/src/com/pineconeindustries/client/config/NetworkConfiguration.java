package com.pineconeindustries.client.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NetworkConfiguration {

	public static String gameServer = "";
	public static String loginServer = "";
	public static String configPath = "config/host.cfg";

	public static void loadConfiguration() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(configPath)));
			String in = "";

			while ((in = br.readLine()) != null) {

				if (in.startsWith("#")) {
					// ignore
				} else {

					String[] options = in.trim().split(":");

					if (in.toLowerCase().contains("loginserver")) {
						loginServer = options[1];
					}
					if (in.toLowerCase().contains("gameserver")) {
						gameServer = options[1];
					}

				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
