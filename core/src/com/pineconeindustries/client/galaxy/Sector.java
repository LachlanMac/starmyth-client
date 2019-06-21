package com.pineconeindustries.client.galaxy;

import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;

public class Sector {

	private int port;

	private ArrayBlockingQueue<PlayerMP> players;
	private Player player;

	public Sector(int port) {
		this.port = port;

	}

	public void render(Batch b) {

		player.render(b);

	}

	public void update() {

		player.update();

	}

	public void registerPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}
