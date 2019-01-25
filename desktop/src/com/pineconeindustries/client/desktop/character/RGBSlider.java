package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RGBSlider extends JPanel {

	JPanel redPanel, greenPanel, bluePanel;
	JLabel redLabel, greenLabel, blueLabel, titleLabel;
	JTextField redValue, greenValue, blueValue;
	JSlider redSlider, greenSlider, blueSlider;
	JPanel infoPanel, sliderPanel, title;
	JButton colorPreview;

	SpritePart part;

	private int red, green, blue;

	public RGBSlider(int red, int green, int blue) {

		this.red = red;
		this.green = green;
		this.blue = blue;

		buildUI();

		title = new JPanel();
		// title.setLayout(new GridLayout(1, 1));
		titleLabel = new JLabel("Primary Color");
		title.add(titleLabel);
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(infoPanel, BorderLayout.WEST);
		this.add(sliderPanel, BorderLayout.EAST);
		this.add(title, BorderLayout.NORTH);

	}

	public void buildUI() {

		this.setLayout(new BorderLayout());

		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1, 2, 5, 5));
		sliderPanel = new JPanel();

		sliderPanel.setLayout(new GridLayout(3, 1));
		sliderPanel.setOpaque(false);
		redPanel = new JPanel();
		redPanel.setOpaque(false);
		redLabel = new JLabel("R");
		redLabel.setFont(CharacterCreationScreen.font);
		redSlider = new JSlider(0, 255, 0);

		redSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				red = redSlider.getValue();
				redValue.setText(Integer.toString(red));
				updateSpritePart();
			}
		});
		redValue = new JTextField(3);
		redValue.setText("0");
		redPanel.add(redLabel);
		redPanel.add(redSlider);
		redPanel.add(redValue);

		greenPanel = new JPanel();
		greenPanel.setOpaque(false);
		greenLabel = new JLabel("G");
		greenLabel.setFont(CharacterCreationScreen.font);
		greenSlider = new JSlider(0, 255, 0);

		greenSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				green = greenSlider.getValue();
				greenValue.setText(Integer.toString(green));

				updateSpritePart();
			}
		});
		greenValue = new JTextField(3);
		greenValue.setText("0");
		greenPanel.add(greenLabel);
		greenPanel.add(greenSlider);
		greenPanel.add(greenValue);

		bluePanel = new JPanel();
		bluePanel.setOpaque(false);
		blueLabel = new JLabel("B");
		blueLabel.setFont(CharacterCreationScreen.font);
		blueSlider = new JSlider(0, 255, 0);

		blueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				blue = blueSlider.getValue();
				blueValue.setText(Integer.toString(blue));
				updateSpritePart();
			}
		});
		blueValue = new JTextField(3);
		blueValue.setText("0");
		bluePanel.add(blueLabel);
		bluePanel.add(blueSlider);
		bluePanel.add(blueValue);

		sliderPanel.add(redPanel);
		sliderPanel.add(greenPanel);
		sliderPanel.add(bluePanel);

		redSlider.setValue(red);
		greenSlider.setValue(green);
		blueSlider.setValue(blue);

		// infoPanel.setLayout(new GridLayout(1, 2));

		// infoPanel.add(new JLabel("Primary Color"));

		colorPreview = new JButton("");
		colorPreview.setBackground(new Color(red, green, blue));
		colorPreview.setBorderPainted(false);
		colorPreview.setOpaque(true);

		colorPreview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		infoPanel.add(colorPreview);

	}

	public void updateSpritePart() {

		if (part == null) {

			return;
		}

		part.updateColor(new Color(red, green, blue));

		colorPreview.setBackground(new Color(red, green, blue));

	}

	public void setSpritePart(SpritePart part) {
		this.part = part;
	}

	public Color getColor() {

		return new Color(red, green, blue);

	}

	public void setColor(Color c) {

		this.red = c.getRed();
		this.green = c.getGreen();
		this.blue = c.getBlue();

	}

}
