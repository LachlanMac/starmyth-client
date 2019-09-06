package com.pineconeindustries.shared.utils;

import com.badlogic.gdx.math.Vector2;

public class VectorMath {

	public static Vector2 getDirectionByInput(boolean[] input) {

		float x = 0, y = 0;

		if (input[0])
			y += 1;
		if (input[1])
			y -= 1;
		if (input[2])
			x -= 1;
		if (input[3])
			x += 1;

		return new Vector2(x, y).nor();

	}

	public static String getPacketDirection(float x, float y) {

		String dir = "";

		if (y > 0) {
			dir += "n";
		}
		if (y < 0) {
			dir += "s";
		}
		if (x > 0) {
			dir += "w";
		}
		if (x < 0) {
			dir += "e";
		}

		return dir;

	}

	public static Vector2 getDirectionByString(String dir) {

		float x = 0, y = 0;

		switch (dir) {
		case "n":
			y = 1;
			break;
		case "e":
			x = -1;
			break;
		case "w":
			x = 1;
			break;
		case "s":
			y = -1;
			break;
		case "ne":
			y = 1;
			x = -1;
			break;
		case "nw":
			y = 1;
			x = 1;
			break;
		case "se":
			y = -1;
			x = -1;
			break;
		case "sw":
			y = -1;
			x = 1;
			break;

		}

		return new Vector2(x, y).nor();

	}

}
