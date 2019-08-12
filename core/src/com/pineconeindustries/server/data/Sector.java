package com.pineconeindustries.server.data;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.objects.NPC;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.net.players.PacketListener;
import com.pineconeindustries.server.net.players.PacketParser;
import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.server.net.players.PlayerConnectionListener;
import com.pineconeindustries.shared.log.Log;

public class Sector {

	ArrayBlockingQueue<PlayerConnection> players;
	ArrayBlockingQueue<NPC> npcs;

	private int port;
	private boolean update = false, render = false;

	PlayerConnectionListener connListener;
	PacketListener packetListener;
	PacketWriter packetWriter;
	PacketParser packetParser;

	DataScheduler scheduler;

	// Class to send all players UDP packets and TCP packets

	public Sector(int port) {
		this.port = port;
		players = new ArrayBlockingQueue<PlayerConnection>(64);
		npcs = new ArrayBlockingQueue<NPC>(128);
		connListener = new PlayerConnectionListener(this);
		packetListener = new PacketListener(this);
		packetWriter = new PacketWriter(this);
		packetParser = new PacketParser(this);

		addNPCs();

		scheduler = new DataScheduler(200, this);
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
							+ conn.getPlayerMP().getStructureID() + "#" + xLoc + "#" + yLoc + "=");
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
							+ "#" + npc.getStructureID() + "#" + xLoc + "#" + yLoc + "=");
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

	}

	public void updateAndRender(boolean val) {
		update = val;
		render = val;
	}

	public void render(Batch b) {

		if (!render) {
			return;
		}

		for (PlayerConnection conn : players) {

			conn.getPlayerMP().render(b);

		}
	}

	public void update() {
		if (!update) {
			return;
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

	public void addNPCs() {

		ArrayList<NPC> tempList = Database.getInstance().getNPCDAO().getDefaultNPCs(port);

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
