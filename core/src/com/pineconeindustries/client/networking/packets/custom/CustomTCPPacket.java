package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.client.networking.packets.Packet;
import com.pineconeindustries.client.networking.packets.TCPPacket;
import com.pineconeindustries.server.data.Sector;

public abstract class CustomTCPPacket extends TCPPacket implements CustomPacket {

	public CustomTCPPacket(int packetID, String data) {
		super(packetID, data);
	}

	@Override
	public Packet getPacket() {
		return this;
	}

}
