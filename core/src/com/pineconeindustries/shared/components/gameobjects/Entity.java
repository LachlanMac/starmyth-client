package com.pineconeindustries.shared.components.gameobjects;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.actions.ActionSet;
import com.pineconeindustries.shared.actions.effects.EffectOverTime;
import com.pineconeindustries.shared.actions.types.ActionPackage;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.text.Text;
import com.pineconeindustries.shared.units.Units;

public abstract class Entity extends GameObject implements Comparable<Entity> {

	protected final int MAX_FRAMES_SINCE_LAST_MOVE = 6;
	protected float interval, velocity, speed;

	protected int framesSinceLastMove, factionID;

	AnimationSet animSet;
	Animation<TextureRegion> currentFrame;
	Vector2 renderLoc, lastDirectionFaced;

	Text textName;
	ActionSet actionSet;
	Structure structure;

	public Entity(int id, String name, Vector2 loc, int sectorID, int structureID, int layer, int factionID) {
		super(id, name, loc, sectorID, structureID, layer);
		interval = 0;
		velocity = 0;
		framesSinceLastMove = 0;
		speed = Units.ENTITY_SPEED;
		renderLoc = loc;
		lastDirectionFaced = loc;

		this.factionID = factionID;

		if (!Global.isHeadlessServer()) {
			textName = new Text(getName(), getCenter(), 64);
			animSet = GameData.getInstance().Assets().getDefaultAnimations();
			currentFrame = animSet.getAnimation(lastDirectionFaced, 0, getAnimationCode());

		} else {

			animSet = null;
		}

		if (!Global.isClient()) {
			structure = Galaxy.getInstance().getSectorByID(sectorID).getStructureByID(structureID);
		}

	}

	@Override
	public void update() {
		interval += Gdx.graphics.getDeltaTime();
		updateEffects(interval);
		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity, getAnimationCode());
		if (getFramesSinceLastMove() > MAX_FRAMES_SINCE_LAST_MOVE) {
			velocity = 0;
		}
	}

	@Override
	public void render(SpriteBatch b) {
		b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y);
	}

	@Override
	public void onClick() {

		if (InputState.leftClick()) {
			if (Intersector.overlaps(new Rectangle(InputManager.mouseX, InputManager.mouseY, 1, 1), this.bounds)) {
				Log.debug("Object: " + name + "[" + loc.x + "," + loc.y + "]");
				LogicController.getInstance().getPlayer().setTarget(this);
			}
		}
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

	public Text getText() {
		return textName;
	}

	public void updateText() {
		textName.setCenter(getCenter());

	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		debugRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

	}

	public int getAnimationCode() {

		int code = 0;

		if (isDowned()) {
			code = AnimationSet.DOWNED;
		}

		return code;

	}

	public Structure getStructure() {
		return structure;
	}

	public void addDefaultEntityPassives() {
		addEffectOverTime(new EffectOverTime(ActionManager.getInstance().getActionByID(7), new ActionPackage(this)));
	}

}
