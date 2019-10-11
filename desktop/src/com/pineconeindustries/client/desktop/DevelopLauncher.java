package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pineconeindustries.client.ClientApp;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.debug.Debug;
import com.pineconeindustries.client.desktop.headless.HeadlessApplication;
import com.pineconeindustries.client.desktop.headless.HeadlessApplicationConfiguration;
import com.pineconeindustries.server.ServerApp;
import com.pineconeindustries.shared.data.Global;

public class DevelopLauncher implements Runnable {

	public DevelopLauncher() {

	}

	public static boolean HEADLESS = true;
	public static final float TICK_RATE = 30f;

	public static void main(String args[]) {
		Global.GAME_SERVER_IP = "192.168.0.1";
		ServerLauncher launcher = new ServerLauncher();
		launcher.registerFonts();

		if (HEADLESS) {
			HeadlessApplicationConfiguration headlessConfig = new HeadlessApplicationConfiguration();
			headlessConfig.renderInterval = 1f / TICK_RATE;
			System.out.println("INTERVAL = " + headlessConfig.renderInterval);
			new HeadlessApplication(new ServerApp(HEADLESS), headlessConfig);
		} else {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.vSyncEnabled = true;
			config.width = 1900;
			config.height = 1200;
			config.foregroundFPS = (int) TICK_RATE;
			config.backgroundFPS = (int) TICK_RATE;
			config.samples = 4;
			// config.fullscreen = true;

			DevelopLauncher d = new DevelopLauncher();
			Thread t = new Thread(d);
			t.start();
			new LwjglApplication(new ServerApp(HEADLESS), config);

		}
	}

	@Override
	public void run() {
		System.out.println("Running Client");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startTestClient();
	}

	public static void startTestClient() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Sansation-Bold.ttf")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.samples = 4;
		// config.fullscreen = true;

		LocalPlayerData data = Debug.getTestClient(3);

		new LwjglApplication(new ClientApp(data), config);
	}
}
