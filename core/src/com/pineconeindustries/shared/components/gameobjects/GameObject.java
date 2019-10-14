package com.pineconeindustries.shared.components.gameobjects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packetdata.MoveData;
import com.pineconeindustries.shared.actions.effects.EffectOverTime;
import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.components.behaviors.Targetable;
import com.pineconeindustries.shared.components.structures.Structure;
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
	protected boolean immobile = false;
	protected int sectorID;
	protected int structureID;
	protected Stats stats;
	protected ArrayBlockingQueue<EffectOverTime> effects;
	protected String name;
	protected GameObject held = null;
	protected GameObject holder = null;
	protected Structure structure = null;
	protected type goType;
	protected DecimalFormat df = new DecimalFormat("#.00");
	protected boolean holdable = false;

	enum type {
		NPC, PLAYER, GROUND_OBJECT, PROJECTILE, GAME_OBJECT;
	};

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
		} else {
			structure = Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID);
			goType = type.GAME_OBJECT;
			effects = new ArrayBlockingQueue<EffectOverTime>(24);
		}

	}

	public boolean isNPC() {
		if (goType == type.NPC)
			return true;
		else
			return false;
	}

	public boolean isPlayer() {
		if (goType == type.PLAYER)
			return true;
		else
			return false;
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

	public GameObject getHolder() {
		return holder;
	}

	public GameObject getHeld() {
		return held;
	}

	public void drop() {
		held = null;
	}

	public void beDropped() {
		holder = null;
	}

	public void pickup(GameObject obj) {
		held = obj;
	}

	public void bePickedup(GameObject obj) {
		holder = obj;
	}

	public static void pickup(GameObject a, GameObject b) {
		a.pickup(b);
		b.bePickedup(a);
	}

	public static void drop(GameObject a, GameObject b) {
		a.drop();
		b.beDropped();
	}

	public void updatePosition(Vector2 location) {

		switch (goType) {
		case NPC:
			String data = new String(getID() + "x" + df.format(location.x) + "x" + df.format(location.y) + "x" + "s"
					+ "x" + layer + "=");
			setLoc(location);
			structure.addNPCMovementData(new MoveData(data, structureID, layer));
			break;
		case PLAYER:
			break;
		case PROJECTILE:
			break;
		default:
			break;

		}
	}

	public void updateEffects(float delta) {

		for (EffectOverTime e : effects) {

			if (e.isRunning()) {
				e.update(delta);
			} else {
				removeEffectOverTime(e);
			}
		}
	}

	public void addEffectOverTime(EffectOverTime e) {
		effects.add(e);
	}

	public void removeEffectOverTime(EffectOverTime e) {
		effects.remove(e);
	}

	public ArrayBlockingQueue<EffectOverTime> getCurrentEffects() {
		return effects;
	}

	public boolean isImmobile() {
		return immobile;
	}

	public void setImmobile(boolean immobile) {
		this.immobile = immobile;
	}

	public boolean isHoldable() {
		return holdable;
	}

	public void setHoldable(boolean holdable) {
		this.holdable = holdable;
	}

	public boolean isDowned() {

		return getStats().getCurrentHP() <= 0;
	}

	public Structure getStructure() {
		return structure;
	}
}
