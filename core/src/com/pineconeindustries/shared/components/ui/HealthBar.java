package com.pineconeindustries.shared.components.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.data.GameData;

public class HealthBar {
	private final float Y_OFFSET = 64;
	TextureRegion r = GameData.getInstance().Assets().getHealthBarRed();
	TextureRegion g = GameData.getInstance().Assets().getHealthBarGreen();
	TextureRegion f = GameData.getInstance().Assets().getHealthBarFrame();

	float x, y, percentage;

	public HealthBar(float x, float y) {
		this.x = x;
		this.y = y;
		this.percentage = 1;
	}

	public void render(SpriteBatch b) {
		b.draw(r, x, y);
		b.draw(g, x, y, (percentage * g.getRegionWidth()), (float) g.getRegionHeight());
		b.draw(f, x, y);
	}

	public void update(float x, float y, float percentage) {
		this.x = x + (GameObject.GAMEOBJECT_WIDTH / 4);
		this.y = y + Y_OFFSET;
		this.percentage = percentage;
	}

}
