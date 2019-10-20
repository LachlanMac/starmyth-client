package com.pineconeindustries.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.pineconeindustries.client.cameras.CameraController;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.client.manager.LightingManager;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.server.ai.roles.RoleManager;
import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.components.gameobjects.Player;
import com.pineconeindustries.shared.data.Assets;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.data.Global.RUN_TYPE;
import com.pineconeindustries.shared.text.TextManager;

import box2dLight.PointLight;

public class ClientApp extends ApplicationAdapter {

	private float state;

	SpriteBatch batch;
	ShapeRenderer shapeBatch;
	Texture img, loadingScreen;
	LocalPlayerData data;
	TextManager textManager;

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

	@Override
	public void create() {

		Box2D.init();

		batch = new SpriteBatch();

		shapeBatch = new ShapeRenderer();

		loadingScreen = new Texture("textures/loadingscreen.png");
		CameraController cam = new CameraController();

		ui = UserInterface.getInstance();

		Gdx.input.setInputProcessor(ui.getStage());
		// Gdx.input.setInputProcessor(InputState.getInstance());

		Assets.getInstance().loadAssets();
		RoleManager.getInstance();
		ActionManager.getInstance();

		textManager = TextManager.getInstance();

		Texture temp = Assets.getInstance().getManager().get("textures/galaxybg1.png");
		bg = new TextureRegion(temp);

		Player player = new Player(data.getCharID(), data.getName(), new Vector2(data.getX(), data.getY()),
				data.getSector(), data.getStructure(), data.getLayer(), data.getFaction(), cam.getPlayerCamera());

		player.connectToChat(ui.getChat());

		Sector s = new Sector(player);
		LogicController.getInstance().registerSector(s);
		LogicController.getInstance().registerConnection(Connection.startConnection(data.getSector()));
		LogicController.getInstance().registerCamera(cam);	

	}

	public void update() {
		state += Gdx.graphics.getDeltaTime();
		ui.update();
		LightingManager.getInstance().update();
		LogicController.getInstance().getSector().update();
		InputState.resetInput();

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update();
		batch.setProjectionMatrix(LogicController.getInstance().getFixedCamera().combined);
		batch.begin();

		batch.draw(bg, -(bg.getRegionWidth() / 2), -(bg.getRegionHeight() / 2), bg.getRegionWidth() / 2,
				bg.getRegionHeight() / 2, bg.getRegionWidth(), bg.getRegionHeight(), 1, 1, state * 1, false);

		//
		batch.end();

		batch.setProjectionMatrix(LogicController.getInstance().getPlayerCamera().combined);
		LogicController.getInstance().getCameraController().update();

		batch.begin();

		LogicController.getInstance().getSector().render(batch);
		textManager.render(batch);
		
	
		
		batch.end();
		LightingManager.getInstance().render();
		ui.render();
		InputManager.updateMouse(LogicController.getInstance().getPlayerCamera());

		

	}

	public void dispose() {
		batch.dispose();
		ui.dispose();
		LightingManager.getInstance().dispose();
		shapeBatch.dispose();
		TextManager.getInstance().dispose();

	}

	@Override
	public void resize(int width, int height) {

		LogicController.getInstance().getCameraController().getPlayerViewport().update(width, height);

	}

}
