package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.TCPPacket;

public abstract class CustomTCPPacket extends TCPPacket implements CustomPacket {

	public CustomTCPPacket(int packetID, String data) {
		super(packetID, data);
	}

	@Override
	public Packet getPacket() {
		return this;
	}

}
