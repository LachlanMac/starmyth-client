package com.pineconeindustries.client.networking.packets.custom;

import com.pineconeindustries.client.networking.packets.Packet;
import com.pineconeindustries.server.data.Sector;

public interface CustomPacket {
	public void update(Sector sector);
	
	public Packet getPacket();
}
