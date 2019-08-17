package com.pineconeindustries.server.data;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.GridTile;
import com.pineconeindustries.shared.objects.Tile;

public class Station extends Structure {

	private ArrayList<PathNode> pathList;
	private ArrayList<GridTile> blockedTiles;
	private ArrayList<GridTile> unblockedTiles;
	private GridTile[][] grid;
	Random rn = new Random(System.currentTimeMillis());

	public Station(String structureName, int structureID, int sector, int factionID, float globalX, float globalY,
			int renderX, int renderY, int width, int height, int layers) {
		super(structureName, structureID, sector, factionID, globalX, globalY, renderX, renderY, width, height, layers);
		if (structureID == 1001) {
			render = true;
		}

		blockedTiles = new ArrayList<GridTile>();
		unblockedTiles = new ArrayList<GridTile>();
		grid = new GridTile[width * 4][height * 4];
		pathList = new ArrayList<PathNode>();
	}

	public Station(int structureID) {
		super(structureID);
	}

	@Override
	public void loadStructure() {

		Log.debug("Loading Structure " + structureName + " : " + structureID);
		String fileName = Integer.toString(structureID) + "-" + 1;

		String data = Files.loadShipLayout(fileName);

		// UNPACK
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

		for (int y = SHIP_SIZE - 1; y >= 0; y--) {
			System.out.println();
			for (int x = 0; x < SHIP_SIZE; x++) {
				tiles[x][y] = new Tile(unpackedData[index], x, y);

				if (unpackedData[index] == 'p') {

				} else {
					tiles[x][y] = (new Tile(unpackedData[index], x, y));
				}
				index++;
			}
		}

		setTileGrid();

		// AStarPath path = new AStarPath(width * 4, width * 4,
		// grid[127][240].getPathNode(), grid[90][110].getPathNode());
		// path.setBlocks(blockedTiles);
		// pathList = path.findPath();

		// for (PathNode p : pathList) {

		// int tileX = p.getX() / 4;
		// int tileY = p.getY() / 4;
		//
		// System.out.println(getTileByVector(tileX, tileY).isBlocked());
		// }

	}

	public PathNode getRandomStartNode() {

		GridTile t = unblockedTiles.get(rn.nextInt(unblockedTiles.size()));

		return t.getPathNode();

	}

	public PathNode getRandomEndNode() {

		GridTile t = unblockedTiles.get(rn.nextInt(unblockedTiles.size()));

		return t.getPathNode();

	}

	public ArrayList<GridTile> getBlocked() {
		return blockedTiles;
	}

	public void setTileGrid() {

		for (int y = 0; y < height * 4; y++) {
			for (int x = 0; x < width * 4; x++) {

				Tile tile = getTileByVector((x / 4), (y / 4));
				GridTile t = new GridTile(x, y);
				t.setBlocked(tile.isBlocked());
				if (tile.isBlocked()) {
					blockedTiles.add(t);
				} else {
					unblockedTiles.add(t);
				}
				grid[x][y] = t;
			}
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	public int getGridWidth() {
		return width * 4;
	}

	public int getGridHeight() {
		return height * 4;
	}

	public Tile getTileByVector(int x, int y) {
		return tiles[x][y];
	}

	@Override
	public void render(SpriteBatch b) {

		if (!render)
			return;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				tiles[x][y].render(b);
			}
		}

		for (PathNode p : pathList) {
			grid[p.getX()][p.getY()].render(b);
		}

	}

}
