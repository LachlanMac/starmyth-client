package com.pineconeindustries.client.networking.packets;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.gameobjects.PlayerMP;
import com.pineconeindustries.shared.components.gameobjects.Projectile;
import com.pineconeindustries.shared.components.structures.Ship;
import com.pineconeindustries.shared.components.structures.Station;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.components.structures.Structure.STRUCTURE_STATE;
import com.pineconeindustries.shared.utils.VectorMath;

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

			for (String npcData : split) {

				if (npcData.trim().length() <= 10) {
					continue;
				}

				try {

					String npcSplit[] = npcData.trim().split("x");
					int id = Integer.parseInt(npcSplit[0]);
					float x = Float.parseFloat(npcSplit[1]);
					float y = Float.parseFloat(npcSplit[2]);
					String dir = npcSplit[3];
					int layer = Integer.parseInt(npcSplit[4]);
					NPC n = LogicController.getInstance().getSector().getNPCByID(id);
					if (n != null) {
						n.setVelocity(10);
						n.setLastDirectionFaced(VectorMath.getDirectionByString(dir));
						n.setLoc(new Vector2(x, y));
						n.setFramesSinceLastMove(0);
						n.setLayer(layer);
					}
				} catch (Exception e) {
					System.out.println("EXCEPTION DATA: " + npcData + "    " + e.getMessage());
				}
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

						LogicController.getInstance().getSector().addPlayer(new PlayerMP(playerID, name,
								new Vector2(xLoc, yLoc), sectorID, structureID, layer, factionID));

					}
				}

			}

			LogicController.getInstance().getSector().cleanPlayerList(ids);

			break;

		case Packets.CHAT_SAY_PACKET:
			System.out.println(packetID + "  " + packetData);
			String playerName = split[0];
			String messageData = split[1];

			UserInterface.getInstance().getChat().addMsg(playerName + " : " + messageData);

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
							new NPC(npcID, name, new Vector2(xLoc, yLoc), sectorID, structureID, layer, factionID));
				}

			}

			LogicController.getInstance().getSector().cleanNPCList(npcIDs);

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
				STRUCTURE_STATE state = Structure.getStateByString(structureData[8]);

				if (!LogicController.getInstance().getSector().structureExists(structureID)) {

					if (type == 1) {

						LogicController.getInstance().getSector().addStructure(new Station(structureName, structureID,
								sectorID, factionID, xLoc, yLoc, 0, 0, layers, state));

					}

					if (type == 2) {

						LogicController.getInstance().getSector().addStructure(new Ship(structureName, structureID,
								sectorID, factionID, xLoc, yLoc, 0, 0, layers, state));
					}

				} else {
					LogicController.getInstance().getSector().getStructureByID(structureID).setState(state);
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

		case Packets.PROJECTILE_MOVE_PACKET:
			rxProjectileMovePacket(split);
			break;
		case Packets.SHIP_HIT_PACKET:
			rxShipHitPacket(split);
			break;
		case Packets.SHIP_START_PACKET:
			rxShipStartPacket(split);
			break;
		case Packets.SHIP_STOP_PACKET:
			rxShipStopPacket(split);
			break;
		case Packets.SHIP_EMERGENCY_PACKET:
			rxShipEmergencyPacket(split);
			break;
		default:
			System.out.println("UNKNOWN PACKET " + packetData);
		}

	}

	public static void rxProjectileMovePacket(String[] data) {
		for (String projectileData : data) {

			if (projectileData.trim().length() <= 10) {
				continue;
			}

			try {

				String npcSplit[] = projectileData.trim().split("x");
				int id = Integer.parseInt(npcSplit[0]);
				float x = Float.parseFloat(npcSplit[1]);
				float y = Float.parseFloat(npcSplit[2]);
				String name = new String("proj" + id);
				int layer = Integer.parseInt(npcSplit[3]);
				float dirX = Float.parseFloat(npcSplit[4]);
				float dirY = Float.parseFloat(npcSplit[5]);
				int structureID = Integer.parseInt(npcSplit[6]);
				Projectile p = LogicController.getInstance().getSector().getProjectileByID(id);
				if (p != null) {
					p.setLoc(new Vector2(x, y));
					p.setDirection(new Vector2(dirX, dirY));
					p.setLayer(layer);
				} else {

					LogicController.getInstance().getSector()
							.addProjectile(new Projectile(name, new Vector2(x, y), new Vector2(dirX, dirY).nor(), layer,
									id, 200, LogicController.getInstance().getSector().getPort(), structureID, 100,
									null, null));
				}

			} catch (Exception e) {
				System.out.println("EXCEPTION DATA: " + projectileData + ":");
				e.printStackTrace();
			}
		}

	}

	public static void rxShipHitPacket(String[] data) {
		int structureID = Integer.parseInt(data[0]);
		float strength = Float.parseFloat(data[1]);
		int tileX = Integer.parseInt(data[2]);
		int tileY = Integer.parseInt(data[3]);
		int layer = Integer.parseInt(data[4]);

		LogicController.getInstance().getSector().getStructureByID(structureID).registerHitEvent(strength, tileX, tileY,
				layer);
	}

	public static void rxShipStartPacket(String[] data) {
		int structureID = Integer.parseInt(data[0]);
		float strength = Float.parseFloat(data[1]);

		LogicController.getInstance().getSector().getStructureByID(structureID).registerShipStartEvent(strength);
	}

	public static void rxShipStopPacket(String[] data) {
		int structureID = Integer.parseInt(data[0]);
		float strength = Float.parseFloat(data[1]);

		LogicController.getInstance().getSector().getStructureByID(structureID).registerShipStopEvent(strength);
	}

	public static void rxShipEmergencyPacket(String[] data) {
		int structureID = Integer.parseInt(data[0]);
		boolean val = (Integer.parseInt(data[1]) == 1);
		LogicController.getInstance().getSector().getStructureByID(structureID).registerShipEmergencyEvent(val);
	}
}
