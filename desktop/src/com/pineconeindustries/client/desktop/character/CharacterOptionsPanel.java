package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.pineconeindustries.client.desktop.character.options.Options;

public class CharacterOptionsPanel extends JPanel {

	CharacterCreationScreen screen;

	public static boolean debug = false;

	JPanel optionsPanel, rgbSliderPanel;

	public CharacterOptionsPanel(CharacterCreationScreen screen) {
		this.screen = screen;
	}

	public void buildUI() {
		this.setLayout(new BorderLayout());

		this.setOpaque(false);
		optionsPanel = new JPanel();
		optionsPanel.setOpaque(false);
		optionsPanel.setLayout(new GridLayout(8, 1, 0, 0));

		SpritePart eyes = new SpritePart("Eyes", this, screen, screen.getSpritePanel(), Color.BLACK,
				Options.eyeOptions);
		// eyes.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
		optionsPanel.add(eyes);

		SpritePart hair = new SpritePart("Hair", this, screen, screen.getSpritePanel(), Color.BLACK, Color.GREEN,
				Options.hairOptions);

		optionsPanel.add(hair);

		rgbSliderPanel = new JPanel();

		rgbSliderPanel.setOpaque(false);

		

		optionsPanel.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .75f)));
		rgbSliderPanel.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .25f)));

		this.add(optionsPanel, BorderLayout.NORTH);
		this.add(rgbSliderPanel, BorderLayout.SOUTH);

		if (debug) {
			optionsPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
			rgbSliderPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
			this.setBorder(BorderFactory.createLineBorder(Color.pink));
		}
		
	}

	public void removeRGBSliders() {
		rgbSliderPanel.removeAll();
	}

	public JPanel getRGBPanel() {
		return rgbSliderPanel;
	}

}
