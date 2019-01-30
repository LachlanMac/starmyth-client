package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.pineconeindustries.client.Client;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.CharacterList;
import com.pineconeindustries.client.desktop.character.options.Options;
import com.pineconeindustries.client.desktop.ecryption.LCrypto;
import com.pineconeindustries.client.desktop.login.LoginClient;
import com.pineconeindustries.client.models.CharacterModel;
import com.pineconeindustries.client.utils.HexConversions;

public class CharacterCreationScreen extends JPanel {

	private boolean showSlider = false;
	CharacterScreen screen;
	BufferedImage image;

	CharacterSpritePanel sprite;
	CharacterOptionsPanel options;

	JPanel eyesPanel, sliderPanel, rgbPanel, leftPanel, rightPanel, namePanel;

	JButton eyesNext, eyesPrevious, hairNext, hairPrevious;
	JTextField characterName;
	JSlider eyesRed, eyesBlue, eyesGreen;

	RGBSlider test;

	int eyePart, hairPart, racePart;

	public static Font font = new Font("Verdana", Font.BOLD, 16);

	public static final int COLUMNS = 8;

	public CharacterCreationScreen(CharacterScreen screen) {

		this.racePart = 1;
		this.eyePart = 0;
		this.hairPart = 0;

		CharacterSpritePanel.debug = true;
		CharacterOptionsPanel.debug = true;

		Options.generateOptions();

		this.screen = screen;

		try {
			image = ImageIO.read(new File("textures/lachlangalaxydark.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setPreferredSize(new Dimension(CharacterScreen.screenWidth, CharacterScreen.screenHeight));

		this.setLayout(new BorderLayout());

		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));
		rightPanel.setOpaque(false);

		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setOpaque(false);

		createSpriteDisplayPanel();
		createCharacterOptionsPanel();

		leftPanel.add(sprite, BorderLayout.NORTH);

		namePanel = new JPanel();
		namePanel.setOpaque(false);
		namePanel.setPreferredSize(
				new Dimension(CharacterScreen.screenWidth / 2, (int) (CharacterScreen.screenHeight * .15f)));

		namePanel.add(new JLabel("Name"));

		characterName = new JTextField("Enter Name");
		namePanel.add(characterName);
		JButton submit = new JButton("Submit");
		StringBuilder response = new StringBuilder();
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toString(racePart) + "x" + HexConversions.hexFromColor(sprite.getRacePrimary()) + "x"
						+ HexConversions.hexFromColor(sprite.getRaceSecondary()) + "-");
				sb.append(Integer.toString(hairPart) + "x" + HexConversions.hexFromColor(sprite.getHairPrimary()) + "x"
						+ HexConversions.hexFromColor(sprite.getHairSecondary()) + "-");
				sb.append(Integer.toString(eyePart) + "x" + HexConversions.hexFromColor(sprite.getEyePrimary()) + "x"
						+ HexConversions.hexFromColor(sprite.getEyeSecondary()));

				try {
					URL url = new URL("http://" + Client.LOGIN_SERVER_IP + "/create-character/" + LoginClient.ACCOUNT_ID
							+ "/" + characterName.getText() + "/" + sb.toString());

					HttpURLConnection con = (HttpURLConnection) url.openConnection();

					con.setRequestMethod("GET");

					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					} catch (FileNotFoundException fnf) {
						System.out.println("No data found");
					}
					String line;
					while ((line = in.readLine()) != null) {
						response.append(line);
					}
					in.close();
				} catch (MalformedURLException murl) {
					murl.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				StringBuilder response2 = new StringBuilder();

				try {

					URL url = new URL("http://" + Client.LOGIN_SERVER_IP + "/authserver/auth/"
							+ LoginClient.encryptedUser + "/" + LoginClient.encryptedPass);

					HttpURLConnection con = (HttpURLConnection) url.openConnection();

					con.setRequestMethod("GET");

					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					} catch (FileNotFoundException fnf) {
						System.out.println("No data found");
					}
					String line;
					while ((line = in.readLine()) != null) {
						response2.append(line);
					}
					in.close();
				} catch (MalformedURLException murl) {
					murl.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				System.out.println(response2.toString());

				Gson g = new Gson();

				CharacterList list = g.fromJson(response2.toString(), CharacterList.class);

				System.out.println(list.getName1());

				ArrayList<LocalPlayerData> dataList = list.getLocalPlayerData();

				new CharacterScreen(dataList);
				screen.dispose();

			}

		});

		namePanel.add(submit);

		leftPanel.add(namePanel, BorderLayout.SOUTH);

		rightPanel.add(options);

		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);

	}

	public JPanel getRGBPanel() {
		return rgbPanel;
	}

	public void removeRGBPanel() {
		rgbPanel.removeAll();
	}

	public void createSpriteDisplayPanel() {
		sprite = new CharacterSpritePanel();
		sprite.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));
		sprite.buildUI();

	}

	public void createCharacterOptionsPanel() {

		options = new CharacterOptionsPanel(this);
		options.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));
		options.buildUI();
	}

	public void setShowSlider(boolean showSlider) {
		this.showSlider = showSlider;
	}

	public boolean getShowSlider() {
		return showSlider;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public CharacterSpritePanel getSpritePanel() {
		return sprite;
	}

	public void setEyePart(int id) {
		this.eyePart = id;
	}

	public void setRacePart(int id) {
		this.racePart = id;
	}

	public void setHairPart(int id) {
		this.hairPart = id;
	}

}
