package com.pineconeindustries.server.net.players;

import com.pineconeindustries.client.networking.packets.Packet;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.TCPPacket;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.data.Sector;
import com.pineconeindustries.server.net.packets.modules.MoveModule;
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
			Log.print("INVALID UDP PACKET " + packet.getPacketID());

		}

	}

	public void parseTCPPacket(TCPPacket packet) {

	}

}
