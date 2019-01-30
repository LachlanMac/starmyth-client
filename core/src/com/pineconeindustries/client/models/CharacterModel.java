package com.pineconeindustries.client.models;

import java.awt.Color;

public class CharacterModel {

	private String data;

	public CharacterModel(String data) {
		this.data = data;

	}

	// expecting this: "ffxffffffxffffff-fffxffffffxffffffff-fffxffffffxffffffff"
	public void parseData() {
		
		String[] options = data.split("-");

		String[] raceOptions = options[0].split("x");
		String[] hairOptions = options[1].split("x");
		String[] eyeOptions = options[2].split("x");

		int raceID = Integer.parseInt(raceOptions[0], 16);

		int racePrimaryR = Integer.parseInt(raceOptions[1].substring(0, 2), 16);
		int racePrimaryG = Integer.parseInt(raceOptions[1].substring(2, 4), 16);
		int racePrimaryB = Integer.parseInt(raceOptions[1].substring(4, 6), 16);

		int raceSecondaryR = Integer.parseInt(raceOptions[2].substring(0, 2), 16);
		int raceSecondaryG = Integer.parseInt(raceOptions[2].substring(2, 4), 16);
		int raceSecondaryB = Integer.parseInt(raceOptions[2].substring(4, 6), 16);

		Color racePrimary = new Color(racePrimaryR, racePrimaryG, racePrimaryB);
		Color raceSeceondary = new Color(raceSecondaryR, raceSecondaryG, raceSecondaryB);

		int hairID = Integer.parseInt(hairOptions[0], 16);

		int hairPrimaryR = Integer.parseInt(hairOptions[1].substring(0, 2), 16);
		int hairPrimaryG = Integer.parseInt(hairOptions[1].substring(2, 4), 16);
		int hairPrimaryB = Integer.parseInt(hairOptions[1].substring(4, 6), 16);

		int hairSecondaryR = Integer.parseInt(hairOptions[2].substring(0, 2), 16);
		int hairSecondaryG = Integer.parseInt(hairOptions[2].substring(2, 4), 16);
		int hairSecondaryB = Integer.parseInt(hairOptions[2].substring(4, 6), 16);

		Color hairPrimary = new Color(hairPrimaryR, hairPrimaryG, hairPrimaryB);
		Color hairSecondary = new Color(hairSecondaryR, hairSecondaryG, hairSecondaryB);
		
		
		
		int eyesID = Integer.parseInt(eyeOptions[0], 16);

		int eyePrimaryR = Integer.parseInt(eyeOptions[1].substring(0, 2), 16);
		int eyePrimaryG = Integer.parseInt(eyeOptions[1].substring(2, 4), 16);
		int eyePrimaryB = Integer.parseInt(eyeOptions[1].substring(4, 6), 16);

		int eyeSecondaryR = Integer.parseInt(eyeOptions[2].substring(0, 2), 16);
		int eyeSecondaryG = Integer.parseInt(eyeOptions[2].substring(2, 4), 16);
		int eyeSecondaryB = Integer.parseInt(eyeOptions[2].substring(4, 6), 16);

		Color eyesPrimary = new Color(eyePrimaryR, eyePrimaryG, eyePrimaryB);
		Color eyesSecondary = new Color(eyeSecondaryR, eyeSecondaryG, eyeSecondaryB);
		

	}

}
