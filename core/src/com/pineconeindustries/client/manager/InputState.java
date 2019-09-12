package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.pineconeindustries.client.ui.UserInterface;

public class InputState implements InputProcessor {

	public static int[] input = new int[10];
	public static int DOWN = Input.Keys.S;
	public static int UP = Input.Keys.W;
	public static int RIGHT = Input.Keys.D;
	public static int LEFT = Input.Keys.A;
	public static int SPRINT = Input.Keys.SHIFT_LEFT;
	public static int ENTER = Input.Keys.ENTER;
	public static int TEST_BUTTON = Input.Keys.U;
	public static int CENTER_CAMERA = Input.Keys.Y;
	public static boolean RIGHT_MOUSE_DOWN = false;
	public static boolean LEFT_MOUSE_DOWN = false;
	public static int PLAYER_BUTTON_1 = Input.Keys.NUM_1;
	public static int PLAYER_BUTTON_2 = Input.Keys.NUM_2;
	public static int PLAYER_BUTTON_3 = Input.Keys.NUM_3;
	public static int PLAYER_BUTTON_4 = Input.Keys.NUM_4;
	public static int PLAYER_BUTTON_5 = Input.Keys.NUM_5;

	private static InputState instance = null;

	private InputState() {

	}

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

	public static void resetInput() {
		LEFT_MOUSE_DOWN = false;
		RIGHT_MOUSE_DOWN = false;
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean rightClick() {
		return RIGHT_MOUSE_DOWN;
	}

	public static boolean leftClick() {
		return LEFT_MOUSE_DOWN;
	}

	public static InputState getInstance() {
		if (instance == null) {
			instance = new InputState();
		}
		return instance;
	}

}
