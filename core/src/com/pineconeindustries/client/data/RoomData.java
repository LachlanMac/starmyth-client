package com.pineconeindustries.client.data;

public class RoomData {

	private int startX, startY, roomWidth, roomHeight, id;
	private String name, type;

	public RoomData(int id, int startX, int startY, int roomWidth, int roomHeight, String name, String type) {
		this.id = id;
		this.startX = startX;
		this.startY = startY;
		this.roomWidth = roomWidth;
		this.roomHeight = roomHeight;
		this.name = name;
		this.type = type;
	}

}
