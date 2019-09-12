package com.pineconeindustries.shared.objects;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.modules.MoveModule;
import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.gameunits.Units;
import com.pineconeindustries.shared.utils.VectorMath;

public class Projectile extends GameObject {

	Vector2 direction;
	Vector2 start;

	Texture plasma;
	Sprite pSprite;
	private DecimalFormat df = new DecimalFormat("#.00");
	private float speed;
	private float state = 0;
	private float life;
	private float speedTho = 0;

	private int framesSinceUpdate = 0;
	private float captureX, captureY;
	private Sector serverSector;

	private Vector2 renderLoc;

	private Animation<TextureRegion> currentFrame;

	private Vector2 startingDirection;

	public Projectile(String name, Vector2 loc, Vector2 direction, int layer, int id, float speed, int sectorID,
			float life) {
		super(name, loc, layer, id, sectorID);
		this.direction = direction;
		this.speed = speed;
		this.start = loc;
		this.life = life;
		renderLoc = loc;

		startingDirection = new Vector2(direction);
		if (Global.isServer()) {
			serverSector = Galaxy.getInstance().getSectorByID(sectorID);
		} else {
			currentFrame = GameData.getInstance().Assets().getShot2Animation();
		}
	}

	@Override
	public void update() {
		speedTho = Gdx.graphics.getDeltaTime();
		if (Global.isServer()) {
			life -= speedTho;

			// setLoc(new Vector2(this.getLoc().x + (direction.x *
			// Gdx.graphics.getDeltaTime() * speed),this.getLoc().y + (direction.y *
			// Gdx.graphics.getDeltaTime() * speed)));
			// System.out.println(direction.x + ", " + direction.y);
			// this.loc.x += 2 * speedTho * speed * direction.x;
			// this.loc.y += 2 * speedTho * speed * direction.y;
			Vector2 dirNew = new Vector2(direction.x * speed * speedTho * 2, direction.y * speed * speedTho * 2);
			this.loc.add(dirNew);

			String data = new String(getID() + "x" + df.format(getLoc().x) + "x" + df.format(getLoc().y) + "x" + layer
					+ "x" + df.format(direction.x) + "x" + df.format(direction.y) + "=");
			serverSector.addProjectileMovementData(data);
			if (life <= 0) {
				serverSector.removeProjectile(this);
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
