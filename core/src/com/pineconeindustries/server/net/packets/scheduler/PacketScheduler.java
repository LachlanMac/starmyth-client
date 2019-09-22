package com.pineconeindustries.server.net.packets.scheduler;

import java.util.ArrayList;

import com.pineconeindustries.client.networking.packets.custom.CustomPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomUDPPacket;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;

public class PacketScheduler extends Thread {

	Sector sector;
	ArrayList<CustomPacket> packets;

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
			try {

				for (CustomPacket p : packets) {
					Thread.sleep(Global.PACKET_SCHEDULER_DELAY);
					p.update(sector);
					sector.getPacketWriter().queueToAll(p.getPacket());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
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
