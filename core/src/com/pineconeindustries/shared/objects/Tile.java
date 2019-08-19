package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;

public class Tile {

	public boolean RENDER = true;

	public static final char DOOR_OPEN_EW = 'y';
	public static final char DOOR_CLOSED_EW = 'm';
	public static final char DOOR_OPEN_NS = 'n';
	public static final char DOOR_CLOSED_NS = 'o';
	public static final char ZONE_CLOSED_E = 'd';
	public static final char ZONE_CLOSED_W = 'e';
	public static final char ZONE_CLOSED_N = 'f';
	public static final char ZONE_CLOSED_S = 'g';
	public static final char ZONE_OPEN_E = 'h';
	public static final char ZONE_OPEN_W = 'i';
	public static final char ZONE_OPEN_N = 'j';
	public static final char ZONE_OPEN_S = 'k';
	public static final char ENGINEERING = 'c';
	public static final char FOYER = 'b';
	public static final char HALL = 'a';
	public static final char MEDICAL = 'x';
	public static final char ROOM = 'w';
	public static final char SHOP = 'v';
	public static final char WALL = 'q';
	public static final char WALL_DIAG_SE = 't';
	public static final char WALL_DIAG_SW = 'u';
	public static final char WALL_DIAG_NE = 'r';
	public static final char WALL_DIAG_NW = 's';
	public static final float TILE_SIZE = 128;

	public boolean isBlocked() {
		boolean blocked = true;
		switch (id) {

		case DOOR_OPEN_EW:
			blocked = false;
			break;
		case DOOR_OPEN_NS:
			blocked = false;
			break;
		case DOOR_CLOSED_EW:
			blocked = false;
			break;
		case DOOR_CLOSED_NS:
			blocked = false;
			break;
		case ZONE_OPEN_E:
			blocked = false;
			break;
		case ZONE_OPEN_W:
			blocked = false;
			break;
		case ZONE_OPEN_S:
			blocked = false;
			break;
		case ZONE_OPEN_N:
			blocked = false;
			break;
		case ENGINEERING:
			blocked = false;
			break;
		case FOYER:
			blocked = false;
			break;
		case HALL:
			blocked = false;
			break;
		case MEDICAL:
			blocked = false;
			break;
		case SHOP:
			blocked = false;
			break;
		case ROOM:
			blocked = false;
			break;

		default:
			blocked = true;
		}

		return blocked;

	}

	private float xLoc, yLoc;
	private int renderX, renderY;
	private char id;

	public Tile(char id, float xLoc, float yLoc, int renderX, int renderY) {
		this.id = id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.renderX = renderX;
		this.renderY = renderY;
	}

	public void update() {

	}

	public void render(SpriteBatch b) {

		float multiplierX = renderX * TILE_SIZE * Structure.STRUCTURE_SIZE;
		float multiplierY = renderY * TILE_SIZE * Structure.STRUCTURE_SIZE;

		if (id == 'p') {
			// donothing
		} else {
			b.draw(GameData.getInstance().Assets().getTileID(id), (xLoc * TILE_SIZE) + multiplierX,
					(yLoc * TILE_SIZE) + multiplierY);
		}

	}

	public void dispose() {

	}

	public char getTileID() {
		return id;
	}

	public float getX() {
		return xLoc;
	}

	public float getY() {
		return yLoc;
	}

	public Vector2 getGlobalLoc() {
		float multiplierX = renderX * TILE_SIZE * Structure.STRUCTURE_SIZE;
		float multiplierY = renderY * TILE_SIZE * Structure.STRUCTURE_SIZE;
		return new Vector2(xLoc + multiplierX, yLoc + multiplierY);
	}

}