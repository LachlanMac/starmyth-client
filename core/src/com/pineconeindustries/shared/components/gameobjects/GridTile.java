package com.pineconeindustries.shared.components.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.server.ai.pathfinding.PathNode;

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
}
