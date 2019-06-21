package com.pineconeindustries.client.networking.packets;

import com.pineconeindustries.client.manager.LogicController;

public class PacketFactory {

	private static long packetNumber = 0;

	public static UDPPacket makeMoveRequestPacket(float x, float y) {
		String data = Integer.toString(LogicController.getInstance().getPlayer().getPlayerID()) + "=" + x + "=" + y;
		
		packetNumber++;
		return new UDPPacket(Packets.MOVE_REQUEST_PACKET, data, packetNumber);

	}

}
