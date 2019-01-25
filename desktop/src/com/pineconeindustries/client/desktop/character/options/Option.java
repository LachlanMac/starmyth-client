package com.pineconeindustries.client.desktop.character.options;

import java.awt.Color;

public class Option {

	private int optionID;
	private String optionDisplay;
	private Color primaryColor, secondaryColor;

	private int colors = 0;

	public Option(int optionID, String optionDisplay, Color primaryColor, Color secondaryColor) {
		this.optionID = optionID;
		this.optionDisplay = optionDisplay;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		colors = 2;

	}

	public Option(int optionID, String optionDisplay, Color primaryColor) {
		this.optionID = optionID;
		this.optionDisplay = optionDisplay;
		this.primaryColor = primaryColor;
		colors = 1;
	}

	public int getOptionID() {
		return optionID;
	}

	public void setOptionID(int optionID) {
		this.optionID = optionID;
	}

	public String getOptionDisplay() {
		return optionDisplay;
	}

	public void setOptionDisplay(String optionDisplay) {
		this.optionDisplay = optionDisplay;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public int getColors() {
		return colors;
	}

	public void setColors(int colors) {
		this.colors = colors;
	}

}
