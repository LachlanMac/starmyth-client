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
import com.pineconeindustries.client.zones.Zone;

public class Game {

	private LAssetManager assets;

	private Player player;
	private NetworkLayer lnet;
	private Camera camera;
	private UserInterface ui;
	private Zone zone;
	private Stage stage;

	public Game() {

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

			for (Ship sp : zone.getShips()) {
				sp.render(batch);
			}

			for (Station st : zone.getStations()) {
				st.render(batch);
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

			shapeBatch.setProjectionMatrix(camera.combined);

			shapeBatch.begin(ShapeType.Line);

			shapeBatch.setColor(Color.RED);

			for (Ship sp : zone.getShips()) {
				sp.renderDebug(shapeBatch);
			}

			player.renderDebug(shapeBatch);

			shapeBatch.end();
		}

	}

	public void update() {
		// System.out.println("Updating");
		InputManager.getMouseCoordinates(camera);

		for (Ship sp : zone.getShips()) {
			sp.update();
		}

		for (Station st : zone.getStations()) {
			st.update();
		}

		zone.update();

		player.update();

		ui.getChat().update();

	}

	public void dispose(SpriteBatch b) {
		b.dispose();
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
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
