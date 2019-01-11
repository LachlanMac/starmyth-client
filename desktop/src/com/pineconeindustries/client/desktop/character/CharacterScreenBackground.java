package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pineconeindustries.client.data.LocalPlayerData;

public class CharacterScreenBackground extends JPanel {

	private int characterCount;

	CharacterScreen screen;

	BufferedImage image;

	JPanel topPanel, characterPanel;

	JLabel title;

	JPanel bufferW, bufferE, bottomPanel;

	ArrayList<LocalPlayerData> data;

	public CharacterScreenBackground(CharacterScreen screen, ArrayList<LocalPlayerData> data) {

		characterCount = data.size();

		this.screen = screen;

		this.setLayout(new BorderLayout());

		bufferW = new JPanel();
		bufferE = new JPanel();
		bottomPanel = new JPanel();

		try {
			image = ImageIO.read(new File("textures/lachlangalaxydark.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		topPanel = new JPanel();
		title = new JLabel("Characters");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Sansation", Font.BOLD, 30));
		topPanel.add(title);

		characterPanel = new JPanel();
		characterPanel.setLayout(new GridLayout(4, 1, 10, 10));
		characterPanel.setPreferredSize(
				new Dimension((int) (CharacterScreen.screenWidth * .6f), (int) (CharacterScreen.screenHeight * .6f)));

		topPanel.setPreferredSize(
				new Dimension((int) (CharacterScreen.screenWidth - characterPanel.getPreferredSize().getWidth()) / 2,
						(int) ((CharacterScreen.screenHeight - characterPanel.getPreferredSize().getHeight()) / 2)));

		bufferE.setPreferredSize(
				new Dimension((int) (CharacterScreen.screenWidth - characterPanel.getPreferredSize().getWidth()) / 2,
						CharacterScreen.screenHeight));
		bufferW.setPreferredSize(
				new Dimension((int) (CharacterScreen.screenWidth - characterPanel.getPreferredSize().getWidth()) / 2,
						CharacterScreen.screenHeight));

		bottomPanel.setPreferredSize(
				new Dimension((int) (CharacterScreen.screenWidth - characterPanel.getPreferredSize().getWidth()) / 2,
						(int) ((CharacterScreen.screenHeight - characterPanel.getPreferredSize().getHeight()) / 2)));

		for (int i = 0; i < characterCount; i++) {

			LocalPlayerData d = data.get(i);

			characterPanel.add(new CButton(screen, d));

		}

		for (int ii = 0; ii < 4 - characterCount; ii++) {
			characterPanel.add(new CButton(screen, null));
		}

		topPanel.setOpaque(false);
		bufferW.setOpaque(false);
		bufferE.setOpaque(false);
		characterPanel.setOpaque(false);
		bottomPanel.setOpaque(false);

		this.add(bufferW, BorderLayout.WEST);
		this.add(bufferE, BorderLayout.EAST);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(characterPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}
