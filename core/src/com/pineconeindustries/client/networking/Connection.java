package com.pineconeindustries.client.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.client.Client;
import com.pineconeindustries.client.config.NetworkConfiguration;
import com.pineconeindustries.client.log.Log;

public class Connection implements Runnable {

	Socket socket;
	PrintWriter out;
	BufferedReader in;

	private boolean isConnected;
	private int port;

	Thread thread;
	NetworkLayer lnet;
	ArrayBlockingQueue<Packet> inPackets;

	public Connection(int port) {
		this.port = port;
		thread = new Thread(this);
	}

	public void switchSector(int newPort) {

	}

	public void connect() {

		try {
			socket = new Socket(Client.TEST_IP, port);

			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			thread.start();

			isConnected = true;

			HeartBeat hb = new HeartBeat(this);

			hb.start();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void disconnect(String reason) {

		Log.print("Connection to Server Lost " + reason);

		isConnected = false;
		socket = null;
		thread.interrupt();

	}

	public void send(Packet p) {
		Log.netTraffic(p.getData(), "Sending!");
		out.println(p.encode());
		out.flush();

	}

	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void run() {

		while (isConnected) {

			String data;

			try {
				while ((data = in.readLine()) != null) {
					if (lnet != null) {

						lnet.processPacket(new Packet(data));
					}
				}
			} catch (Exception e) {

				Log.print("Client Connection Error");
				e.printStackTrace();
				disconnect(e.getMessage());
			}

		}
	}

	public void setNetworkLayer(NetworkLayer lnet) {
		this.lnet = lnet;
	}

}

class HeartBeat extends Thread {
	Connection c;

	public HeartBeat(Connection c) {
		this.c = c;
	}

	@Override
	public void run() {

		while (true) {

			try {
				Thread.sleep(1000);
				c.send(new Packet(0, "hb", Packet.HEARTBEAT_PACKET));

			} catch (InterruptedException e) {
				c.disconnect(e.getMessage());
				e.printStackTrace();
			}

		}

	}

}
