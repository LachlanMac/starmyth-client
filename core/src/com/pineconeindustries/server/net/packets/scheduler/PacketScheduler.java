package com.pineconeindustries.server.net.packets.scheduler;

import java.util.ArrayList;

import com.pineconeindustries.client.networking.packets.custom.CustomPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomUDPPacket;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.log.Log;

public class PacketScheduler extends Thread {

	int currentIndex = 0;
	int interval = 0;
	Sector sector;

	ArrayList<CustomPacket> packets;

	public PacketScheduler(int interval, Sector sector) {
		this.interval = interval;
		this.sector = sector;
		packets = new ArrayList<CustomPacket>();
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				for (CustomPacket p : packets) {

					Thread.sleep(interval);
					p.update(sector);
					sector.getPacketWriter().queueToAll(p.getPacket());

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
