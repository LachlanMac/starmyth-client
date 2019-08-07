package com.pineconeindustries.client.galaxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.objects.Entity;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;

public class Sector {

	
	TileMap t;
	
	private int port;

	private ArrayBlockingQueue<PlayerMP> players;
	private Player player;

	private CopyOnWriteArrayList<Entity> renderList;

	public Sector(int port, Player player) {
		this.port = port;
		players = new ArrayBlockingQueue<PlayerMP>(64);
		renderList = new CopyOnWriteArrayList<Entity>();
		registerPlayer(port, player);
		

		
		//TEST
		t = new TileMap();
		t.loadTestTileMap();
	}

	public void render(Batch b) {
		t.render(b);
		Collections.sort(renderList);
		for (Entity e : renderList) {
			e.render(b);
		}

	}

	public void update() {
		
		
		player.update();

		for (PlayerMP p : players) {
			p.update();
		}
	}

	public void registerPlayer(int port, Player player) {
		this.player = player;
		player.setSectorID(port);
		renderList.add(player);

	}

	public Player getPlayer() {
		return player;
	}

	public ArrayBlockingQueue<PlayerMP> getPlayers() {
		return players;
	}

	public PlayerMP getPlayerByID(int id) {
		PlayerMP player = null;

		for (PlayerMP p : players) {
			if (p.getPlayerID() == id)
				player = p;
		}

		return player;
	}

	public void cleanPlayerList(ArrayList<Integer> ids) {

		for (PlayerMP p : players) {
			boolean playerExists = false;
			for (Integer i : ids) {
				if (p.getPlayerID() == i) {
					playerExists = true;
				}
			}

			if (!playerExists) {
				System.out.println("REMOPVING!");
				removePlayer(getPlayerByID(p.getPlayerID()));
			}
		}

	}

	public void addPlayer(PlayerMP player) {
		players.add(player);
		renderList.add(player);

	}

	public void removePlayer(PlayerMP player) {
		players.remove(player);
		renderList.remove(player);

	}
	
	public void loadSector() {
		
		
		
		
	}

	public PlayerMP getPlayerMPByID(int id) {

		PlayerMP pmp = null;

		for (PlayerMP p : players) {
			if (p.getPlayerID() == id) {
				pmp = p;
			}
		}
		return pmp;

	}

}
