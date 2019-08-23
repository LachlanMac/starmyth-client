package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputState {

	public static int[] input = new int[10];
	public static int DOWN = Input.Keys.S;
	public static int UP = Input.Keys.W;
	public static int RIGHT = Input.Keys.D;
	public static int LEFT = Input.Keys.A;
	public static int SPRINT = Input.Keys.SHIFT_LEFT;
	public static int ENTER = Input.Keys.ENTER;
	public static int TEST_BUTTON = Input.Keys.U;
	public static int CENTER_CAMERA = Input.Keys.Y;
	public static int MOUSE_DOWN = Input.Buttons.LEFT;
	public static int PLAYER_BUTTON_1 = Input.Keys.NUM_1;
	public static int PLAYER_BUTTON_2 = Input.Keys.NUM_2;
	public static int PLAYER_BUTTON_3 = Input.Keys.NUM_3;
	public static int PLAYER_BUTTON_4 = Input.Keys.NUM_4;
	public static int PLAYER_BUTTON_5 = Input.Keys.NUM_5;

	public static boolean isPressed(int keycode) {

		if (Gdx.input.isKeyPressed(keycode)) {
			return true;
		} else {
			return false;
		}

	}

	public static int isPressedState(int keycode) {

		if (Gdx.input.isKeyPressed(keycode)) {
			return 1;
		} else {
			return 0;
		}

	}

	public static void update() {

		input[0] = isPressedState(UP);
		input[1] = isPressedState(DOWN);
		input[2] = isPressedState(LEFT);
		input[3] = isPressedState(RIGHT);
		input[4] = isPressedState(SPRINT);
		input[5] = isPressedState(PLAYER_BUTTON_1);
		input[6] = isPressedState(PLAYER_BUTTON_2);
		input[7] = isPressedState(PLAYER_BUTTON_3);
		input[8] = isPressedState(PLAYER_BUTTON_4);
		input[9] = isPressedState(PLAYER_BUTTON_5);

	}

	public static String getDefaultState() {
		return new String("0000000000");
	}

	public static String getState() {

		return Integer.toString(input[0]) + Integer.toString(input[1]) + Integer.toString(input[2])
				+ Integer.toString(input[3]) + Integer.toString(input[4]) + Integer.toString(input[5])
				+ Integer.toString(input[6]) + Integer.toString(input[7]) + Integer.toString(input[8])
				+ Integer.toString(input[9]);

	}

}
