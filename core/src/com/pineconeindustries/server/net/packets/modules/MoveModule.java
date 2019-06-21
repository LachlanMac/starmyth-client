package com.pineconeindustries.server.net.packets.modules;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.server.data.Sector;
import com.pineconeindustries.shared.log.Log;

public class MoveModule {

	public static void rxMoveRequest(String data, Sector sector) {

		String[] split = data.split("=");
		try {
			int playerID = Integer.parseInt(split[0]);
			float x = Float.parseFloat(split[1]);
			float y = Float.parseFloat(split[2]);

			PlayerMP player = sector.getPlayerByID(playerID);
			if (player == null) {
				return;
			}
			// check for collision

			Vector2 loc = new Vector2(player.getLoc().x + x, player.getLoc().y + y);
			player.setLoc(loc);
			String packetData = new String(player.getPlayerID() + "=" + player.getLoc().x + "=" + player.getLoc().y);

			sector.getPacketWriter().queueToAll(new UDPPacket(Packets.MOVE_PACKET, packetData, UDPPacket.packetCounter()));
			
		} catch (NumberFormatException e) {
			Log.print("INVALID PACKET : " + e.getMessage());
			return;
		}

	}

}
