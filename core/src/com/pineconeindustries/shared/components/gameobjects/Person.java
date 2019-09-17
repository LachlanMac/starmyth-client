package com.pineconeindustries.shared.components.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;

public abstract class Person extends Entity {

	protected float state = 0f;
	private boolean tickrender = false;
	private boolean spin = false;

	public Person(int id, String name, Vector2 loc, int sectorID, int structureID, int layer, int factionID) {
		super(id, name, loc, sectorID, structureID, layer, factionID);

		if (!GameData.getInstance().isHeadless()) {
			setAnimations();
		}
	}

	public void setAnimations() {
		animSet = GameData.getInstance().Assets().getPlayerAnimations();
		currentFrame = animSet.getAnimation(new Vector2(0, 1), 0);
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

	public void setSpin(boolean spin) {
		this.spin = spin;
	}

	public boolean getSpin() {
		return this.spin;
	}

}
