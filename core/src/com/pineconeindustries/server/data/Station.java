package com.pineconeindustries.server.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.client.files.Files;

public class Station extends Structure{

	public Station(String structureName, int structureID, int sector, int factionID, int globalX, int globalY,
			int renderX, int renderY, int width, int height) {
		super(structureName, structureID, sector, factionID, globalX, globalY, renderX, renderY, width, height);
		
	}
	
	public Station(int structureID) {
		super(structureID);
	}

	@Override
	public void loadStructure(String data) {
	
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch b) {
		// TODO Auto-generated method stub
		
	}

}
