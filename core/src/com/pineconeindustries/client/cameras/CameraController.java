package com.pineconeindustries.client.cameras;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.pineconeindustries.client.manager.LogicController;

public class CameraController {
	private OrthographicCamera playerCamera, fixedCamera;
	private ScalingViewport playerViewport, fixedViewport;

	private float aspectRatio;
	private boolean playerCameraActive = true;

	public CameraController() {
		this.playerCamera = new OrthographicCamera();
		this.fixedCamera = new OrthographicCamera();
		aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		playerViewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, playerCamera);
		fixedViewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, fixedCamera);
		fixedViewport.apply();
		playerViewport.apply();

	}

	public void update() {

		if (playerCameraActive) {
			float lerp = 0.9f;
			Vector3 position = playerCamera.position;

			if (new Vector2(position.x, position.y).dst(LogicController.getInstance().getPlayer().getLoc()) > 100) {
				position.x += (LogicController.getInstance().getPlayer().getLoc().x - position.x) * lerp
						* Gdx.graphics.getDeltaTime();
				position.y += (LogicController.getInstance().getPlayer().getLoc().y - position.y) * lerp
						* Gdx.graphics.getDeltaTime();
			}

			playerCamera.update();

			if (CameraRumble.getRumbleTimeLeft() > 0) {
				CameraRumble.tick(Gdx.graphics.getDeltaTime());
				playerCamera.translate(CameraRumble.getPos());
			}

		}

	}

	public void setRumble(float rumblePower, float rumbleLength) {
		CameraRumble.rumble(rumblePower, rumbleLength);
	}

	public void setPlayerCameraPosition(Vector2 pos) {
		playerCamera.position.set(pos.x, pos.y, 0);
	}

	public OrthographicCamera getPlayerCamera() {
		return playerCamera;
	}

	public OrthographicCamera getFixedCamera() {
		return fixedCamera;
	}

	public void setPlayerCameraActive(boolean val) {
		this.playerCameraActive = val;
	}

	public ScalingViewport getPlayerViewport() {
		return playerViewport;
	}

	public ScalingViewport getFixedViewport() {
		return fixedViewport;
	}
}
