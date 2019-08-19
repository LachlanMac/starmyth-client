package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;

public abstract class GameObject {

	Vector2 loc;
	GameData game;

	protected boolean debugMode = false;
	protected int layer = 0;
	protected int DEFAULT_BOUNDS_WIDTH = 64;
	protected int DEFAULT_BOUNDS_HEIGHT = 64;
	protected float rotation;
	private int boundsWidth = 64;
	private int boundsHeight = 64;
	Rectangle bounds;

	private String name;

	public GameObject(String name, Vector2 loc, GameData game, int layer) {
		this.name = name;
		this.loc = loc;
		this.game = game;
		this.layer = layer;

		bounds = new Rectangle(loc.x, loc.y, boundsWidth, boundsHeight);
	}

	public abstract void update();

	public abstract void renderDebug(ShapeRenderer b);

	public abstract void render(Batch b);

	public abstract void dispose();

	public Vector2 getLoc() {
		return loc;
	}

	public void setLoc(Vector2 loc) {
		this.loc = loc;
		updateBounds();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean getDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public void updateBounds() {

		this.bounds.setX(loc.x);
		this.bounds.setY(loc.y);

	}

	public void hover() {

		if (Intersector.overlaps(new Rectangle(InputManager.mouseX, InputManager.mouseY, 1, 1), this.bounds)) {
			Log.debug("Object: " + name + "[" + loc.x + "," + loc.y + "]");
		}

	}

	public float getRotation() {
		return rotation;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

}