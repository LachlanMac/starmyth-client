package com.pineconeindustries.client.manager;

import com.pineconeindustries.client.networking.NetworkLayer;
import com.pineconeindustries.client.objects.Player;

public class Game {

	private LAssetManager assets;
	private InputManager input;
	private Player player;
	private NetworkLayer lnet;

	public Game() {

		assets = new LAssetManager();
		input = new InputManager();
	}

	public LAssetManager Assets() {
		return assets;

	}

	public InputManager Input() {
		return input;
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
}
