package com.pineconeindustries.server.galaxy;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.net.packets.modules.ScheduleModule;
import com.pineconeindustries.server.net.packets.scheduler.PacketScheduler;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.players.PacketListener;
import com.pineconeindustries.server.net.players.PacketParser;
import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.server.net.players.PlayerConnectionListener;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.GameObject;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Projectile;
import com.pineconeindustries.shared.objects.Station;
import com.pineconeindustries.shared.objects.Structure;

public class Sector {

	ArrayBlockingQueue<PlayerConnection> players;
	ArrayBlockingQueue<NPC> npcs;
	ArrayBlockingQueue<Structure> structures;
	ArrayBlockingQueue<Projectile> projectiles;
	ArrayBlockingQueue<String> npcMoveList;
	ArrayBlockingQueue<String> projectileMoveList;

	private int port, globalX, globalY;
	private String name;
	private boolean update = false, render = false;

	PlayerConnectionListener connListener;
	PacketListener packetListener;
	PacketWriter packetWriter;
	PacketParser packetParser;
	PacketScheduler scheduler;

	// Class to sed all players UDP packets and TCP packets

	public Sector(int port, int globalX, int globalY, String name) {
		this.port = port;
		this.globalX = globalX;
		this.globalY = globalY;
		this.name = name;
		players = new ArrayBlockingQueue<PlayerConnection>(64);
		npcs = new ArrayBlockingQueue<NPC>(128);
		structures = new ArrayBlockingQueue<Structure>(16);
		npcMoveList = new ArrayBlockingQueue<String>(128);
		projectileMoveList = new ArrayBlockingQueue<String>(128);
		projectiles = new ArrayBlockingQueue<Projectile>(256);
		connListener = new PlayerConnectionListener(this);
		packetListener = new PacketListener(this);
		packetWriter = new PacketWriter(this);
		packetParser = new PacketParser(this);
		scheduler = new PacketScheduler(200, this);
		registerScheduledFunctions();
		scheduler.start();

		Log.serverLog("Sector configured on port " + port);

	}

	public void registerScheduledFunctions() {

		scheduler.registerPacket(ScheduleModule.makeNPCListScheduler(this));
		scheduler.registerPacket(ScheduleModule.makePlayerListScheduler(this));
		scheduler.registerPacket(ScheduleModule.makeStructureListScheduler(this));

	}

	public void addNPCMovementData(String data) {
		npcMoveList.add(data);
	}

	public void addProjectileMovementData(String data) {
		projectileMoveList.add(data);
	}

	public void updateAndRender(boolean val) {
		update = val;
		render = val;
	}

	public void render(SpriteBatch b) {

		if (!render) {
			return;
		}

		for (Structure structure : structures) {
			structure.render(b);
		}

		for (Projectile p : projectiles) {
			p.render(b);
		}

		for (PlayerConnection conn : players) {

			conn.getPlayerMP().render(b);

		}
		for (NPC npc : npcs) {
			npc.render(b);
		}
	}

	public void debugRender(ShapeRenderer debugRenderer) {
		for (Structure structure : structures) {
			structure.debugRender(debugRenderer);
		}

		for (PlayerConnection conn : players) {

			conn.getPlayerMP().debugRender(debugRenderer);

		}
		for (NPC npc : npcs) {
			npc.debugRender(debugRenderer);
		}
	}

	public void update() {
		if (!update) {
			return;
		}
		for (NPC npc : npcs) {
			npc.update();
		}

		for (Projectile p : projectiles) {
			p.update();
		}

		for (PlayerConnection conn : players) {
			if (conn.isVerified()) {
				conn.getPlayerMP().update();
			}
		}
		if (!npcMoveList.isEmpty()) {
			packetWriter.sendNPCMoves(npcMoveList);
			npcMoveList.clear();
		}
		if (!projectileMoveList.isEmpty()) {
			packetWriter.sendProjectileMoves(projectileMoveList);
			projectileMoveList.clear();
		}

	}

	public void stopSector() {
		Log.serverLog("Stopping sector on port " + port);
		update = false;
		render = false;
		connListener.stopListener();
		packetListener.stopListener();
		packetWriter.stopWriter();
	}

	public void startSector() {
		Log.serverLog("Starting sector on port " + port);
		addStructures();
		addNPCs();

		update = true;
		render = true;
		connListener.start();
		packetListener.start();
		packetWriter.registerSocket(packetListener.getSocket());
		packetWriter.start();

	}

	public void connectPlayer(PlayerConnection player) {
		player.connect();
	}

	public void addPlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] added to Sector " + port);
		players.add(player);
		Galaxy.getInstance().addPlayerToGlobal(player);

	}

	public void addProjectile(Projectile p) {
		projectiles.add(p);

	}

	public void removeProjectile(Projectile p) {
		projectiles.remove(p);
	}

	public void addStructures() {
		ArrayList<Structure> tempList = Database.getInstance().getStructureDAO().loadStructuressBySectorID(port);

		for (Structure structure : tempList) {

			structures.add(structure);

		}
	}

	public Structure getStructureByID(int id) {

		Structure structure = null;

		for (Structure s : structures) {
			if (s.getStructureID() == id)
				structure = s;
		}

		return structure;

	}

	public void addNPCs() {

		ArrayList<NPC> tempList = Database.getInstance().getNPCDAO().loadNPCsBySectorID(port);

		for (NPC npc : tempList) {

			npcs.add(npc);

		}

	}

	public void removePlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] removed from Sector " + port);
		Database.getInstance().getPlayerDAO().savePlayer(player.getPlayerMP());
		players.remove(player);
		Galaxy.getInstance().removePlayerFromGlobal(player);
	}

	public int getPort() {
		return port;
	}

	public PlayerConnection getPlayerConnectionByID(int id) {

		PlayerConnection playerConnection = null;

		for (PlayerConnection c : players) {
			if (c.getPlayerMP().getID() == id)
				playerConnection = c;
		}

		return playerConnection;
	}

	public GameObject getGameObectByTypeAndID(String type, int id) {

		GameObject g = null;

		if (type.trim().equals("n")) {

			g = getNPCByID(id);
		} else if (type.trim().equals("p")) {
			g = getPlayerByID(id);
		}

		return g;

	}

	public NPC getNPCByID(int id) {

		NPC npc = null;

		for (NPC n : npcs) {
			if (n.getID() == id)
				npc = n;
		}

		return npc;
	}

	public PlayerMP getPlayerByID(int id) {

		PlayerMP player = null;

		for (PlayerConnection c : players) {
			if (c.getPlayerMP().getID() == id)
				player = c.getPlayerMP();
		}

		return player;
	}

	public ArrayList<PlayerConnection> getPlayerConnectionsInRange(int structureID, int layer, Vector2 origin,
			float distance) {

		ArrayList<PlayerConnection> connections = new ArrayList<PlayerConnection>();

		for (PlayerConnection c : players) {

			if (c.getPlayerMP().getStructureID() == structureID && c.getPlayerMP().getLayer() == layer
					&& origin.dst(c.getPlayerMP().getLoc()) < distance) {
				connections.add(c);
			}

		}

		return connections;

	}

	public ArrayList<NPC> getNPCsInRange(int structureID, int layer, Vector2 origin, float distance) {

		ArrayList<NPC> n = new ArrayList<NPC>();

		for (NPC npc : npcs) {

			if (npc.getStructureID() == structureID && npc.getLayer() == layer && origin.dst(npc.getLoc()) < distance) {
				n.add(npc);
			}

		}

		return n;

	}

	public PacketParser getPacketParser() {
		return packetParser;
	}

	public PacketListener getPacketListener() {
		return packetListener;
	}

	public PacketWriter getPacketWriter() {
		return packetWriter;
	}

	public ArrayBlockingQueue<Structure> getStructures() {
		return structures;
	}

	public ArrayBlockingQueue<NPC> getNPCs() {
		return npcs;
	}

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	@Override
	public String toString() {
		return "Sector : " + name + " [" + globalX + ", " + globalY + "]  on Port: " + port;
	}

}
