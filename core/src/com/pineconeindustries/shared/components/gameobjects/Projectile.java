package com.pineconeindustries.shared.components.gameobjects;

import java.text.DecimalFormat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.manager.SoundEffectManager;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.components.gameobjects.GameObject.type;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.components.structures.Tile;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.units.Units;
import com.pineconeindustries.shared.utils.PseudoRandom;
import com.pineconeindustries.shared.utils.VectorMath;

public class Projectile extends GameObject {

	private Vector2 direction;
	private DecimalFormat df = new DecimalFormat("#.00");
	private float speed;
	private float state = 0;
	private float life;
	private float speedTho = 0;
	private int framesSinceUpdate = 0;
	private Sector serverSector;
	private Structure structure;
	private Vector2 renderLoc;
	private Animation<TextureRegion> currentFrame;
	private ActionBase action;
	private boolean setToBounce = false;
	private boolean setToRemove = false;
	private Vector2 bounceDir = new Vector2(0, 0);
	private DataPackage data;

	public Projectile(String name, Vector2 loc, Vector2 direction, int layer, int id, float speed, int sectorID,
			int structureID, float life, ActionBase action, DataPackage data) {
		super(id, name, loc, sectorID, structureID, layer);
		this.action = action;
		this.direction = direction;
		this.speed = speed;
		this.life = life;
		renderLoc = loc;
		this.data = data;

		if (Global.isServer()) {
			if (!Global.isHeadlessServer()) {
				currentFrame = GameData.getInstance().Assets().getShot2Animation();
			}
			goType = type.PROJECTILE;
			serverSector = Galaxy.getInstance().getSectorByID(sectorID);
			structure = serverSector.getStructureByID(structureID);
		} else {

			float distance = loc.dst(LogicController.getInstance().getPlayer().getLoc());
			float distancePercent = distance / 600;
			float volume = 1 - distancePercent;
			int i = PseudoRandom.getInt(1, 4);
			
			if (i == 1) {
				SoundEffectManager.getInstance().playSoundEffect(GameData.getInstance().Assets().getSoundEffect("pew1"),
						volume);
			} else if (i == 2) {
				SoundEffectManager.getInstance().playSoundEffect(GameData.getInstance().Assets().getSoundEffect("pew2"),
						volume);
			} else {
				SoundEffectManager.getInstance().playSoundEffect(GameData.getInstance().Assets().getSoundEffect("pew3"),
						volume);
			}

			currentFrame = GameData.getInstance().Assets().getShot2Animation();

		}
	}

	@Override
	public void update() {
		speedTho = Gdx.graphics.getDeltaTime();
		if (Global.isServer()) {

			action.loop(data);

			if (setToRemove) {
				action.onEnd(data);
				structure.removeProjectile(this);
				return;
			}

			if (setToBounce) {
				direction = bounceDir;
				setToBounce = false;
			}
			life -= speedTho;

			Vector2 dirNew = new Vector2(direction.x * speed * speedTho, direction.y * speed * speedTho);

			Vector2 proposedVector = new Vector2(loc.x, loc.y).add(dirNew);

			for (NPC n : Galaxy.getInstance().getSectorByID(sectorID).getNPCsInRange(structureID, layer, proposedVector,
					100)) {

				if (n.getBounds().contains(proposedVector)) {

					if (n.getID() == data.getCaster().getID()) {

					} else if (n.getFactionID() == data.getCaster().getFactionID()) {
						if (data.getCaster().isPlayer()) {
							if (!setToRemove && !n.isDowned()) {

								data.setEntityHit(n);
								n.addToAggroList(data.getCaster());
								action.onHitEntity(data);
								setToRemove = true;
							}
						}

					} else {

						if (!setToRemove && !n.isDowned()) {

							data.setEntityHit(n);
							n.addToAggroList(data.getCaster());
							action.onHitEntity(data);
							setToRemove = true;
						}
					}

				}

			}

			for (PlayerConnection mp : Galaxy.getInstance().getSectorByID(sectorID)
					.getPlayerConnectionsInRange(structureID, layer, proposedVector, 100)) {
				if (mp.getPlayerMP().getBounds().contains(proposedVector)) {
					if (mp.getPlayerMP().equals(data.getCaster())) {

					} else {
						if (!setToRemove) {
							data.setEntityHit(mp.getPlayerMP());
							action.onHitEntity(data);
							setToRemove = true;
						}
					}

				}

			}

			for (Tile tile : getBorderTiles()) {

				if (tile.isCollidable()) {
					Vector2 collide = VectorMath.getIntersectionPoint(tile.getBounds(), this.loc, proposedVector);
					if (collide.x != 0 || collide.y != 0) {

						if (!setToRemove) {

							setToRemove = true;
							this.loc = collide;
						}
						/*
						 * String dirString = VectorMath.getDirectionLetter(collide.x, collide.y); if
						 * (dirString.equals("n") || dirString.equals("s")) {
						 * 
						 * bounceDir = new Vector2(direction.x, -direction.y); setToBounce = true;
						 * 
						 * } else {
						 * 
						 * bounceDir = new Vector2(-direction.x, direction.y); setToBounce = true;
						 * 
						 * }
						 * 
						 * this.loc = collide;
						 */

					}
				}
			}

			if (!setToBounce) {
				this.loc = proposedVector;
			}

			String data = new String(getID() + "x" + df.format(getLoc().x) + "x" + df.format(getLoc().y) + "x" + layer
					+ "x" + df.format(direction.x) + "x" + df.format(direction.y) + "x" + structureID + "=");
			structure.addProjectileMovementData(data);
			if (life <= 0) {

				setToRemove = true;

				return;
			}
		}
	}

	@Override
	public void render(SpriteBatch b) {
		framesSinceUpdate++;
		state += Gdx.graphics.getDeltaTime();

		TextureRegion t = currentFrame.getKeyFrame(state, true);
		b.draw(t, renderLoc.x, renderLoc.y, t.getRegionWidth() / 2, t.getRegionHeight() / 2, t.getRegionWidth(),
				t.getRegionHeight(), 1, 1, direction.angle() + 90f, false);

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();

		}

		if (Global.isClient()) {
			if (framesSinceUpdate >= 6) {
				LogicController.getInstance().getSector().removeProjectile(this);
			}
		}

	}

	@Override
	public void setLoc(Vector2 loc) {

		framesSinceUpdate = 0;

		this.loc = loc;
		updateBounds();

	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		return "b";
	}

}
