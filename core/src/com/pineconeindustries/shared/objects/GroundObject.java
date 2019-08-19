package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;

public class GroundObject extends GameObject {

	public GroundObject(String name, Vector2 loc, GameData game, int layer) {
		super(name, loc, game, layer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Batch b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void onClick() {

	}

	@Override
	public void renderDebug(ShapeRenderer b) {
		b.rect(bounds.x, bounds.y, bounds.width, bounds.height);

	}

}
