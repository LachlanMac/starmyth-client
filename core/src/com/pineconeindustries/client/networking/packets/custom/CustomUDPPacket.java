package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.client.networking.packets.Packet;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.data.Sector;

public abstract class CustomUDPPacket extends UDPPacket implements CustomPacket {

	public CustomUDPPacket(int packetID, String data, long packetNumber) {
		super(packetID, data, packetNumber);

	}

	@Override
	public Packet getPacket() {
		return this;
	}

}
