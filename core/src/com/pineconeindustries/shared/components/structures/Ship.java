package com.pineconeindustries.shared.components.structures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pineconeindustries.shared.components.gameobjects.Elevator;
import com.pineconeindustries.shared.data.Global;

public class Ship extends Structure {

	private int renderLayer = 1;
	
	public Ship(String structureName, int structureID, int sector, int factionID, int renderX, int renderY,
			float globalX, float globalY, int layers, STRUCTURE_STATE currentState) {
		super(structureName, structureID, sector, factionID, renderX, renderY, globalX, globalY, layers, currentState);
		this.type = 2;

		this.render = true;

	}

	public Ship(int structureID) {
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

		if (!render)
			return;

		if (Global.isServer()) {
			renderServer(b);
		} else {
			renderClient(b);
		}
	}

	public void renderServer(SpriteBatch b) {
		for (StructureLayer layer : layerList) {

			if (layer.getLayer() == renderLayer) {

				layer.render(b);
			}

		}

	}

	public void renderClient(SpriteBatch b) {

		for (StructureLayer layer : layerList) {

			if (layer.getLayer() == renderLayer) {

				layer.render(b);
			}

		}
	}

	public void setRenderLayer(int renderLayer) {
		this.renderLayer = renderLayer;
	}

	@Override
	public void debugRender(ShapeRenderer debugRenderer) {
		for (StructureLayer layer : layerList) {

			if (layer.getLayer() == renderLayer) {
				layer.debugRender(debugRenderer);
			}
		}
	}

}
