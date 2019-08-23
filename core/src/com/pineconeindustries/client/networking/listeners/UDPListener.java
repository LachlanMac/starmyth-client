package com.pineconeindustries.client.networking.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.shared.log.Log;

public class UDPListener extends Thread {

	private ArrayBlockingQueue<String> udpQueue;

	private final int PACKET_SIZE = 512;
	private DatagramSocket socket;
	private boolean isRunning = false;
	private DatagramPacket packet;

	private byte[] packetData;

	public UDPListener(DatagramSocket socket) {
		this.socket = socket;

	}

	@Override
	public void run() {

		while (isRunning) {

			try {
				packetData = new byte[PACKET_SIZE];
				packet = new DatagramPacket(packetData, PACKET_SIZE);
				socket.receive(packet);
				udpQueue.add(new String(packet.getData()));

			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void startListener(ArrayBlockingQueue<String> udpQueue) {
		try {
			socket.setSoTimeout(3000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.udpQueue = udpQueue;
		isRunning = true;
		this.start();
	}

	public void stopListener() {
		isRunning = false;
		udpQueue.clear();

	}
}
