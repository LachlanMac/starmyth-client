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

}
