package com.pineconeindustries.shared.components.structures;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.client.networking.packets.PacketRequester;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.components.gameobjects.GridTile;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.units.Units;

public class StructureLayer {

	private boolean render = false;
	private String raw;
	private boolean gotLayerData = false;
	private int layer;
	private Tile[][] tiles;
	protected GridTile[][] grid;
	private Structure parent;
	private ArrayBlockingQueue<GridTile> blockedTiles;
	private ArrayBlockingQueue<GridTile> unblockedTiles;
	protected Random rn = new Random(System.currentTimeMillis());

	public StructureLayer(Structure parent, int layer) {
		this.layer = layer;
		this.parent = parent;
		tiles = new Tile[Units.STRUCTURE_SIZE][Units.STRUCTURE_SIZE];
		blockedTiles = new ArrayBlockingQueue<GridTile>(54000);
		unblockedTiles = new ArrayBlockingQueue<GridTile>(54000);
		grid = new GridTile[Units.GRID_SIZE][Units.GRID_SIZE];

		if (Global.isServer()) {
			loadServerLayer();
		} else {
			requestData();
		}

		

	}

	public void render(SpriteBatch b) {

		if (!render)
			return;

		for (int y = 0; y < Units.STRUCTURE_SIZE; y++) {
			for (int x = 0; x < Units.STRUCTURE_SIZE; x++) {
				tiles[x][y].render(b);
			}
		}

		for (Elevator e : parent.getElevators()) {
			e.render(b);
		}
	}

	public void debugRender(ShapeRenderer debugRenderer) {

		if (true)
			return;

		for (int y = 0; y < Units.STRUCTURE_SIZE; y++) {
			for (int x = 0; x < Units.STRUCTURE_SIZE; x++) {
				tiles[x][y].debugRender(debugRenderer);
			}
		}
	}

	public void update() {

	}

	public void loadServerLayer() {

		String fileName = Integer.toString(parent.getStructureID()) + "-" + layer;
		raw = Files.loadShipLayout(fileName);
		long checksum = 0;
		char[] dataArray = raw.toCharArray();
		StringBuilder rawData = new StringBuilder();
		for (char c : dataArray) {
			checksum += c;
			if (Character.isAlphabetic(c)) {
				if (Character.isUpperCase(c)) {
					char lc = Character.toLowerCase(c);
					char[] temp = new char[] { lc, lc, lc, lc, lc, lc };
					rawData.append(new String(temp));
				} else {
					rawData.append(Character.toString(c));
				}
			} else {
				rawData.append(Character.toString(c));
			}
		}

		char[] unpackedData = rawData.toString().toCharArray();

		int index = 0;

		for (int y = Units.STRUCTURE_SIZE - 1; y >= 0; y--) {

			for (int x = 0; x < Units.STRUCTURE_SIZE; x++) {

				tiles[x][y] = new Tile(unpackedData[index], x + (parent.getRenderX() * Units.STRUCTURE_SIZE),
						y + (parent.getRenderY() * Units.STRUCTURE_SIZE), parent.getRenderX(), parent.getRenderY());

				if (unpackedData[index] == 'p') {

				} else {
					tiles[x][y] = (new Tile(unpackedData[index], x + (parent.getRenderX() * Units.STRUCTURE_SIZE),
							y + (parent.getRenderY() * Units.STRUCTURE_SIZE), parent.getRenderX(), parent.getRenderY()));
				}
				index++;
			}
		}
		String pathMapData = Files.loadShipPathmap(fileName);
		char[] pathMapArray = pathMapData.toCharArray();

		int pathIndex = 0;
		for (int y = (Units.STRUCTURE_SIZE * 4 - 1); y >= 0; y--) {
			for (int x = 0; x < Units.STRUCTURE_SIZE * 4; x++) {
				char c = pathMapArray[pathIndex];
				GridTile t;
				if (c == '1') {
					t = new GridTile(x, y, true);
					grid[x][y] = t;
					blockedTiles.add(t);
				} else {
					t = new GridTile(x, y, false);
					grid[x][y] = t;
					unblockedTiles.add(t);
				}
				pathIndex++;
			}
		}
		render = true;

	}

	public void loadClientLayer(String data) {

		char[] dataArray = data.toCharArray();
		StringBuilder rawData = new StringBuilder();
		for (char c : dataArray) {
			if (Character.isAlphabetic(c)) {
				if (Character.isUpperCase(c)) {
					char lc = Character.toLowerCase(c);
					char[] temp = new char[] { lc, lc, lc, lc, lc, lc };
					rawData.append(new String(temp));
				} else {
					rawData.append(Character.toString(c));
				}
			} else {
				rawData.append(Character.toString(c));
			}
		}

		char[] unpackedData = rawData.toString().toCharArray();

		int index = 0;

		for (int y = Units.STRUCTURE_SIZE - 1; y >= 0; y--) {

			for (int x = 0; x < Units.STRUCTURE_SIZE; x++) {

				tiles[x][y] = new Tile(unpackedData[index], x, y, parent.getRenderX(), parent.getRenderY());

				if (unpackedData[index] == 'p') {

				} else {
					tiles[x][y] = (new Tile(unpackedData[index], x, y, parent.getRenderX(), parent.getRenderY()));
				}
				index++;
			}
		}

		render = true;

	}

	public ArrayBlockingQueue<GridTile> getBlocked() {
		return blockedTiles;
	}

	public void requestData() {
		PacketRequester requester = new PacketRequester(
				PacketFactory.makeLayerRequestPacket(parent.getStructureID(), parent.getSectorID(), layer), 2, 5) {
			@Override
			public void checkValidity() {

				if (gotLayerData) {
					this.kill();
					return;
				}

			}
		};

		requester.start();
	}

	public int getLayer() {
		return layer;
	}

	public Tile getTileAt(float x, float y) {

		int tileX = (int) x / Units.TILE_SIZE;
		int tileY = (int) y / Units.TILE_SIZE;

		return tiles[tileX][tileY];
	}

	public PathNode getRandomStartNode() {

		int counter = 0;
		int index = rn.nextInt(unblockedTiles.size());

		for (GridTile t : unblockedTiles) {
			if (index == counter) {
				return t.getPathNode();

			}
			counter++;
		}

		return unblockedTiles.peek().getPathNode();

	}

	public void setLayerData(String data) {
		gotLayerData = true;
		loadClientLayer(data);

	}

	public PathNode getRandomEndNode() {

		int counter = 0;
		int index = rn.nextInt(unblockedTiles.size());

		for (GridTile t : unblockedTiles) {
			if (index == counter) {
				return t.getPathNode();

			}
			counter++;
		}

		return unblockedTiles.peek().getPathNode();

	}

	public String getRaw() {
		return raw;
	}

}
