package com.pineconeindustries.shared.objects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.chat.Chatbox;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.gameunits.Units;

public class Player extends Person {

	// TEST

	ArrayList<Projectile> projectiles;

	final float camspeed = 0.1f, icamspeed = 1.0f - camspeed;
	BitmapFont font = new BitmapFont();

	private boolean moveDisabled = false;

	Chatbox chatbox;
	Camera camera;

	public Player(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, int sectorID,
			Camera camera, int layer) {
		super(name, loc, game, factionID, structureID, id, sectorID, layer);
		this.camera = camera;
		projectiles = new ArrayList<Projectile>();

	}

	@Override
	public void render(Batch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);

		if (velocity == 999) {

			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1, 3f + (state * 100), false);

		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}
		font.draw(b, printVector(), loc.x, loc.y - 15);

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

		for (Projectile project : projectiles) {
			project.render(b);

		}

	}

	@Override
	public void dispose() {
		font.dispose();
	}

	public void update() {

		hover();

		for (Projectile project : projectiles) {
			project.update();

		}

		if (!chatbox.isTyping())
			move();
	}

	public String printVector() {

		return "Pos:[" + loc.x + ", " + loc.y + "]";

	}

	public void move() {

		if (moveDisabled)
			return;
		Vector2 click;
		float x = 0;
		float y = 0;

		float magnitude = speed;

		if (InputManager.isPressed(InputManager.SPRINT)) {
			magnitude = magnitude * 1.35f;
		}

		if (InputManager.isPressed(InputManager.DOWN)) {
			y--;
		}

		if (InputManager.isPressed(InputManager.UP)) {
			y++;
		}

		if (InputManager.isPressed(InputManager.RIGHT)) {
			x++;
		}

		if (InputManager.isPressed(InputManager.LEFT)) {
			x--;
		}
		if (InputManager.isPressed(InputManager.CENTER_CAMERA)) {

		}

		if (InputManager.isPressed(InputManager.TEST_BUTTON)) {

			Net.getConnection().switchSector(7781);

		}

		if ((click = InputManager.mouseDown(InputManager.MOUSE_DOWN)) != null) {
			Vector3 worldCoordinates = camera.unproject(new Vector3(click.x, click.y, 0));

		}

		Vector2 movVector = new Vector2(x, y).nor();

		Vector2 convertedVector = new Vector2((movVector.x * magnitude * 2), (movVector.y * magnitude * 2));

		if (x == 0 && y == 0) {
			// do nothing
		} else {

			LogicController.getInstance().sendUDP(PacketFactory.makeMoveRequestPacket(
					convertedVector.x * Gdx.graphics.getDeltaTime(), convertedVector.y * Gdx.graphics.getDeltaTime()));
			// lnet.sendMove(playerID, convertedVector);
		}
	}

	public void disableInput() {
		moveDisabled = true;
	}

	public void enableInput() {
		moveDisabled = false;
	}

	public void connectToChat(Chatbox chatbox) {
		this.chatbox = chatbox;
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean changedStructure() {

		int tempSectorX = (int) this.loc.x / 8192;
		int tempSectorY = (int) this.loc.y / 8192;

		if (tempSectorX != sectorX || tempSectorY != sectorY) {
			return true;
		} else {
			return false;
		}

	}

}
