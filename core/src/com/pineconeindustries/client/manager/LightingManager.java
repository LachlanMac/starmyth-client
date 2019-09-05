package com.pineconeindustries.client.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

public class LightingManager {

	RayHandler rh;
	World world;
	private static LightingManager instance = null;

	public static LightingManager getInstance() {
		if (instance == null) {
			instance = new LightingManager();
		}
		return instance;
	}

	private LightingManager() {
		world = new World(new Vector2(0, 0), true);
		rh = new RayHandler(world);
		rh.setAmbientLight(0.01f, 0.01f, 0.01f, 0.6f);

	}

	public void setAmbientLight(float r, float g, float b, float alpha) {
		rh.setAmbientLight(0.01f, 0.01f, 0.01f, 0.6f);
	}

	public RayHandler getLighting() {
		return rh;
	}

	public void dispose() {
		rh.dispose();
	}

	public void render() {
		rh.setCombinedMatrix(LogicController.getInstance().getPlayerCamera());
		rh.updateAndRender();
		world.step(1 / 60f, 6, 2);
	}

}
