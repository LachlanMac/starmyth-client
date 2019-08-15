package com.pineconeindustries.server.data;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.objects.Tile;

public class Station extends Structure {

	public Station(String structureName, int structureID, int sector, int factionID, float globalX, float globalY,
			int renderX, int renderY, int width, int height, int layers) {
		super(structureName, structureID, sector, factionID, globalX, globalY, renderX, renderY, width, height, layers);

	}

	public Station(int structureID) {
		super(structureID);
	}

	@Override
	public void loadStructure() {

		String fileName = Integer.toString(structureID) + "-" + 1;

		String data = Files.loadShipLayout(fileName);

		// UNPACK
		char[] dataArray = data.toCharArray();
		StringBuilder rawData = new StringBuilder();
		for (char c : dataArray) {
			if (Character.isAlphabetic(c)) {
				if (Character.isUpperCase(c)) {
					System.out.println("GOT A " + Character.toString(c));
					char lc = Character.toLowerCase(c);
					char[] temp = new char[] { lc, lc, lc, lc, lc, lc };
					rawData.append(new String(temp));
				} else {
					rawData.append(Character.toString(c));
				}
			} else {
				rawData.append(Character.toString(c));
			}
		}

		char[] unpackedData = rawData.toString().toCharArray();
		int index = 0;

		for (int y = 0; y < SHIP_SIZE; y++) {
			for (int x = 0; x < SHIP_SIZE; x++) {
				tiles[x][y] = new Tile(unpackedData[index], x, y);
				index++;
			}
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Batch b) {

		if (!render)
			return;

		for (int y = 0; y < SHIP_SIZE; y++) {
			for (int x = 0; x < SHIP_SIZE; x++) {
				tiles[x][y].render(b);
			}
		}
	}

}
