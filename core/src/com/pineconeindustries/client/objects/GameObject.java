package com.pineconeindustries.client.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.Game;

public abstract class GameObject {

	Vector2 loc;
	Game game;
	private String name;

	public GameObject(String name, Vector2 loc, Game game) {
		this.name = name;
		this.loc = loc;
		this.game = game;

	}

	public abstract void update();

	public abstract void render(Batch b);

	public abstract void dispose();

	public Vector2 getLoc() {
		return loc;
	}

	public void setLoc(Vector2 loc) {
		this.loc = loc;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
