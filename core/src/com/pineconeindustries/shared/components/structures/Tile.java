package com.pineconeindustries.shared.components.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;

public class Tile {

	public boolean RENDER = true;

	private float state = 0;
	public static final char HALL = 'a';
	public static final char FOYER = 'b';
	public static final char ENGINEERING = 'c';

	public static final char ZONE_CLOSED_E = 'd';
	public static final char ZONE_CLOSED_W = 'e';
	public static final char ZONE_CLOSED_N = 'f';
	public static final char ZONE_CLOSED_S = 'g';

	public static final char HANGAR = 'h';
	public static final char BARRACK = 'i';
	public static final char GYM = 'j';
	public static final char PRACTICE_RANGE = 'k';
	// public static final char ZONE_OPEN_N = 'j';
	// public static final char ZONE_OPEN_S = 'k';

	public static final char THRUSTER = 'l';
	public static final char DOOR_CLOSED_EW = 'm';

	public static final char DOOR_CLOSED_NS = 'o';
	public static final char OFFICE = 'n';
	public static final char WALL = 'q';
	public static final char WALL_DIAG_NE = 'r';
	public static final char WALL_DIAG_NW = 's';
	public static final char WALL_DIAG_SE = 't';
	public static final char WALL_DIAG_SW = 'u';
	public static final char SHOP = 'v';
	public static final char ROOM = 'w';
	public static final char MEDICAL = 'x';

	public static final char BRIDGE = 'z';

	public static final float TILE_SIZE = 128;

	public boolean isBlocked() {
		boolean blocked = true;
		switch (id) {

		case DOOR_CLOSED_EW:
			blocked = false;
			break;
		case DOOR_CLOSED_NS:
			blocked = false;
			break;
		case ENGINEERING:
			blocked = false;
			break;
		case FOYER:
			blocked = false;
			break;
		case HANGAR:
			blocked = false;
			break;
		case OFFICE:
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
		case '8':
			blocked = false;
			break;
		case '7':
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
	private Rectangle bounds;
	private boolean collidable;
	private float multiplierX = renderX * TILE_SIZE * Structure.STRUCTURE_SIZE;
	private float multiplierY = renderY * TILE_SIZE * Structure.STRUCTURE_SIZE;

	public Tile(char id, float xLoc, float yLoc, int renderX, int renderY) {
		this.id = id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.renderX = renderX;
		this.renderY = renderY;
		this.bounds = new Rectangle((xLoc * TILE_SIZE) + multiplierX, (yLoc * TILE_SIZE) + multiplierY, TILE_SIZE,
				TILE_SIZE);
		collidable = isBlocked();

	}

	public void update() {

	}

	public void render(SpriteBatch b) {

		if (id == 'p' || id == '8' || id == '7') {
			// donothing
		} else if (id == 'l') {
			state += Gdx.graphics.getDeltaTime();
			b.draw(GameData.getInstance().Assets().getThursterAnimation().getKeyFrame(state * 20, true),
					(xLoc * TILE_SIZE) + multiplierX, (yLoc * TILE_SIZE) + multiplierY);

		} else {

			b.draw(GameData.getInstance().Assets().getTileID(id), (xLoc * TILE_SIZE) + multiplierX,
					(yLoc * TILE_SIZE) + multiplierY);
		}

	}

	public void debugRender(ShapeRenderer debugRenderer) {
		if (collidable && id != 'p') {
			debugRenderer.setColor(Color.CYAN);
			debugRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
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

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public void setID(char id) {
		this.id = id;
	}

}
