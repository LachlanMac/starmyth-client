package com.pineconeindustries.client.desktop.character.options;

import java.awt.Color;
import java.util.ArrayList;

public class Options {

	public static ArrayList<Option> eyeOptions;
	public static ArrayList<Option> hairOptions;

	public static void generateOptions() {
		eyeOptions = new ArrayList<Option>();

		eyeOptions.add(new Option(0, "Rounded", Color.BLUE));
		eyeOptions.add(new Option(1, "HardStare", Color.RED));
		eyeOptions.add(new Option(2, "CuteEyes", Color.GREEN));

		hairOptions = new ArrayList<Option>();

		hairOptions.add(new Option(0, "Styled", Color.BLACK));
		hairOptions.add(new Option(1, "Long", Color.GREEN));
	}

}
