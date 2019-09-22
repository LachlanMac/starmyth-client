package com.pineconeindustries.server.net.players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.data.Global;

public class PacketListener extends Thread {

	private DatagramSocket socket;
	private Sector sector;
	private boolean isRunning = false, shutdown = false;
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

					sector.getPacketParser().parsePacket(p);

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				try {
					Thread.sleep(Global.THREAD_WAIT_TIME);
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
			return null;
		}
		return socket;
	}

}
