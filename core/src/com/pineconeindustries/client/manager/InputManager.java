package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.shared.objects.Projectile;

public class InputManager {

	public static float mouseX = 0f;
	public static float mouseY = 0f;

	public static void updateMouse(Camera cam) {
		Vector3 worldCoordinates = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		mouseX = worldCoordinates.x;
		mouseY = worldCoordinates.y;
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
