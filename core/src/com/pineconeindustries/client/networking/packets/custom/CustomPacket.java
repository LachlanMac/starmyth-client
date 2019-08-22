package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;

public interface CustomPacket {
	public void update(Sector sector);
	
	public Packet getPacket();
}
