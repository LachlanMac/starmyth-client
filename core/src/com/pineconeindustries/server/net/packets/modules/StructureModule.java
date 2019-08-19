package com.pineconeindustries.server.net.packets.modules;

import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.TCPPacket;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
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
				.sendTCP(new TCPPacket(Packets.STRUCTURE_INFO_RESPONSE_PACKET, rawData));

	}

}
