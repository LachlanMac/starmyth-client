package com.pineconeindustries.client.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pineconeindustries.client.cameras.CameraController;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.networking.packets.PacketParser;
import com.pineconeindustries.client.networking.packets.PacketRequester;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.components.gameobjects.Player;
import com.pineconeindustries.shared.log.Log;

public class LogicController {

	private static LogicController instance;

	private Sector sector;
	private Connection conn;
	private CameraController cam;
	private OrthographicCamera playerCamera, fixedCamera;

	ExecutorService requestPool;

	private LogicController() {

		requestPool = Executors.newFixedThreadPool(3);

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
			PacketParser.parsePacket(packetID, packetData);
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
			PacketParser.parsePacket(packetID, packetData);
		} catch (Exception e) {
			Log.print("Invalid UDP Packet " + data + "     " + e.getMessage());
		}

	}

	public void registerSector(Sector sector) {
		this.sector = sector;
	}

	public void registerConnection(Connection conn) {
		this.conn = conn;

	}

	public Connection getConnection() {
		return conn;
	}

	public void registerCamera(CameraController cam) {
		this.cam = cam;
		this.fixedCamera = cam.getFixedCamera();
		this.playerCamera = cam.getPlayerCamera();
	}

	public CameraController getCameraController() {
		return cam;
	}

	public OrthographicCamera getPlayerCamera() {
		return playerCamera;
	}

	public OrthographicCamera getFixedCamera() {
		return fixedCamera;
	}

	public Player getPlayer() {
		return sector.getPlayer();
	}

	public Sector getSector() {
		return sector;
	}

	public void addToRequestPool(PacketRequester request) {
		requestPool.execute(request);
	}

}
