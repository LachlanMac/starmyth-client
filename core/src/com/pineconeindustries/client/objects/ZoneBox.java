package com.pineconeindustries.client.objects;

import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ZoneBox {

	Rectangle origin, destination;
	private int destinationSectorID, originSectorID;

	Random rn = new Random(System.currentTimeMillis());

	public ZoneBox(Rectangle origin, Rectangle destination, int originSectorID, int destinationSectorID) {
		this.origin = origin;
		this.destination = destination;
		this.originSectorID = originSectorID;
		this.destinationSectorID = destinationSectorID;
	}

	public void update(Player p) {

		if (Intersector.overlaps(origin, p.getBounds())) {
			System.out.println("PLAYER INTERSECTS!");
			if (p.getSectorID() == destinationSectorID) {

				System.out.println("Moving to " + destination.x + 4 + " x  " + destination.y + 4);
				p.transport(new Vector2(destination.x + 4, destination.y + 4));
			}

		}

	}

	public void renderDebug(ShapeRenderer shapeBatch) {

		shapeBatch.rect(origin.x, origin.y, origin.width, origin.height);

	}

}
