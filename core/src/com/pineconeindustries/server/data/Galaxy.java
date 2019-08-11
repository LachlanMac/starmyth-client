package com.pineconeindustries.server.data;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pineconeindustries.server.net.players.PlayerConnection;

public class Galaxy {

	private static Galaxy instance = null;
	private ArrayList<Sector> sectors;

	private ArrayBlockingQueue<PlayerConnection> globalPlayerList;

	private Galaxy() {

		sectors = new ArrayList<Sector>();
		globalPlayerList = new ArrayBlockingQueue<PlayerConnection>(1024);

	}

	public void update() {
		for (Sector s : sectors) {
			s.update();
		}
	}

	public void render(Batch b) {
		for (Sector s : sectors) {
			s.render(b);
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

		Sector testSector = new Sector(7780);
		addSector(testSector);
		testSector.startSector();
		testSector.updateAndRender(true);

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

}
