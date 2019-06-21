package com.pineconeindustries.client.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.data.ShipData;
import com.pineconeindustries.client.data.StructureTileData;
import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.manager.GameController;
import com.pineconeindustries.shared.data.GameData;

public class Ship extends GameObject {

	private final int SHIP_GRID_SIZE = 8192;

	ShipData data;

	Texture wall, diag, hall, floor;

	Vector2 interiorLoc;

	ArrayList<ZoneBox> zones;

	public Ship(String name, Vector2 loc, GameData game, ShipData data) {
		super(name, loc, game);
		this.data = data;

		data.setShip(this);

		interiorLoc = new Vector2(SHIP_GRID_SIZE * data.getLocalX(), SHIP_GRID_SIZE * data.getLocalY());

		int zbXStart = (int) interiorLoc.x + 10;
		int zbYStart = (int) interiorLoc.y + 10;

		// Rectangle origin = new Rectangle(zbXStart, zbYStart, 100, 100);
		// Rectangle destination = new Rectangle(zbXStart + 300, zbYStart + 300, 100,
		// 100);

		zones = new ArrayList<ZoneBox>();
		// load zones
	}

	public void addZone(ZoneBox b) {
		zones.add(b);
	}

	public ShipData getData() {
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

				// if (tileLoc.dst(game.getPlayer().getLoc()) < 750) {
				TextureRegion tr = game.Assets().getStructureTileByID(layout[x][y].getTileID());
				if (tr != null) {

					b.draw(tr, tileLoc.x, tileLoc.y);

				}

				// }

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

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderDebug(ShapeRenderer b) {

	}

}
