package com.pineconeindustries.shared.utils;

import java.util.Random;

public class PseudoRandom {

	public static Random rn = new Random(System.currentTimeMillis());

	public static int getInt(int min, int max) {
		int val = rn.nextInt(max - min) + min;
		return val;
	}

	public static float getFloat(float min, float max) {

		float val = min + (rn.nextFloat() * (max - min));
		return val;
	}

}
