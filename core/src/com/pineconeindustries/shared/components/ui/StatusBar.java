package com.pineconeindustries.shared.components.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.data.Assets;

public class StatusBar {
	private final float Y_OFFSET = 64;
	TextureRegion r = Assets.getInstance().getHealthBarRed();
	TextureRegion g = Assets.getInstance().getHealthBarGreen();
	TextureRegion f = Assets.getInstance().getHealthBarFrame();
	TextureRegion e = Assets.getInstance().getEnergyBarYellow();
	TextureRegion be = Assets.getInstance().getEnergyBarBlack();
	private int barWidth;

	float x, y, hpPercentage, energyPercentage;

	public StatusBar(float x, float y) {
		this.x = x;
		this.y = y;
		this.hpPercentage = 1;
		energyPercentage = 1;
		barWidth = r.getRegionWidth();
	}

	public void render(SpriteBatch b) {
		b.draw(r, x, y);
		b.draw(g, x, y, (hpPercentage * g.getRegionWidth()), (float) g.getRegionHeight());

		b.draw(be, x, y);
		b.draw(e, x, y, (energyPercentage * g.getRegionWidth()), (float) g.getRegionHeight());
		b.draw(f, x, y);

	}

	public void update(float x, float y, float hpPercentage, float energyPercentage) {
		this.x = x + ((GameObject.GAMEOBJECT_WIDTH - barWidth) / 2);
		this.y = y + Y_OFFSET;
		this.hpPercentage = hpPercentage;
		this.energyPercentage = energyPercentage;
	}

}
