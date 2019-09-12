package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.omg.CORBA.portable.InputStream;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pineconeindustries.client.ClientApp;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.character.CharacterScreen;
import com.pineconeindustries.client.desktop.debug.Debug;
import com.pineconeindustries.client.desktop.login.LoginClient;
import com.pineconeindustries.server.ServerApp;

public class ClientLauncher {

	public static boolean debugmode = false;

	public static boolean useLocalServer = true;

	public static void main(String[] arg) {

		// new LoginClient();

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