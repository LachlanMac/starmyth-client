package com.pineconeindustries.client.desktop.character;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pineconeindustries.client.data.LocalPlayerData;

public class CharacterScreen extends JFrame {

	CharacterSelectionScreen home;
	
	CharacterCreationScreen creator;

	JPanel testPane;
	JLabel test;

	JPanel currentScreen;

	public static int screenWidth = 960;
	public static int screenHeight = 720;

	ArrayList<LocalPlayerData> data;

	public CharacterScreen(ArrayList<LocalPlayerData> data) {

		this.setTitle("StarMyth");
		this.setSize(screenWidth, screenHeight);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		creator = new CharacterCreationScreen(this);
		home = new CharacterSelectionScreen(this, data);

		this.add(home);
		this.validate();
		this.repaint();

		this.pack();

	}

	public void setCurrentScreen(JPanel p) {

		this.remove(home);
		this.remove(creator);
		this.add(p);
		this.validate();
		this.repaint();

	}

	public JPanel getHomeScreen() {
		return home;
	}

	public JPanel getCreatorScreen() {
		return creator;
	}
}
