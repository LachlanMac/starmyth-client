package com.pineconeindustries.client.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.pineconeindustries.client.objects.Ship;
import com.pineconeindustries.client.objects.Station;

public class StationData {

	private int stationID, sectorID, localX, localY, tileWidth, tileHeight, checksum;
	private String stationName, stationClass, filePath, stationData, roomData;
	private float x, y;
	private boolean ready = false;
	private boolean pendingDataRequest = false;

	private boolean roomDataLoaded = false;

	Station station;

	ArrayList<RoomData> rooms;

	StructureTileData[][] layout;

	public StationData(int stationID, int sectorID, String stationName, String stationClass, int localX, int localY,
			String filePath, int checksum) {

		this.stationID = stationID;
		this.sectorID = sectorID;
		this.stationName = stationName;
		this.stationClass = stationClass;
		this.localX = localX;
		this.localY = localY;
		this.checksum = checksum;

		if (stationClass.toLowerCase().equals("Cruiser-A")) {

			this.tileWidth = 64;
			this.tileHeight = 64;
		} else {
			this.tileWidth = 64;
			this.tileHeight = 64;
		}

		this.filePath = filePath;

		layout = new StructureTileData[tileWidth][tileHeight];

		loadStationLayout();

		// parseTileData();
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {

		return ready;
	}

	public int getStationID() {
		return stationID;
	}

	public void setStationID(int stationID) {
		this.stationID = stationID;
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

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationClass() {
		return stationClass;
	}

	public void setStationClass(String stationClass) {
		this.stationClass = stationClass;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStationData() {
		return stationData;
	}

	public void setStationData(String stationData) {
		this.stationData = stationData;
	}

	public void writeStationData(String stationData) {

		File f = new File("stationlayouts/" + filePath);
		BufferedWriter bw;

		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(stationData);
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

	public StructureTileData[][] getLayout() {
		return layout;
	}

	public void setLayout(StructureTileData[][] layout) {
		this.layout = layout;

	}

	public void loadStationLayout() {

		File f = new File("stationlayouts/" + filePath);

		if (f.exists()) {

			BufferedReader br = null;

			try {
				br = new BufferedReader(new FileReader(new File("stationlayouts/" + filePath)));

				stationData = br.readLine();

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

			if (stationData == null) {
				stationData = "";
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

		if (stationData.length() < 100) {

			return 0;
		}

		int startIndex = 0;
		int endIndex = 2;

		int checksum = 0;

		for (int y = 0; y < tileHeight; y++) {

			for (int x = 0; x < tileWidth; x++) {

				String data = stationData.substring(startIndex, endIndex);
				startIndex += 2;
				endIndex += 2;

				int val = Integer.parseInt(data, 16);
				checksum += val;

				layout[x][y] = new StructureTileData(x, y, val);
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

	public void setStation(Station station) {
		this.station = station;
	}

}
