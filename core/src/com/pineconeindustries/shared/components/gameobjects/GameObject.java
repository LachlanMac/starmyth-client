package com.pineconeindustries.shared.components.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.components.behaviors.Targetable;
import com.pineconeindustries.shared.components.structures.StructureLayer;
import com.pineconeindustries.shared.components.structures.Tile;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.stats.Stats;
import com.pineconeindustries.shared.units.Units;

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
	protected int structureID;
	protected Stats stats;

	protected String name;

	// OLD String name, Vector2 loc, int layer, int id, int sectorID, int
	// structureID
	// NEW int id, String name, Vector2 loc, int sectorID, int structureID, int
	// layer

	public GameObject(int id, String name, Vector2 loc, int sectorID, int structureID, int layer) {
		this.name = name;
		this.sectorID = sectorID;
		this.structureID = structureID;
		this.loc = loc;
		this.layer = layer;
		this.id = id;
		bounds = new Rectangle(loc.x, loc.y, boundsWidth, boundsHeight);

		if (Global.isClient()) {
			stats = new Stats();
		}

	}

	public abstract String getType();

	public abstract void update();

	public abstract void render(SpriteBatch b);

	public abstract void debugRender(ShapeRenderer debugRenderer);

	public abstract void dispose();

	public Vector2 getLoc() {
		return loc;
	}

	public Rectangle getProposedPoint(Vector2 vec) {
		return new Rectangle(vec.x - 1, vec.y - 1, 3, 3);
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

	public ArrayList<Tile> getBorderTiles() {

		ArrayList<Tile> tiles = new ArrayList<Tile>();

		Sector s = Galaxy.getInstance().getSectorByID(sectorID);

		StructureLayer l = Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID)
				.getLayerByNumber(layer);

		Vector2 localVector = getLocalVector();

		tiles.add(l.getTileAt(localVector.x, localVector.y));
		tiles.add(l.getTileAt(localVector.x - Units.TILE_SIZE, localVector.y));
		tiles.add(l.getTileAt(localVector.x + Units.TILE_SIZE, localVector.y));
		tiles.add(l.getTileAt(localVector.x, localVector.y - Units.TILE_SIZE));
		tiles.add(l.getTileAt(localVector.x, localVector.y + Units.TILE_SIZE));
		tiles.add(l.getTileAt(localVector.x - Units.TILE_SIZE, localVector.y - Units.TILE_SIZE));
		tiles.add(l.getTileAt(localVector.x + Units.TILE_SIZE, localVector.y - Units.TILE_SIZE));
		tiles.add(l.getTileAt(localVector.x + Units.TILE_SIZE, localVector.y + Units.TILE_SIZE));
		tiles.add(l.getTileAt(localVector.x - Units.TILE_SIZE, localVector.y + Units.TILE_SIZE));

		return tiles;

	}

	public Vector2 getLocalVector() {

		return Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID).getLocalVector(loc);

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

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Stats getStats() {
		return stats;
	}

}
