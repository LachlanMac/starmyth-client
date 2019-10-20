package com.pineconeindustries.shared.components.structures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.data.Global;

public class Station extends Structure {

	public Station(String structureName, int structureID, int sector, int factionID, int renderX, int renderY,
			float globalX, float globalY, int layers, STRUCTURE_STATE currentState) {
		super(structureName, structureID, sector, factionID, renderX, renderY, globalX, globalY, layers, currentState);
		this.type = 1;

	}

	public Station(int structureID) {
		super(structureID);
	}

	@Override
	public void update() {
		for (Elevator e : elevators) {
			e.update();
		}
	}

	@Override
	public void render(SpriteBatch b) {
		if (Global.isServer()) {
			renderServer(b);
		} else {
			renderClient(b);
		}
	}

	public void renderServer(SpriteBatch b) {
		for (StructureLayer layer : layerList) {

			if (layer.getLayer() == 1) {

				layer.render(b);
			}

		}

	}

	public void renderClient(SpriteBatch b) {

		for (StructureLayer layer : layerList) {

			if (Global.isClient()) {
				if (layer.getLayer() == LogicController.getInstance().getPlayer().getLayer()) {
					// if (cameraLoc.dst(tiles[x][y].getGlobalLoc()) < 60)
					layer.render(b);
				}
			} else {

			}

		}
	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		// TODO Auto-generated method stub

	}

}
