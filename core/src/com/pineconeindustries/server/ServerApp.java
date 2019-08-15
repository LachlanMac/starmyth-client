package com.pineconeindustries.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pineconeindustries.client.manager.LAssetManager;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.data.Global.RUN_TYPE;

public class ServerApp extends ApplicationAdapter {

	public boolean headless = false;
	private OrthographicCamera camera, fixedCamera;
	private GameData gameData;
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch;
	private World world;
	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;

	private Galaxy galaxy;

	Sprite bg;

	public ServerApp(boolean headless) {
		this.headless = headless;
		if (headless) {
			Global.runType = RUN_TYPE.headless_server;
		} else {
			Global.runType = RUN_TYPE.server;
		}
	}

	@Override
	public void create() {

		Database.getInstance();

		gameData = GameData.getInstance();
		gameData.setHeadless(headless);
		gameData.registerAssetManager(new LAssetManager());
		gameData.loadAssets();
		if (!headless) {

			Box2D.init();
			world = new World(new Vector2(0, 0), true);
			stage = new Stage();
			createCamera();

			Gdx.input.setInputProcessor(stage);

		}
		galaxy = Galaxy.getInstance();
		galaxy.loadSectors();

	}

	@Override
	public void render() {

		if (headless) {
			return;
		}

		Gdx.gl.glClearColor(0, 0.5f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bg, -(WORLD_WIDTH / 2), -(WORLD_HEIGHT / 2), WORLD_WIDTH, WORLD_HEIGHT);

		galaxy.render(batch);

		batch.end();
		world.step(1 / 60f, 6, 2);
	}

	public void update() {

		galaxy.update();

		camera.position.set(0, 0, 0);
		camera.update();

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
			camera.zoom -= 0.02;
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {

		if (headless) {
			return;
		}

		viewport.update(width, height);

	}

	public void createCamera() {

		if (headless) {
			return;
		}

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		fixedCamera = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, camera);
		bg = new Sprite(new Texture("textures/lachlangalaxy.jpg"));
		ScalingViewport scalingViewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, fixedCamera);
		scalingViewport.apply();
		viewport.apply();
	}

	public boolean isHeadless() {
		return headless;
	}
}
