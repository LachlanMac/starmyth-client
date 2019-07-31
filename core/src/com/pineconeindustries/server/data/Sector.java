package com.pineconeindustries.server.data;

import java.util.concurrent.ArrayBlockingQueue;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.server.net.players.PacketListener;
import com.pineconeindustries.server.net.players.PacketParser;
import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.server.net.players.PlayerConnectionListener;
import com.pineconeindustries.server.tests.TestSender;
import com.pineconeindustries.shared.log.Log;

public class Sector {

	ArrayBlockingQueue<PlayerConnection> players;
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
		connListener = new PlayerConnectionListener(this);
		packetListener = new PacketListener(this);
		packetWriter = new PacketWriter(this);
		packetParser = new PacketParser(this);

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
					sb.append(conn.getPlayerID() + "#" + conn.getPlayerMP().getName() + "#"
							+ conn.getPlayerMP().getFactionID() + "=");
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
		Log.serverLog("Player ID[" + player.getPlayerID() + "] connecting to Sector " + port);
		player.connect();
	}

	public void addPlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] added to Sector " + port);
		players.add(player);
		Galaxy.getInstance().addPlayerToGlobal(player);

	}

	public void removePlayer(PlayerConnection player) {
		Log.serverLog("Player ID[" + player.getPlayerID() + "] removed from Sector " + port);
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

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	public PlayerMP getPlayerByID(int id) {

		PlayerMP player = null;

		for (PlayerConnection c : players) {
			if (c.getPlayerMP().getPlayerID() == id)
				player = c.getPlayerMP();
		}

		return player;
	}

	public PacketParser getPacketParser() {
		return packetParser;
	}

}
