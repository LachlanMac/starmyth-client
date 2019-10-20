package com.pineconeindustries.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pineconeindustries.server.ai.roles.RoleManager;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.net.packets.modules.StructureEventsModule;
import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.data.Assets;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.data.Global.RUN_TYPE;

public class ServerApp extends ApplicationAdapter {

	private OrthographicCamera camera, fixedCamera;
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch;
	private ShapeRenderer debugRenderer;
	private World world;
	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;
	double delta = 0;
	private Galaxy galaxy;
	Sprite bg;

	public ServerApp(boolean headless) {

		if (headless) {
			Global.runType = RUN_TYPE.headless_server;
		} else {
			Global.runType = RUN_TYPE.server;
		}
	}

	@Override
	public void create() {

		Assets.getInstance().loadAssets();
		RoleManager.getInstance();
		ActionManager.getInstance();
		Database.getInstance();

		if (!Global.isHeadlessServer()) {

			Box2D.init();
			world = new World(new Vector2(0, 0), true);
			stage = new Stage();
			createCamera();

			Gdx.input.setInputProcessor(stage);
			camera.position.set(0, 0, 0);
		}
		galaxy = Galaxy.getInstance();
		galaxy.loadSectors();

	}

	@Override
	public void render() {

		if (Global.isHeadlessServer()) {

			update();

			return;
		}

		Gdx.gl.glClearColor(0, 0.5f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		batch.setProjectionMatrix(fixedCamera.combined);
		batch.begin();
		batch.draw(bg, -(WORLD_WIDTH / 2), -(WORLD_HEIGHT / 2), WORLD_WIDTH, WORLD_HEIGHT);
		batch.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		galaxy.render(batch);
		batch.end();

		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeType.Line);
		galaxy.debugRender(debugRenderer);
		debugRenderer.end();
		world.step(1 / 60f, 6, 2);
	}

	public void update() {

		galaxy.update();

		if (Global.isHeadlessServer())
			return;

		camera.update();
		debugInput();

	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {

		if (Global.isHeadlessServer()) {
			return;
		}

		viewport.update(width, height);

	}

	public void createCamera() {

		if (Global.isHeadlessServer()) {
			return;
		}

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		fixedCamera = new OrthographicCamera();

		viewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, camera);
		bg = new Sprite(new Texture("textures/lachlangalaxy.jpg"));
		ScalingViewport scalingViewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, fixedCamera);
		scalingViewport.apply();
		viewport.apply();
	}

	public void debugInput() {

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
			camera.zoom += 0.05;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
			camera.zoom -= 0.05;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.position.set(camera.position.x + 50, camera.position.y, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.position.set(camera.position.x - 50, camera.position.y, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.position.set(camera.position.x, camera.position.y + 50, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.position.set(camera.position.x, camera.position.y - 50, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			Galaxy.getInstance().getSectorByID(7780).getPacketWriter()
					.queueToAll(StructureEventsModule.getShipHitPacket(1001, 5, 4, 5, 1));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			Galaxy.getInstance().getSectorByID(7780).getPacketWriter()
					.queueToAll(StructureEventsModule.getShipStartPacket(1001, 2));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
			Galaxy.getInstance().getSectorByID(7780).getPacketWriter()
					.queueToAll(StructureEventsModule.getShipStopPacket(1001, 2));
		}

	}

}
