package com.pineconeindustries.server.net.packets.types;

public class TCPPacket extends Packet {

	public TCPPacket(int packetID, String data) {
		super(packetID, data);
		this.type = PACKET_TYPE.TCP;

	}

	public TCPPacket(String inMsg) {
		super(inMsg);
		this.type = PACKET_TYPE.TCP;

		split = inMsg.split(":");

		try {

			packetID = Integer.parseInt(split[0]);
			data = split[1];

		} catch (NumberFormatException e) {
			isValid = false;
		}

	}

	@Override
	public String getRaw() {

		return new String(Integer.toString(packetID) + ":" + data);
	}

}
