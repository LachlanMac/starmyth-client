package com.pineconeindustries.client.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pineconeindustries.client.networking.NetworkLayer;
import com.pineconeindustries.client.objects.NPC;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.client.objects.Ship;
import com.pineconeindustries.client.objects.Station;
import com.pineconeindustries.client.ui.UserInterface;

public class GameController {

	private LAssetManager assets;

	private Player player;
	private NetworkLayer lnet;
	private Camera camera;
	private UserInterface ui;

	private Stage stage;

	public GameController() {

		assets = new LAssetManager();

	}

	public void render(SpriteBatch batch, ShapeRenderer shapeBatch) {

		update();
		batch.setProjectionMatrix(camera.combined);

		if (player.shouldTickRender()) {

			float lerp = 0.9f;
			Vector3 position = camera.position;

			if (new Vector2(position.x, position.y).dst(player.getLoc()) > 100) {
				position.x += (player.getLoc().x - position.x) * lerp * Gdx.graphics.getDeltaTime();
				position.y += (player.getLoc().y - position.y) * lerp * Gdx.graphics.getDeltaTime();
			}

			camera.update();

			batch.begin();
			batch.enableBlending();

			player.render(batch);

			batch.end();

			stage.act(Gdx.graphics.getDeltaTime());

			stage.draw();

			shapeBatch.setProjectionMatrix(camera.combined);

			shapeBatch.begin(ShapeType.Line);

			shapeBatch.setColor(Color.RED);

			shapeBatch.end();
		}

	}

	public void update() {
		// System.out.println("Updating");
		InputManager.getMouseCoordinates(camera);

		player.update();

		ui.getChat().update();

	}

	public void dispose(SpriteBatch b) {
		b.dispose();
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public UserInterface getUI() {
		return ui;
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;
	}

	public LAssetManager Assets() {
		return assets;

	}

	public void setPlayer(Player p) {
		this.player = p;
	}

	public Player getPlayer() {
		return player;
	}

	public void setLnet(NetworkLayer lnet) {
		this.lnet = lnet;
	}

	public NetworkLayer getLnet() {
		return lnet;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
}
