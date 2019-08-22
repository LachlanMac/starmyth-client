package com.pineconeindustries.server.net.players;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.modules.ConnectionModule;
import com.pineconeindustries.server.net.packets.modules.MoveModule;
import com.pineconeindustries.server.net.packets.modules.StructureModule;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.log.Log;

public class PacketParser {

	Sector sector;

	public PacketParser(Sector sector) {
		this.sector = sector;
	}

	public void parsePacket(Packet packet) {

		if (packet.getType() == Packet.PACKET_TYPE.UDP) {

			parseUDPPacket((UDPPacket) packet);

		} else if (packet.getType() == Packet.PACKET_TYPE.TCP) {

			parseTCPPacket((TCPPacket) packet);

		}

	}

	public void parseUDPPacket(UDPPacket packet) {

		switch (packet.getPacketID()) {

		case Packets.MOVE_REQUEST_PACKET:
			MoveModule.rxMoveRequest(packet.getData(), sector);
			break;
		default:
			Log.netTraffic("Packet ID: " + packet.getPacketID(), "Invalid UDP Packet");

		}

	}

	public void parseTCPPacket(TCPPacket packet) {
		switch (packet.getPacketID()) {

		case Packets.HEART_BEAT_PACKET:
			ConnectionModule.rxHeartbeat(packet.getData(), sector);
			break;
		case Packets.VERIFY_PACKET:
			break;
		case Packets.STRUCTURE_LAYER_REQUEST_PACKET:
			StructureModule.rxStructureLayoutRequest(packet.getData(), sector);
			break;
		case Packets.STRUCTURE_ELEVATOR_REQUEST_PACKET:
			StructureModule.rxStructureElevatorRequest(packet.getData(), sector);
			break;
		default:
			Log.netTraffic("Packet ID: " + packet.getPacketID(), "Invalid TCP Packet");

		}
	}

}
