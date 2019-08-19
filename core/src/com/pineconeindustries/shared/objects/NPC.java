package com.pineconeindustries.shared.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.gameunits.Units;

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

	// CLIENT CONSTRUCTOR
	public NPC(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, int sectorID,
			int layer) {
		super(name, loc, game, factionID, structureID, id, sectorID, layer);
	}

	// SERVER CONSTRUCTOR
	public NPC(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, Sector sector,
			int layer) {
		super(name, loc, game, factionID, structureID, id, sector.getPort(), layer);

		if (Global.isServer()) {
			this.sector = sector;
			this.structure = sector.getStructureByID(structureID);
			fsm = new FiniteStateMachine(this);
		}
	}

	@Override
	public void render(Batch b) {

		state += Gdx.graphics.getDeltaTime();

		if (spin == true) {
			velocity = 999;
		}

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity);

		if (velocity == 999) {

			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1,
					3f + (state * Units.SPIN_SPEED), false);

		} else {
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
			loc.x += (directionX * speed * (Gdx.graphics.getDeltaTime() * 100));
			loc.y += (directionY * speed * (Gdx.graphics.getDeltaTime() * 100));

			velocity = (Math.abs(loc.x) + Math.abs(loc.y)) / 2;

			setLastDirectionFaced(new Vector2(directionX, directionY));
			setVelocity(velocity);

			String packetData = new String(getID() + "=" + getLoc().x + "=" + getLoc().y + "=" + directionX + "="
					+ directionY + "=" + velocity + "=" + layer);

			sector.getPacketWriter()
					.queueToAll(new UDPPacket(Packets.NPC_MOVE_PACKET, packetData, UDPPacket.packetCounter()));

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

	public void setSpin(boolean spin) {
		this.spin = spin;
	}

	public boolean hasPath() {
		return hasPath;
	}

	@Override
	public void update() {
		hover();

		if (Global.isServer()) {
			fsm.performAction();

		}
	}

}