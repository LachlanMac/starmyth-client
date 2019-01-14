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
import com.pineconeindustries.client.objects.Station;

public class Zone {

	private String name;
	private int port;

	Game game;
	ArrayBlockingQueue<PlayerMP> playerList;
	ArrayBlockingQueue<Ship> shipList;
	ArrayBlockingQueue<Station> stationList;
	ArrayBlockingQueue<NPC> npcList;

	public Zone(String name, int port, Game game) {
		this.name = name;
		this.port = port;
		this.game = game;
		shipList = new ArrayBlockingQueue<Ship>(32);
		stationList = new ArrayBlockingQueue<Station>(32);
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
		System.out.println("Adding Ship " + ship.getName());
		shipList.add(ship);
	}

	public void addStation(Station station) {
		Log.print("Adding Station " + station.getName());
		stationList.add(station);
	}

	public void removeNPC(NPC npc) {
		npcList.remove(npc);
	}

	public void removeShip(Ship ship) {
		shipList.remove(ship);
	}

	public void removeStation(Station station) {
		stationList.remove(station);
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

	public ArrayBlockingQueue<Station> getStations() {
		return stationList;
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

	public int getStructureIDB(int sectorX, int sectorY) {
		int structID = 0;
		int secX = 0;
		int secY = 0;

		for (Station s : stationList) {

			if (s.getData().getLocalX() == sectorX && s.getData().getLocalY() == sectorY) {
				structID = s.getData().getStationID();
			}

		}

		for (Ship s : shipList) {

			if (s.getData().getLocalX() == sectorX && s.getData().getLocalY() == sectorY) {
				structID = s.getData().getShipID();
			}

		}
		Log.print("RETURNING STRCUT THO " + structID);
		return structID;

	}

	public Station getStationByID(int id) {
		Station station = null;
		for (Station s : stationList) {

			if (s.getData().getStationID() == id) {
				station = s;
			}

		}
		return station;
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

		for (Station s : stationList) {

			if (!s.getData().isReady() && s.getData().isPendingDataRequest() == false) {
				game.getLnet().sendStationLayoutRequest(s);
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
