package com.pineconeindustries.server.net.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.TCPPacket;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.server.data.Sector;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;

public class PlayerConnection extends Thread {

	private Socket tcpSock;
	private Sector sector;

	private long packetCounter = 0;

	private BufferedReader in;
	private PrintWriter out;

	private InetAddress ip;

	private int socketPort = 0;

	private int failures = 0;
	private final int MAX_FAILURES = 20;

	private int playerID;

	private boolean isConnected = false;
	private boolean isRunning = false;
	private boolean verified = false;

	// TEMP
	PlayerMP playerMP;

	public PlayerConnection(Socket tcpSock, Sector sector) {
		this.sector = sector;
		this.tcpSock = tcpSock;
		this.ip = tcpSock.getInetAddress();

		this.playerID = 0;

		isRunning = true;

		start();
	}

	@Override
	public void run() {

		while (isRunning) {

			if (isConnected) {

				try {
					String incoming;
					while ((incoming = in.readLine()) != null) {

						receiveTCP(incoming);
						failures = 0;
					}

				} catch (IOException e) {
					failures++;
					checkForDisconnect(failures, e.getMessage());
				}

			} else {
				try {

					Thread.sleep(1000);

				} catch (InterruptedException e) {
					failures++;
					checkForDisconnect(failures, e.getMessage());
				}
			}

		}

	}

	public void checkForDisconnect(int failures, String msg) {

		if (failures >= MAX_FAILURES) {
			Log.serverLog("Player Disconnected " + msg);
			disconnect();
		}

	}

	public void sendTCP(TCPPacket packet) {
		if (!verified) {
			return;
		}

		out.println(packet.getRaw());
		out.flush();
	}

	public void loadPlayerMP() {

		playerMP = new PlayerMP("TESTHARD", new Vector2(100, 100), GameData.getInstance(), 0, 0, playerID);

	}

	public void sendUDP(UDPPacket packet) {
		if (!verified) {
			return;
		}

		DatagramPacket temp = packet.getDatagramPacket(ip, socketPort);

		getSector().getPacketWriter().addToSendQueue(temp);

	}

	public void receiveUDP() {
		if (!verified) {
			return;
		}
	}

	public void receiveTCP(String inMsg) {

		if (!verified) {
			verify(inMsg);
		} else {

		}

	}

	public void connect() {
		try {
			in = new BufferedReader(new InputStreamReader(tcpSock.getInputStream()));
			out = new PrintWriter(tcpSock.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}

		isConnected = true;

	}

	public void disconnect() {

		try {

			isConnected = false;
			in.close();
			out.close();
			tcpSock.close();
			tcpSock = null;
			isRunning = false;
			Thread.sleep(10000);
			sector.removePlayer(this);

		} catch (IOException | InterruptedException e) {

			e.printStackTrace();
		}

		isRunning = false;
	}

	public void verify(String inMsg) {

		String[] split = inMsg.split(":");

		if (split[0].equals("V")) {

			playerID = Integer.parseInt(split[2]);
			socketPort = Integer.parseInt(split[3]);
			loadPlayerMP();
			verified = true;
			sector.addPlayer(this);

		}

	}

	public InetAddress getIP() {
		return ip;
	}

	public Long getPacketCounter() {
		packetCounter++;
		return packetCounter;
	}

	public int getConnectedPort() {

		if (socketPort == 0) {
			System.out.println("Socket port not defined");
			return 9999;
		}
		return socketPort;

	}

	public int getPlayerID() {
		return playerID;
	}

	public Sector getSector() {
		return sector;
	}

	public PlayerMP getPlayerMP() {
		return playerMP;
	}

	public boolean isVerified() {
		return verified;
	}

}
