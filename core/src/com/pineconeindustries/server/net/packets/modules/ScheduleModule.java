package com.pineconeindustries.server.net.packets.modules;

import com.pineconeindustries.client.networking.packets.custom.CustomTCPPacket;
import com.pineconeindustries.client.networking.packets.custom.CustomUDPPacket;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.stats.Stats;
import com.pineconeindustries.shared.utils.Formatter;

public class ScheduleModule {

	public static CustomTCPPacket makePlayerListScheduler(Structure structure, Sector sector, float seconds) {

		CustomTCPPacket playerListPacket = new CustomTCPPacket(Packets.PLAYER_LIST_PACKET, Packet.empty, seconds) {
			@Override
			public void update() {

				StringBuilder sb = new StringBuilder();

				for (PlayerConnection conn : structure.getPlayers()) {
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
					this.data = Packet.empty;
				}

			}
		};

		return playerListPacket;

	}

	public static CustomUDPPacket makeNPCStatListPacket(Structure structure, Sector sector, float seconds) {

		CustomUDPPacket statPacket = new CustomUDPPacket(Packets.NPC_STAT_LIST_PACKET, Packet.empty, 0, seconds) {
			@Override
			public void update() {

				StringBuilder sb = new StringBuilder();

				for (NPC npc : structure.getNPCs()) {
					Stats s = npc.getStats();
					sb.append(npc.getID() + "#" + Formatter.format(s.getCurrentHP()) + "#" + Formatter.format(s.getHp())
							+ "#" + Formatter.format(s.getCurrentEnergy()) + "#" + Formatter.format(s.getEnergy())
							+ "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = Packet.empty;
				}

			}
		};

		return statPacket;

	}

	public static CustomUDPPacket makePlayerStatListPacket(Structure structure, Sector sector, float seconds) {

		CustomUDPPacket statPacket = new CustomUDPPacket(Packets.PLAYER_STAT_LIST_PACKET, Packet.empty, 0, seconds) {
			@Override
			public void update() {

				StringBuilder sb = new StringBuilder();

				for (PlayerConnection p : structure.getPlayers()) {
					Stats s = p.getPlayerMP().getStats();
					sb.append(p.getPlayerMP().getID() + "#" + Formatter.format(s.getCurrentHP()) + "#"
							+ Formatter.format(s.getHp()) + "#" + Formatter.format(s.getCurrentEnergy()) + "#"
							+ Formatter.format(s.getEnergy()) + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = Packet.empty;
				}

			}
		};

		return statPacket;

	}

	public static CustomTCPPacket makeNPCListScheduler(Structure structure, Sector sector, float seconds) {

		CustomTCPPacket npcListPacket = new CustomTCPPacket(Packets.NPC_LIST_PACKET, Packet.empty, seconds) {
			@Override
			public void update() {

				StringBuilder sb = new StringBuilder();

				for (NPC npc : structure.getNPCs()) {
					String xLoc = Integer.toString((int) npc.getLoc().x);
					String yLoc = Integer.toString((int) npc.getLoc().y);
					sb.append(npc.getID() + "#" + npc.getName() + "#" + npc.getFactionID() + "#" + npc.getSectorID()
							+ "#" + npc.getStructureID() + "#" + xLoc + "#" + yLoc + "#" + npc.getLayer() + "=");
				}

				String data = sb.toString();

				if (data.length() > 2) {
					this.data = data.substring(0, data.length() - 1);
				} else {
					this.data = Packet.empty;
				}

			}
		};

		return npcListPacket;

	}

	public static CustomTCPPacket makeStructureListScheduler(Sector sector, float seconds) {

		CustomTCPPacket structureListPacket = new CustomTCPPacket(Packets.STRUCTURE_LIST_PACKET, Packet.empty,
				seconds) {
			@Override
			public void update() {

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
					this.data = Packet.empty;
				}

			}
		};
		return structureListPacket;
	}

}
