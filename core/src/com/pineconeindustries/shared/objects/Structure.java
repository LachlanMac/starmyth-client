package com.pineconeindustries.shared.objects;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.client.networking.packets.PacketRequester;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.log.Log;

public abstract class Structure {

	public final static int STRUCTURE_SIZE = 64;
	protected int width, height, structureID, sectorID, factionID, renderX, renderY, layers, type;
	protected float globalX, globalY;
	private String data;
	protected long checksum;
	protected String structureName, packetData;
	protected boolean render = false, gotElevatorData = false;
	protected Tile[][] tiles;
	protected ArrayBlockingQueue<GridTile> blockedTiles;
	protected ArrayBlockingQueue<GridTile> unblockedTiles;
	protected ArrayBlockingQueue<StructureLayer> layerList;
	protected GridTile[][] grid;
	protected Random rn = new Random(System.currentTimeMillis());
	protected ArrayList<Elevator> elevators;

	public Structure(String structureName, int structureID, int sectorID, int factionID, int renderX, int renderY,
			float globalX, float globalY, int layers) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.width = Units.STRUCTURE_SIZE;
		this.height = Units.STRUCTURE_SIZE;
		this.structureID = structureID;
		this.factionID = factionID;
		this.sectorID = sectorID;
		this.structureName = structureName;
		this.layers = layers;
		layerList = new ArrayBlockingQueue<StructureLayer>(4);
		this.type = 0;
		loadLayers();
		elevators = new ArrayList<Elevator>();

		if (Global.isClient()) {
			requestData();
		}

	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public Structure(int structureID) {
		this.structureID = structureID;
	}

	public void loadLayers() {

		for (int i = 1; i <= layers; i++) {
			if (Global.isServer()) {
				layerList.add(new StructureLayer(this, i));

			} else {
				layerList.add(new StructureLayer(this, i));
			}

		}

	}

	public Vector2 getGlobalVector(Vector2 local) {

		return new Vector2(local.x + (renderX * STRUCTURE_SIZE * Tile.TILE_SIZE),
				local.y + (renderX * STRUCTURE_SIZE * Tile.TILE_SIZE));

	}

	public Vector2 getLocalVector(Vector2 global) {

		return new Vector2(global.x - (renderX * STRUCTURE_SIZE * Tile.TILE_SIZE),
				global.y - (renderX * STRUCTURE_SIZE * Tile.TILE_SIZE));

	}

	public abstract void update();

	public abstract void render(SpriteBatch b);

	public abstract void debugRender(ShapeRenderer debugRenderer);

	public Tile[][] getTiles() {
		return tiles;
	}

	public int getLayers() {
		return layers;
	}

	public String getStructureName() {
		return structureName;
	}

	public int getStructureID() {
		return structureID;
	}

	public int getGridWidth() {
		return width * 4;
	}

	public int getGridHeight() {
		return height * 4;
	}

	public int getFactionID() {
		return factionID;
	}

	public Tile getTileByVector(int x, int y) {
		return tiles[x][y];
	}

	public int getRenderX() {
		return renderX;
	}

	public int getRenderY() {
		return renderY;
	}

	public int getType() {
		return type;
	}

	public int getSectorID() {
		return sectorID;
	}

	public StructureLayer getLayerByNumber(int i) {

		StructureLayer layer = null;
		for (StructureLayer l : layerList) {
			if (l.getLayer() == i) {
				layer = l;
			}
		}
		return layer;
	}

	public void requestData() {

		PacketRequester requester = new PacketRequester(
				PacketFactory.makeElevatorRequestPacket(getStructureID(), getSectorID()), 2, 5) {
			@Override
			public void checkValidity() {

				if (gotElevatorData) {
					this.kill();
					return;
				}

			}
		};

		requester.start();
	}

	public void loadElevators(ArrayList<Elevator> elevators) {
		gotElevatorData = true;
		this.elevators = elevators;
	}

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}

}
