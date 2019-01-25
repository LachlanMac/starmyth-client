package com.pineconeindustries.client.desktop.character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ColorSlider extends JPanel {

	CharacterSprite sprite;

	public final int COLUMNS = 6;

	public enum sprite_part {
		eyeColor, hairColor
	}

	private boolean displaySliders = false;

	JPanel infoPanel, sliderPanel;

	JPanel redPanel, greenPanel, bluePanel;
	JLabel redLabel, greenLabel, blueLabel;
	JTextField redValue, greenValue, blueValue;
	JSlider redSlider, greenSlider, blueSlider;
	JButton showSliders, colorPreview;

	private Color color;
	private int red = 0, green = 0, blue = 0;

	private sprite_part part;

	public ColorSlider(CharacterSprite sprite, sprite_part part) {
		this.sprite = sprite;
		this.part = part;

		buildUI();

		this.add(infoPanel, BorderLayout.WEST);

		this.setPreferredSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight / COLUMNS));
		this.setMaximumSize(new Dimension(CharacterScreen.screenWidth / 2, CharacterScreen.screenHeight / COLUMNS));

	}

	public void buildUI() {

		this.setLayout(new BorderLayout());

		infoPanel = new JPanel();
		sliderPanel = new JPanel();

		sliderPanel.setLayout(new GridLayout(3, 1));
		redPanel = new JPanel();
		redLabel = new JLabel("R");
		redSlider = new JSlider(0, 255, 0);

		redSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				red = redSlider.getValue();
				redValue.setText(Integer.toString(red));
				notifySprite();
			}
		});
		redValue = new JTextField(3);
		redValue.setText("0");
		redPanel.add(redLabel);
		redPanel.add(redSlider);
		redPanel.add(redValue);

		greenPanel = new JPanel();
		greenLabel = new JLabel("G");
		greenSlider = new JSlider(0, 255, 0);

		greenSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				green = greenSlider.getValue();
				greenValue.setText(Integer.toString(green));

				notifySprite();
			}
		});
		greenValue = new JTextField(3);
		greenValue.setText("0");
		greenPanel.add(greenLabel);
		greenPanel.add(greenSlider);
		greenPanel.add(greenValue);

		bluePanel = new JPanel();
		blueLabel = new JLabel("B");
		blueSlider = new JSlider(0, 255, 0);

		blueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				blue = blueSlider.getValue();
				blueValue.setText(Integer.toString(blue));
				notifySprite();
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

		// infoPane.setLayout(new GridLayout(1, 2));
		
		infoPanel.add(new JLabel("Eye Color"));

		colorPreview = new JButton("");
		colorPreview.setBackground(new Color(0, 0, 0));
		colorPreview.setBorderPainted(false);
		colorPreview.setOpaque(true);
		// colorPreview.setBorder(new EmptyBorder(10, 10, 10, 10));

		colorPreview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (displaySliders) {

					remove(sliderPanel);
					displaySliders = false;
					validate();
					repaint();

				} else {
					displaySliders = true;
					add(sliderPanel, BorderLayout.EAST);

					validate();
					repaint();
				}

			}
		});

		infoPanel.add(colorPreview);

	}

	public void notifySprite() {

		if (part == sprite_part.eyeColor) {

			sprite.updateEyes(red, green, blue);
			colorPreview.setBackground(new Color(red, green, blue));
			colorPreview.validate();
			colorPreview.repaint();
		}

	}

	public Color getColor() {
		return color;
	}

	public void setDefaultColor(Color color) {
		this.color = color;
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
	}

	public float[] getHSB() {
		return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
	}

}
