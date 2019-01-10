package com.pineconeindustries.client.desktop.login;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LoginPanel extends JPanel{

	Image image;
	
	public LoginPanel() {
		
		try {
			image = ImageIO.read(new File("textures/lachlangalaxydark.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	  protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	        g.drawImage(image, 0, 0, null);
	}
	
}
