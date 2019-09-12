package com.pineconeindustries.shared.objects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.gameunits.Units;

public abstract class Person extends Entity {

	protected float state = 0f;
	private boolean tickrender = false;
	private boolean spin = false;

	public Person(String name, Vector2 loc, int factionID, int structureID, int id, int sectorID, int layer) {
		super(name, loc, factionID, structureID, layer, id, sectorID);

		if (!GameData.getInstance().isHeadless()) {
			setAnimations();
		}
	}

	public void setAnimations() {
		animSet = GameData.getInstance().Assets().getPlayerAnimations();
		currentFrame = animSet.getAnimation(new Vector2(0, 1), 0);
	}

	public int getID() {
		return id;
	}

	public void setID(int playerID) {
		this.id = playerID;
	}

	public boolean shouldTickRender() {
		return tickrender;
	}

	public void disableTickRender() {
		tickrender = false;
	}

	public void enableTickRender() {

		tickrender = true;
	}

	public Vector2 getLocalVector() {

		return Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID).getLocalVector(loc);

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

	public void setSpin(boolean spin) {
		this.spin = spin;
	}

	public boolean getSpin() {
		return this.spin;
	}

}
