package com.pineconeindustries.server.net.packets.modules;

import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;

public class StructureEventsModule {

	public static TCPPacket getShipHitPacket(int structureID, float strength, int tileX, int tileY, int layer) {
		String packetData = new String(structureID + "=" + strength + "=" + tileX + "=" + tileY + "=" + layer + "=");

		return new TCPPacket(Packets.SHIP_HIT_PACKET, packetData);
	}

	public static TCPPacket getShipStartPacket(int structureID, float strength) {
		String packetData = new String(structureID + "=" + strength + "=");
		return new TCPPacket(Packets.SHIP_START_PACKET, packetData);
	}

	public static TCPPacket getShipStopPacket(int structureID, float strength) {
		String packetData = new String(structureID + "=" + strength + "=");
		return new TCPPacket(Packets.SHIP_STOP_PACKET, packetData);
	}

	public static TCPPacket getShipEmergencyPacket(int structureID, int emergency) {
		String packetData = new String(structureID + "=" + emergency + "=");
		return new TCPPacket(Packets.SHIP_EMERGENCY_PACKET, packetData);
	}

}
