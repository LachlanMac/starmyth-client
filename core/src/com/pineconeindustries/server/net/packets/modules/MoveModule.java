package com.pineconeindustries.server.net.packets.modules;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Tile;

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

			Vector2 adjustedMov = new Vector2(x * Units.PLAYER_MOVE_SPEED, y * Units.PLAYER_MOVE_SPEED);
			float velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;

			Vector2 playerLoc = new Vector2(player.getLoc());
			Vector2 wantedLoc = playerLoc.add(adjustedMov);

			boolean canMove = true;
			for (Tile tile : player.getBorderTiles()) {
				if (player.getProposedBounds(wantedLoc).overlaps(tile.getBounds()) && tile.isCollidable())
					canMove = false;
			}

			if (!canMove) {
				return;
			}
			player.setLastDirectionFaced(new Vector2(adjustedMov.x, adjustedMov.y));
			player.setVelocity(velocity);
			player.setLoc(wantedLoc);
			sector.getPacketWriter().queueToAll(
					getMovePacket(player.getID(), player.getLoc(), adjustedMov, velocity, player.getLayer()));

		} catch (NumberFormatException e) {
			Log.netTraffic(e.getMessage(), "Invalid Packet");
			return;
		}

	}

	public static UDPPacket getMovePacket(int playerID, Vector2 playerLoc, Vector2 movement, float velocity,
			int layer) {
		String packetData = new String(playerID + "=" + playerLoc.x + "=" + playerLoc.y + "=" + movement.x + "="
				+ movement.y + "=" + velocity + "=" + layer + "=");

		return new UDPPacket(Packets.MOVE_PACKET, packetData, UDPPacket.packetCounter());

	}

}
