package com.pineconeindustries.client.desktop;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.omg.CORBA.portable.InputStream;

import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.character.CharacterScreen;
import com.pineconeindustries.client.desktop.login.LoginClient;

public class ClientLauncher {

	public static boolean debugmode = false;

	public static boolean useLocalServer = true;

	public static void main(String[] arg) {

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Sansation-Bold.ttf")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//new LoginClient();
		ArrayList<LocalPlayerData> dataList = new ArrayList<LocalPlayerData>();
		new CharacterScreen(dataList);

	}
}