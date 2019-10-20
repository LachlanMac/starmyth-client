package com.pineconeindustries.shared.components.structures;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.lighting.DarkenEffect;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.manager.SoundEffectManager;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.client.networking.packets.PacketRequester;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packetdata.MoveData;
import com.pineconeindustries.server.net.packets.modules.ScheduleModule;
import com.pineconeindustries.server.net.packets.scheduler.PacketScheduler;
import com.pineconeindustries.server.net.packets.scheduler.StructurePacketScheduler;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.components.gameobjects.GridTile;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.gameobjects.Projectile;
import com.pineconeindustries.shared.data.Assets;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.units.Units;

public abstract class Structure {
	public enum STRUCTURE_STATE {
		running, off, emergency, none
	};

	ArrayBlockingQueue<MoveData> npcMoveList;
	ArrayBlockingQueue<String> projectileMoveList;
	protected ArrayBlockingQueue<NPC> npcs;
	protected ArrayBlockingQueue<PlayerConnection> players;
	protected ArrayBlockingQueue<Packet> sendQueue;
	protected ArrayBlockingQueue<Projectile> projectiles;
	PacketScheduler scheduler;
	Sector sector;

	public final static int STRUCTURE_SIZE = 64;
	protected int width, height, structureID, sectorID, factionID, renderX, renderY, layers, type;
	protected float globalX, globalY;
	private String data;
	private boolean emergency = false;
	private boolean enginesOn = false;
	protected STRUCTURE_STATE currentState = STRUCTURE_STATE.none;
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
	protected Sound engine = Assets.getInstance().getSoundEffect("shiploop");

	public Structure(String structureName, int structureID, int sectorID, int factionID, int renderX, int renderY,
			float globalX, float globalY, int layers, STRUCTURE_STATE currentState) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.width = Units.STRUCTURE_SIZE;
		this.height = Units.STRUCTURE_SIZE;
		this.structureID = structureID;
		this.factionID = factionID;
		this.sectorID = sectorID;
		this.structureName = structureName;
		this.layers = layers;
		this.renderX = renderX;
		this.renderY = renderY;
		this.sector = Galaxy.getInstance().getSectorByID(sectorID);
		layerList = new ArrayBlockingQueue<StructureLayer>(4);
		this.type = 0;
		loadLayers();
		elevators = new ArrayList<Elevator>();

		if (Global.isClient()) {
			requestData();
		} else {
			scheduler = new StructurePacketScheduler(sector, this);
			npcs = new ArrayBlockingQueue<NPC>(64);
			players = new ArrayBlockingQueue<PlayerConnection>(64);
			sendQueue = new ArrayBlockingQueue<Packet>(1024);
			projectiles = new ArrayBlockingQueue<Projectile>(256);
			npcMoveList = new ArrayBlockingQueue<MoveData>(1024);
			projectileMoveList = new ArrayBlockingQueue<String>(1024);

			scheduler.registerPacket(ScheduleModule.makeNPCListScheduler(this, sector, 5.0f));
			scheduler.registerPacket(ScheduleModule.makePlayerListScheduler(this, sector, 5.0f));
			scheduler.registerPacket(ScheduleModule.makeNPCStatListPacket(this, sector, 0.2f));
			scheduler.registerPacket(ScheduleModule.makePlayerStatListPacket(this, sector, 0.2f));
			scheduler.start();
		}
		setState(currentState);

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

	public void setState(STRUCTURE_STATE state) {

		if (state == currentState)
			return;

		this.currentState = state;

		if (Global.isClient()) {

			switch (currentState) {

			case running:
				engine.stop();
				long id1 = engine.play(1.0f);
				engine.setLooping(id1, true);
				break;
			case off:
				engine.stop();
				long id2 = engine.play(0.4f);
				engine.setLooping(id2, false);
				break;
			case emergency:
				break;
			default:
				break;
			}
		}
	}

	public void registerHitEvent(float strength, int tileX, int tileY, int layer) {
		if (LogicController.getInstance().getPlayer().getStructureID() == this.structureID) {

			SoundEffectManager.getInstance().playSoundEffect(Assets.getInstance().getSoundEffect("explosion"), 0.5f);
			LogicController.getInstance().getCameraController().setRumble(strength, 2);
			new DarkenEffect(0.2f, 5);
		}
	}

	public void registerShipStartEvent(float strength) {
		if (LogicController.getInstance().getPlayer().getStructureID() == this.structureID) {
			SoundEffectManager.getInstance().playSoundEffect(Assets.getInstance().getSoundEffect("shipstart"), 0.05f);
			LogicController.getInstance().getCameraController().setRumble(1, 25);

		}
	}

	public void registerShipStopEvent(float strength) {
		if (LogicController.getInstance().getPlayer().getStructureID() == this.structureID) {
			LogicController.getInstance().getCameraController().setRumble(1, 6);

			SoundEffectManager.getInstance().playSoundEffect(Assets.getInstance().getSoundEffect("shipstop"), 0.05f);

		}
	}

	public void registerShipEmergencyEvent(boolean emergency) {
		this.emergency = emergency;
	}

	public String getStructureState() {

		switch (currentState) {

		case running:
			return "r";
		case off:
			return "o";
		case emergency:
			return "e";
		default:
			return "";
		}

	}

	public static STRUCTURE_STATE getStateByString(String state) {

		switch (state) {

		case "r":
			return STRUCTURE_STATE.running;
		case "o":
			return STRUCTURE_STATE.off;
		case "e":
			return STRUCTURE_STATE.emergency;
		default:
			return STRUCTURE_STATE.off;
		}

	}

	public void addToSendQueue(Packet p) {
		sendQueue.add(p);
	}

	public ArrayBlockingQueue<Packet> getSendQueue() {
		return sendQueue;
	}

	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}

	public void removeProjectile(Projectile p) {
		projectiles.remove(p);
	}

	public ArrayBlockingQueue<Projectile> getProjectiles() {
		return projectiles;
	}

	public void addPlayer(PlayerConnection p) {
		Log.debug("Adding player to Structure " + structureID);
		players.add(p);
	}

	public void removePlayer(PlayerConnection p) {
		Log.debug("Removing player from Structure " + structureID);
		players.remove(p);
	}

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	public void addNPC(NPC npc) {
		npcs.add(npc);
	}

	public void removeNPC(NPC npc) {
		npcs.remove(npc);
	}

	public ArrayBlockingQueue<NPC> getNPCs() {
		return npcs;
	}

	public void addNPCMovementData(MoveData data) {
		npcMoveList.add(data);
	}

	public void addProjectileMovementData(String data) {
		projectileMoveList.add(data);
	}

	public ArrayBlockingQueue<MoveData> getNPCMoveList() {
		return npcMoveList;
	}

	public ArrayBlockingQueue<String> getProjectileMoveList() {
		return projectileMoveList;
	}

}
