package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.gameunits.Units;

public abstract class Entity extends GameObject implements Comparable<Entity> {

	protected final int MAX_FRAMES_SINCE_LAST_MOVE = 6;
	protected float interval, velocity, speed;
	protected int framesSinceLastMove, factionID, structureID;

	AnimationSet animSet;
	Animation<TextureRegion> currentFrame;
	Vector2 renderLoc, lastDirectionFaced;

	public Entity(String name, Vector2 loc, int factionID, int structureID, int layer) {
		super(name, loc, layer);

		interval = 0;
		velocity = 0;
		framesSinceLastMove = 0;
		speed = Units.ENTITY_SPEED;
		renderLoc = loc;
		lastDirectionFaced = loc;
		this.factionID = factionID;
		this.structureID = structureID;

		if (!GameData.getInstance().isHeadless()) {
			animSet = GameData.getInstance().Assets().getDefaultAnimations();
			currentFrame = animSet.getAnimation(lastDirectionFaced, 0);
		} else {
			animSet = null;
		}

	}

	@Override
	public void update() {
		interval += Gdx.graphics.getDeltaTime();
		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);
		if (getFramesSinceLastMove() > MAX_FRAMES_SINCE_LAST_MOVE) {
			velocity = 0;
		}
	}

	@Override
	public void render(Batch b) {
		b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y);
	}

	@Override
	public void dispose() {

	}

	public float getInterval() {
		return interval;
	}

	public void setInterval(float interval) {
		this.interval = interval;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getFramesSinceLastMove() {
		return framesSinceLastMove;
	}

	public void setFramesSinceLastMove(int framesSinceLastMove) {
		this.framesSinceLastMove = framesSinceLastMove;
	}

	public AnimationSet getAnimSet() {
		return animSet;
	}

	public void setAnimSet(AnimationSet animSet) {
		this.animSet = animSet;
	}

	public Animation<TextureRegion> getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(Animation<TextureRegion> currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Vector2 getRenderLoc() {
		return renderLoc;
	}

	public void setRenderLoc(Vector2 renderLoc) {
		this.renderLoc = renderLoc;
	}

	public Vector2 getLastDirectionFaced() {
		return lastDirectionFaced;
	}

	public void setLastDirectionFaced(Vector2 lastDirectionFaced) {
		this.lastDirectionFaced = lastDirectionFaced;
	}

	public int getMAX_FRAMES_SINCE_LAST_MOVE() {
		return MAX_FRAMES_SINCE_LAST_MOVE;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public int getStructureID() {
		return structureID;
	}

	public void setStructureID(int structureID) {
		this.structureID = structureID;
	}

	public Float getRenderOrder() {
		return -loc.y;

	}

	@Override
	public int compareTo(Entity o) {

		return getRenderOrder().compareTo(o.getRenderOrder());

	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		debugRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

	}

}
