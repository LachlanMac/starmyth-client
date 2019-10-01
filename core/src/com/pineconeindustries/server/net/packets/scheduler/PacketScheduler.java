package com.pineconeindustries.server.net.packets.scheduler;

import java.util.ArrayList;

import com.pineconeindustries.client.networking.packets.custom.CustomPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomUDPPacket;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;

public class PacketScheduler extends Thread {

	Sector sector;
	ArrayList<CustomPacket> packets;

	protected float interval = (float) Global.PACKET_SCHEDULER_TIMER / (float) 1000;

	public PacketScheduler(Sector sector) {
		this.sector = sector;
		packets = new ArrayList<CustomPacket>();
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(Global.PACKET_SCHEDULER_TIMER);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			for (CustomPacket p : packets) {

				p.send(interval, sector);
				if (!p.getPacket().getData().equals(Packet.empty)) {
					sector.getPacketWriter().queueToAll(p.getPacket());
				}
			}

		}
	}

	public void registerPacket(CustomTCPPacket packet) {
		packets.add(packet);
	}

	public void registerPacket(CustomUDPPacket packet) {
		packets.add(packet);
	}

}
