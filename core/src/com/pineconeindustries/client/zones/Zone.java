package com.pineconeindustries.client.zones;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.manager.Game;
import com.pineconeindustries.client.objects.NPC;

import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.client.objects.Ship;

public class Zone {

	private String name;
	private int port;

	Game game;
	ArrayBlockingQueue<PlayerMP> playerList;
	ArrayBlockingQueue<Ship> shipList;
	ArrayBlockingQueue<NPC> npcList;

	public Zone(String name, int port, Game game) {
		this.name = name;
		this.port = port;
		this.game = game;
		shipList = new ArrayBlockingQueue<Ship>(32);
		playerList = new ArrayBlockingQueue<PlayerMP>(1024);
		npcList = new ArrayBlockingQueue<NPC>(1024);
	}

	public int getSectorID() {
		return port;
	}

	public void addNPC(NPC npc) {
		Log.print("Adding " + npc.getName());
		npcList.add(npc);
	}

	public void addShip(Ship ship) {
		System.out.println("ADDING " + ship.getName());
		shipList.add(ship);
	}

	public void removeNPC(NPC npc) {
		npcList.remove(npc);
	}

	public void removeShip(Ship ship) {
		shipList.remove(ship);
	}

	public void addPlayer(PlayerMP playerMP) {

		playerList.add(playerMP);

	}

	public void removePlayer(PlayerMP playerMP) {

		playerList.remove(playerMP);
	}

	public ArrayBlockingQueue<Ship> getShips() {
		return shipList;
	}

	public ArrayBlockingQueue<PlayerMP> getPlayers() {
		return playerList;
	}

	public ArrayBlockingQueue<NPC> getNPCs() {
		return npcList;
	}

	public NPC getNPCByID(int id) {

		NPC npc = null;

		for (NPC n : npcList) {
			if (n.getID() == id) {
				npc = n;
			}
		}
		return npc;
	}

	public Ship getShipByID(int id) {
		Ship ship = null;
		for (Ship s : shipList) {

			if (s.getData().getShipID() == id) {
				ship = s;
			}

		}
		return ship;
	}

	public PlayerMP getPlayerByID(int id) {

		PlayerMP player = null;

		for (PlayerMP p : playerList) {

			if (p.getPlayerID() == id)
				player = p;
		}

		return player;

	}

	public void update() {

		// for all ships
		for (Ship s : shipList) {

			if (!s.getData().isReady() && s.getData().isPendingDataRequest() == false) {
				game.getLnet().sendShipLayoutRequest(s);
			}

		}
		// for all players
		for (PlayerMP p : playerList) {

			if (p.isSetToDisconnect()) {
				removePlayer(p);
			}
			p.update();

		}

	}

}
