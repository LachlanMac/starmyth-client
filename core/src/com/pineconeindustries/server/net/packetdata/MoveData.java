package com.pineconeindustries.server.net.packetdata;

public class MoveData {

	private String data;
	private int structureID;
	private int layer;

	public MoveData(String data, int structureID, int layer) {
		this.data = data;
		this.structureID = structureID;
		this.layer = layer;
	}

	public String getData() {
		return data;
	}

	public int getStructureID() {
		return structureID;
	}

	public int getLayer() {
		return layer;
	}

}
