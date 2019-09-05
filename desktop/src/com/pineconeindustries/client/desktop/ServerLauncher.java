package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pineconeindustries.client.desktop.headless.HeadlessApplication;
import com.pineconeindustries.client.desktop.headless.HeadlessApplicationConfiguration;
import com.pineconeindustries.server.ServerApp;

public class ServerLauncher {

	public static boolean HEADLESS = false;

	public static void main(String[] args) {

		for (String arg : args) {

			if (arg.equals("headless")) {
				HEADLESS = true;
			}
		}
		HEADLESS = true;
		ServerLauncher launcher = new ServerLauncher();
		launcher.registerFonts();

		if (HEADLESS) {
			HeadlessApplicationConfiguration headlessConfig = new HeadlessApplicationConfiguration();
			headlessConfig.renderInterval = 0.04f;
			new HeadlessApplication(new ServerApp(HEADLESS), headlessConfig);
		} else {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.vSyncEnabled = true;
			config.width = 200;
			config.height = 100;

			config.foregroundFPS = 60;
			config.backgroundFPS = 60;
			config.samples = 4;
			// config.fullscreen = true;
			new LwjglApplication(new ServerApp(HEADLESS), config);

		}
	}

	public void registerFonts() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream("fonts/Sansation-Bold.ttf")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
