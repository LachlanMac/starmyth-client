package com.pineconeindustries.client.objects;

public class PlayerMPLight {

	private String name;
	private int playerID;

	public PlayerMPLight() {

	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}
	
	public void setPlayerName(String name) {
		this.name = name;
	}

	public String getPlayerName() {
		return name;
	}
}
