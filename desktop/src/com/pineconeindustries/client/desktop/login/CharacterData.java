package com.pineconeindustries.client.desktop.login;

import com.pineconeindustries.client.data.LocalPlayerData;

//"charid":"3","faction":"1","layer":"1","model":"ffffeeee00002222","name":"lachlanmac","sector":"7780","structure":"1001","x":"4000.0000","y":"4000.0000"}

public class CharacterData {

	int charid, sector, structure, layer, faction;
	float x, y;
	String model, name;

	public CharacterData(int charid, int faction, int layer, String model, String name, int sector, int structure,
			float x, float y) {
		this.charid = charid;
		this.faction = faction;
		this.layer = layer;
		this.model = model;
		this.name = name;
		this.sector = sector;
		this.structure = structure;
		this.x = x;
		this.y = y;
	}

	public LocalPlayerData getLocalPlayerData() {
		return new LocalPlayerData(charid, charid, model, name, sector, faction, structure, "good", name, x, y, layer);
	}

}
