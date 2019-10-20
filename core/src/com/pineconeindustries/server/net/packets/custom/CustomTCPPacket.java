package com.pineconeindustries.server.net.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.TCPPacket;

public abstract class CustomTCPPacket extends TCPPacket implements CustomPacket {

	private float sendEvery;
	private float elapsedTime = 0f;

	public CustomTCPPacket(int packetID, String data, float sendEvery) {
		super(packetID, data);
		this.sendEvery = sendEvery;
	}

	public void send(float delta, Sector sector) {
		elapsedTime += delta;
		if (elapsedTime >= sendEvery) {
			update();
			elapsedTime = 0;
		}

	}

	@Override
	public Packet getPacket() {
		return this;
	}

}
