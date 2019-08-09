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
import javax.swing.SwingConstants;

import com.pineconeindustries.client.desktop.character.options.Option;
import com.pineconeindustries.shared.log.Log;

public class SpritePart extends JPanel {

	JButton next, previous, primaryColorBtn, secondaryColorBtn;

	JLabel selected, option, description;

	JPanel selector, colors;

	CharacterSpritePanel main;

	RGBSlider primarySliders, secondarySliders;

	Color primaryColor, secondaryColor;

	ArrayList<Option> options;
	CharacterCreationScreen screenRef;

	Option currentOption;

	int currentIndex = 0;
	int maxIndex = 0;
	int colorCount = 0;

	String descriptor;
	CharacterOptionsPanel optionPanel;
	boolean showPrimarySliders = false, showSecondarySliders = false;

	private boolean showSliders = false;

	public SpritePart(String descriptor, CharacterOptionsPanel optionPanel, CharacterCreationScreen screenRef,
			CharacterSpritePanel main, Color primaryColor, ArrayList<Option> options) {
		this.options = options;
		this.screenRef = screenRef;
		this.main = main;
		this.optionPanel = optionPanel;

		this.descriptor = descriptor;
		currentOption = options.get(0);
		this.primaryColor = options.get(0).getPrimaryColor();
		primarySliders = new RGBSlider(this.primaryColor.getRed(), this.primaryColor.getGreen(),
				this.primaryColor.getBlue(), 1);
		maxIndex = options.size() - 1;
		primarySliders.setSpritePart(this);
		colorCount = 1;
		buildUI();

	}

	public SpritePart(String descriptor, CharacterOptionsPanel optionPanel, CharacterCreationScreen screenRef,
			CharacterSpritePanel main, Color primaryColor, Color secondaryColor, ArrayList<Option> options) {
		System.out.println("Calling secondary construvtor");
		this.options = options;
		this.screenRef = screenRef;
		this.main = main;
		this.optionPanel = optionPanel;

		this.descriptor = descriptor;

		System.out.println(this.secondaryColor);

		currentOption = options.get(0);
		this.primaryColor = options.get(0).getPrimaryColor();
		this.secondaryColor = options.get(0).getSecondaryColor();
		primarySliders = new RGBSlider(this.primaryColor.getRed(), this.primaryColor.getGreen(),
				this.primaryColor.getBlue(), 1);
		secondarySliders = new RGBSlider(this.secondaryColor.getRed(), this.secondaryColor.getGreen(),
				this.secondaryColor.getBlue(), 2);
		maxIndex = options.size() - 1;
		primarySliders.setSpritePart(this);
		secondarySliders.setSpritePart(this);
		colorCount = 2;
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
				(CharacterScreen.screenHeight / CharacterCreationScreen.COLUMNS) - 30));

		this.setLayout(new BorderLayout());

		// this.setLayout(new GridLayout(1, 2));
		ImageIcon prevIcon = resizeIcon(new ImageIcon("creation/ui/previousArrow.png"), 64, 32);
		ImageIcon nextIcon = resizeIcon(new ImageIcon("creation/ui/nextArrow.png"), 64, 32);

		JPanel nextContainer = new JPanel();
		JPanel prevContainer = new JPanel();
		JPanel textContainer = new JPanel();
		JPanel descriptionContainer = new JPanel();

		descriptionContainer.setOpaque(false);
		nextContainer.setOpaque(false);
		prevContainer.setOpaque(false);
		textContainer.setOpaque(false);

		descriptionContainer.setLayout(new GridLayout(1, 1));
		nextContainer.setLayout(new GridLayout(1, 1));
		prevContainer.setLayout(new GridLayout(1, 1));
		textContainer.setLayout(new GridLayout(1, 1));

		nextContainer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

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

		selector.setPreferredSize(new Dimension((int) (this.getPreferredSize().getWidth() * .80),
				(int) this.getPreferredSize().getHeight()));

		colors = new JPanel();

		colors.setOpaque(false);

		colors.setPreferredSize(new Dimension((int) (this.getPreferredSize().getWidth() * .20),
				(int) this.getPreferredSize().getHeight()));

		colors.setBorder(BorderFactory.createLineBorder(Color.GREEN));

		colors.setLayout(new BorderLayout());

		Dimension btnDim = new Dimension((int) (selector.getPreferredSize().getWidth() * .15),
				(int) colors.getPreferredSize().getHeight());

		nextContainer.setPreferredSize(btnDim);

		prevContainer.setPreferredSize(btnDim);

		textContainer.setPreferredSize(new Dimension((int) (selector.getPreferredSize().getWidth() * .4),
				(int) selector.getPreferredSize().getHeight()));

		descriptionContainer.setPreferredSize(btnDim);

		description = new JLabel(descriptor, SwingConstants.CENTER);
		description.setFont(CharacterCreationScreen.font);
		description.setForeground(Color.white);

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

				if (descriptor.toLowerCase().equals("eyes")) {
					screenRef.setEyePart(currentOption.getOptionID());
				} else if (descriptor.toLowerCase().equals("hair")) {
					screenRef.setHairPart(currentOption.getOptionID());
				} else {
					Log.print("ERROR");
				}

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

		descriptionContainer.add(description);

		selector.add(descriptionContainer);
		selector.add(prevContainer);

		String opt = currentOption.getOptionDisplay();

		option = new JLabel(opt, SwingConstants.CENTER);

		option.setFont(CharacterCreationScreen.font);
		option.setForeground(Color.white);

		textContainer.add(option);
		textContainer.setBorder(BorderFactory.createLineBorder(Color.blue));

		selector.add(textContainer);

		selector.add(nextContainer);

		drawColorSelectors();
		add(selector, BorderLayout.WEST);
		add(colors, BorderLayout.EAST);

	}

	public void drawColorSelectors() {

		JPanel primaryContainer = new JPanel();
		primaryContainer.setOpaque(false);
		primaryContainer.setBorder(BorderFactory.createLineBorder(Color.yellow));
		primaryContainer.setLayout(new GridLayout(1, 1));
		JPanel secondaryContainer = new JPanel();
		secondaryContainer.setOpaque(false);
		secondaryContainer.setBorder(BorderFactory.createLineBorder(Color.yellow));
		secondaryContainer.setLayout(new GridLayout(1, 1));

		if (colorCount == 1) {
			primaryColorBtn = new JButton();
			primaryColorBtn.setBackground(primaryColor);
			primaryColorBtn.setBorderPainted(false);
			primaryColorBtn.setOpaque(true);

			primaryColorBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (showPrimarySliders) {

						showPrimarySliders = false;

						optionPanel.removeRGBSliders();
						optionPanel.validate();
						optionPanel.repaint();

					} else {

						showPrimarySliders = true;
						showSecondarySliders = false;
						optionPanel.removeRGBSliders();
						optionPanel.getRGBPanel().add(primarySliders);

						optionPanel.validate();
						optionPanel.repaint();
					}

				}

			});

			primaryContainer.add(primaryColorBtn);

			colors.add(primaryContainer, BorderLayout.CENTER);
		} else if (colorCount == 2) {

			System.out.println("COLOR COUNT IS 2");

			primaryColorBtn = new JButton();
			primaryColorBtn.setBackground(primaryColor);
			primaryColorBtn.setBorderPainted(false);
			primaryColorBtn.setOpaque(true);

			primaryColorBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (showPrimarySliders) {

						showPrimarySliders = false;

						optionPanel.removeRGBSliders();
						optionPanel.validate();
						optionPanel.repaint();

					} else {

						showPrimarySliders = true;
						showSecondarySliders = false;
						optionPanel.removeRGBSliders();
						optionPanel.getRGBPanel().add(primarySliders);

						optionPanel.validate();
						optionPanel.repaint();
					}

				}

			});

			primaryContainer.add(primaryColorBtn);

			colors.add(primaryContainer, BorderLayout.NORTH);

			secondaryColorBtn = new JButton();
			secondaryColorBtn.setBackground(secondaryColor);

			secondaryColorBtn.setBorderPainted(false);
			secondaryColorBtn.setOpaque(true);

			secondaryColorBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (showSecondarySliders) {

						showSecondarySliders = false;

						optionPanel.removeRGBSliders();
						optionPanel.validate();
						optionPanel.repaint();

					} else {

						showSecondarySliders = true;
						showPrimarySliders = false;
						optionPanel.removeRGBSliders();
						optionPanel.getRGBPanel().add(secondarySliders);
						optionPanel.validate();
						optionPanel.repaint();
					}

				}

			});

			secondaryContainer.add(secondaryColorBtn);

			colors.add(secondaryContainer, BorderLayout.SOUTH);

		}

	}

	public void updateColor(Color c, int colorCode) {

		if (descriptor.toLowerCase().equals("eyes")) {

			if (colorCode == 1) {
				primaryColorBtn.setBackground(c);
				primaryColorBtn.validate();
				primaryColorBtn.repaint();

				main.updateEyes(c.getRed(), c.getGreen(), c.getBlue(), 1);

			} else if (colorCode == 2) {

				secondaryColorBtn.setBackground(c);
				secondaryColorBtn.validate();
				secondaryColorBtn.repaint();

				main.updateEyes(c.getRed(), c.getGreen(), c.getBlue(), 2);
			}

		} else if (descriptor.toLowerCase().equals("hair")) {
			if (colorCode == 1) {
				primaryColorBtn.setBackground(c);
				primaryColorBtn.validate();
				primaryColorBtn.repaint();

				main.updateHair(c.getRed(), c.getGreen(), c.getBlue(), 1);

			} else if (colorCode == 2) {

				secondaryColorBtn.setBackground(c);
				secondaryColorBtn.validate();
				secondaryColorBtn.repaint();

				main.updateHair(c.getRed(), c.getGreen(), c.getBlue(), 2);
			}
		}

	}
}
