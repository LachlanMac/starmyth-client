package com.pineconeindustries.server.net.packets.modules;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.GameObject;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Tile;
import com.pineconeindustries.shared.utils.VectorMath;

public class MoveModule {

	public static void rxInputChangePacket(String data, Sector sector) {

		String[] split = data.split("=");
		int playerID = Integer.parseInt(split[0]);
		String state = split[1];
		int targetID = Integer.parseInt(split[2]);
		String targetType = split[3];

		PlayerMP player = sector.getPlayerByID(playerID);
		if (player == null) {
			return;
		}

		GameObject obj = sector.getGameObectByTypeAndID(targetType, targetID);
		player.setTarget(obj);

		boolean inputState[] = new boolean[10];

		if (state.charAt(0) == '1') {
			inputState[0] = true;
		}
		if (state.charAt(1) == '1') {
			inputState[1] = true;
		}
		if (state.charAt(2) == '1') {
			inputState[2] = true;
		}
		if (state.charAt(3) == '1') {
			inputState[3] = true;
		}
		if (state.charAt(4) == '1') {
			inputState[4] = true;
		}
		if (state.charAt(5) == '1') {
			inputState[5] = true;
		}
		if (state.charAt(6) == '1') {
			inputState[6] = true;
		}
		if (state.charAt(7) == '1') {
			inputState[7] = true;
		}
		if (state.charAt(8) == '1') {
			inputState[8] = true;
		}
		if (state.charAt(9) == '1') {
			inputState[9] = true;
		}
		
		// if input stte changed....
		player.setInputState(inputState);

	}

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

	public static UDPPacket ds(int playerID, Vector2 playerLoc, Vector2 movement, float velocity,
			int layer, int structureID) {
		String packetData = new String(playerID + "=" + playerLoc.x + "=" + playerLoc.y + "=" + movement.x + "="
				+ movement.y + "=" + velocity + "=" + layer + "=" + structureID + "=");

		return new UDPPacket(Packets.MOVE_PACKET, packetData, UDPPacket.packetCounter());

	}

	public static UDPPacket getMovePacket(int playerID, Vector2 playerLoc, Vector2 movement, float velocity,
			int layer) {
		String packetData = new String(playerID + "=" + playerLoc.x + "=" + playerLoc.y + "=" + movement.x + "="
				+ movement.y + "=" + velocity + "=" + layer + "=");

		return new UDPPacket(Packets.MOVE_PACKET, packetData, UDPPacket.packetCounter());

	}

}
