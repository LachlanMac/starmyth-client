package com.pineconeindustries.client;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.pineconeindustries.client.log.Log;

public class GameInput implements InputProcessor {

	public static int DOWN = Input.Keys.S;
	public static int UP = Input.Keys.W;
	public static int RIGHT = Input.Keys.D;
	public static int LEFT = Input.Keys.A;
	public static int SPRINT = Input.Keys.SHIFT_LEFT;
	public static int ENTER = Input.Keys.ENTER;
	public static int CENTER_CAMERA = Input.Keys.Y;
	public static int MOUSE_DOWN = Input.Buttons.LEFT;


	public GameInput() {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Log.print("Click at " + screenX + " , " + screenY);
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

}
