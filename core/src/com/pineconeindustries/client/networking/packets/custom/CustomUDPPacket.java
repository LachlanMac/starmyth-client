package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.UDPPacket;

public abstract class CustomUDPPacket extends UDPPacket implements CustomPacket {

	public CustomUDPPacket(int packetID, String data, long packetNumber) {
		super(packetID, data, packetNumber);

	}

	@Override
	public Packet getPacket() {
		return this;
	}

}
