package com.pineconeindustries.server.net.packets.modules;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.PlayerMP;

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

			Vector2 adjustedMov = new Vector2(x * Units.PLAYER_MOVE_SPEED, y * Units.PLAYER_MOVE_SPEED);
			float velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;

			Vector2 loc = player.getLoc().add(adjustedMov);

			player.setLoc(loc);
			player.setLastDirectionFaced(new Vector2(adjustedMov.x, adjustedMov.y));
			player.setVelocity(velocity);

			String packetData = new String(player.getID() + "=" + player.getLoc().x + "=" + player.getLoc().y + "="
					+ adjustedMov.x + "=" + adjustedMov.y + "=" + velocity + "=" + player.getLayer() + "=");

			sector.getPacketWriter()
					.queueToAll(new UDPPacket(Packets.MOVE_PACKET, packetData, UDPPacket.packetCounter()));

		} catch (NumberFormatException e) {
			Log.netTraffic(e.getMessage(), "Invalid Packet");
			return;
		}

	}

}
