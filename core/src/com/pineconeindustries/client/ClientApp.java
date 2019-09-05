package com.pineconeindustries.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pineconeindustries.client.cameras.CameraController;
import com.pineconeindustries.client.config.NetworkConfiguration;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.LAssetManager;
import com.pineconeindustries.client.manager.LightingManager;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.tests.Test;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.data.Global.RUN_TYPE;
import com.pineconeindustries.shared.objects.Player;
import box2dLight.RayHandler;

public class ClientApp extends ApplicationAdapter {

	private float state;

	SpriteBatch batch;
	ShapeRenderer shapeBatch;
	Texture img, loadingScreen;
	LocalPlayerData data;

	UserInterface ui;
	TextureRegion bg;

	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;

	// END TEST

	public ClientApp(LocalPlayerData data) {

		Global.runType = RUN_TYPE.client;
		this.data = data;

	}

	public void loadSettings() {
		NetworkConfiguration.loadConfiguration();
	}

	@Override
	public void create() {

		Box2D.init();

		batch = new SpriteBatch();

		shapeBatch = new ShapeRenderer();

		loadingScreen = new Texture("textures/loadingscreen.png");
		CameraController cam = new CameraController();

		ui = UserInterface.getInstance();

		Gdx.input.setInputProcessor(ui.getStage());

		GameData.getInstance().registerAssetManager(new LAssetManager());
		GameData.getInstance().loadAssets();
		
		Texture temp = GameData.getInstance().Assets().get("textures/galaxybg1.png");
		bg = new TextureRegion(temp);
		Player player = new Player(data.getName(), new Vector2(data.getX(), data.getY()), 1, data.getId(),
				data.getCharID(), data.getSector(), cam.getPlayerCamera(), data.getLayer());

		player.connectToChat(ui.getChat());

		Sector s = new Sector(player);
		LogicController.getInstance().registerSector(s);
		LogicController.getInstance().registerConnection(Connection.startConnection(data.getSector()));
		LogicController.getInstance().registerCamera(cam);

		player.enableTickRender();

		// TESTLIGHTING

		// PointLight p1 = new PointLight(rh, 10, new Color(0.1f, 0, 0, 1), 2000, 4000,
		// 750);
		// p1.setSoft(true);

	}

	public void update() {
		state += Gdx.graphics.getDeltaTime();
		ui.update();
		LogicController.getInstance().getSector().update();

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update();
		batch.setProjectionMatrix(LogicController.getInstance().getFixedCamera().combined);
		batch.begin();

		// draw(Texture texture, float x, float y, float originX, float originY, float
		// width, float height, float scaleX, float scaleY, float rotation, int srcX,
		// int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)

		batch.draw(bg, -(bg.getRegionWidth() / 2), -(bg.getRegionHeight() / 2), bg.getRegionWidth() / 2,
				bg.getRegionHeight() / 2, bg.getRegionWidth(), bg.getRegionHeight(), 1, 1, state * 1, false);

		//
		batch.end();

		batch.setProjectionMatrix(LogicController.getInstance().getPlayerCamera().combined);
		LogicController.getInstance().getCameraController().update();

		batch.begin();

		LogicController.getInstance().getSector().render(batch);

		batch.end();

		ui.render();
		InputManager.updateMouse(LogicController.getInstance().getPlayerCamera());

		LightingManager.getInstance().render();

	}

	public void dispose() {
		batch.dispose();
		ui.dispose();
		LightingManager.getInstance().dispose();
		shapeBatch.dispose();

	}

	@Override
	public void resize(int width, int height) {

		LogicController.getInstance().getCameraController().getPlayerViewport().update(width, height);

	}

}
