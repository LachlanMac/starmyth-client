package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.pineconeindustries.client.desktop.character.options.Option;

public class SpritePart extends JPanel {

	JButton next, previous, primaryColorBtn, secondaryColorBtn;

	JLabel selected, option;

	JPanel selector, colors;

	CharacterSprite main;

	RGBSlider primarySliders, secondarySliders;

	Color primaryColor, secondaryColor;

	ArrayList<Option> options;
	CharacterCreationScreen screenRef;

	Option currentOption;

	int currentIndex = 0;
	int maxIndex = 0;

	boolean showPrimarySliders = false, showSecondarySliders = false;

	private boolean showSliders = false;

	public SpritePart(CharacterCreationScreen screenRef, CharacterSprite main, Color primaryColor,
			ArrayList<Option> options) {
		this.options = options;
		this.screenRef = screenRef;
		this.main = main;
		this.primaryColor = primaryColor;
		currentOption = options.get(0);
		this.primaryColor = options.get(0).getPrimaryColor();
		primarySliders = new RGBSlider(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue());
		maxIndex = options.size() - 1;
		primarySliders.setSpritePart(this);
		buildUI();

	}

	public SpritePart(CharacterCreationScreen screenRef, CharacterSprite main, Color primaryColor, Color secondaryColor,
			ArrayList<Option> options) {
		this.options = options;
		this.screenRef = screenRef;
		this.main = main;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;

		currentOption = options.get(0);
		this.primaryColor = options.get(0).getPrimaryColor();
		this.secondaryColor = options.get(0).getSecondaryColor();
		primarySliders = new RGBSlider(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue());
		secondarySliders = new RGBSlider(secondaryColor.getRed(), secondaryColor.getGreen(), secondaryColor.getBlue());
		maxIndex = options.size() - 1;
		primarySliders.setSpritePart(this);
		secondarySliders.setSpritePart(this);
		buildUI();

	}

	public void clearRGBPanel() {

		screenRef.removeRGBPanel();
	}

	private static ImageIcon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	public void buildUI() {
		this.setOpaque(false);

		this.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2,
				CharacterScreen.screenHeight / CharacterCreationScreen.COLUMNS));

		this.setLayout(new BorderLayout());

		// this.setLayout(new GridLayout(1, 2));
		ImageIcon prevIcon = resizeIcon(new ImageIcon("creation/ui/previousArrow.png"), 64, 32);
		ImageIcon nextIcon = resizeIcon(new ImageIcon("creation/ui/nextArrow.png"), 64, 32);

		JPanel nextContainer = new JPanel();
		JPanel prevContainer = new JPanel();
		JPanel textContainer = new JPanel();

		nextContainer.setOpaque(false);
		prevContainer.setOpaque(false);
		textContainer.setOpaque(false);

		nextContainer.setLayout(new GridLayout(1, 1));
		prevContainer.setLayout(new GridLayout(1, 1));
		textContainer.setLayout(new GridLayout(1, 1));

		// DEBUG
		/*
		 * nextContainer.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		 * prevContainer.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		 * textContainer.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		 * this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		 * 
		 */

		selector = new JPanel();
		selector.setOpaque(false);

		selector.setPreferredSize(new Dimension((int) (this.getPreferredSize().getWidth() * .70),
				(int) this.getPreferredSize().getHeight()));

		colors = new JPanel();

		colors.setOpaque(false);

		colors.setPreferredSize(new Dimension((int) (this.getPreferredSize().getWidth() * .30),
				(int) this.getPreferredSize().getHeight()));

		Dimension btnDim = new Dimension((int) (selector.getPreferredSize().getWidth() * .2),
				(int) colors.getPreferredSize().getHeight());

		nextContainer.setPreferredSize(btnDim);
		prevContainer.setPreferredSize(btnDim);

		textContainer.setPreferredSize(new Dimension((int) (selector.getPreferredSize().getWidth() * .4),
				(int) selector.getPreferredSize().getHeight()));

		next = new JButton(nextIcon);
		previous = new JButton(prevIcon);

		previous.setOpaque(false);
		previous.setBorderPainted(false);
		next.setOpaque(false);
		next.setBorderPainted(false);

		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				currentIndex++;

				if (currentIndex > maxIndex) {
					currentIndex = 0;
				}

				currentOption = options.get(currentIndex);
				option.setText(currentOption.getOptionDisplay());

			}

		});

		previous.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentIndex--;

				if (currentIndex < 0) {
					currentIndex = maxIndex;
				}

				currentOption = options.get(currentIndex);
				option.setText(currentOption.getOptionDisplay());

			}

		});

		nextContainer.add(next);
		prevContainer.add(previous);

		selector.add(prevContainer);

		String opt = currentOption.getOptionDisplay();

		option = new JLabel(opt);

		option.setFont(CharacterCreationScreen.font);
		option.setForeground(Color.white);

		textContainer.add(option);

		selector.add(textContainer);

		selector.add(nextContainer);

		primaryColorBtn = new JButton();
		primaryColorBtn.setBackground(primaryColor);
		primaryColorBtn.setBorderPainted(false);
		primaryColorBtn.setOpaque(true);

		primaryColorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (showPrimarySliders) {

					showPrimarySliders = false;

					screenRef.removeRGBPanel();
					screenRef.validate();
					screenRef.repaint();

				} else {

					showPrimarySliders = true;
					showSecondarySliders = false;
					screenRef.removeRGBPanel();
					screenRef.getRGBPanel().add(primarySliders);

					screenRef.validate();
					screenRef.repaint();
				}

			}

		});

		colors.add(primaryColorBtn);

		if (secondaryColor != null) {
			secondaryColorBtn = new JButton();
			secondaryColorBtn.setBackground(secondaryColor);
			secondaryColorBtn.setBorderPainted(false);
			secondaryColorBtn.setOpaque(true);

			colors.add(secondaryColorBtn);

			secondaryColorBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (showSecondarySliders) {

						showSecondarySliders = false;

						screenRef.removeRGBPanel();
						screenRef.validate();
						screenRef.repaint();

					} else {

						showSecondarySliders = true;
						showPrimarySliders = false;
						screenRef.removeRGBPanel();
						screenRef.getRGBPanel().add(secondarySliders);
						screenRef.validate();
						screenRef.repaint();
					}

				}

			});
		}

		add(selector, BorderLayout.WEST);
		add(colors, BorderLayout.EAST);

	}

	public void updateColor(Color c) {
		primaryColorBtn.setBackground(c);
		primaryColorBtn.validate();
		primaryColorBtn.repaint();

		main.updateEyes(c.getRed(), c.getGreen(), c.getBlue());

	}
}
