package com.pineconeindustries.shared.data;

import com.pineconeindustries.client.manager.LAssetManager;

public class GameData {

	private static GameData instance;

	private LAssetManager assetManager;

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

		Assets().loadTextures();
		Assets().finishLoading();
		Assets().loadAnimations();
		Assets().finishLoading();
		Assets().update();
		Assets().loadShipTiles();

	}

	public LAssetManager Assets() {
		return assetManager;
	}

}
