package com.pineconeindustries.server.net.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.UDPPacket;

public abstract class CustomUDPPacket extends UDPPacket implements CustomPacket {

	private float sendEvery;
	private float elapsedTime = 0f;

	public CustomUDPPacket(int packetID, String data, long packetNumber, float sendEvery) {
		super(packetID, data, packetNumber);
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
