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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.pineconeindustries.client.desktop.character.ColorSlider.sprite_part;
import com.pineconeindustries.client.desktop.character.options.Options;

public class CharacterCreationScreen extends JPanel {

	private boolean showSlider = false;
	CharacterScreen screen;
	BufferedImage image;

	CharacterSprite sprite;

	JPanel eyesPanel, sliderPanel, rgbPanel;

	JButton eyesNext, eyesPrevious, hairNext, hairPrevious;

	JSlider eyesRed, eyesBlue, eyesGreen;

	ColorSlider eyeSlider, hairSlider, shirtSlider, pantsSlider;

	RGBSlider test;

	public static Font font = new Font("Verdana", Font.BOLD, 16);

	public static final int COLUMNS = 8;

	public CharacterCreationScreen(CharacterScreen screen) {

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

		sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(COLUMNS, 1, 0, 0));

		sliderPanel.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));

		createSpriteDisplayPanel();
		createEyeSelectorPanel();
		createRGBPanel();

		// debug

		sliderPanel.setBorder(BorderFactory.createLineBorder(Color.PINK));
		sprite.setBorder(BorderFactory.createLineBorder(Color.PINK));
		rgbPanel.setBorder(BorderFactory.createLineBorder(Color.PINK));

		sliderPanel.setOpaque(false);
		rgbPanel.setOpaque(false);
		sprite.setOpaque(false);

		this.add(sliderPanel, BorderLayout.EAST);
		this.add(sprite, BorderLayout.WEST);
		this.add(rgbPanel, BorderLayout.SOUTH);
	}

	public void createRGBPanel() {

		rgbPanel = new JPanel();
		rgbPanel.setPreferredSize(new Dimension(CharacterScreen.screenWidth, 200));
		test = new RGBSlider(100, 31, 200);

		SpritePart eyes = new SpritePart(this, sprite, Color.blue, Options.eyeOptions);
		SpritePart hair = new SpritePart(this, sprite, Color.DARK_GRAY, Color.YELLOW, Options.hairOptions);

		sliderPanel.add(eyes);
		sliderPanel.add(hair);

		test.setSpritePart(eyes);

		// rgbPanel.add(test);

	}

	public JPanel getRGBPanel() {
		return rgbPanel;
	}

	public void removeRGBPanel() {
		rgbPanel.removeAll();
	}

	public void createEyeSelectorPanel() {

		eyeSlider = new ColorSlider(sprite, sprite_part.eyeColor);
		hairSlider = new ColorSlider(sprite, sprite_part.eyeColor);
		shirtSlider = new ColorSlider(sprite, sprite_part.eyeColor);
		pantsSlider = new ColorSlider(sprite, sprite_part.eyeColor);

	}

	public void createSpriteDisplayPanel() {
		sprite = new CharacterSprite();
		sliderPanel.add(sprite, BorderLayout.WEST);
		sprite.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight));

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

}
