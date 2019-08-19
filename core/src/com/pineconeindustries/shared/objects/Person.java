package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;

public class Person extends Entity {

	protected float state = 0f;
	private int id, sectorID;
	private boolean tickrender = false;

	public Person(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, int sectorID,
			int layer) {
		super(name, loc, game, factionID, structureID, layer);
		this.id = id;
		this.sectorID = sectorID;
		if (!game.isHeadless()) {
			setAnimations();
		}
	}

	public void setAnimations() {
		animSet = GameData.getInstance().Assets().getPlayerAnimations();
		currentFrame = animSet.getAnimation(new Vector2(0, 1), 0);
	}

	public int getID() {
		return id;
	}

	public void setID(int playerID) {
		this.id = playerID;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public boolean shouldTickRender() {
		return tickrender;
	}

	public void disableTickRender() {
		tickrender = false;
	}

	public void enableTickRender() {

		tickrender = true;
	}

}
