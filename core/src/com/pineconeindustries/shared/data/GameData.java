package com.pineconeindustries.shared.data;

import com.pineconeindustries.client.manager.LAssetManager;
import com.pineconeindustries.server.ServerApp;

public class GameData {

	private static GameData instance;

	private LAssetManager assetManager;

	private boolean headless;

	private GameData() {

	}

	public static GameData getInstance() {
		if (instance == null) {
			instance = new GameData();
		}
		return instance;
	}

	public void registerAssetManager(LAssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public void loadAssets() {

		if (headless) {
			return;
		}

		Assets().loadTextures();
		Assets().finishLoading();
		Assets().loadAnimations();
		Assets().loadSoundEffects();
		Assets().finishLoading();
		Assets().update();
		Assets().loadShipTiles();

	}

	public LAssetManager Assets() {
		return assetManager;
	}

	public boolean isHeadless() {
		return headless;
	}

	public void setHeadless(boolean val) {
		this.headless = val;
	}

}
