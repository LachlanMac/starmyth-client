package com.pineconeindustries.server.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.shared.objects.Tile;

public abstract class Structure {

	protected int globalX, globalY, width, height, structureID, sector, factionID, renderX, renderY;
	protected String structureName;
	
	protected Tile[][] tiles;
	
	public Structure(String structureName, int structureID, int sector, int factionID, int globalX, int globalY, int renderX, int renderY, int width, int height) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.width = width;
		this.height = height;
		this.structureID = structureID;
		this.factionID = factionID;
		this.sector = sector;
		this.structureName = structureName;
	}
	
	public Structure(int structureID) {
		this.structureID = structureID;
	}
	

	public abstract void loadStructure(String data);
	
	public abstract void update();
	
	public abstract void render(SpriteBatch b);
	
	public Tile[][] getTiles(){
		return tiles;
	}
	
	public String getStructureName() {
		return structureName;
	}
	
	public int getStructureID() {
		return structureID;
	}
}
