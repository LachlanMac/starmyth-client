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
import com.pineconeindustries.server.net.packetdata.MoveData;
import com.pineconeindustries.server.net.packets.modules.ScheduleModule;
import com.pineconeindustries.server.net.packets.scheduler.PacketScheduler;
import com.pineconeindustries.server.net.packets.scheduler.SectorPacketScheduler;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.players.PacketListener;
import com.pineconeindustries.server.net.players.PacketParser;
import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.server.net.players.PlayerConnectionListener;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.gameobjects.PlayerMP;
import com.pineconeindustries.shared.components.gameobjects.Projectile;
import com.pineconeindustries.shared.components.structures.Station;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.log.Log;

public class Sector {

	ArrayBlockingQueue<Structure> structures;
	ArrayBlockingQueue<PlayerConnection> players;

	private int port, globalX, globalY;
	private String name;
	private boolean update = false, render = false;

	PlayerConnectionListener connListener;
	PacketListener packetListener;
	PacketWriter packetWriter;
	PacketParser packetParser;
	SectorPacketScheduler scheduler;

	public Sector(int port, int globalX, int globalY, String name) {
		this.port = port;
		this.globalX = globalX;
		this.globalY = globalY;
		this.name = name;
		connListener = new PlayerConnectionListener(this);
		packetListener = new PacketListener(this);
		packetWriter = new PacketWriter(this);
		packetParser = new PacketParser(this);
		scheduler = new SectorPacketScheduler(this);
		structures = new ArrayBlockingQueue<Structure>(64);
		players = new ArrayBlockingQueue<PlayerConnection>(64);
		registerScheduledFunctions();
		scheduler.start();

		Log.serverLog("Sector configured on port " + port);

	}

	public void registerScheduledFunctions() {

		scheduler.registerPacket(ScheduleModule.makeStructureListScheduler(this, 8.0f));

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
			for (Projectile p : structure.getProjectiles()) {
				p.render(b);
			}

			for (PlayerConnection conn : structure.getPlayers()) {
				conn.getPlayerMP().render(b);
			}

			for (NPC npc : structure.getNPCs()) {
				npc.render(b);
			}

		}

	}

	public void debugRender(ShapeRenderer debugRenderer) {
		for (Structure structure : structures) {
			structure.debugRender(debugRenderer);

			for (PlayerConnection conn : structure.getPlayers()) {
				conn.getPlayerMP().debugRender(debugRenderer);
			}
			for (NPC npc : structure.getNPCs()) {
				npc.debugRender(debugRenderer);
			}

		}

	}

	public void update() {
		if (!update) {
			return;
		}

		for (Structure s : structures) {

			for (NPC npc : s.getNPCs())
				npc.update();
			for (Projectile projectile : s.getProjectiles())
				projectile.update();
			for (PlayerConnection player : s.getPlayers()) {
				if (player.isVerified())
					player.getPlayerMP().update();
			}

			if (!s.getNPCMoveList().isEmpty()) {
				packetWriter.sendNPCMoves(s);
				s.getNPCMoveList().clear();
			}
			if (!s.getProjectileMoveList().isEmpty()) {
				packetWriter.sendProjectileMoves(s);
				s.getProjectileMoveList().clear();
			}

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
		loadStructures();
		loadNPCs();

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

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	public void addPlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] added to Sector " + port + " to structure "
				+ player.getPlayerMP().getStructureID());
		getStructureByID(player.getPlayerMP().getStructureID()).addPlayer(player);
		players.add(player);
		Galaxy.getInstance().addPlayerToGlobal(player);

	}

	public void removePlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] removed from Sector " + port);
		Database.getInstance().getPlayerDAO().savePlayer(player.getPlayerMP());
		players.remove(player);
		getStructureByID(player.getPlayerMP().getStructureID()).removePlayer(player);
		Galaxy.getInstance().removePlayerFromGlobal(player);
	}

	public void loadStructures() {
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

	public void loadNPCs() {

		for (Structure s : structures) {

			for (NPC n : Database.getInstance().getNPCDAO().loadNPCsbyStructureID(s.getStructureID())) {
				s.addNPC(n);
			}

		}

	}

	public int getPort() {
		return port;
	}

	public PlayerConnection getPlayerConnectionByID(int id) {

		PlayerConnection playerConnection = null;

		for (Structure s : structures) {

			for (PlayerConnection c : s.getPlayers()) {
				if (c.getPlayerMP().getID() == id)
					playerConnection = c;
			}

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

		for (Structure s : structures) {
			for (NPC n : s.getNPCs()) {
				if (n.getID() == id)
					npc = n;
			}
		}
		return npc;
	}

	public PlayerMP getPlayerByID(int id) {

		PlayerMP player = null;
		for (Structure s : structures) {
			for (PlayerConnection c : s.getPlayers()) {
				if (c.getPlayerMP().getID() == id)
					player = c.getPlayerMP();
			}
		}
		return player;
	}

	public ArrayList<PlayerConnection> getPlayerConnectionsInRange(int structureID, int layer, Vector2 origin,
			float distance) {

		ArrayList<PlayerConnection> connections = new ArrayList<PlayerConnection>();

		Structure s = getStructureByID(structureID);

		for (PlayerConnection c : s.getPlayers()) {

			if (c.getPlayerMP().getStructureID() == structureID && c.getPlayerMP().getLayer() == layer
					&& origin.dst(c.getPlayerMP().getLoc()) < distance) {
				connections.add(c);
			}

		}

		return connections;

	}

	public ArrayList<NPC> getNPCsInRange(int structureID, int layer, Vector2 origin, float distance) {

		ArrayList<NPC> n = new ArrayList<NPC>();
		Structure s = getStructureByID(structureID);

		for (NPC npc : s.getNPCs()) {

			if (npc.getLayer() == layer && origin.dst(npc.getLoc()) < distance) {
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

	@Override
	public String toString() {
		return "Sector : " + name + " [" + globalX + ", " + globalY + "]  on Port: " + port;
	}

}
