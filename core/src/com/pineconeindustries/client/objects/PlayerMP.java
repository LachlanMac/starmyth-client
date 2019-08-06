package com.pineconeindustries.client.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.AnimationSet;
import com.pineconeindustries.shared.data.GameData;

public class PlayerMP extends Entity{

	int dcCount = 0;
	boolean setToDisconnect = false;
	private int playerID;
	
	private float lerp = 7f;
	private float state = 0f;

	public PlayerMP(String name, Vector2 loc, GameData game, int factionID, int structureID, int playerID) {
		super(name, loc, game, factionID, structureID);
		this.playerID = playerID;

		setAnimations();

	}
	

	public void setAnimations() {
		animSet = GameData.getInstance().Assets().getPlayerAnimations();
		currentFrame = animSet.getAnimation(new Vector2(0, 1), 0);
	}

	public boolean isSetToDisconnect() {
		return setToDisconnect;
	}

	@Override
	public void render(Batch b) {

		state += Gdx.graphics.getDeltaTime();
		
		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);
		
		if (velocity == 999) {

			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, 32, 32, t.getRegionWidth(),
					t.getRegionHeight(), 1, 1, 3f + (state * 100), false);
			// b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			float lerp = 15f;
			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * lerp * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * lerp * Gdx.graphics.getDeltaTime();
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

	public void lerp() {

		Vector2 position = renderLoc;
		renderLoc.x += (loc.x - position.x) * lerp * Gdx.graphics.getDeltaTime();
		renderLoc.y += (loc.y - position.y) * lerp * Gdx.graphics.getDeltaTime();

	}

	public void refresh() {
		dcCount = 0;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

}
