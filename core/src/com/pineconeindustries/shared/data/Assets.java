package com.pineconeindustries.shared.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pineconeindustries.client.models.AnimationSet;
import com.pineconeindustries.shared.log.Log;

public class Assets {
	private AssetManager assetManager;
	private static Assets instance;

	AnimationSet playerAnimations;
	Animation<TextureRegion> thrusterAnimation, targetAnimation, shotAnimation, shot2Animation;
	Texture shipSS, elevator, ionThrusterSS, targetSS, shotSS, shot2SS, healthBarSS;
	TextureRegion selectTile, selectGridTile;
	TextureRegion[][] shipTiles, thrusterTiles, targetTiles, shotTiles, shot2Tiles, healthBar;
	TextureRegion wall, wallDiagSW, wallDiagNE, wallDiagNW, wallDiagSE, hall, room, doorClosedEW, doorClosedNS,
			doorOpenEW, doorOpenNS, zoneClosedE, zoneClosedW, zoneOpenE, zoneOpenW, thrusterOff, thruster1, thruster2,
			thruster3, thruster4, elevatorTile;
	// unassigned
	TextureRegion zoneClosedN, zoneClosedS, zoneOpenN, zoneOpenS;
	Sound explosion, shipStart, shipStop, shipLoop, pew1, pew2, pew3;
	TextureRegion healthBarFrame, healthBarGreen, healthBarRed, energyBarYellow, energyBarBlack;

	private Assets() {
		assetManager = new AssetManager();
	}

	public static Assets getInstance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	public void loadAssets() {

		if (Global.isHeadlessServer()) {
			return;
		}

		loadTextures();
		assetManager.finishLoading();
		loadAnimations();
		loadSoundEffects();
		assetManager.finishLoading();
		assetManager.update();
		loadShipTiles();

	}

	public void loadTextures() {
		assetManager.load("textures/targetSS.png", Texture.class);
		assetManager.load("textures/healthbarSS.png", Texture.class);
		assetManager.load("textures/shotSS.png", Texture.class);
		assetManager.load("textures/shot2SS.png", Texture.class);
		assetManager.load("textures/galaxybg1.png", Texture.class);
		assetManager.load("textures/plasma.png", Texture.class);
		assetManager.load("textures/shiptiles/shipSS.png", Texture.class);
		assetManager.load("textures/shiptiles/ionthrusterSS.png", Texture.class);
		assetManager.load("textures/playerfront.png", Texture.class);
		assetManager.load("textures/shiptiles/diagwall.png", Texture.class);
		assetManager.load("textures/shiptiles/floor.png", Texture.class);
		assetManager.load("textures/shiptiles/hall.png", Texture.class);
		assetManager.load("textures/shiptiles/wall.png", Texture.class);
		assetManager.load("textures/playerSS.png", Texture.class);
		assetManager.load("textures/elevator.png", Texture.class);
		assetManager.load("textures/healthbar.png", Texture.class);
		assetManager.load("textures/path.png", Texture.class);
		assetManager.load("textures/start.png", Texture.class);
		assetManager.load("textures/end.png", Texture.class);
		assetManager.load("textures/selectTile.png", Texture.class);
		assetManager.load("textures/selectGridTile.png", Texture.class);

	}

	public void loadSoundEffects() {
		explosion = Gdx.audio.newSound(Gdx.files.internal("audio/effects/explosion.mp3"));
		shipStart = Gdx.audio.newSound(Gdx.files.internal("audio/effects/ship-start.wav"));
		shipStop = Gdx.audio.newSound(Gdx.files.internal("audio/effects/ship-stop.wav"));
		shipLoop = Gdx.audio.newSound(Gdx.files.internal("audio/effects/shiploop.wav"));
		pew1 = Gdx.audio.newSound(Gdx.files.internal("audio/effects/pew1mod.mp3"));
		pew2 = Gdx.audio.newSound(Gdx.files.internal("audio/effects/pew2mod.mp3"));
		pew3 = Gdx.audio.newSound(Gdx.files.internal("audio/effects/pew3mod.mp3"));
	}

	public Sound getSoundEffect(String sound) {
		Sound s = null;
		switch (sound) {

		case "explosion":
			s = explosion;
			break;
		case "shipstart":
			s = shipStart;
			break;
		case "shipstop":
			s = shipStop;
			break;
		case "shiploop":
			s = shipLoop;
			break;
		case "pew1":
			s = pew1;
			break;
		case "pew2":
			s = pew2;
			break;
		case "pew3":
			s = pew3;
			break;

		}
		return s;

	}

	public Animation<TextureRegion> getTargetAnimation() {
		return targetAnimation;
	}

	public Animation<TextureRegion> getShotAnimation() {
		return shotAnimation;
	}

	public Animation<TextureRegion> getShot2Animation() {
		return shot2Animation;
	}

	public void loadTarget() {

		targetSS = assetManager.get("textures/targetSS.png");
		targetTiles = TextureRegion.split(targetSS, targetSS.getWidth() / 2, targetSS.getHeight() / 2);
		targetAnimation = new Animation<TextureRegion>(5,
				new TextureRegion[] { targetTiles[0][0], targetTiles[0][1], targetTiles[1][0], targetTiles[1][1] });

	}

	public void loadShot() {

		shotSS = assetManager.get("textures/shotSS.png");
		shotTiles = TextureRegion.split(shotSS, shotSS.getWidth() / 2, shotSS.getHeight() / 2);
		shotAnimation = new Animation<TextureRegion>(5,
				new TextureRegion[] { shotTiles[0][0], shotTiles[0][1], shotTiles[1][0], shotTiles[1][1] });

		shot2SS = assetManager.get("textures/shot2SS.png");
		shot2Tiles = TextureRegion.split(shot2SS, shot2SS.getWidth() / 1, shot2SS.getHeight() / 2);
		shot2Animation = new Animation<TextureRegion>(5, new TextureRegion[] { shot2Tiles[0][0], shot2Tiles[1][0] });

	}

	public void loadHealthBar() {

		Texture hbSS = assetManager.get("textures/healthbarSS.png");

		healthBar = TextureRegion.split(hbSS, hbSS.getWidth(), hbSS.getHeight() / 5);

		healthBarFrame = healthBar[0][0];
		healthBarGreen = healthBar[3][0];
		healthBarRed = healthBar[4][0];
		energyBarYellow = healthBar[1][0];
		energyBarBlack = healthBar[2][0];

	}

	public TextureRegion getHealthBarFrame() {
		return healthBarFrame;
	}

	public TextureRegion getHealthBarGreen() {
		return healthBarGreen;
	}

	public TextureRegion getHealthBarRed() {
		return healthBarRed;
	}

	public TextureRegion getEnergyBarYellow() {
		return energyBarYellow;
	}

	public TextureRegion getEnergyBarBlack() {
		return energyBarBlack;
	}

	public void loadShipTiles() {

		shipSS = assetManager.get("textures/shiptiles/shipSS.png");
		shipTiles = TextureRegion.split(shipSS, shipSS.getWidth() / 4, shipSS.getHeight() / 4);
		wall = shipTiles[0][0];
		wallDiagSE = shipTiles[0][1];
		wallDiagSW = shipTiles[2][0];
		wallDiagNE = shipTiles[2][2];
		wallDiagNW = shipTiles[2][1];
		hall = shipTiles[0][2];
		elevatorTile = shipTiles[2][3];
		doorOpenEW = shipTiles[3][2];
		doorClosedEW = shipTiles[3][0];
		doorOpenNS = shipTiles[3][3];
		doorClosedNS = shipTiles[3][1];
		zoneClosedE = shipTiles[1][0];
		zoneOpenE = shipTiles[1][1];
		zoneClosedW = shipTiles[1][3];
		zoneOpenW = shipTiles[1][2];
		room = shipTiles[0][3];

		ionThrusterSS = assetManager.get("textures/shiptiles/ionthrusterSS.png");
		thrusterTiles = TextureRegion.split(ionThrusterSS, ionThrusterSS.getWidth() / 2, ionThrusterSS.getHeight() / 3);

		thrusterOff = thrusterTiles[0][0];
		thruster1 = thrusterTiles[1][0];
		thruster2 = thrusterTiles[0][1];
		thruster3 = thrusterTiles[1][1];

		thrusterAnimation = new Animation<TextureRegion>(5, new TextureRegion[] { thruster1, thruster2, thruster3 });

		loadShot();

		loadTarget();

		loadHealthBar();
	}

	public Animation<TextureRegion> getThursterAnimation() {
		return thrusterAnimation;
	}

	public TextureRegion debug(char id) {

		TextureRegion x = null;
		if (id == 'p') {
			return null;
		} else {
			return wall;
		}

	}

	public TextureRegion getElevatorTile() {
		return elevatorTile;
	}

	public TextureRegion getTileID(char id) {

		TextureRegion x = null;
		switch (id) {
		case 'z':
			x = room;// bridge
			break;
		case '&':
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
		case 'l':
			x = thrusterOff;
			break;
		default:
			break;
		}
		if (x == null)
			Log.print("Could not find Tile with id " + id);
		return x;
	}

	public TextureRegion getHall() {
		return hall;
	}

	public void loadAnimations() {
		if (Global.isHeadlessServer()) {
			return;
		}
		Texture t = assetManager.get("textures/playerSS.png");

		playerAnimations = new AnimationSet(t);
		playerAnimations.loadAnimations();

	}

	public AnimationSet getDefaultAnimations() {
		if (Global.isHeadlessServer()) {
			return null;
		}

		Texture t = assetManager.get("textures/playerSS.png");
		AnimationSet p = new AnimationSet(t);
		p.loadAnimations();

		return p;

	}

	public AnimationSet getPlayerAnimations() {
		if (Global.isHeadlessServer()) {
			return null;
		}

		Texture t = assetManager.get("textures/playerSS.png");

		AnimationSet p = new AnimationSet(t);
		p.loadAnimations();

		return p;
	}

	public AssetManager getManager() {
		return assetManager;
	}

}
