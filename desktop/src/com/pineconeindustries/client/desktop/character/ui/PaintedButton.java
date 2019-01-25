package com.pineconeindustries.client.desktop.character.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class PaintedButton extends JButton {

	BufferedImage image;

	public PaintedButton(String path) {

		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}
