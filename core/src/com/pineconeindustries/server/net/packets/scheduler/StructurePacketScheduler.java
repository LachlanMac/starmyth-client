package com.pineconeindustries.server.net.packets.scheduler;

import com.pineconeindustries.client.networking.packets.custom.CustomPacket;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.data.Global;

public class StructurePacketScheduler extends PacketScheduler {

	private Structure structure;

	public StructurePacketScheduler(Sector sector, Structure structure) {
		super(sector);
		this.structure = structure;
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(Global.PACKET_SCHEDULER_TIMER);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			for (CustomPacket p : packets) {

				p.getPacket().setStructureRestriction(structure.getStructureID());

				p.send(interval, sector);
				if (!p.getPacket().getData().equals(Packet.empty)) {

					sector.getPacketWriter().queueToStructure(p.getPacket(), structure);
				}
			}

		}
	}

}
