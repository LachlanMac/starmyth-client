package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject implements Targetable {

	public static final int GAMEOBJECT_WIDTH = 64;
	public static final int GAMEOBJECT_HEIGHT = 64;

	protected Vector2 loc;
	protected boolean debugMode = false;
	public boolean target = false;
	protected int layer = 0;
	protected int id = 0;
	protected int DEFAULT_BOUNDS_WIDTH = 64;
	protected int DEFAULT_BOUNDS_HEIGHT = 64;
	protected float rotation;
	private int boundsWidth = DEFAULT_BOUNDS_WIDTH;
	private int boundsHeight = DEFAULT_BOUNDS_HEIGHT;
	protected Rectangle bounds;
	protected int sectorID;

	protected String name;

	public GameObject(String name, Vector2 loc, int layer, int id, int sectorID) {
		this.name = name;
		this.sectorID = sectorID;
		this.loc = loc;
		this.layer = layer;
		this.id = id;
		bounds = new Rectangle(loc.x, loc.y, boundsWidth, boundsHeight);
	}

	public abstract String getType();

	public abstract void update();

	public abstract void render(SpriteBatch b);

	public abstract void debugRender(ShapeRenderer debugRenderer);

	public abstract void dispose();

	public Vector2 getLoc() {
		return loc;
	}

	public Rectangle getProposedBounds(Vector2 vec) {
		return new Rectangle(vec.x, vec.y, boundsWidth, boundsHeight);
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

	}

	public void onClick() {

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

	@Override
	public void setTarget(boolean target) {
		this.target = target;
	}

	@Override
	public boolean isTarget() {
		return target;
	}

	public int getID() {
		return id;
	}

	public boolean isInRange(GameObject target, float range) {

		if (this.getLoc().dst(target.getLoc()) <= range) {
			return true;
		} else {
			return false;
		}

	}

	public Vector2 getCenter() {

		return new Vector2(getLoc().x + (GAMEOBJECT_WIDTH / 2), getLoc().y + (GAMEOBJECT_HEIGHT / 2));

	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public boolean isInLineOfSight(GameObject target) {
		return true;
	}

}
