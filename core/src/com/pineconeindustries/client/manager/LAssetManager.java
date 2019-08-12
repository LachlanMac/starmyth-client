package com.pineconeindustries.client.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.shared.data.GameData;

public class LAssetManager extends AssetManager {

	AnimationSet playerAnimations;
	Texture shipSS;

	TextureRegion[][] shipTiles;

	TextureRegion wall, wallDiagSW, wallDiagNE, wallDiagNW, wallDiagSE, hall, room, doorClosedEW, doorClosedNS,
			doorOpenEW, doorOpenNS, zoneClosedE, zoneClosedW, zoneOpenE, zoneOpenW;

	public LAssetManager() {

	}

	public void loadTextures() {
		load("textures/galaxybg1.png", Texture.class);
		load("textures/plasma.png", Texture.class);
		load("textures/shiptiles/shipSS.png", Texture.class);
		load("textures/playerfront.png", Texture.class);
		load("textures/shiptiles/diagwall.png", Texture.class);
		load("textures/shiptiles/floor.png", Texture.class);
		load("textures/shiptiles/hall.png", Texture.class);
		load("textures/shiptiles/wall.png", Texture.class);
		load("textures/playerSS.png", Texture.class);

	}

	public void loadShipTiles() {

		shipSS = get("textures/shiptiles/shipSS.png");
		shipTiles = TextureRegion.split(shipSS, shipSS.getWidth() / 4, shipSS.getHeight() / 4);
		wall = shipTiles[0][0];
		wallDiagSE = shipTiles[0][1];
		wallDiagSW = shipTiles[2][0];
		wallDiagNE = shipTiles[2][2];
		wallDiagNW = shipTiles[2][1];
		hall = shipTiles[0][2];

		doorOpenEW = shipTiles[3][2];
		doorClosedEW = shipTiles[3][0];
		doorOpenNS = shipTiles[3][3];
		doorClosedNS = shipTiles[3][1];

		zoneClosedE = shipTiles[1][0];
		zoneOpenE = shipTiles[1][1];
		zoneClosedW = shipTiles[1][3];
		zoneOpenW = shipTiles[1][2];

		room = shipTiles[0][3];
	}

	public TextureRegion getStructureTileByID(int id) {

		switch (id) {

		case 0:
			return null;
		case 1:
			return wall;
		case 2:
			return wallDiagSE;
		case 3:
			return wallDiagSW;
		case 4:
			return wallDiagNE;
		case 5:
			return wallDiagNW;
		case 31:
			return doorOpenEW;
		case 32:
			return doorClosedEW;
		case 33:
			return doorOpenNS;
		case 34:
			return doorClosedNS;

		case 40:
			return zoneClosedE;
		case 41:
			return zoneClosedW;
		case 42:
			return zoneOpenE;
		case 43:
			return zoneClosedW;

		case 21:
			return hall;
		case 200:
			return room;
		default:
			System.out.println("UNRECOGNIZED TILED " + id);
			return null;
		}

	}

	public void loadAnimations() {
		if (GameData.getInstance().isHeadless()) {
			return;
		}
		Texture t = get("textures/playerSS.png");

		playerAnimations = new AnimationSet(t);
		playerAnimations.loadAnimations();

	}

	public AnimationSet getDefaultAnimations() {
		if (GameData.getInstance().isHeadless()) {
			return null;
		}

		Texture t = get("textures/playerSS.png");
		AnimationSet p = new AnimationSet(t);
		p.loadAnimations();

		return p;

	}

	public AnimationSet getPlayerAnimations() {
		if (GameData.getInstance().isHeadless()) {
			return null;
		}

		Texture t = get("textures/playerSS.png");

		AnimationSet p = new AnimationSet(t);
		p.loadAnimations();

		return p;
	}

}
