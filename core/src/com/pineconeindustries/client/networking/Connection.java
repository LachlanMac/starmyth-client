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

import com.pineconeindustries.client.Client;
import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.listeners.TCPListener;
import com.pineconeindustries.client.networking.listeners.UDPListener;
import com.pineconeindustries.client.networking.packets.Packet;

public class Connection implements Runnable {

	Socket tcpSocket;
	DatagramSocket udpSocket;
	PrintWriter out;
	BufferedReader in;

	private int udpPort;
	ArrayBlockingQueue<String> inUDPQueue, inTCPQueue;

	private boolean isConnected, isVerified = false;
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

	}

	public void connect() {

		try {
			tcpSocket = new Socket(Client.GAME_SERVER_IP, port);
			udpSocket = new DatagramSocket();

			udpPort = udpSocket.getLocalPort();

			out = new PrintWriter(tcpSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

			udpListener = new UDPListener(udpSocket);
			tcpListener = new TCPListener(in);

			udpListener.startListener(inUDPQueue);
			tcpListener.startListener(inTCPQueue);

			thread.start();

			isConnected = true;


		} catch (Exception e) {

			Log.print("Could not connect to server " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Could not connect to the Game Server", "Connection Error", 0);
			System.exit(0);

		}

	}

	public void sendVerificationPacket() {

		System.out.println("Sending Verification Packet");

		String tempPacket = new String("V:" + 0 + ":"+ Net.getLocalPlayer().getPlayerID() +":" + udpPort);

		sendTCP(tempPacket);

	}

	public void disconnect(String reason) {

		Log.print("Connection to Server Lost " + reason);

		isConnected = false;
		tcpSocket = null;
		thread.interrupt();

	}

	public void sendTCP(String outMsg) {
		out.println(outMsg);
		out.flush();
	}

	public void sendUDP(String outMsg) {

		byte[] out = outMsg.getBytes();
		try {
			DatagramPacket dp = new DatagramPacket(out, out.length, InetAddress.getByName(Client.GAME_SERVER_IP), port);
			udpSocket.send(dp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void run() {

		while (isConnected) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (!inUDPQueue.isEmpty()) {

				LogicController.getInstance().receiveUDP(inUDPQueue.poll());

			}
			while (!inTCPQueue.isEmpty()) {

				LogicController.getInstance().receiveTCP(inTCPQueue.poll());

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
