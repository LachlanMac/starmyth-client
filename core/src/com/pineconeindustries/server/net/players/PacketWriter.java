package com.pineconeindustries.server.net.players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.data.Sector;

public class PacketWriter extends Thread {

	private Sector sector;
	private DatagramSocket socket;
	private boolean isRunning = false;

	private ArrayBlockingQueue<DatagramPacket> sendQueue;

	public PacketWriter(Sector sector) {
		this.sector = sector;
		sendQueue = new ArrayBlockingQueue<DatagramPacket>(1024);

	}

	public void registerSocket(DatagramSocket socket) {
		this.socket = socket;
		isRunning = true;
	}

	public void run() {

		while (isRunning) {

			while (!sendQueue.isEmpty()) {

				try {

					DatagramPacket toSend = sendQueue.poll();
					socket.send(toSend);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public void queueToAll(UDPPacket packet) {

		for (PlayerConnection conn : sector.getPlayers()) {

			sendQueue.add(packet.getDatagramPacket(conn.getIP(), conn.getConnectedPort()));

		}

	}

	public void queueToPlayer() {

	}

	public void addToSendQueue(DatagramPacket packet) {
		System.out.println("Adding to send queue");

		sendQueue.add(packet);
	}

	public void stopWriter() {
		isRunning = false;
		sendQueue.clear();
	}

}
