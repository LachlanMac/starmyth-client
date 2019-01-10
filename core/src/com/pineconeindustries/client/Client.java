package com.pineconeindustries.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pineconeindustries.client.config.NetworkConfiguration;
import com.pineconeindustries.client.data.LocalPlayerData;
import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.manager.Game;
import com.pineconeindustries.client.networking.Connection;
import com.pineconeindustries.client.networking.NetworkLayer;
import com.pineconeindustries.client.objects.NPC;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.client.objects.Ship;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.client.zones.Zone;

import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

public class Client extends ApplicationAdapter {

	public static String TEST_IP = "127.0.0.1";
	public static String LOGIN_SERVER_IP = "142.93.48.155";

	SpriteBatch batch;
	Texture img, loadingScreen;
	LocalPlayerData data;
	Connection conn;
	NetworkLayer lnet;
	UserInterface ui;
	OrthographicCamera camera;
	Player player;

	Viewport viewport;
	private boolean loading = true;

	Game game;
	Zone zone;
	Sprite bg;
	Sprite testShip;

	Table table;
	Stage stage, loadingStage;

	// TEST

	Ship shippo;

	public int WORLD_WIDTH = 1920;
	public int WORLD_HEIGHT = 1080;
	public float aspectRatio = 1;

	// END TEST

	RayHandler rh;
	World world;

	public Client(LocalPlayerData data) {

		this.data = data;

		System.out.println(this.data.toString());

		game = new Game();

		loadSettings();

		startNetwork();

	}

	public void loadSettings() {
		NetworkConfiguration.loadConfiguration();
	}

	public void startNetwork() {

		conn = new Connection(data.getSector());
		conn.connect();

		if (conn.isConnected() == false) {
			Log.debug("No Connection");
		}

	}

	@Override
	public void create() {

		Box2D.init();
		world = new World(new Vector2(0, 0), true);

		batch = new SpriteBatch();
		loadingScreen = new Texture("textures/loadingscreen.png");

		bg = new Sprite(new Texture("textures/lachlangalaxy.jpg"));
		bg.setPosition(0, 0);
		bg.setSize(WORLD_WIDTH, WORLD_HEIGHT);

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();

		camera = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fit, 1080, 1080 * aspectRatio, camera);
		viewport.apply();

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		ui = new UserInterface(stage);

		game.Assets().loadTextures();
		game.Assets().finishLoading();
		game.Assets().loadAnimations();

		game.Assets().finishLoading();
		game.Assets().update();
		game.Assets().loadShipTiles();

		System.out.println(data.getX() + "   " + data.getY());

		player = new Player(data.getName(), new Vector2(data.getX(), data.getY()), game, 1, 0, data.getCharID(),
				camera);

		zone = new Zone("TESTZONE", data.getSector(), game);

		NetworkLayer lnet = new NetworkLayer(player, zone, conn, game, ui);
		conn.setNetworkLayer(lnet);

		player.setLnet(lnet);
		player.connectToChat(ui.getChat());

		game.setPlayer(player);
		game.setLnet(lnet);

		player.enableTickRender();

		rh = new RayHandler(world);

		rh.setAmbientLight(0.01f, 0.01f, 0.01f, 0.6f);
		// PointLight p1 = new PointLight(rh, 10, new Color(0.1f, 0, 0, 1), 2000, 4000,
		// 750);
		// p1.setSoft(true);

	}

	public void update() {

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
			camera.zoom -= 0.02;
		}

		zone.update();
		player.update();
		ui.getChat().update();

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		if (player.shouldTickRender()) {

			update();

			float lerp = 0.5f;
			Vector3 position = camera.position;
			position.x += (player.getLoc().x - position.x) * lerp * Gdx.graphics.getDeltaTime();
			position.y += (player.getLoc().y - position.y) * lerp * Gdx.graphics.getDeltaTime();

			// camera.position.set(player.getLoc().x, player.getLoc().y, 0);
			camera.update();
			batch.begin();
			batch.enableBlending();

			bg.draw(batch);

			batch.draw(bg, player.getLoc().x - (WORLD_WIDTH / 2), player.getLoc().y - (WORLD_HEIGHT / 2), WORLD_WIDTH,
					WORLD_HEIGHT);

			for (Ship sp : zone.getShips()) {
				sp.render(batch);
			}

			for (PlayerMP mp : zone.getPlayers()) {
				mp.render(batch);
			}

			for (NPC npc : zone.getNPCs()) {
				npc.render(batch);
			}

			player.render(batch);

			batch.end();
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		}
		rh.setCombinedMatrix(camera);
		rh.updateAndRender();
		world.step(1 / 60f, 6, 2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		player.dispose();
		rh.dispose();

	}

	@Override
	public void resize(int width, int height) {

		viewport.update(width, height);

	}

}