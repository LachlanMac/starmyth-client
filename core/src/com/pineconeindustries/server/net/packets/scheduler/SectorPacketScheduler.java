package com.pineconeindustries.server.net.packets.scheduler;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.custom.CustomPacket;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.shared.data.Global;

public class SectorPacketScheduler extends PacketScheduler {

	public SectorPacketScheduler(Sector sector) {
		super(sector);
		// TODO Auto-generated constructor stub
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

}
