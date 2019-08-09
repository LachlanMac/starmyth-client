package com.pineconeindustries.client.desktop.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pineconeindustries.shared.log.Log;

public class ClientSettings {

	public static int width = 1200;
	public static int height = 800;
	public static boolean fullScreen = false, vSync = true;
	public static int fps = 60;
	public static int MSAA = 4;

	public static LwjglApplicationConfiguration loadSettings() {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(new File("config/settings.cfg")));

			String line;

			while ((line = br.readLine()) != null) {

				String[] option = line.split("=");

				if (option[0].trim().equals("window_width")) {
					width = Integer.parseInt(option[1].trim());
				} else if (option[0].trim().equals("window_height")) {
					height = Integer.parseInt(option[1].trim());
				} else if (option[0].trim().equals("window_full_screen")) {
					fullScreen = option[1].equals("true");
				} else if (option[0].trim().equals("fps")) {
					fps = Integer.parseInt(option[1].trim());
				} else if (option[0].trim().equals("sSync")) {
					vSync = option[1].equals("true");
				} else if (option[0].trim().equals("MSAA_Samples")) {
					MSAA = Integer.parseInt(option[1].trim());
				}

			}

			br.close();
		} catch (FileNotFoundException e) {
			Log.print("Error Loading Configuration :  Cannot find configuration file");
		} catch (IOException e) {
			Log.print("Error Loading Configuration :  Cannot read configuration file");
		}

		config.width = width;
		config.height = height;
		config.samples = MSAA;
		config.fullscreen = fullScreen;
		config.foregroundFPS = fps;
		config.vSyncEnabled = vSync;

		return config;

	}

}
