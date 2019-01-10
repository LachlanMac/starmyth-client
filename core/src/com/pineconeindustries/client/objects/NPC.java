package com.pineconeindustries.client.objects;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.Game;

public class NPC extends Entity {

	private int id;
	
	public NPC(int id, String name, Vector2 loc, Game game, int factionID, int structureID) {
		super(name, loc, game, factionID, structureID);
		this.id = id;

	}

	public int getID() {
		return id;
	}

	public int getFactionID() {
		return factionID;
	}

	public int getStructureID() {
		return structureID;
	}

}
