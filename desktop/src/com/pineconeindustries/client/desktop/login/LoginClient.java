package com.pineconeindustries.client.desktop.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.gson.*;
import com.pineconeindustries.client.ClientApp;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.CharacterList;
import com.pineconeindustries.client.desktop.character.CharacterScreen;
import com.pineconeindustries.client.desktop.debug.Debug;
import com.pineconeindustries.client.desktop.ecryption.LCrypto;
import com.pineconeindustries.shared.data.Global;

public class LoginClient extends JFrame {

	JPanel newsPanel, loginPanel, backgroundPanel, userPanel, passwordPanel;
	JTextField userField;
	JPasswordField passwordField;
	JLabel userLabel, passwordLabel;
	JFrame frameReference;

	public static int ACCOUNT_ID = 0;

	public static String encryptedUser, encryptedPass;

	public LoginClient() {

		this.setSize(700, 360);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		buildGUI();

		this.frameReference = this;
	}

	public void buildGUI() {

		Font font = new Font("Verdana", Font.BOLD, 16);

		backgroundPanel = new LoginPanel();
		this.setLayout(new BorderLayout());
		this.add(backgroundPanel);

		newsPanel = new JPanel();

		loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout());
		loginPanel.setOpaque(false);

		userLabel = new JLabel("Username");
		userLabel.setFont(font);
		userLabel.setForeground(Color.white);
		userField = new JTextField(20);
		userField.setFont(font);
		passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.white);
		passwordLabel.setFont(font);
		passwordField = new JPasswordField(20);
		passwordField.setFont(font);
		passwordField.setEchoChar('*');
		userPanel = new JPanel();
		userPanel.setOpaque(false);
		userPanel.add(userLabel);
		userPanel.add(userField);
		passwordPanel = new JPanel();
		passwordPanel.setOpaque(false);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);

		JButton temp = new JButton("Connect");

		temp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String user = userField.getText();

				String password = new String(passwordField.getPassword());
				StringBuffer sb = new StringBuffer();
				URL url;

				if (user.equals(Debug.DEBUG_USER_1)) {

					LocalPlayerData data = Debug.getTestClient(1);
					frameReference.dispose();
					LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
					config.vSyncEnabled = true;
					config.width = 1920;
					config.height = 1080;
					config.foregroundFPS = 60;
					config.samples = 4;
					// config.fullscreen = true;
					new LwjglApplication(new ClientApp(data), config);

				} else if (user.equals(Debug.DEBUG_USER_2)) {
					LocalPlayerData data = Debug.getTestClient(2);
					frameReference.dispose();
					LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
					config.vSyncEnabled = true;
					config.width = 1920;
					config.height = 1080;
					config.foregroundFPS = 60;
					config.samples = 4;
					// config.fullscreen = true;
					new LwjglApplication(new ClientApp(data), config);

				} else {

					try {

						encryptedUser = LCrypto.encrypt(user);
						encryptedPass = LCrypto.encrypt(password);

						url = new URL("http://" + Global.LOGIN_SERVER_IP + "/authserver/auth/" + encryptedUser + "/"
								+ encryptedPass);

						HttpURLConnection con = (HttpURLConnection) url.openConnection();

						con.setRequestMethod("GET");

						BufferedReader in = null;
						try {
							in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						} catch (FileNotFoundException e) {
							System.out.println("No data found");
						}
						String line;
						while ((line = in.readLine()) != null) {
							sb.append(line);
						}
						in.close();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println(sb.toString());

					Gson g = new Gson();

					CharacterList list = g.fromJson(sb.toString(), CharacterList.class);

					System.out.println(list.getName1());

					ArrayList<LocalPlayerData> dataList = list.getLocalPlayerData();

					if (dataList.size() != 0) {

						ACCOUNT_ID = dataList.get(0).getId();

						System.out.println(ACCOUNT_ID + "    ID???");
						new CharacterScreen(dataList);
						frameReference.dispose();
					} else {

						if (list.getAccountID() != 0) {
							ACCOUNT_ID = list.getAccountID();
							new CharacterScreen(dataList);
							frameReference.dispose();

						} else {

							System.exit(0);
						}

					}

				}
			}

		});

		loginPanel.add(userPanel, BorderLayout.NORTH);
		loginPanel.add(passwordPanel, BorderLayout.CENTER);
		loginPanel.add(temp, BorderLayout.SOUTH);

		backgroundPanel.add(loginPanel, BorderLayout.CENTER);

		this.validate();
		this.repaint();

	}

}
