package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputManager {

	public static int DOWN = Input.Keys.S;
	public static int UP = Input.Keys.W;
	public static int RIGHT = Input.Keys.D;
	public static int LEFT = Input.Keys.A;
	public static int SPRINT = Input.Keys.SHIFT_LEFT;
	public static int ENTER = Input.Keys.ENTER;
	public static int CENTER_CAMERA = Input.Keys.Y;

	public static boolean isPressed(int keycode) {

		if (Gdx.input.isKeyPressed(keycode)) {
			return true;
		} else {
			return false;
		}

	}

	public void checkInput() {

	}

}
