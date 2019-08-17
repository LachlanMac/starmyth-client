package com.pineconeindustries.server.data;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.shared.objects.Tile;

public abstract class Structure {

	protected final static int SHIP_SIZE = 64;

	protected int width, height, structureID, sector, factionID, renderX, renderY, layers;
	protected float globalX, globalY;
	protected String structureName;
	protected boolean render = false;
	protected Tile[][] tiles;

	public Structure(String structureName, int structureID, int sector, int factionID, float globalX, float globalY,
			int renderX, int renderY, int width, int height, int layers) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.width = width;
		this.height = height;
		this.structureID = structureID;
		this.factionID = factionID;
		this.sector = sector;
		this.structureName = structureName;
		this.layers = layers;
		tiles = new Tile[SHIP_SIZE][SHIP_SIZE];

	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public Structure(int structureID) {
		this.structureID = structureID;
	}

	public abstract void loadStructure();

	public abstract void update();

	public abstract void render(SpriteBatch b);

	public Tile[][] getTiles() {
		return tiles;
	}

	public String getStructureName() {
		return structureName;
	}

	public int getStructureID() {
		return structureID;
	}
}
