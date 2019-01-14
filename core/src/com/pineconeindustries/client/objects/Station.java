package com.pineconeindustries.client.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.data.ShipData;
import com.pineconeindustries.client.data.StationData;
import com.pineconeindustries.client.data.StructureTileData;
import com.pineconeindustries.client.manager.Game;

public class Station extends GameObject {

	private final int SHIP_GRID_SIZE = 8192;

	StationData data;

	Texture wall, diag, hall, floor;

	Vector2 interiorLoc;

	public Station(String name, Vector2 loc, Game game, StationData data) {
		super(name, loc, game);
		this.data = data;

		data.setStation(this);

		interiorLoc = new Vector2(SHIP_GRID_SIZE * data.getLocalX(), SHIP_GRID_SIZE * data.getLocalY());

	}

	public StationData getData() {
		return data;
	}

	@Override
	public void render(Batch b) {

		Vector2 tileLoc = null;
		StructureTileData[][] layout = data.getLayout();

		for (int y = 0; y < data.getTileHeight(); y++) {
			for (int x = 0; x < data.getTileWidth(); x++) {

				tileLoc = new Vector2(interiorLoc.x + (x * StructureTileData.SHIP_TILE_SIZE),
						interiorLoc.y + (y * StructureTileData.SHIP_TILE_SIZE));

				if (tileLoc.dst(game.getPlayer().getLoc()) < 750) {
					TextureRegion tr = game.Assets().getStructureTileByID(layout[x][y].getTileID());
					if (tr != null) {

						b.draw(tr, tileLoc.x, tileLoc.y);

					}

				}

			}
		}

	}

	public Vector2 getLocalCoordinates(Vector2 global) {

		int localX = data.getLocalX() * StructureTileData.SHIP_TILE_SIZE;
		int localY = data.getLocalY() * StructureTileData.SHIP_TILE_SIZE;

		return new Vector2(global.x - localX, global.y - localY);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
