package com.pineconeindustries.client.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.Game;

public class Projectile extends GameObject {

	Vector2 direction;
	Vector2 start;

	Texture plasma;
	Sprite pSprite;

	public Projectile(String name, Vector2 loc, Game game, Vector2 direction) {
		super(name, loc, game);
		this.direction = direction;
		this.start = loc;
		plasma = game.Assets().get("textures/plasma.png", Texture.class);
		pSprite = new Sprite(plasma);
	}

	@Override
	public void update() {

		this.loc.x += Gdx.graphics.getDeltaTime() * 1000;

	}

	@Override
	public void render(Batch b) {

		b.draw(pSprite, loc.x, loc.y, 32, 32, plasma.getWidth(), plasma.getHeight(), 1, 1, 3f, false);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
