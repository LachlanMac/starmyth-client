package com.pineconeindustries.server.net.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.modules.ConnectionModule;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.components.gameobjects.PlayerMP;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;

public class PlayerConnection extends Thread {

	private Socket tcpSock;
	private Sector sector;

	private final static int MAX_TIMEOUT = 1000;
	private final static int MAX_FAILURES = 20;

	private long packetCounter = 0;

	private BufferedReader in;
	private PrintWriter out;

	private InetAddress ip;

	private int socketPort = 0;

	private int timeout = 0;
	private int failures = 0;

	private int playerID;

	private boolean connected = false;
	private boolean running = false;
	private boolean verified = false;

	// TEMP
	PlayerMP playerMP;

	public PlayerConnection(Socket tcpSock, Sector sector) {
		this.sector = sector;
		this.tcpSock = tcpSock;
		this.ip = tcpSock.getInetAddress();

		this.playerID = 0;

		running = true;

		start();
	}

	@Override
	public void run() {

		while (running) {

			if (connected) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				timeout++;
				if (timeout >= MAX_TIMEOUT) {
					failures++;
					checkForDisconnect(failures, "Timeout");
				}

				try {
					String incoming;
					while ((incoming = in.readLine()) != null) {

						receiveTCP(new TCPPacket(incoming));
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

	public void refresh() {
		timeout = 0;
	}

	public void checkForDisconnect(int failures, String msg) {

		if (!connected)
			return;
		if (failures >= MAX_FAILURES) {
			Log.connection("Player Disconnected (Reason:" + msg + ")");
			disconnect(true);
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

		playerMP = Database.getInstance().getPlayerDAO().loadPlayerByID(playerID);
		if (playerMP == null) {
			Log.print("ERROR LOADING PLAYER - NO ID!");
		}
	}

	public void rxVerificationPacket(TCPPacket packet) {
		ConnectionModule.rxVerifyRequest(this, packet.getData(), sector);
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

	public void receiveTCP(TCPPacket packet) {

		if (!verified) {
			if (packet.getPacketID() == Packets.VERIFY_PACKET) {
				rxVerificationPacket(packet);
			}
		} else {

			sector.getPacketParser().parseTCPPacket(packet);

		}

	}

	public void connect() {
		try {
			in = new BufferedReader(new InputStreamReader(tcpSock.getInputStream()));
			out = new PrintWriter(tcpSock.getOutputStream());
			connected = true;
		} catch (IOException e) {

			Log.connection("Player could not connect (Reason:" + e.getMessage() + ")");

		}

		connected = true;

	}

	public void disconnect(boolean killConnection) {

		try {

			connected = false;
			in.close();
			out.close();
			if (tcpSock != null)
				tcpSock.close();
			tcpSock = null;
			if (killConnection) {
				running = false;
				Thread.sleep(5000);
				sector.removePlayer(this);
			}
			this.interrupt();
		} catch (IOException | InterruptedException e) {

			e.getMessage();
		}

	}

	public void verify() {
		verified = true;
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

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		;
	}
}
