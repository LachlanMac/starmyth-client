package com.pineconeindustries.server.net.players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.data.Sector;

public class PacketListener extends Thread {

	private DatagramSocket socket;
	private Sector sector;
	private boolean isRunning = false, shutdown = false;
	private static final int THREAD_WAIT_TIME = 1000;
	private static final int PACKET_SIZE = 512;

	private DatagramPacket inPacket;
	private byte[] inData;

	public PacketListener(Sector sector) {
		this.sector = sector;
		startListener();
	}

	@Override
	public void run() {

		while (!shutdown) {

			if (isRunning) {

				inData = new byte[PACKET_SIZE];
				inPacket = new DatagramPacket(inData, inData.length);

				try {
					socket.receive(inPacket);

					UDPPacket p = new UDPPacket(new String(inPacket.getData()));

					// PARSE THE PACKET

					sector.getPacketParser().parsePacket(p);

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				try {
					Thread.sleep(THREAD_WAIT_TIME);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

		}

	}

	public void startListener() {

		stopListener();

		try {
			socket = new DatagramSocket(sector.getPort());
			isRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopListener() {

		if (socket != null) {

			socket.close();
			socket = null;
			isRunning = false;

		}

	}

	public void shutdownListener() {
		stopListener();
		shutdown = true;
	}

	public DatagramSocket getSocket() {

		if (socket == null) {
			System.out.println("WTF");
		}
		return socket;
	}

}
