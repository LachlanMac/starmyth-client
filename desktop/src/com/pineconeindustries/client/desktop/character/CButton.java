package com.pineconeindustries.client.desktop.character;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.pineconeindustries.client.ClientApp;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.desktop.login.ClientSettings;

public class CButton extends JButton {

	Font f;
	String text;
	CharacterScreen screen;

	BufferedImage staticImage, hoverImage;

	JLabel staticLabel;
	JLabel hoverLabel;

	LocalPlayerData data;

	private String btnText = "Create new Character";

	public CButton(CharacterScreen screen, LocalPlayerData data) {
		super();

		this.data = data;

		f = new Font("Sansation", Font.BOLD, 26);
		try {

			staticImage = ImageIO.read(new File("ui/Button.png"));
			hoverImage = ImageIO.read(new File("ui/ButtonHover.png"));

			this.setFocusPainted(false);

			staticLabel = new JLabel(new ImageIcon(staticImage));
			staticLabel.setOpaque(false);
			hoverLabel = new JLabel(new ImageIcon(hoverImage));

			staticLabel.setFont(f);
			hoverLabel.setFont(f);

			staticLabel.setForeground(Color.WHITE);
			hoverLabel.setForeground(Color.PINK);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (data != null) {
			setText(data.getName());
		} else {
			setText(btnText);

		}

		this.screen = screen;

		this.setFont(f);

		this.setContentAreaFilled(false);

		this.setOpaque(false);

		// this.setBorder(BorderFactory.createEmptyBorder());

		this.add(staticLabel);

		this.validate();
		this.repaint();

		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (data != null) {

					new LwjglApplication(new ClientApp(data), ClientSettings.loadSettings());

				} else {

					screen.setCurrentScreen(screen.getCreatorScreen());

				}
			}
		});

		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				remove(staticLabel);
				add(hoverLabel);
				validate();
				repaint();
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				remove(hoverLabel);
				add(staticLabel);
				validate();
				repaint();
			}
		});

	}

	public void setText(String txt) {
		staticLabel.setText(txt);
		hoverLabel.setText(txt);
		staticLabel.setHorizontalTextPosition(JButton.CENTER);
		staticLabel.setVerticalTextPosition(JButton.CENTER);
		hoverLabel.setHorizontalTextPosition(JButton.CENTER);
		hoverLabel.setVerticalTextPosition(JButton.CENTER);
	}

}
