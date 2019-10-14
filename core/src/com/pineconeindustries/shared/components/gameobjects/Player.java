package com.pineconeindustries.shared.components.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.cameras.CameraController;
import com.pineconeindustries.client.chat.Chatbox;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Net;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.components.structures.Tile;
import com.pineconeindustries.shared.components.ui.StatusBar;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.text.Text;
import com.pineconeindustries.shared.units.Units;

public class Player extends Entity {

	// TEST

	ArrayList<Projectile> projectiles;

	public Entity target = null;
	final float camspeed = 0.1f, icamspeed = 1.0f - camspeed;

	private String lastState;
	private boolean moveDisabled = false;
	private boolean targetChanged = false;
	private StatusBar hb;
	Chatbox chatbox;
	Camera camera;

	GridTile gridTileSelected = null;
	Tile tileSelected = null;
	int gridOrTile = 0;
	boolean buildMode = false;
	Texture gridTexture;
	Texture tileTexture;

	public Player(int id, String name, Vector2 loc, int sectorID, int structureID, int factionID, int layer,
			Camera camera) {
		super(id, name, loc, sectorID, structureID, layer, factionID);
		this.camera = camera;
		projectiles = new ArrayList<Projectile>();
		lastState = InputState.getDefaultState();
		hb = new StatusBar(loc.x, loc.y);
		if (!Global.isHeadlessServer()) {
			textName = new Text(getName(), getCenter(), 64);
			animSet = GameData.getInstance().Assets().getDefaultAnimations();
			currentFrame = animSet.getAnimation(lastDirectionFaced, 0, getAnimationCode());
			gridTexture = GameData.getInstance().Assets().get("textures/selectGridTile.png");
			tileTexture = GameData.getInstance().Assets().get("textures/selectTile.png");

		} else {

			animSet = null;
		}

	}

	@Override
	public void render(SpriteBatch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity, getAnimationCode());

		if (velocity == 999) {
			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1, 3f + (state * 100), false);

		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
			hb.render(b);
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

		if (gridTileSelected != null) {
			Vector2 render = GridTile.getRenderCoordinates(gridTileSelected,
					LogicController.getInstance().getSector().getStructureByID(this.getStructureID()));
			b.draw(gridTexture, render.x, render.y);

		}
		if (tileSelected != null) {
			Vector2 render = tileSelected.getRenderCoordinates();
			b.draw(tileTexture, render.x, render.y);
		}

	}

	public void dispose() {

	}

	public void update() {
		InputState.update();
		buildMode();
		hover();

		hb.update(renderLoc.x, renderLoc.y, stats.getCurrentHP() / stats.getHp(),
				stats.getCurrentEnergy() / stats.getEnergy());

		if (!chatbox.isTyping())
			move();

	}

	public String printVector() {

		return "Pos:[" + loc.x + ", " + loc.y + "]";

	}

	public void buildMode() {

		if (InputState.debugInput[0] == 1) {
			if (buildMode) {
				buildMode = false;
				gridTileSelected = null;
				tileSelected = null;
				System.out.println("Build mode off");
			} else {
				System.out.println("Build mode on");
				buildMode = true;
			}
		}

		if (buildMode) {

			if (InputState.debugInput[1] == 1) {
				if (gridOrTile == 0)
					gridOrTile = 1;
				else
					gridOrTile = 0;
			}

			if (InputState.leftClick()) {
				Vector2 coordinates = InputManager.getMouseCoordinates(getCamera());
				System.out.println(coordinates);
				Structure s = LogicController.getInstance().getSector()
						.getStructureByID(LogicController.getInstance().getPlayer().getStructureID());

				if (gridOrTile == 0) {

					gridTileSelected = s.getLayerByNumber(LogicController.getInstance().getPlayer().getLayer())
							.getGridTileAt(coordinates.x, coordinates.y);

					tileSelected = null;

				} else {
					tileSelected = s.getLayerByNumber(LogicController.getInstance().getPlayer().getLayer())
							.getTileAt(coordinates.x, coordinates.y);
					gridTileSelected = null;
				}

			}
		}
	}

	public void move() {

		if (moveDisabled)
			return;

		String currentState = InputState.getState();

		if (!lastState.equals(currentState) || targetChanged) {

			LogicController.getInstance().sendUDP(PacketFactory.makeInputChangePacket(currentState, target));
			lastState = currentState;
			targetChanged = false;
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

	public boolean isPlayerTarget(GameObject obj) {
		if (obj.equals(target)) {
			return true;
		} else {
			return false;
		}
	}

	public void setTarget(Entity obj) {
		this.target = obj;
		targetChanged = true;
	}

	@Override
	public String getType() {
		return "p";
	}

}
