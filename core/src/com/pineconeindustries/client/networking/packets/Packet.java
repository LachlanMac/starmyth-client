package com.pineconeindustries.client.networking.packets;

public abstract class Packet {

	private static long udpCount = 0;

	protected String split[];

	protected int packetID;
	protected String data, raw;

	protected boolean isValid = true;

	protected PACKET_TYPE type;

	public enum PACKET_TYPE {
		UDP, TCP;
	}

	public Packet(int packetID, String data) {

		this.packetID = packetID;
		this.data = data;

	}

	public Packet(String inMsg) {

		this.raw = inMsg;

	}

	public abstract String getRaw();

	public PACKET_TYPE getType() {
		return type;
	}

	public boolean isValid() {
		return isValid;
	}

	public int getPacketID() {
		return packetID;
	}

	public String getData() {
		return data;
	}

}
