package com.pineconeindustries.client.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;

import com.pineconeindustries.client.ClientApp;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.listeners.TCPListener;
import com.pineconeindustries.client.networking.listeners.UDPListener;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;

public class Connection implements Runnable {

	Socket tcpSocket;
	DatagramSocket udpSocket;
	PrintWriter out;
	BufferedReader in;

	private int udpPort;
	ArrayBlockingQueue<String> inUDPQueue, inTCPQueue;

	private boolean connected, running = true;
	private int port;

	UDPListener udpListener;
	TCPListener tcpListener;

	Thread thread;

	ArrayBlockingQueue<Packet> inPackets;

	public Connection(int port) {
		this.port = port;
		thread = new Thread(this);
		inUDPQueue = new ArrayBlockingQueue<String>(128);
		inTCPQueue = new ArrayBlockingQueue<String>(128);
	}

	public void switchSector(int newPort) {
		this.port = newPort;
		disconnect("Changing port to " + newPort);
		reconnect();

	}

	public void connect() {

		try {

			tcpSocket = new Socket(Global.GAME_SERVER_IP, port);
			udpSocket = new DatagramSocket();

			udpPort = udpSocket.getLocalPort();

			out = new PrintWriter(tcpSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

			udpListener = new UDPListener(udpSocket);
			tcpListener = new TCPListener(in);

			udpListener.startListener(inUDPQueue);
			tcpListener.startListener(inTCPQueue);

			thread.start();
			connected = true;
			sendVerificationPacket();
		} catch (Exception e) {

			Log.print("Could not connect to server " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Could not connect to the Game Server", "Connection Error", 0);
			System.exit(0);

		}

	}

	public void reconnect() {

		try {

			tcpSocket = new Socket(Global.GAME_SERVER_IP, port);
			udpSocket = new DatagramSocket();

			udpPort = udpSocket.getLocalPort();

			out = new PrintWriter(tcpSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

			udpListener = new UDPListener(udpSocket);
			tcpListener = new TCPListener(in);

			tcpListener.setErrorCode(123);
			udpListener.startListener(inUDPQueue);
			tcpListener.startListener(inTCPQueue);
			connected = true;

			sendVerificationPacket();
		} catch (Exception e) {
			Log.connection("Error Reconnecting " + e.getMessage());
		}
	}

	public void sendVerificationPacket() {

		sendTCP(PacketFactory.makeVerifyRequestPacket(udpPort));

	}

	public void disconnect(String reason) {

		Log.connection("Player Disconnected    Reason : " + reason);

		connected = false;

		udpListener.stopListener();
		tcpListener.stopListener();

		try {
			Thread.sleep(1000);
			tcpSocket.close();
			udpSocket.close();
			out.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		out = null;
		in = null;
		udpListener.interrupt();
		tcpListener.interrupt();
		tcpSocket = null;
		udpSocket = null;
		udpListener = null;
		tcpListener = null;

	}

	public void sendTCP(TCPPacket outPacket) {
		out.println(outPacket.getRaw());
		out.flush();
	}

	public void sendUDP(String outMsg) {

		byte[] out = outMsg.getBytes();
		try {
			DatagramPacket dp = new DatagramPacket(out, out.length, InetAddress.getByName(Global.GAME_SERVER_IP), port);
			udpSocket.send(dp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isConnected() {
		return connected;
	}

	@Override
	public void run() {

		int heartbeatCounter = 0;

		while (running) {

			if (connected) {

				heartbeatCounter += 1;

				while (!inUDPQueue.isEmpty()) {

					LogicController.getInstance().receiveUDP(inUDPQueue.poll());

				}
				while (!inTCPQueue.isEmpty()) {

					LogicController.getInstance().receiveTCP(inTCPQueue.poll());

				}

				if (heartbeatCounter >= 600) {
					heartbeatCounter = 0;
					sendTCP(PacketFactory.makeHeartbeatPacket());
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public static Connection startConnection(int sector) {
		Connection conn = new Connection(sector);

		conn.connect();

		if (conn.isConnected() == false) {
			Log.debug("No Connection");
			return null;
		}

		return conn;
	}

}
