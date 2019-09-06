package com.pineconeindustries.server.net.packets.modules;

import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.Structure;

public class ScheduleModule {

	public static CustomTCPPacket makePlayerListScheduler(Sector sector) {

		CustomTCPPacket playerListPacket = new CustomTCPPacket(Packets.PLAYER_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (PlayerConnection conn : sector.getPlayers()) {
					String xLoc = Integer.toString((int) conn.getPlayerMP().getLoc().x);
					String yLoc = Integer.toString((int) conn.getPlayerMP().getLoc().y);
					sb.append(conn.getPlayerID() + "#" + conn.getPlayerMP().getName() + "#"
							+ conn.getPlayerMP().getFactionID() + "#" + conn.getPlayerMP().getSectorID() + "#"
							+ conn.getPlayerMP().getStructureID() + "#" + xLoc + "#" + yLoc + "#"
							+ conn.getPlayerMP().getLayer() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};

		return playerListPacket;

	}

	public static CustomTCPPacket makeNPCListScheduler(Sector sector) {

		CustomTCPPacket npcListPacket = new CustomTCPPacket(Packets.NPC_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (NPC npc : sector.getNPCs()) {
					String xLoc = Integer.toString((int) npc.getLoc().x);
					String yLoc = Integer.toString((int) npc.getLoc().y);
					sb.append(npc.getID() + "#" + npc.getName() + "#" + npc.getFactionID() + "#" + npc.getSectorID()
							+ "#" + npc.getStructureID() + "#" + xLoc + "#" + yLoc + "#" + npc.getLayer() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};

		return npcListPacket;

	}

	public static CustomTCPPacket makeStructureListScheduler(Sector sector) {

		CustomTCPPacket structureListPacket = new CustomTCPPacket(Packets.STRUCTURE_LIST_PACKET, "TEST DATA") {
			@Override
			public void update(Sector sector) {

				StringBuilder sb = new StringBuilder();

				for (Structure s : sector.getStructures()) {

					sb.append(s.getStructureID() + "#" + s.getStructureName() + "#" + sector.getPort() + "#"
							+ s.getFactionID() + "#" + s.getLayers() + "#" + s.getRenderX() + "#" + s.getRenderY() + "#"
							+ s.getType() + "#" + s.getStructureState() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = "";
				}

			}
		};
		return structureListPacket;
	}

}
