package com.pineconeindustries.server.net.players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packet;
import com.pineconeindustries.server.net.packets.types.TCPPacket;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.server.net.packets.types.Packet.PACKET_TYPE;
import com.pineconeindustries.shared.log.Log;

public class PacketWriter extends Thread {

	private Sector sector;
	private DatagramSocket socket;
	private boolean isRunning = false;

	private ArrayBlockingQueue<DatagramPacket> udpSendQueue;
	private ArrayBlockingQueue<TCPPacket> tcpSendQueue;

	public PacketWriter(Sector sector) {
		this.sector = sector;
		udpSendQueue = new ArrayBlockingQueue<DatagramPacket>(1024);
		tcpSendQueue = new ArrayBlockingQueue<TCPPacket>(1024);
	}

	public void registerSocket(DatagramSocket socket) {
		this.socket = socket;
		isRunning = true;
	}

	public void run() {

		while (isRunning) {

			while (!udpSendQueue.isEmpty()) {
				try {
					DatagramPacket toSend = udpSendQueue.poll();
					socket.send(toSend);

				} catch (IOException e) {
					Log.netTraffic("Error sending UDP Packet", "UDP Packet Error");
				}
			}

			while (!tcpSendQueue.isEmpty()) {

				TCPPacket toSend = tcpSendQueue.poll();
				for (PlayerConnection conn : sector.getPlayers()) {
					conn.sendTCP(toSend);
				}
			}
		}
	}

	public void queueToAll(Packet packet) {

		if (packet.getType() == PACKET_TYPE.UDP) {

			for (PlayerConnection conn : sector.getPlayers()) {

				UDPPacket udpPacket = (UDPPacket) packet;

				addToSendQueue(udpPacket.getDatagramPacket(conn.getIP(), conn.getConnectedPort()));
			}
		} else if (packet.getType() == PACKET_TYPE.TCP) {

			tcpSendQueue.add((TCPPacket) packet);
		}
	}

	public void addToSendQueue(DatagramPacket packet) {

		udpSendQueue.add(packet);
	}

	public void stopWriter() {
		isRunning = false;
		udpSendQueue.clear();
	}

}
