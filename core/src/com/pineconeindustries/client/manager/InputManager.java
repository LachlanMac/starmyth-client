package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.objects.Projectile;

public class InputManager {

	public static int DOWN = Input.Keys.S;
	public static int UP = Input.Keys.W;
	public static int RIGHT = Input.Keys.D;
	public static int LEFT = Input.Keys.A;
	public static int SPRINT = Input.Keys.SHIFT_LEFT;
	public static int ENTER = Input.Keys.ENTER;
	public static int TEST_BUTTON = Input.Keys.U;
	public static int CENTER_CAMERA = Input.Keys.Y;
	public static int MOUSE_DOWN = Input.Buttons.LEFT;
	public static float mouseX = 0f;
	public static float mouseY = 0f;

	public static boolean isPressed(int keycode) {

		if (Gdx.input.isKeyPressed(keycode)) {
			return true;
		} else {
			return false;
		}

	}

	public static Vector2 getMouseCoordinates(Camera cam) {

		Vector3 worldCoordinates = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		mouseX = worldCoordinates.x;
		mouseY = worldCoordinates.y;

		return new Vector2(worldCoordinates.x, worldCoordinates.y);

	}

	public static Vector2 mouseDown(int mouseCode) {

		if (Gdx.input.isButtonPressed(mouseCode)) {
			return new Vector2(Gdx.input.getX(), Gdx.input.getY());

		} else {
			return null;
		}

	}

	public void checkInput() {

	}

}
