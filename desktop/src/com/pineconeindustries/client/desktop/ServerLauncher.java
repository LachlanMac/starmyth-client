package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pineconeindustries.server.Server;

public class ServerLauncher {

	public static void main(String[] arg) {

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
		config.samples = 4;
		// config.fullscreen = true;
		new LwjglApplication(new Server(), config);

	}
}
