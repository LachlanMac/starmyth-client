package com.pineconeindustries.client.desktop.character;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CharacterSprite extends JPanel {

	Color black = new Color(0, 0, 0, 255);
	Color white = new Color(255, 255, 255, 255);

	BufferedImage eyesBase, eyesDisplay;

	public CharacterSprite() {

		this.setPreferredSize(new Dimension(256, 256));

		try {
			eyesBase = ImageIO.read(new File("creation/eyetest2.png"));
			updateEyes(100, 0, 0);

		} catch (IOException e) {

			e.printStackTrace();
		}
	
	}
	
	public void updateEyes(int primaryR, int primaryG, int primaryB) {

		// reset image
		BufferedImage temp = eyesBase;

		for (int y = 0; y < temp.getHeight(); y++) {
			for (int x = 0; x < temp.getWidth(); x++) {

				Color col = new Color(temp.getRGB(x, y));

				if (!col.equals(black) && !col.equals(white)) {

					float[] hsbVals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

					float[] customHSB = Color.RGBtoHSB(primaryR, primaryG, primaryB, null);

					temp.setRGB(x, y, Color.HSBtoRGB(customHSB[0], customHSB[1], hsbVals[2]));

				}
			}
		}

		eyesDisplay = temp;
		this.validate();
		this.repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(eyesDisplay, 0, 0, null);
	}

}
