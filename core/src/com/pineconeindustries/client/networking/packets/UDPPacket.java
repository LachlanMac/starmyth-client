package com.pineconeindustries.client.networking.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPPacket extends Packet {

	private static long packetCounter = 0;

	private long packetNumber;

	public UDPPacket(int packetID, String data, long packetNumber) {
		super(packetID, data);
		this.type = PACKET_TYPE.UDP;
		this.packetNumber = packetNumber;

	}

	public UDPPacket(String inMsg) {
		super(inMsg);
		this.type = PACKET_TYPE.UDP;
		// UDP [PacketID : PacketNumber : Data]
		// TCP [PacketID : Data]
		split = inMsg.split(":");

		try {

			packetID = Integer.parseInt(split[0]);
			packetNumber = Long.parseLong(split[1]);
			data = split[2];

		} catch (NumberFormatException e) {
			isValid = false;
		}

	}

	public long getPacketNumber() {
		return packetNumber;
	}

	public byte[] getDataBytes() {
		return data.getBytes();
	}

	@Override
	public String getRaw() {

		return new String(Integer.toString(packetID) + ":" + Long.toString(packetNumber) + ":" + data);
	}

	public DatagramPacket getDatagramPacket(InetAddress destination, int port) {

		byte[] data = getRaw().getBytes();

		return new DatagramPacket(data, data.length, destination, port);

	}

	public static long packetCounter() {
		packetCounter++;
		return packetCounter;
	}

}
