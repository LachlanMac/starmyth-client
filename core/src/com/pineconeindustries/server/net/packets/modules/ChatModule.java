package com.pineconeindustries.server.net.packets.modules;

import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.server.net.players.PlayerConnection;

public class ChatModule {

	public static void rxChatSayPacket(String data, Sector sector) {

		String[] split = data.split("=");
		int originPlayerID = Integer.parseInt(split[0]);
		PlayerConnection pc = Galaxy.getInstance().getPlayerConnectionByID(originPlayerID);
		String msg = pc.getPlayerMP().getName() + "=" + split[1];

		for (PlayerConnection c : sector.getPlayerConnectionsInRange(pc.getPlayerMP().getStructureID(),
				pc.getPlayerMP().getLayer(), pc.getPlayerMP().getLoc(), 500)) {
			c.sendTCP(new TCPPacket(Packets.CHAT_SAY_PACKET, msg));

		}

	}

	public static void rxChatCommandPacket(String data, Sector sector) {
		System.out.println("GOT CHAT COMMAND : " + data);
		String[] split = data.split("=");
		int originPlayerID = Integer.parseInt(split[0]);
		PlayerConnection pc = Galaxy.getInstance().getPlayerConnectionByID(originPlayerID);

		String[] msg = split[1].split("-");

		switch (msg[0]) {
		case "!shiphit":
			pc.getSector().getPacketWriter()
					.queueToAll(StructureEventsModule.getShipHitPacket(pc.getPlayerMP().getStructureID(), 5, 4, 5, 1));
			break;
		case "!shipstart":
			pc.getSector().getPacketWriter()
					.queueToAll(StructureEventsModule.getShipStartPacket(pc.getPlayerMP().getStructureID(), 5));
			break;
		case "!shipstop":
			pc.getSector().getPacketWriter()
					.queueToAll(StructureEventsModule.getShipStopPacket(pc.getPlayerMP().getStructureID(), 5));
			break;
		default:
			break;

		}

	}

	public static void rxChatTellPacket(String data, Sector sector) {

	}

	public static void rxChatGroupPacket(String data, Sector sector) {

	}

	public static void rxChatIntercomPacket(String data, Sector sector) {

	}

}
