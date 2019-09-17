package com.pineconeindustries.server.net.packets.modules;

import java.util.ArrayList;

import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.log.Log;

public class StructureModule {

	public static void rxStructureLayoutRequest(String data, Sector sector) {

		String split[] = data.split("=");
		int structureID = Integer.parseInt(split[0]);
		int sectorID = Integer.parseInt(split[1]);
		int layer = Integer.parseInt(split[2]);
		int playerID = Integer.parseInt(split[3]);

		String rawData = structureID + "=" + layer + "=" + Galaxy.getInstance().getSectorByID(sectorID)
				.getStructureByID(structureID).getLayerByNumber(layer).getRaw();

		Galaxy.getInstance().getPlayerConnectionByID(playerID)
				.sendTCP(new TCPPacket(Packets.STRUCTURE_LAYER_RESPONSE_PACKET, rawData));

	}

	public static void rxStructureElevatorRequest(String data, Sector sector) {
		String split[] = data.split("=");
		int structureID = Integer.parseInt(split[0]);
		int sectorID = Integer.parseInt(split[1]);
		int playerID = Integer.parseInt(split[2]);

		Structure s = Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID);
		ArrayList<Elevator> elevators = Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID)
				.getElevators();
		
		StringBuilder sb = new StringBuilder();
		for (Elevator e : elevators) {
			sb.append(e.getData());
		}

		String outData = sb.toString();

		outData = outData.substring(0, outData.length() - 1);

		Galaxy.getInstance().getPlayerConnectionByID(playerID)
				.sendTCP(new TCPPacket(Packets.STRUCTURE_ELEVATOR_RESPONSE_PACKET, outData));

	}

}
