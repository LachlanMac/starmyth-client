package com.pineconeindustries.shared.gameunits;

public class Units {

	public static final int TILE_SIZE = 128;
	public static final int STRUCTURE_SIZE = 64;
	public static final int ENTITY_WIDTH = 64;
	public static final int ENTITY_HEIGHT = 64;
	public static final float SPIN_SPEED = 100;
	public static final float ENTITY_SPEED = 8;
	public static final float NPC_SPEED = 60;
	
	public static final int REGION_SIZE = TILE_SIZE * STRUCTURE_SIZE;
	public static final float PLAYER_MOVE_SPEED = 8;
	public static final int GRID_SQUARES_PER_TILE = 4;
	public static final int GRID_INTERVAL = TILE_SIZE / GRID_SQUARES_PER_TILE;
	public static final int GRID_SIZE = GRID_SQUARES_PER_TILE * STRUCTURE_SIZE;
	public static final float NPC_LERP = 15f;
	public static final float PLAYER_LERP = 15f;

}
