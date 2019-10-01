package com.pineconeindustries.server.galaxy;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.states.*;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.net.players.PlayerConnection;
import com.pineconeindustries.shared.data.Global;

public class Galaxy {

	private static Galaxy instance = null;
	private ArrayList<Sector> sectors;

	private ArrayBlockingQueue<PlayerConnection> globalPlayerList;

	private Galaxy() {

		sectors = new ArrayList<Sector>();
		globalPlayerList = new ArrayBlockingQueue<PlayerConnection>(1024);

	}

	public void update() {

		Clock.getInstance().tick();

		for (Sector s : sectors) {
			s.update();
		}
	}

	public void render(SpriteBatch b) {
		for (Sector s : sectors) {
			s.render(b);
		}
	}

	public void debugRender(ShapeRenderer debugRenderer) {
		for (Sector s : sectors) {
			s.debugRender(debugRenderer);
		}
	}

	public static Galaxy getInstance() {

		if (instance == null) {
			instance = new Galaxy();
		}
		return instance;

	}

	public void stopSectors() {

		for (Sector s : sectors) {
			s.stopSector();
		}

	}

	public void loadSectors() {

		if (Global.useDatabase) {

			sectors = Database.getInstance().getSectorDAO().loadSectors();
			for (Sector s : sectors) {
				s.startSector();
				s.updateAndRender(true);
			}

		} else {
			Sector testSector = null, testSector2 = null;
			testSector = new Sector(7780, 1, 1, "Home Sector Test");
			testSector2 = new Sector(7781, 1, 2, "Secondary Sector Test");
			addSector(testSector);
			addSector(testSector2);
			testSector2.startSector();
			testSector2.updateAndRender(true);
			testSector.startSector();
			testSector.updateAndRender(true);
		}

	}

	public Sector getSectorByID(int sectorID) {
		Sector s = null;
		for (Sector sector : sectors) {
			if (sector.getPort() == sectorID)
				s = sector;
		}

		return s;
	}

	public void addSector(Sector sector) {
		sectors.add(sector);
	}

	public void addPlayerToGlobal(PlayerConnection player) {
		globalPlayerList.add(player);
	}

	public void removePlayerFromGlobal(PlayerConnection player) {
		globalPlayerList.remove(player);
	}

	public PlayerConnection getPlayerConnectionByID(int id) {

		PlayerConnection connection = null;

		for (PlayerConnection conn : globalPlayerList) {
			if (conn.getPlayerID() == id)
				connection = conn;
		}

		return connection;

	}

	@Override
	public String toString() {
		return "Galaxy!";
	}
}
