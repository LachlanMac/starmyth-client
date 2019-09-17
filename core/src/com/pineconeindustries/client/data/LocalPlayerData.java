package com.pineconeindustries.client.data;

public class LocalPlayerData {
	private String name, user, model;
	private String status;
	private int charID, id, sector, layer, structure, faction;
	private float x, y;

	public LocalPlayerData(int charID, int id, String model, String name, int sector, int faction, int structure,
			String status, String user, float x, float y, int layer) {
		this.charID = charID;
		this.id = id;
		this.model = model;
		this.name = name;
		this.sector = sector;
		this.status = status;
		this.user = user;
		this.x = x;
		this.y = y;
		this.layer = layer;
		this.structure = structure;
		this.faction = faction;

	}

	public void setFaction(int faction) {
		this.faction = faction;
	}

	public int getFaction() {
		return faction;
	}

	public LocalPlayerData() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCharID() {
		return charID;
	}

	public void setCharID(int charID) {
		this.charID = charID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getSector() {
		return sector;
	}

	public void setSector(int sector) {
		this.sector = sector;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getStructure() {
		return structure;
	}

	public void setStructure(int structure) {
		this.structure = structure;
	}

	public String toString() {

		return "ID:" + charID + " NAME:" + name + "  [" + x + ", " + y + "] SECTOR: " + sector;

	}

	public int getLayer() {
		return layer;
	}

}
