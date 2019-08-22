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
	Texture shipSS, elevator;

	TextureRegion[][] shipTiles;

	TextureRegion wall, wallDiagSW, wallDiagNE, wallDiagNW, wallDiagSE, hall, room, doorClosedEW, doorClosedNS,
			doorOpenEW, doorOpenNS, zoneClosedE, zoneClosedW, zoneOpenE, zoneOpenW;
	// unassigned
	TextureRegion zoneClosedN, zoneClosedS, zoneOpenN, zoneOpenS;

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
		load("textures/elevator.png", Texture.class);
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

		elevator = get("textures/elevator.png");
	}

	public TextureRegion debug(char id) {

		TextureRegion x = null;
		if (id == 'p') {
			return null;
		} else {
			return wall;
		}

	}

	public TextureRegion getTileID(char id) {

		TextureRegion x = null;
		switch (id) {
		case 'z':
			x = room;// bridge
			break;
		case '&':
			System.out.println("DEFAULT DOOR loaded");
			break;
		case 'y':
			x = doorOpenEW;
			break;
		case 'm':
			x = doorClosedEW;
			break;
		case 'n':
			x = doorOpenNS;
			break;
		case 'o':
			x = doorClosedNS;
			break;
		case '_':
			System.out.println("Default zone loaded");
			break;
		case 'd':
			x = zoneClosedE;
			break;
		case 'e':
			x = zoneClosedW;
			break;
		case 'f':
			x = zoneClosedN;
			break;
		case 'g':
			x = zoneClosedS;
			break;
		case 'h':
			x = zoneOpenE;
			break;
		case 'i':
			x = zoneOpenW;
			break;
		case 'j':
			x = zoneOpenN;
			break;
		case 'k':
			x = zoneOpenS;
			break;
		case 'c':
			x = room; // engineering
			break;
		case 'b':
			x = room; // foyer
			break;
		case 'a':
			x = hall;
			break;
		case 'x':
			x = room; // medical
			break;
		case 'w':
			x = room; // room
			break;
		case 'v':
			x = room; // shop
			break;
		case 'p':
			x = null;
			break;
		case 'q':
			x = wall;
			break;
		case 't':
			x = wallDiagSE;
			break;
		case 'u':
			x = wallDiagSW;
			break;
		case 'r':
			x = wallDiagNE;
			break;
		case 's':
			x = wallDiagNW;
			break;
		default:
			System.out.println("UNRECONGIZED PARAM = " + Character.toString(id));
			break;
		}
		if (x == null)
			System.out.println("DLKSFLDF  " + Character.toString(id));
		return x;
	}

	public TextureRegion getHall() {
		return hall;
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
