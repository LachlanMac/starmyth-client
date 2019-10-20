package com.pineconeindustries.shared.components.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.shared.components.behaviors.Targetable;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.shared.data.Assets;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.units.Units;

public class Elevator implements Targetable {

	private Structure structure;
	private int tileX, tileY, id;
	private String wall;

	private float rotation;
	private Rectangle bounds;

	private Vector2 loc;

	private Sprite t;

	public Elevator(int id, int tileX, int tileY, Structure structure, String wall) {
		this.id = id;
		this.tileX = tileX;
		this.tileY = tileY;
		this.structure = structure;
		this.wall = wall;

		loc = structure.getGlobalVector(new Vector2(tileX * Units.TILE_SIZE, tileY * Units.TILE_SIZE));

		this.bounds = new Rectangle(loc.x, loc.y, Units.TILE_SIZE, Units.TILE_SIZE);

		if (!Global.isHeadlessServer()) {

			TextureRegion elevatorText = Assets.getInstance().getElevatorTile();
			t = new Sprite(elevatorText);
			this.rotation = getRotation();
			t.setRotation(rotation);
			t.setPosition(loc.x, loc.y);
		}
	}

	public float getRotation() {

		float r;
		switch (wall) {
		case "North":
			r = 90;
			break;
		case "South":
			r = 180;
			break;
		case "East":
			r = 90;
			break;
		case "West":
			r = 270;
			break;
		default:
			r = 0;
		}

		return r;
	}

	public void update() {

		if (Global.isClient()) {
			onClick();
		}

	}

	public void render(Batch b) {

		t.draw(b);
	}

	public void debugRender(ShapeRenderer debugRenderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick() {
		if (Global.isClient()) {

			if (InputState.leftClick()) {

				if (Intersector.overlaps(new Rectangle(InputManager.mouseX, InputManager.mouseY, 1, 1), this.bounds)) {
					if (LogicController.getInstance().getPlayer().getLoc().dst(loc) < 200) {

						UserInterface.getInstance().adddSelectBox();
					}
				}
			}

		}

	}

	public String getData() {
		return new String(id + "#" + tileX + "#" + tileY + "#" + structure.getStructureID() + "#" + wall);
	}

	@Override
	public void setTarget(boolean target) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTarget() {
		// TODO Auto-generated method stub
		return false;
	}

}
