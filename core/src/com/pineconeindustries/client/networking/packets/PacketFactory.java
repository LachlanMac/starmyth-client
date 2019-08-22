package com.pineconeindustries.client.networking.packets;

import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.server.net.packets.types.UDPPacket;

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

	public static TCPPacket makeLayerRequestPacket(int structureID, int sectorID, int layer) {
		String data = new String(
				structureID + "=" + sectorID + "=" + layer + "=" + LogicController.getInstance().getPlayer().getID());
		return new TCPPacket(Packets.STRUCTURE_LAYER_REQUEST_PACKET, data);
	}

	public static TCPPacket makeElevatorRequestPacket(int structureID, int sectorID) {
		System.out.println("REQUESTIN ELEVATOR");
		String data = new String(
				structureID + "=" + sectorID + "=" + LogicController.getInstance().getPlayer().getID());
		return new TCPPacket(Packets.STRUCTURE_ELEVATOR_REQUEST_PACKET, data);
	}

}
