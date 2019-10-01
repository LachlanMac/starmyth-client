package com.pineconeindustries.server.net.packets.types;

public abstract class Packet {

	public final static String empty = "EMPTY";
	private static long udpCount = 0;
	protected String split[];
	protected int packetID;
	protected String data, raw;
	protected boolean isValid = true;

	protected boolean restrictDistance = false;
	protected boolean restrictLayer = false;
	protected boolean restrictStructure = false;
	protected float restrictedDistance = 0;
	protected int restrictedLayer = 0;
	protected int restrictedStructure = 0;

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

	public void setDistanceRestriction(float restrictedDistance) {
		this.restrictedDistance = restrictedDistance;
		this.restrictDistance = true;
	}

	public void setLayerRestriction(int restrictedLayer) {
		this.restrictedLayer = restrictedLayer;
		this.restrictLayer = true;
	}

	public void setStructureRestriction(int restrictedStructure) {
		this.restrictedStructure = restrictedStructure;
		this.restrictStructure = true;
	}

	public boolean isDistanceRestricted() {
		return restrictDistance;
	}

	public boolean isLayerRestricted() {
		return restrictLayer;
	}

	public boolean isStructureRestricted() {
		return restrictStructure;
	}

	public float getDistanceRestriction() {
		return restrictedDistance;
	}

	public int getLayerRestriction() {
		return restrictedLayer;
	}

	public int getStructureRestriction() {
		return restrictedStructure;
	}

}
