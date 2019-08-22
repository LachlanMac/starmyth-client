package com.pineconeindustries.server.galaxy;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.net.packets.scheduler.PacketScheduler;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.players.PacketListener;
import com.pineconeindustries.server.net.players.PacketParser;
import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.server.net.players.PlayerConnectionListener;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Station;
import com.pineconeindustries.shared.objects.Structure;

public class Sector {

	ArrayBlockingQueue<PlayerConnection> players;
	ArrayBlockingQueue<NPC> npcs;
	ArrayBlockingQueue<Structure> structures;

	private int port, globalX, globalY;
	private String name;
	private boolean update = false, render = false;

	PlayerConnectionListener connListener;
	PacketListener packetListener;
	PacketWriter packetWriter;
	PacketParser packetParser;

	PacketScheduler scheduler;

	// Class to send all players UDP packets and TCP packets

	public Sector(int port, int globalX, int globalY, String name) {
		this.port = port;
		this.globalX = globalX;
		this.globalY = globalY;
		this.name = name;
		players = new ArrayBlockingQueue<PlayerConnection>(64);
		npcs = new ArrayBlockingQueue<NPC>(128);
		structures = new ArrayBlockingQueue<Structure>(16);
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

		CustomTCPPacket playerList = new CustomTCPPacket(Packets.PLAYER_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (PlayerConnection conn : players) {
					String xLoc = Integer.toString((int) conn.getPlayerMP().getLoc().x);
					String yLoc = Integer.toString((int) conn.getPlayerMP().getLoc().y);
					sb.append(conn.getPlayerID() + "#" + conn.getPlayerMP().getName() + "#"
							+ conn.getPlayerMP().getFactionID() + "#" + conn.getPlayerMP().getSectorID() + "#"
							+ conn.getPlayerMP().getStructureID() + "#" + xLoc + "#" + yLoc + "#"
							+ conn.getPlayerMP().getLayer() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};

		CustomTCPPacket npcList = new CustomTCPPacket(Packets.NPC_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (NPC npc : npcs) {
					String xLoc = Integer.toString((int) npc.getLoc().x);
					String yLoc = Integer.toString((int) npc.getLoc().y);
					sb.append(npc.getID() + "#" + npc.getName() + "#" + npc.getFactionID() + "#" + npc.getSectorID()
							+ "#" + npc.getStructureID() + "#" + xLoc + "#" + yLoc + "#" + npc.getLayer() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};

		// structures.add(new Station(name, structureID, id, factionID, localX, localY,
		// globalX, globalY, 64, 64, layers));
		CustomTCPPacket structureList = new CustomTCPPacket(Packets.STRUCTURE_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (Structure s : structures) {

					sb.append(s.getStructureID() + "#" + s.getStructureName() + "#" + sector.getPort() + "#"
							+ s.getFactionID() + "#" + s.getLayers() + "#" + s.getRenderX() + "#" + s.getRenderY() + "#"
							+ s.getType() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};

		scheduler.registerPacket(playerList);
		scheduler.registerPacket(npcList);
		scheduler.registerPacket(structureList);

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

		for (PlayerConnection conn : players) {
			if (conn.isVerified()) {
				conn.getPlayerMP().update();
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

	public PacketListener getPacketListener() {
		return packetListener;
	}

	public PacketWriter getPacketWriter() {
		return packetWriter;
	}

	public ArrayBlockingQueue<NPC> getNPCs() {
		return npcs;
	}

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	public PlayerConnection getPlayerConnectionByID(int id) {

		PlayerConnection playerConnection = null;

		for (PlayerConnection c : players) {
			if (c.getPlayerMP().getID() == id)
				playerConnection = c;
		}

		return playerConnection;
	}

	public PlayerMP getPlayerByID(int id) {

		PlayerMP player = null;

		for (PlayerConnection c : players) {
			if (c.getPlayerMP().getID() == id)
				player = c.getPlayerMP();
		}

		return player;
	}

	public PacketParser getPacketParser() {
		return packetParser;
	}

}
