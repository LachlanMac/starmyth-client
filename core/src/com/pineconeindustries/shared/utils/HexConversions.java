package com.pineconeindustries.shared.utils;

import java.awt.Color;

public class HexConversions {

	public static Color colorFromHex(String hex) {

		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);

		return new Color(r, g, b);
	}

	public static String hexFromColor(Color c) {

		String r = Integer.toHexString(c.getRed());

		if (r.length() == 1) {
			r = "0" + r;
		}
		String g = Integer.toHexString(c.getGreen());

		if (g.length() == 1) {
			g = "0" + g;
		}
		String b = Integer.toHexString(c.getBlue());

		if (b.length() == 1) {
			b = "0" + b;
		}

		return r + g + b;

	}

}
