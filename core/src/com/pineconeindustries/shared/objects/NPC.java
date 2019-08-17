package com.pineconeindustries.shared.objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.networking.packets.Packets;
import com.pineconeindustries.client.networking.packets.UDPPacket;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;

public class NPC extends Person {

	private Sector sector;
	private FiniteStateMachine fsm;

	private Vector2 destination = null;
	private boolean destinationReached = false;
	private float moveSpeed = 300;
	private boolean spin = false;
	private boolean initiliazed = false;

	public NPC(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, int sectorID) {
		super(name, loc, game, factionID, structureID, id, sectorID);

	}

	public NPC(String name, Vector2 loc, GameData game, int factionID, int structureID, int id, Sector sector) {
		super(name, loc, game, factionID, structureID, id, sector.getPort());

		if (Global.isServer()) {
			this.sector = sector;
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

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, 32, 32, t.getRegionWidth(),
					t.getRegionHeight(), 1, 1, 3f + (state * 100), false);
			// b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		} else {
			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);
		}

		if (renderLoc.dst(loc) > 500) {
			System.out.println("RENDER LOCK IS NOW " + renderLoc);
			renderLoc = loc;
		} else {

			float lerp = 15f;
			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * lerp * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * lerp * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}

		if (getFramesSinceLastMove() > 20) {
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
					+ directionY + "=" + velocity);

			sector.getPacketWriter()
					.queueToAll(new UDPPacket(Packets.NPC_MOVE_PACKET, packetData, UDPPacket.packetCounter()));

			if (dest.dst(loc) <= 5) {

				// Random r = new Random(System.currentTimeMillis());
				// destination = new Vector2(r.nextInt(1000) - 500, r.nextInt(1000) - 500);
				destinationReached = true;
				System.out.println("Destination Reached");
				destination = null;
			}

		}
	}

	public void initialize() {
		this.initiliazed = true;
	}

	public boolean isInitialized() {
		return this.initiliazed;
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

	@Override
	public void update() {
		hover();

		if (Global.isServer()) {
			fsm.performAction();

		}
	}
}
