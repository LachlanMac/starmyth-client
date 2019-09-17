package com.pineconeindustries.shared.components.structures;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.components.structures.Structure.STRUCTURE_STATE;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.log.Log;

public class Station extends Structure {

	private int renderLayer = 1;

	public Station(String structureName, int structureID, int sector, int factionID, int renderX, int renderY,
			float globalX, float globalY, int layers, STRUCTURE_STATE currentState) {
		super(structureName, structureID, sector, factionID, renderX, renderY, globalX, globalY, layers, currentState);
		this.type = 1;
		if (structureID == 1001) {
			render = true;
		}

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
		/*
		 * Vector2 cameraLoc = new Vector2(
		 * LogicController.getInstance().getCameraController().getPlayerCamera().
		 * position.x,
		 * LogicController.getInstance().getCameraController().getPlayerCamera().
		 * position.y);
		 */

		for (StructureLayer layer : layerList) {

			if (layer.getLayer() == renderLayer) {
				// if (cameraLoc.dst(tiles[x][y].getGlobalLoc()) < 60)
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
