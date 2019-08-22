package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.gameunits.Units;

public class Elevator implements Clickable {

	private Structure structure;
	private int tileX, tileY, id;
	private String wall;

	private float renderX, renderY, rotation;

	private Vector2 loc;

	private Sprite t;

	public Elevator(int id, int tileX, int tileY, Structure structure, String wall) {
		this.id = id;
		this.tileX = tileX;
		this.tileY = tileY;
		this.structure = structure;
		this.wall = wall;
		if (!Global.isHeadlessServer()) {
			Texture text = GameData.getInstance().Assets().get("textures/elevator.png");
			t = new Sprite(text);
		}

		loc = structure.getGlobalVector(
				new Vector2((tileX * Units.TILE_SIZE) + getXOffset(), (tileY * Units.TILE_SIZE) + getYOffset()));
		this.rotation =

				getRotation();

		t.setRotation(rotation);
		t.setPosition(loc.x, loc.y);

	}

	public int getXOffset() {
		int x;
		switch (wall) {
		case "North":
			x = (Units.TILE_SIZE / 4);
			break;
		case "South":
			x = (Units.TILE_SIZE / 4);
			break;
		case "East":
			x = 0;
			break;
		case "West":
			x = 0;
			break;
		default:
			x = 0;
		}

		return x;

	}

	public int getYOffset() {
		int y;
		switch (wall) {
		case "North":
			y = -(Units.TILE_SIZE / 2);
			break;
		case "South":
			y = +(Units.TILE_SIZE / 2);
			break;
		case "East":
			y = 0;
			break;
		case "West":
			y = 0;
			break;
		default:
			y = 0;
		}

		return y;

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
		// TODO Auto-generated method stub

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

			if (LogicController.getInstance().getPlayer().getLoc().dst(loc) < 100) {
				System.out.println("CLICKO!");
			}

		}

	}

	public String getData() {
		return new String(id + "#" + tileX + "#" + tileY + "#" + structure.getStructureID() + "#" + wall);
	}

}
