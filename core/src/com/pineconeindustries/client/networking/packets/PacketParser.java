package com.pineconeindustries.client.networking.packets;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.Elevator;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Station;
import com.pineconeindustries.shared.objects.Structure;

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
				int layer = Integer.parseInt(split[6]);

				if (Net.isLocalPlayer(playerID)) {

					Net.getLocalPlayer().setVelocity(velocity);
					Net.getLocalPlayer().setLastDirectionFaced(new Vector2(dirX, dirY));
					Net.getLocalPlayer().setLoc(new Vector2(x, y));
					Net.getLocalPlayer().setFramesSinceLastMove(0);
					Net.getLocalPlayer().setLayer(layer);

				} else {

					PlayerMP pmp = Net.getPlayerMP(playerID);
					if (pmp != null) {
						pmp.setVelocity(velocity);
						pmp.setLastDirectionFaced(new Vector2(dirX, dirY));
						pmp.setLoc(new Vector2(x, y));
						pmp.setFramesSinceLastMove(0);
						pmp.setLayer(layer);
					}
				}

			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
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
				int layer = Integer.parseInt(split[6].trim());

				NPC n = LogicController.getInstance().getSector().getNPCByID(id);
				if (n != null) {
					n.setVelocity(velocity);
					n.setLastDirectionFaced(new Vector2(dirX, dirY));
					n.setLoc(new Vector2(x, y));
					n.setFramesSinceLastMove(0);
					n.setLayer(layer);
				}

			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
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
				int layer = Integer.parseInt(playerData[7]);

				if (Net.isLocalPlayer(playerID)) {
					// player is still part of sector list
				} else {
					ids.add(playerID);
					if (LogicController.getInstance().getSector().getPlayerByID(playerID) == null) {

						LogicController.getInstance().getSector().addPlayer(new PlayerMP(name, new Vector2(xLoc, yLoc),
								factionID, structureID, playerID, sectorID, layer));

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
				int layer = Integer.parseInt(npcData[7]);
				npcIDs.add(npcID);

				if (!LogicController.getInstance().getSector().npcExists(npcID)) {

					LogicController.getInstance().getSector().addNPC(
							new NPC(name, new Vector2(xLoc, yLoc), factionID, structureID, npcID, sectorID, layer));
				}

			}

			LogicController.getInstance().getSector().cleanPlayerList(npcIDs);

			break;
		case Packets.STRUCTURE_LIST_PACKET:

			ArrayList<Integer> structureIDs = new ArrayList<Integer>();

			for (String data : split) {

				String[] structureData = data.split("#");
				int structureID = Integer.parseInt(structureData[0]);
				String structureName = structureData[1];
				int sectorID = Integer.parseInt(structureData[2]);
				int factionID = Integer.parseInt(structureData[3]);
				int layers = Integer.parseInt(structureData[4]);
				int xLoc = Integer.parseInt(structureData[5]);
				int yLoc = Integer.parseInt(structureData[6]);
				int type = Integer.parseInt(structureData[7]);

				if (!LogicController.getInstance().getSector().structureExists(structureID)) {

					if (type == 1) {

						LogicController.getInstance().getSector().addStructure(
								new Station(structureName, structureID, sectorID, factionID, xLoc, yLoc, 0, 0, layers));
					}

				}

			}
			break;
		case Packets.STRUCTURE_ELEVATOR_RESPONSE_PACKET:

			ArrayList<Elevator> elevators = new ArrayList<Elevator>();

			int structID = 0;

			for (String data : split) {

				String elevatorData[] = data.split("#");
				int elevatorID = Integer.parseInt(elevatorData[0]);
				int tileX = Integer.parseInt(elevatorData[1]);
				int tileY = Integer.parseInt(elevatorData[2]);
				int eStructID = Integer.parseInt(elevatorData[3]);
				String wall = elevatorData[4];
				Structure struct = LogicController.getInstance().getSector().getStructureByID(eStructID);
				structID = struct.getStructureID();

				Elevator e = new Elevator(elevatorID, tileX, tileY, struct, wall.trim());
				elevators.add(e);

			}
			LogicController.getInstance().getSector().getStructureByID(structID).loadElevators(elevators);

			break;

		case Packets.STRUCTURE_LAYER_RESPONSE_PACKET:

			int id = Integer.parseInt(split[0]);
			int layer = Integer.parseInt(split[1]);
			String layoutData = split[2];

			LogicController.getInstance().getSector().getStructureByID(id).getLayerByNumber(layer)
					.setLayerData(layoutData);

			break;
		default:
		}

	}
}
