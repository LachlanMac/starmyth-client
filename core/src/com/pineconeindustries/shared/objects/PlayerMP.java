package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.modules.MoveModule;
import com.pineconeindustries.shared.actions.Action;
import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.stats.Stats;
import com.pineconeindustries.shared.utils.VectorMath;

public class PlayerMP extends Person {

	int dcCount = 0;
	boolean setToDisconnect = false;
	private boolean spin = false;
	private boolean inputChanged = false;
	private boolean[] inputState = new boolean[10];

	private GameObject target = null;

	Sector serverSector;
	Action a;
	private Stats stats;

	public PlayerMP(String name, Vector2 loc, int factionID, int structureID, int playerID, int sectorID, int layer) {
		super(name, loc, factionID, structureID, playerID, sectorID, layer);
		speed = Units.PLAYER_MOVE_SPEED;

		if (Global.isServer()) {
			serverSector = Galaxy.getInstance().getSectorByID(sectorID);
			a = ActionManager.getInstance().getActionByID(1);
		}

	}

	public boolean[] getInputState() {
		return inputState;
	}

	public void setInputState(boolean[] inputState) {
		this.inputState = inputState;
		inputChanged = true;
	}

	public boolean isSetToDisconnect() {
		return setToDisconnect;
	}

	@Override
	public void render(SpriteBatch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);

		if (velocity == 999 || spin == true) {
			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1,
					3f + (state * Units.SPIN_SPEED), false);

		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}
		if (Global.isClient()) {
			if (LogicController.getInstance().getPlayer().isPlayerTarget(this)) {
				b.draw(GameData.getInstance().Assets().getTargetAnimation().getKeyFrame(state * 50, true), renderLoc.x,
						renderLoc.y);

			}
		}

		if (getFramesSinceLastMove() > 5) {
			if (velocity != 999)
				velocity = 0;
		}

	}

	public void setLocation(Vector2 loc) {
		this.loc = loc;
		this.renderLoc = loc;

	}

	@Override
	public void update() {

		dcCount++;
		if (dcCount > 600) {
			setToDisconnect = true;
		}
		if (Global.isClient()) {
			onClick();
		}

		if (Global.isServer()) {
			a.update(Gdx.graphics.getDeltaTime());
			Vector2 dir = VectorMath.getDirectionByInput(inputState).nor();

			float x = dir.x;
			float y = dir.y;

			if (inputState[5]) {
				if (a.isReady()) {
					a.castDirect(this, target);
				}
			}
			
			if (x == 0 && y == 0) {
				return;
			}

			Vector2 adjustedMov = new Vector2(x * Units.PLAYER_MOVE_SPEED, y * Units.PLAYER_MOVE_SPEED);
			float velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;
			setLastDirectionFaced(new Vector2(adjustedMov.x, adjustedMov.y));

			Vector2 attemptedMov = new Vector2(loc);
			attemptedMov.add(new Vector2(adjustedMov.x, adjustedMov.y));

			// check for collision

			boolean canMove = true;
			for (Tile tile : getBorderTiles()) {
				if (getProposedBounds(attemptedMov).overlaps(tile.getBounds()) && tile.isCollidable())
					canMove = false;
			}
			if (canMove) {
				setLoc(loc.add(new Vector2(adjustedMov.x, adjustedMov.y)));
			}
			serverSector.getPacketWriter()
					.queueToAll(MoveModule.getMovePacket(getID(), getLoc(), dir, velocity, getLayer()));
			inputChanged = false;
		}

	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

	public void refresh() {
		dcCount = 0;
	}

	@Override
	public String getType() {
		return "p";
	}

}
