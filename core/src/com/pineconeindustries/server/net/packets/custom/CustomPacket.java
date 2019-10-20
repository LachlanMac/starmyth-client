package com.pineconeindustries.server.net.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;

public interface CustomPacket {


	public Packet getPacket();

	public void send(float delta, Sector sector);

	public void update();
}
