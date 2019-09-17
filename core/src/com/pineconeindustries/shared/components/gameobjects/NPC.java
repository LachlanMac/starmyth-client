package com.pineconeindustries.shared.components.gameobjects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packets.types.Packets;
import com.pineconeindustries.server.net.packets.types.UDPPacket;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.units.Units;
import com.pineconeindustries.shared.utils.VectorMath;

public class NPC extends Person {

	private Sector sector;
	private FiniteStateMachine fsm;
	private Vector2 destination = null;
	private boolean destinationReached = false;
	private boolean spin = false;
	private boolean hasPath = false;
	private AStarPath pathfinder;
	private Structure structure;
	private LinkedList<PathNode> path;
	private Globals local;
	private LuaValue script;
	private boolean hasScript = false;

	private DecimalFormat df = new DecimalFormat("#.00");

	private LuaFunction getName, getLocation, getDestination;
	private LuaFunction _ON_DEATH, _ON_HIT, _ON_NEW_PATH;

	// CLIENT CONSTRUCTOR
	public NPC(int id, String name, Vector2 loc, int sectorID, int structureID, int layer, int factionID) {
		super(id, name, loc, sectorID, structureID, layer, factionID);
	}

	// SERVER CONSTRUCTOR
	public NPC(int id, String name, Vector2 loc, Sector sector, int structureID, int layer, int factionID) {
		super(id, name, loc, sector.getPort(), structureID, layer, factionID);

		registerScript();
		this.sector = sector;
		this.structure = sector.getStructureByID(structureID);
		fsm = new FiniteStateMachine(this);
		speed = Units.NPC_SPEED;

	}

	@Override
	public void render(SpriteBatch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);

		if (velocity == 999 || spin == true) {
			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1,
					3f + (state * Units.SPIN_SPEED), false);

		} else {

			if (Global.isClient()) {
				if (LogicController.getInstance().getPlayer().isPlayerTarget(this)) {
					b.draw(GameData.getInstance().Assets().getTargetAnimation().getKeyFrame(state * 50, true),
							renderLoc.x, renderLoc.y);

				}
			}

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);

		}

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}

		if (getFramesSinceLastMove() > 5) {
			if (velocity != 999)

				velocity = 0;
		}

	}

	public void setLocation(Vector2 loc) {
		this.loc = loc;
		this.renderLoc = loc;

	}

	public void move() {

		if (destination != null) {

			calculateMov(destination);
		}

	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}

	public void calculateMov(Vector2 dest) {

		float distance = (float) Math.sqrt(Math.pow(dest.x - loc.x, 2) + Math.pow(dest.y - loc.y, 2));

		if (distance == 0) {
			destinationReached = true;
			destination = null;
			return;
		}
		float directionX = (dest.x - loc.x) / distance;
		float directionY = (dest.y - loc.y) / distance;

		// On update
		if (!destinationReached) {
			loc.x += (directionX * speed * (Gdx.graphics.getDeltaTime()));
			loc.y += (directionY * speed * (Gdx.graphics.getDeltaTime()));

			velocity = (Math.abs(loc.x) + Math.abs(loc.y)) / 2;

			setLastDirectionFaced(new Vector2(directionX, directionY));
			setVelocity(velocity);
			bounds.x = loc.x;
			bounds.y = loc.y;

			String data = new String(getID() + "x" + df.format(getLoc().x) + "x" + df.format(getLoc().y) + "x"
					+ VectorMath.getPacketDirection(directionX, directionY) + "x" + layer + "=");

			sector.addNPCMovementData(data);

			if (dest.dst(loc) <= 5) {

				destinationReached = true;
				destination = null;
			}

		}
	}

	public void moveOnPath() {

		if (destinationReached) {

			PathNode p = path.pop();
			if (!path.isEmpty()) {
				fsm.getOwner().setDestinationReached(false);
				Vector2 vector = structure
						.getGlobalVector(new Vector2(p.getX() * Units.GRID_INTERVAL, p.getY() * Units.GRID_INTERVAL));

				setDestination(vector);

			} else {

				hasPath = false;
			}

		}

		move();

	}

	public void clearPath() {

		destinationReached = false;
		path = null;
		destination = null;

	}

	public void registerScript() {
		local = JsePlatform.standardGlobals();
		hasScript = true;
		LuaValue instance = CoerceJavaToLua.coerce(this);
		local.set("npc", instance);
		try {
			local.get("dofile").call("lua/npc_" + getID() + ".lua");

		} catch (Exception e) {

		}

		registerScriptFunctions();
	}

	public void registerScriptFunctions() {
		try {
			_ON_DEATH = (LuaFunction) local.get("_ON_DEATH");
			_ON_HIT = (LuaFunction) local.get("_ON_HIT");
			_ON_NEW_PATH = (LuaFunction) local.get("_ON_NEW_PATH");

		} catch (Exception e) {

		}

	}

	public void die() {
		if (_ON_DEATH == null)
			return;
		LuaValue returned = _ON_DEATH.call();
	}

	public void test() {

	}

	public void findRandomPath() {

		PathNode end = structure.getLayerByNumber(layer).getRandomEndNode();
		pathfinder = new AStarPath(structure.getGridWidth(), structure.getGridHeight(),
				new PathNode((int) (getLoc().x / Units.GRID_INTERVAL), (int) (getLoc().y / Units.GRID_INTERVAL)), end);
		pathfinder.setBlocks(structure.getLayerByNumber(layer).getBlocked());
		ArrayList<PathNode> pathArray = pathfinder.findPath();
		if (pathArray.size() == 0) {
			// no path found
		} else {

			path = new LinkedList<PathNode>();
			for (PathNode p : pathArray) {
				path.add(p);
			}

			Vector2 firstVector = structure.getGlobalVector(
					new Vector2(path.peek().getX() * Units.GRID_INTERVAL, path.peek().getY() * Units.GRID_INTERVAL));

			if (path.size() == 0) {
				setLocation(firstVector);
				hasPath = false;
			} else {
				setDestination(firstVector);
				hasPath = true;
				path.pop();

			}
		}

	}

	public Vector2 getDestination() {
		return destination;
	}

	public boolean isDestinationReached() {
		return destinationReached;
	}

	public void setDestinationReached(boolean destinationReached) {
		this.destinationReached = destinationReached;
	}

	public boolean hasPath() {
		return hasPath;
	}

	@Override
	public void update() {

		if (Global.isServer()) {
			fsm.performAction();
		}
		if (Global.isClient()) {
			onClick();
		}
	}

	@Override
	public String getType() {
		return "n";
	}

}
