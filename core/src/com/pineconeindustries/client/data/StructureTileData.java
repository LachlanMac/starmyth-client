package com.pineconeindustries.client.data;

public class StructureTileData {

	public static final int SHIP_TILE_SIZE = 128;

	private int shipTileID, x, y;

	public StructureTileData(int x, int y, int shipTileID) {

		this.x = x;
		this.y = y;
		this.shipTileID = shipTileID;
	}

	
	public int getTileID() {
		return shipTileID;
	}

}
