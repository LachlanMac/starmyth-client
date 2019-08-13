package com.pineconeindustries.client.networking.packets;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;

public class PacketParser {

	public static void parsePacket(int packetID, String packetData) {

		String split[] = packetData.split("=");

		switch (packetID) {

		case Packets.MOVE_PACKET:

			try {
				int playerID = Integer.parseInt(split[0]);
				float x = Float.parseFloat(split[1]);
				float y = Float.parseFloat(split[2]);
				float dirX = Float.parseFloat(split[3]);
				float dirY = Float.parseFloat(split[4]);
				float velocity = Float.parseFloat(split[5]);

				if (Net.isLocalPlayer(playerID)) {

					Net.getLocalPlayer().setVelocity(velocity);
					Net.getLocalPlayer().setLastDirectionFaced(new Vector2(dirX, dirY));
					Net.getLocalPlayer().setLoc(new Vector2(x, y));
					Net.getLocalPlayer().setFramesSinceLastMove(0);

				} else {

					PlayerMP pmp = Net.getPlayerMP(playerID);
					if (pmp != null) {
						pmp.setVelocity(velocity);
						pmp.setLastDirectionFaced(new Vector2(dirX, dirY));
						pmp.setLoc(new Vector2(x, y));
						pmp.setFramesSinceLastMove(0);
					}
				}

			} catch (NumberFormatException e) {

			}

			break;

		case Packets.NPC_MOVE_PACKET:

			try {
				int id = Integer.parseInt(split[0]);
				float x = Float.parseFloat(split[1]);
				float y = Float.parseFloat(split[2]);
				float dirX = Float.parseFloat(split[3]);
				float dirY = Float.parseFloat(split[4]);
				float velocity = Float.parseFloat(split[5]);

				NPC n = LogicController.getInstance().getSector().getNPCByID(id);
				if (n != null) {
					n.setVelocity(velocity);
					n.setLastDirectionFaced(new Vector2(dirX, dirY));
					n.setLoc(new Vector2(x, y));
					n.setFramesSinceLastMove(0);
				}

			} catch (NumberFormatException e) {

			}

			break;

		case Packets.PLAYER_LIST_PACKET:

			ArrayList<Integer> ids = new ArrayList<Integer>();

			for (String data : split) {

				String[] playerData = data.split("#");

				int playerID = Integer.parseInt(playerData[0]);
				String name = playerData[1];
				int factionID = Integer.parseInt(playerData[2]);
				int sectorID = Integer.parseInt(playerData[3]);
				int structureID = Integer.parseInt(playerData[4]);
				float xLoc = Float.parseFloat(playerData[5]);
				float yLoc = Float.parseFloat(playerData[6]);

				if (Net.isLocalPlayer(playerID)) {
					// player is still part of sector list
				} else {
					ids.add(playerID);
					if (LogicController.getInstance().getSector().getPlayerByID(playerID) == null) {

						LogicController.getInstance().getSector().addPlayer(new PlayerMP(name, new Vector2(xLoc, yLoc),
								GameData.getInstance(), factionID, structureID, playerID, sectorID));

					}
				}
			}

			LogicController.getInstance().getSector().cleanPlayerList(ids);

			break;

		case Packets.NPC_LIST_PACKET:

			ArrayList<Integer> npcIDs = new ArrayList<Integer>();

			for (String data : split) {

				String[] npcData = data.split("#");

				int npcID = Integer.parseInt(npcData[0]);
				String name = npcData[1];
				int factionID = Integer.parseInt(npcData[2]);
				int sectorID = Integer.parseInt(npcData[3]);
				int structureID = Integer.parseInt(npcData[4]);
				float xLoc = Float.parseFloat(npcData[5]);
				float yLoc = Float.parseFloat(npcData[6]);

				npcIDs.add(npcID);

				if (!LogicController.getInstance().getSector().npcExists(npcID)) {
					LogicController.getInstance().getSector().addNPC(new NPC(name, new Vector2(xLoc, yLoc),
							GameData.getInstance(), factionID, structureID, npcID, sectorID));
				}

			}

			LogicController.getInstance().getSector().cleanPlayerList(npcIDs);

		default:
		}

	}
}
