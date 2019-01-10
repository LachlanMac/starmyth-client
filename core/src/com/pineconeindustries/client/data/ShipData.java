package com.pineconeindustries.client.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.objects.Ship;

public class ShipData {

	private int shipID, sectorID, localX, localY, tileWidth, tileHeight, checksum;
	private String shipName, shipClass, filePath, shipData, roomData;
	private float x, y;
	private boolean ready = false;
	private boolean pendingDataRequest = false;

	private boolean roomDataLoaded = false;

	Ship ship;

	ArrayList<RoomData> rooms;

	ShipTileData[][] layout;

	public ShipData(int shipID, int sectorID, String shipName, String shipClass, int localX, int localY,
			String filePath, int checksum) {

		this.shipID = shipID;
		this.sectorID = sectorID;
		this.shipName = shipName;
		this.shipClass = shipClass;
		this.localX = localX;
		this.localY = localY;
		this.checksum = checksum;

		if (shipClass.toLowerCase().equals("Cruiser-A")) {

			this.tileWidth = 64;
			this.tileHeight = 64;
		} else {
			this.tileWidth = 64;
			this.tileHeight = 64;
		}

		this.filePath = filePath;

		layout = new ShipTileData[tileWidth][tileHeight];

		loadShipLayout();

		// parseTileData();
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {

		return ready;
	}

	public int getShipID() {
		return shipID;
	}

	public void setShipID(int shipID) {
		this.shipID = shipID;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public int getLocalX() {
		return localX;
	}

	public void setLocalX(int localX) {
		this.localX = localX;
	}

	public int getLocalY() {
		return localY;
	}

	public void setLocalY(int localY) {
		this.localY = localY;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipClass() {
		return shipClass;
	}

	public void setShipClass(String shipClass) {
		this.shipClass = shipClass;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getShipData() {
		return shipData;
	}

	public void setShipData(String shipData) {
		this.shipData = shipData;
	}

	public void writeShipData(String shipData) {

		File f = new File("shiplayouts/" + filePath);
		BufferedWriter bw;

		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(shipData);
			bw.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public ShipTileData[][] getLayout() {
		return layout;
	}

	public void setLayout(ShipTileData[][] layout) {
		this.layout = layout;

	}

	public void loadShipLayout() {

		File f = new File("shiplayouts/" + filePath);

		if (f.exists()) {

			BufferedReader br = null;

			try {
				br = new BufferedReader(new FileReader(new File("shiplayouts/" + filePath)));

				shipData = br.readLine();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (shipData == null) {
				shipData = "";
			}

			int pChecksum = parseTileData();

			if (pChecksum == checksum) {

				ready = true;
			}
		} else {

			try {
				f.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public int parseTileData() {

		if (shipData.length() < 100) {

			return 0;
		}

		int startIndex = 0;
		int endIndex = 2;

		int checksum = 0;

		for (int y = 0; y < tileHeight; y++) {

			for (int x = 0; x < tileWidth; x++) {

				String data = shipData.substring(startIndex, endIndex);
				startIndex += 2;
				endIndex += 2;

				int val = Integer.parseInt(data, 16);
				checksum += val;

				layout[x][y] = new ShipTileData(x, y, val);
			}
		}

		return checksum;
	}

	public void loadRoomData(String roomData) {

		this.roomData = roomData;

		rooms = new ArrayList<RoomData>();

		String roomDataSplit[] = roomData.split("=");

		int roomCount = Integer.parseInt(roomDataSplit[1]);

		for (int i = 2; i < roomCount + 2; i++) {

			String[] temp = roomDataSplit[i].split("-");

			int id = Integer.parseInt(temp[0]);
			int startX = Integer.parseInt(temp[1]);
			int startY = Integer.parseInt(temp[2]);
			int width = Integer.parseInt(temp[3]);
			int height = Integer.parseInt(temp[4]);
			String name = temp[5];
			String type = temp[6];

			RoomData r = new RoomData(id, startX, startY, width, height, name, type);

			rooms.add(r);
		}

		roomDataLoaded = true;
	}

	public boolean roomDataLoaded() {
		return roomDataLoaded;
	}

	public boolean isPendingDataRequest() {
		return pendingDataRequest;
	}

	public void setPendingDataRequest(boolean b) {
		this.pendingDataRequest = b;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
