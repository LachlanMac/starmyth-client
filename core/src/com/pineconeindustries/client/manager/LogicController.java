package com.pineconeindustries.client.manager;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.shared.log.Log;

public class LogicController {

	private static LogicController instance;

	private Sector sector;
	private Connection conn;

	private LogicController() {

	}

	public static LogicController getInstance() {
		if (instance == null) {
			instance = new LogicController();
		}
		return instance;
	}

	public void sendUDP(UDPPacket p) {

		if (conn == null) {

			return;
		}

		conn.sendUDP(p.getRaw());

	}

	public void receiveTCP(String data) {
		try {
			String[] split = data.split(":");
			int packetID = Integer.parseInt(split[0]);
			String packetData = split[1];
			parsePacket(packetID, packetData);
		} catch (NumberFormatException e) {
			Log.print("Invalid TCP Packet");
		}

	}

	public void receiveUDP(String data) {
		try {
			String[] split = data.split(":");
			int packetID = Integer.parseInt(split[0]);
			long packetNumber = Long.parseLong(split[1]);
			String packetData = split[2];
			parsePacket(packetID, packetData);
		} catch (NumberFormatException e) {
			Log.print("Invalid TCP Packet");
		}

	}

	public void parsePacket(int packetID, String packetData) {

		switch (packetID) {

		case Packets.MOVE_PACKET:

			// TEMP
			String split[] = packetData.split("=");
			try {
				int playerID = Integer.parseInt(split[0]);
				float x = Float.parseFloat(split[1]);
				float y = Float.parseFloat(split[2]);

				sector.getPlayer().setLoc(new Vector2(x, y));
			} catch (NumberFormatException e) {
				
			}

			break;
		default:
		}

	}

	public void registerSector(Sector sector) {
		this.sector = sector;
	}

	public void registerConnection(Connection conn) {
		this.conn = conn;
	}

	public Player getPlayer() {
		return sector.getPlayer();
	}

	public Sector getSector() {
		return sector;
	}

}
