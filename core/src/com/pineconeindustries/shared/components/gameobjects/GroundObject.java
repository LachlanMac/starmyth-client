package com.pineconeindustries.shared.components.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GroundObject extends GameObject {

	// OLD String name, Vector2 loc, int layer, int id, int sectorID, int
	// structureID
	// NEW int id, String name, Vector2 loc, int sectorID, int structureID, int
	// layer
	public GroundObject(String name, Vector2 loc, int layer, int id, int sectorID, int structureID) {
		super(id, name, loc, sectorID, structureID, layer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		return "g";
	}

}
