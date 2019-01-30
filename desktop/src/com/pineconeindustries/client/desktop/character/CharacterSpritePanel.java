package com.pineconeindustries.client.desktop.character;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pineconeindustries.client.models.CharacterModel;

public class CharacterSpritePanel extends JPanel {

	Color black = new Color(0, 0, 0, 255);
	Color white = new Color(255, 255, 255, 255);

	Color eyePrimary;
	Color racePrimary;
	Color hairPrimary;
	Color eyeSecondary;
	Color raceSecondary;
	Color hairSecondary;

	BufferedImage eyesBase, eyesDisplay;

	BufferedImage base, eyes, hair, eyeDisplay, hairDisplay;

	public static boolean debug = false;

	public CharacterSpritePanel() {

		this.setRacePrimaryColor(Color.white);
		this.setRaceSecondaryColor(Color.RED);

	}

	public void buildUI() {
		try {

			base = blowUp(ImageIO.read(new File("creation/playerSheet.png")), 4);

			// base = ImageIO.read(new File("creation/playerSheet.png"));
			eyes = blowUp(ImageIO.read(new File("creation/eyeSheet.png")), 4);
			hair = blowUp(ImageIO.read(new File("creation/hairSheet.png")), 4);

			eyesBase = ImageIO.read(new File("creation/eyetest2.png"));

			hairDisplay = blowUp(ImageIO.read(new File("creation/hairSheet.png")), 4);
			eyeDisplay = blowUp(ImageIO.read(new File("creation/eyeSheet.png")), 4);
			updateEyes(100, 0, 0, 1);

		} catch (IOException e) {

			e.printStackTrace();
		}

		if (debug) {
			this.setBorder(BorderFactory.createLineBorder(Color.pink));
		}

		this.setOpaque(false);

	}

	public void updateHair(int primaryR, int primaryG, int primaryB, int colorCode) {

		if (colorCode == 1) {
			setHairPrimaryColor(new Color(primaryR, primaryG, primaryB));
			for (int y = 0; y < hair.getHeight(); y++) {
				for (int x = 0; x < hair.getWidth(); x++) {

					Color col = new Color(hair.getRGB(x, y));

					if (!col.equals(black) && !col.equals(white)) {

						int redVal = col.getRed();
						int blueVal = col.getBlue();
						int greenVal = col.getGreen();

						if (redVal == blueVal && redVal == greenVal) {

							float[] hsbVals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

							float[] customHSB = Color.RGBtoHSB(primaryR, primaryG, primaryB, null);

							// System.out.println(customHSB[0] + " = " + customHSB[1] + " = " +
							// customHSB[2]);
							// System.out.println(hsbVals[0] + " = " + hsbVals[1] + " = " + hsbVals[2]);
							float shadeMix = (hsbVals[1] + customHSB[1]) / 2;
							float mix = (hsbVals[2] + customHSB[2]) / 2;
							hairDisplay.setRGB(x, y, Color.HSBtoRGB(customHSB[0], customHSB[1], mix));
						}
					}
				}
				// hairDisplay = temp;
			}
		} else if (colorCode == 2) {
			setHairSecondaryColor(new Color(primaryR, primaryG, primaryB));

			for (int y = 0; y < hair.getHeight(); y++) {
				for (int x = 0; x < hair.getWidth(); x++) {

					Color col = new Color(hair.getRGB(x, y));

					if (!col.equals(black) && !col.equals(white)) {

						int redVal = col.getRed();
						int blueVal = col.getBlue();
						int greenVal = col.getGreen();

						if (redVal > blueVal) {

							float[] hsbVals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

							float[] customHSB = Color.RGBtoHSB(primaryR, primaryG, primaryB, null);
							float mix = ((hsbVals[2] + customHSB[2]) / 2);
							float shadeMix = (hsbVals[1] + customHSB[1]) / 2;
							hairDisplay.setRGB(x, y, Color.HSBtoRGB(customHSB[0], customHSB[1], hsbVals[2]));
						}

					}
				}
			}
			// hairDisplay = temp;
		}

		this.validate();
		this.repaint();

	}

	public void updateEyes(int primaryR, int primaryG, int primaryB, int colorCode) {

		if (colorCode == 1) {

			setEyePrimaryColor(new Color(primaryR, primaryG, primaryB));
			setEyeSecondaryColor(new Color(primaryR, primaryG, primaryB));

			for (int y = 0; y < eyes.getHeight(); y++) {
				for (int x = 0; x < eyes.getWidth(); x++) {

					Color col = new Color(eyes.getRGB(x, y));

					if (!col.equals(black) && !col.equals(white)) {

						float[] hsbVals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

						float[] customHSB = Color.RGBtoHSB(primaryR, primaryG, primaryB, null);

						eyeDisplay.setRGB(x, y, Color.HSBtoRGB(customHSB[0], customHSB[1], hsbVals[2]));

					}
				}
				
			}
		} else if (colorCode == 2) {
			setEyePrimaryColor(new Color(primaryR, primaryG, primaryB));
			setEyeSecondaryColor(new Color(primaryR, primaryG, primaryB));

			for (int y = 0; y < eyes.getHeight(); y++) {
				for (int x = 0; x < eyes.getWidth(); x++) {

					Color col = new Color(eyes.getRGB(x, y));

					if (!col.equals(black) && !col.equals(white)) {

						float[] hsbVals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

						float[] customHSB = Color.RGBtoHSB(primaryR, primaryG, primaryB, null);

						eyeDisplay.setRGB(x, y, Color.HSBtoRGB(customHSB[0], customHSB[1], hsbVals[2]));

					}
				}
			}

		}

		this.validate();
		this.repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(base.getSubimage(0, 64 * 4, 64 * 4, 64 * 4), 0, 0, null);
		g.drawImage(eyeDisplay.getSubimage(0, 64 * 4, 64 * 4, 64 * 4), 0, 0, null);
		g.drawImage(hairDisplay.getSubimage(0, 64 * 4, 64 * 4, 64 * 4), 0, 0, null);

		g.drawImage(base.getSubimage(0, 128 * 4, 64 * 4, 64 * 4), 64 * 4, 0, null);
		g.drawImage(eyeDisplay.getSubimage(0, 128 * 4, 64 * 4, 64 * 4), 64 * 4, 0, null);
		g.drawImage(hairDisplay.getSubimage(0, 128 * 4, 64 * 4, 64 * 4), 64 * 4, 0, null);

	}

	public BufferedImage blowUp(BufferedImage img, int multiplier) {

		int blownWidth = img.getWidth() * multiplier;
		int blownHeight = img.getHeight() * multiplier;

		BufferedImage blownImg = new BufferedImage(blownWidth, blownHeight, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < blownHeight; y++) {
			for (int x = 0; x < blownWidth; x++) {

				int smallX = x / multiplier;
				int smallY = y / multiplier;

				blownImg.setRGB(x, y, img.getRGB(smallX, smallY));

			}

		}

		File outputfile = new File("saved.png");
		try {
			ImageIO.write(blownImg, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return blownImg;

	}

	public void setHairPrimaryColor(Color c) {
		this.hairPrimary = c;
	}

	public void setHairSecondaryColor(Color c) {
		this.hairSecondary = c;
	}

	public void setEyePrimaryColor(Color c) {
		this.eyePrimary = c;
	}

	public void setEyeSecondaryColor(Color c) {
		this.eyeSecondary = c;
	}

	public void setRacePrimaryColor(Color c) {
		this.racePrimary = c;
	}

	public void setRaceSecondaryColor(Color c) {
		this.raceSecondary = c;
	}

	public Color getEyePrimary() {
		return eyePrimary;
	}

	public Color getRacePrimary() {
		return racePrimary;
	}

	public Color getHairPrimary() {
		return hairPrimary;
	}

	public Color getEyeSecondary() {
		return eyeSecondary;
	}

	public Color getRaceSecondary() {
		return raceSecondary;
	}

	public Color getHairSecondary() {
		return hairSecondary;
	}

}
