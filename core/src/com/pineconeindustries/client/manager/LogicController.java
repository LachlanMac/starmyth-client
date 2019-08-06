package com.pineconeindustries.client.manager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;

public class LogicController {

	private static LogicController instance;

	private Sector sector;
	private Connection conn;
	OrthographicCamera camera;
	
	private LogicController() {

	}

	public static LogicController getInstance() {
		if (instance == null) {
			instance = new LogicController();
		}
		return instance;
	}

	public void sendUDP(UDPPacket p) {

		if (conn == null) {

			return;
		}

		conn.sendUDP(p.getRaw());

	}

	public void receiveTCP(String data) {
		try {
			String[] split = data.split(":");
			int packetID = Integer.parseInt(split[0]);
			String packetData = split[1];
			parsePacket(packetID, packetData);
		} catch (NumberFormatException e) {
			Log.print("Invalid TCP Packet " + data);
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.print("Invalid TCP Pacekt :" + data);
		}

	}

	public void receiveUDP(String data) {
		try {
			String[] split = data.split(":");
			int packetID = Integer.parseInt(split[0]);
			long packetNumber = Long.parseLong(split[1]);
			String packetData = split[2];
			parsePacket(packetID, packetData);
		} catch (NumberFormatException e) {
			Log.print("Invalid TCP Packet");
		}

	}

	public void parsePacket(int packetID, String packetData) {

		String split[] = packetData.split("=");

		switch (packetID) {

		case Packets.MOVE_PACKET:

			try {
				int playerID = Integer.parseInt(split[0]);
				float x = Float.parseFloat(split[1]);
				float y = Float.parseFloat(split[2]);
				float dirX = Float.parseFloat(split[3]);
				float dirY = Float.parseFloat(split[4]);
				float velocity = Float.parseFloat(split[5]);

				if (Net.isLocalPlayer(playerID)) {

					Net.getLocalPlayer().setVelocity(velocity);
					Net.getLocalPlayer().setLastDirectionFaced(new Vector2(dirX, dirY));
					Net.getLocalPlayer().setLoc(new Vector2(x, y));
					Net.getLocalPlayer().setFramesSinceLastMove(0);

				} else {

					PlayerMP pmp = Net.getPlayerMP(playerID);
					if (pmp != null) {
						pmp.setVelocity(velocity);
						pmp.setLastDirectionFaced(new Vector2(dirX, dirY));
						pmp.setLoc(new Vector2(x, y));
						pmp.setFramesSinceLastMove(0);
					}
				}

			} catch (NumberFormatException e) {

			}

			break;

		case Packets.PLAYER_LIST_PACKET:

			ArrayList<Integer> ids = new ArrayList<Integer>();

			for (String data : split) {

				String[] playerData = data.split("#");

				int id = Integer.parseInt(playerData[0]);
				String name = playerData[1];

				if (Net.isLocalPlayer(id)) {
					// player is still part of sector list
				} else {
					ids.add(id);
					if (sector.getPlayerByID(id) == null) {

						sector.addPlayer(new PlayerMP(name, new Vector2(0, 0), GameData.getInstance(), 0, 0, id));

					}
				}
			}

			sector.cleanPlayerList(ids);

			break;
		default:
		}

	}

	public void registerSector(Sector sector) {
		this.sector = sector;
	}

	public void registerConnection(Connection conn) {
		this.conn = conn;
		conn.sendVerificationPacket();
	}
	
	public void registerCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}

	public Player getPlayer() {
		return sector.getPlayer();
	}

	public Sector getSector() {
		return sector;
	}

}
