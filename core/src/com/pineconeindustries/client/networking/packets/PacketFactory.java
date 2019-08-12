package com.pineconeindustries.client.networking.packets;

import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;

public class PacketFactory {

	private static long packetNumber = 0;

	public static UDPPacket makeMoveRequestPacket(float x, float y) {
		String data = Integer.toString(LogicController.getInstance().getPlayer().getID()) + "=" + x + "=" + y;

		packetNumber++;
		return new UDPPacket(Packets.MOVE_REQUEST_PACKET, data, packetNumber);

	}

	public static TCPPacket makeHeartbeatPacket() {
		String data = Integer.toString(LogicController.getInstance().getPlayer().getID());
		return new TCPPacket(Packets.HEART_BEAT_PACKET, data);
	}

	public static TCPPacket makeVerifyRequestPacket(int udpPort) {

		String data = new String(Net.getLocalPlayer().getID() + "=" + udpPort);
		return new TCPPacket(Packets.VERIFY_PACKET, data);

	}

}
