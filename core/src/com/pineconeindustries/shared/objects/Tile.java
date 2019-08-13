package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.shared.data.GameData;

public class Tile {

	private final static int TILE_SIZE = 64;
	private int xLoc, yLoc, tileID;
	
	private Texture texture;
	
	
	public Tile(int xLoc, int yLoc, int tileID) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.tileID = tileID;
	}

	
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
	public void renderDebug(ShapeRenderer b) {
		
	}

	
	public void render(Batch b) {
		b.draw(GameData.getInstance().Assets().getStructureTileByID(tileID), xLoc * TILE_SIZE, yLoc * TILE_SIZE);	
	}

	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
