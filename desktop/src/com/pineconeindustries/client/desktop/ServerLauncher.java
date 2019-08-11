package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.pineconeindustries.client.desktop.headless.HeadlessApplication;
import com.pineconeindustries.server.ServerApp;

public class ServerLauncher {

	
	
	public static boolean HEADLESS = true;
	public static void main(String[] args) {

	
		if(args.length >= 1) {
			if(args[0].equals("headless")) {
				 LwjglNativesLoader.load();
			     Gdx.files = new LwjglFiles();
			     HEADLESS = true;
			}
		}
		
		
		
		ServerLauncher launcher = new ServerLauncher();
		launcher.registerFonts();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 60;
		config.samples = 4;
		// config.fullscreen = true;
		new HeadlessApplication(new ServerApp(HEADLESS));

	}
	
	
	public void registerFonts() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fonts/Sansation-Bold.ttf")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
