package com.pineconeindustries.shared.utils;

import java.text.DecimalFormat;

public class Formatter {

	private static Formatter instance = null;

	private Formatter() {

	}

	public static Formatter getInstance() {
		if (instance == null)
			instance = new Formatter();

		return instance;
	}

	public static String format(float f) {
		return String.format("%.2f", f);

	}

}
