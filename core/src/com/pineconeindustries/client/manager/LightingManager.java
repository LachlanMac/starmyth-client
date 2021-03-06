package com.pineconeindustries.client.manager;

import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pineconeindustries.client.cameras.CameraRumble;
import com.pineconeindustries.client.lighting.DynamicLight;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightingManager {

	Body body;
	BodyDef bodyDef;
	public final float DEFAULT_ALPHA = 0.6f;
	private float r, g, b, a;
	RayHandler rh;
	World world;

	ArrayBlockingQueue<DynamicLight> lights;
	private static LightingManager instance = null;

	public static LightingManager getInstance() {
		if (instance == null) {
			instance = new LightingManager();
		}
		return instance;
	}

	public float getAlpha() {
		return a;
	}

	public void update() {

		for (DynamicLight l : lights) {
			l.update();
		}

	}

	private LightingManager() {
		lights = new ArrayBlockingQueue<DynamicLight>(1024);
		world = new World(new Vector2(0, 0), true);

		rh = new RayHandler(world);

		setAmbientLight(0.01f, 0.01f, 0.01f, 0.6f);
		rh.setBlurNum(3);

	}

	public void addLight(DynamicLight l) {
		lights.add(l);
	}

	public void removeLight(DynamicLight l) {
		lights.remove(l);
		l.remove();
	}

	public void setAlpha(float a) {

		rh.setAmbientLight(r, g, b, a);

	}

	public void lowerAlpha(float delta) {

		a = delta + a;
		rh.setAmbientLight(r, g, b, a);

	}

	public void raiseAlpha(float delta) {
		a = delta + a;
		rh.setAmbientLight(r, g, b, a);
	}

	public void setAmbientLight(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		rh.setAmbientLight(r, g, b, a);
	}

	public RayHandler getLighting() {
		return rh;
	}

	public void dispose() {
		rh.dispose();
	}

	public void render() {
		rh.setCombinedMatrix(LogicController.getInstance().getPlayerCamera());
		try {
			rh.updateAndRender();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(0);
		}
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
	}

}
