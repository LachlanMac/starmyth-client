package com.pineconeindustries.client.galaxy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.objects.Tile;

public class TileMap {
	
	private String name, data;
	private int height, width;
	private Tile[][] tiles;
	
	public TileMap() {
		
	}
	
	public void loadTileMap(int height, int width, String data, String checksum) {
		
		tiles = new Tile[width][height];
		
		
	}
	
	public void render(Batch b) {
		
	
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				tiles[x][y].render(b);
			
				
			}
		}
	}
	
	
	public void loadTestTileMap() {
		
		width = 10;
		height = 10;
		tiles = new Tile[width][height];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				tiles[x][y] = new Tile(x, y, 21);
				
			}
		}
		
	}
	
	public Tile[][] getMap(){
		return tiles;
	}
	
	

}
