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

public class Server extends ApplicationAdapter {

	private OrthographicCamera camera, fixedCamera;
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch;
	private World world;
	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;

	Sprite bg;

	public Server() {

	}

	@Override
	public void create() {

		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		stage = new Stage();

		createCamera();

		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(fixedCamera.combined);
		batch.begin();
		batch.draw(bg, -(WORLD_WIDTH / 2), -(WORLD_HEIGHT / 2), WORLD_WIDTH, WORLD_HEIGHT);
		batch.end();
		world.step(1 / 60f, 6, 2);
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
			camera.zoom -= 0.02;
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {

		viewport.update(width, height);

	}

	public void createCamera() {

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
}
