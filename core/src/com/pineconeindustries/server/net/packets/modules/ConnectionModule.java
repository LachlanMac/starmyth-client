package com.pineconeindustries.server.net.packets.modules;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.components.gameobjects.PlayerMP;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.units.Units;

public class ConnectionModule {

	public static void rxHeartbeat(String data, Sector sector) {

		String[] split = data.split("=");
		try {
			int playerID = Integer.parseInt(split[0]);

			PlayerConnection conn = sector.getPlayerConnectionByID(playerID);
			if (conn == null) {
				return;
			}

			conn.refresh();

		} catch (NumberFormatException e) {
			Log.netTraffic(e.getMessage(), "Invalid Packet");
			return;
		}

	}

	public static void rxVerifyRequest(PlayerConnection conn, String data, Sector sector) {

		String[] split = data.split("=");

		int playerID = Integer.parseInt(split[0]);
		int socketPort = Integer.parseInt(split[1]);

		conn.setSocketPort(socketPort);
		conn.setPlayerID(playerID);
		conn.loadPlayerMP();
		conn.verify();
		sector.addPlayer(conn);

	}
}
