package com.pineconeindustries.shared.components.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.units.Units;

public class GridTile {

	private boolean blocked = true;
	private int x, y;

	public GridTile(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public GridTile(int x, int y, boolean blocked) {
		this.x = x;
		this.y = y;
		this.blocked = blocked;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public PathNode getPathNode() {
		return new PathNode(x, y);
	}

	public void render(SpriteBatch b) {

	}

	public static Vector2 getRenderCoordinates(GridTile g, Structure s) {
		System.out.println("DD " + g.getX());
		System.out.println("EE " + s.getRenderX());
		float x = g.getX() * Units.GRID_INTERVAL + (s.getRenderX() * (Units.STRUCTURE_SIZE * Units.TILE_SIZE));
		float y = g.getY() * Units.GRID_INTERVAL + (s.getRenderY() * (Units.STRUCTURE_SIZE * Units.TILE_SIZE));

		return new Vector2(x, y);
	}

}
