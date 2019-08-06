package com.pineconeindustries.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pineconeindustries.client.cameras.CameraController;
import com.pineconeindustries.client.config.NetworkConfiguration;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.galaxy.Sector;
import com.pineconeindustries.client.manager.LAssetManager;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.shared.data.GameData;
import box2dLight.RayHandler;

public class Client extends ApplicationAdapter {

	public static String TEST_IP = "127.0.0.1";
	public static String LOGIN_SERVER_IP = "142.93.48.155";
	public static String GAME_SERVER_IP = "127.0.0.1";// "142.93.48.155";

	SpriteBatch batch;
	ShapeRenderer shapeBatch;
	Texture img, loadingScreen;
	LocalPlayerData data;



	Sprite bg;

	Stage stage, loadingStage;

	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;

	// END TEST

	RayHandler rh;
	World world;

	// NEW

	public Client(LocalPlayerData data) {

		this.data = data;

		//game = new GameController();
		

	}

	public void loadSettings() {
		NetworkConfiguration.loadConfiguration();
	}


	@Override
	public void create() {

		Box2D.init();
		world = new World(new Vector2(0, 0), true);

		batch = new SpriteBatch();

		shapeBatch = new ShapeRenderer();

		loadingScreen = new Texture("textures/loadingscreen.png");

		bg = new Sprite(new Texture("textures/lachlangalaxy.jpg"));
	
		
	
		CameraController cam = new CameraController();
		
	
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		UserInterface ui = new UserInterface(stage);
		
		
		GameData.getInstance().registerAssetManager(new LAssetManager());
		GameData.getInstance().loadAssets();

		Player player = new Player(data.getName(), new Vector2(data.getX(), data.getY()), GameData.getInstance(), 1, 0, data.getCharID(),
				cam.getPlayerCamera());
		
		player.connectToChat(ui.getChat());
	
		Sector s = new Sector(player.getSectorID(), player);
		LogicController.getInstance().registerSector(s);
		LogicController.getInstance().registerConnection(Connection.startConnection(data.getSector()));
		LogicController.getInstance().registerCamera(cam);
		
		
		player.enableTickRender();
	
		
		//TESTLIGHTING
		rh = new RayHandler(world);
		rh.setAmbientLight(0.01f, 0.01f, 0.01f, 0.6f);
		// PointLight p1 = new PointLight(rh, 10, new Color(0.1f, 0, 0, 1), 2000, 4000,
		// 750);
		// p1.setSoft(true);

	}

	public void update() {

		LogicController.getInstance().getSector().update();

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update();
		batch.setProjectionMatrix(LogicController.getInstance().getPlayerCamera().combined);
		LogicController.getInstance().getCameraController().update();
		batch.begin();

		batch.draw(bg, -(WORLD_WIDTH / 2), -(WORLD_HEIGHT / 2), WORLD_WIDTH, WORLD_HEIGHT);
		LogicController.getInstance().getSector().render(batch);
		batch.end();

		// game.render(batch, shapeBatch);

		rh.setCombinedMatrix(LogicController.getInstance().getPlayerCamera());
		rh.updateAndRender();
		world.step(1 / 60f, 6, 2);
	}

	public void dispose() {
		batch.dispose();
		stage.dispose();
		rh.dispose();
		shapeBatch.dispose();

	}

	@Override
	public void resize(int width, int height) {

		LogicController.getInstance().getCameraController().getPlayerViewport().update(width, height);

	}

}
