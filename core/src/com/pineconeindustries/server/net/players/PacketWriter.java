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
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.shared.log.Log;

public class PacketWriter extends Thread {

	private Sector sector;
	private DatagramSocket socket;
	private boolean isRunning = false;

	public static long packetsSent = 0;

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
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (!udpSendQueue.isEmpty()) {
				try {
					DatagramPacket toSend = udpSendQueue.poll();
					socket.send(toSend);
					packetsSent++;

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

	public void sendNPCMoves(ArrayBlockingQueue<String> moves) {
		StringBuilder sb = new StringBuilder();
		int counter = 0;

		for (String move : moves) {

			sb.append(move);

			if (counter >= 10) {
				queueToAll(new UDPPacket(Packets.NPC_MOVE_PACKET, sb.toString(), UDPPacket.packetCounter()));
				counter = 0;
				sb = new StringBuilder();
			}

		}
		if (sb.toString().equals(""))
			return;
		queueToAll(new UDPPacket(Packets.NPC_MOVE_PACKET, sb.toString(), UDPPacket.packetCounter()));

	}

	public void sendProjectileMoves(ArrayBlockingQueue<String> moves) {

		StringBuilder sb = new StringBuilder();
		int counter = 0;

		for (String move : moves) {

			sb.append(move);

			if (counter >= 10) {
				queueToAll(new UDPPacket(Packets.PROJECTILE_MOVE_PACKET, sb.toString(), UDPPacket.packetCounter()));
				counter = 0;
				sb = new StringBuilder();
			}

		}
		if (sb.toString().equals(""))
			return;
		queueToAll(new UDPPacket(Packets.PROJECTILE_MOVE_PACKET, sb.toString(), UDPPacket.packetCounter()));

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
