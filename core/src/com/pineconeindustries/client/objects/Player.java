package com.pineconeindustries.client.objects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.chat.Chatbox;
import com.pineconeindustries.client.data.RoomData;
import com.pineconeindustries.client.manager.GameController;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.NetworkLayer;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.shared.data.GameData;

public class Player extends Entity {

	// TEST

	ArrayList<Projectile> projectiles;

	final float camspeed = 0.1f, icamspeed = 1.0f - camspeed;
	BitmapFont font = new BitmapFont();
	private boolean tickrender = false;
	private int playerID;
	private boolean moveDisabled = false;

	private int sectorID = 0;

	NetworkLayer lnet;
	Chatbox chatbox;
	Camera camera;

	RoomData currentRoom;

	private float state = 0f;

	public Player(String name, Vector2 loc, GameData game, int factionID, int structureID, int playerID,
			Camera camera) {
		super(name, loc, game, factionID, structureID);
		this.camera = camera;
		this.playerID = playerID;

		projectiles = new ArrayList<Projectile>();

	}

	@Override
	public void render(Batch b) {
		state += Gdx.graphics.getDeltaTime();
		
		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);
		
		
		if (velocity == 999) {

			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y, 32, 32, t.getRegionWidth(),
					t.getRegionHeight(), 1, 1, 3f + (state * 100), false);
			// b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}

		// b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);

		font.draw(b, printVector(), loc.x, loc.y - 15);

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			float lerp = 15f;
			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * lerp * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * lerp * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}

		if (getFramesSinceLastMove() > 6) {
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

		if ((click = InputManager.mouseDown(InputManager.MOUSE_DOWN)) != null) {
			Vector3 worldCoordinates = camera.unproject(new Vector3(click.x, click.y, 0));

		}

		Vector2 movVector = new Vector2(x, y).nor();

		Vector2 convertedVector = new Vector2((movVector.x * magnitude * 2), (movVector.y * magnitude * 2));

		if (x == 0 && y == 0) {
			// do nothing
		} else {

			LogicController.getInstance()
					.sendUDP(PacketFactory.makeMoveRequestPacket(convertedVector.x * Gdx.graphics.getDeltaTime(), convertedVector.y * Gdx.graphics.getDeltaTime()));
			// lnet.sendMove(playerID, convertedVector);
		}
	}

	public void transport(Vector2 destination) {

		// lnet.sendTransport(destination);

	}

	public void sectorTransport(int sector, Vector2 destination) {

		// lnet.sendSectorTransport(sector, destination);

	}

	public void setLnet(NetworkLayer lnet) {
		this.lnet = lnet;
	}

	public int getPlayerID() {
		return playerID;
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

	public boolean shouldTickRender() {
		return tickrender;
	}

	public void disableTickRender() {
		tickrender = false;
	}

	public void enableTickRender() {

		tickrender = true;
	}

	public Camera getCamera() {
		return camera;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
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
