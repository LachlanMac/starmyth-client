package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.gameunits.Units;

public class PlayerMP extends Person {

	int dcCount = 0;
	boolean setToDisconnect = false;
	private boolean spin = true;

	public PlayerMP(String name, Vector2 loc, int factionID, int structureID, int playerID, int sectorID, int layer) {
		super(name, loc, factionID, structureID, playerID, sectorID, layer);
		speed = Units.PLAYER_MOVE_SPEED;

	}

	public boolean isSetToDisconnect() {
		return setToDisconnect;
	}

	@Override
	public void render(Batch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);

		if (velocity == 999 || spin == true) {

			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1, 3f + (state * 100), false);

		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * Units.PLAYER_LERP * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * Units.PLAYER_LERP * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}

		if (getFramesSinceLastMove() > 20) {
			if (velocity != 999)

				velocity = 0;
		}

	}

	@Override
	public void update() {

		dcCount++;
		if (dcCount > 600) {
			setToDisconnect = true;
		}

	}

	public void refresh() {
		dcCount = 0;
	}

}
