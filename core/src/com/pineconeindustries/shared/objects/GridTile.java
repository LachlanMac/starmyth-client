package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.gameunits.Units;

public class GridTile {

	private boolean blocked = true;
	private int x, y;

	private Texture t;

	public GridTile(int x, int y) {
		this.x = x;
		this.y = y;
		t = GameData.getInstance().Assets().get("textures/path.png");
	}

	public GridTile(int x, int y, boolean blocked) {
		this.x = x;
		this.y = y;
		this.blocked = blocked;
		t = GameData.getInstance().Assets().get("textures/path.png");
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
		b.draw(t, x * Units.GRID_INTERVAL, y * Units.GRID_INTERVAL);
	}
}
