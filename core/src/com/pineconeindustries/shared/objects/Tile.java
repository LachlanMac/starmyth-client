package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.client.manager.LAssetManager;
import com.pineconeindustries.shared.data.GameData;

public class Tile {

	public static final int TILE_SIZE = 64;

	private int xLoc, yLoc;
	private char id;

	public Tile(char id, int xLoc, int yLoc) {
		this.id = id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}

	public void update() {

	}

	public void render(Batch b) {
		System.out.print(Character.toString(id));

		LAssetManager ref = GameData.getInstance().Assets();
		if (id == 'p') {

		} else {
			b.draw(ref.getTileID(id), xLoc * TILE_SIZE, yLoc * TILE_SIZE);
		}

	}

}
