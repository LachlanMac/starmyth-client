package com.pineconeindustries.shared.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.components.structures.StructureLayer;
import com.pineconeindustries.shared.components.structures.Tile;

public class VectorMath {

	public static Vector2 getDirectionByInput(boolean[] input) {

		float x = 0, y = 0;

		if (input[0])
			y += 1;
		if (input[1])
			y -= 1;
		if (input[2])
			x -= 1;
		if (input[3])
			x += 1;

		return new Vector2(x, y).nor();

	}

	public static ArrayList<Vector2> getPointsBetweenVectors(Vector2 startingPoint, Vector2 endingPoint) {

		ArrayList<Vector2> points = new ArrayList<Vector2>();

		Vector2 dir = new Vector2(endingPoint.x, endingPoint.y).sub(startingPoint.x, startingPoint.y).nor();

		Vector2 dir2 = new Vector2(dir.x * 5, dir.y * 5);

		Vector2 start = new Vector2(startingPoint);

		Vector2 currentPoint = start.add(dir2);

		points.add(new Vector2(currentPoint));

		while (currentPoint.dst(endingPoint) >= 10) {

			currentPoint.add(dir2);
			points.add(new Vector2(currentPoint));

		}

		return points;

	}

	public static Vector2 getDirection(Vector2 start, Vector2 end) {

		Vector2 startTemp = new Vector2(start);
		Vector2 endTemp = new Vector2(end);

		return new Vector2(endTemp.sub(startTemp)).nor();

	}

	public static boolean hasLineOfSight(StructureLayer layer, Vector2 startingPoint, Vector2 endingPoint) {

		ArrayList<Vector2> points = getPointsBetweenVectors(startingPoint, endingPoint);

		for (Vector2 v : points) {

			Tile t = layer.getTileAt(v.x, v.y);
			if (t.isCollidable()) {

				return false;
			}
		}

		return true;

	}

	public static Vector2 getIntersectionPoint(Rectangle r, Vector2 startingPoint, Vector2 endingPoint) {
		Vector2 intersect = new Vector2(0, 0);
		int distance = (int) startingPoint.dst(endingPoint);
		Line2D line = new Line2D.Double(startingPoint.x, startingPoint.y, endingPoint.x, endingPoint.y);

		int immune = 2;
		int counter = 0;
		for (Point2D p : pointsAlongLine(line, distance)) {
			counter++;
			if (counter >= distance) {

				if (r.contains(new Vector2((float) p.getX(), (float) p.getY()))) {

					intersect = new Vector2((float) p.getX(), (float) p.getY());
					return intersect;
				}
			}

		}
		return intersect;
	}

	public static Point2D[] pointsAlongLine(Line2D line, int n) {
		Point2D[] points = new Point2D[n];
		if (n == 1) {
			points[0] = line.getP1();
			return points;
		}

		double dy = line.getY2() - line.getY1();
		double dx = line.getX2() - line.getX1();
		double theta = dx > 0.001 ? Math.atan(dy / dx) : dy < 0 ? -Math.PI : Math.PI;

		double length = Math.abs(line.getP1().distance(line.getP2()));
		int numSegments = n - 1;
		double segmentLength = length / numSegments;
		double x = line.getX1();
		double y = line.getY1();
		double ddx = segmentLength * Math.cos(theta);
		double ddy = segmentLength * Math.sin(theta);

		for (int i = 0; i < n; i++) {
			points[i] = new Point2D.Double(x, y);
			x += ddx;
			y += ddy;
		}

		return points;
	}

	public static String getDirectionLetter(float x, float y) {

		String dir = "";
		if (y > 0) {
			if (Math.abs(x) > Math.abs(y) && x < 0) {
				dir = "w";
			} else if (Math.abs(x) > Math.abs(y) && x > 0) {
				dir = "e";
			} else {
				dir = "n";
			}
		} else if (y < 0) {
			if (Math.abs(x) > Math.abs(y) && x < 0) {
				dir = "w";
			} else if (Math.abs(x) > Math.abs(y) && x > 0) {
				dir = "e";
			} else {
				dir = "s";
			}
		} else {

			if (x < 0) {
				dir = "w";
			} else if (x > 0) {
				dir = "e";
			} else {
				dir = "s";
			}
		}

		return dir;

	}

	public static String getPacketDirection(float x, float y) {

		String dir = "";

		if (y > 0) {
			dir += "n";
		}
		if (y < 0) {
			dir += "s";
		}
		if (x > 0) {
			dir += "w";
		}
		if (x < 0) {
			dir += "e";
		}

		return dir;

	}

	public static Vector2 getDirectionByString(String dir) {

		float x = 0, y = 0;

		switch (dir) {
		case "n":
			y = 1;
			break;
		case "e":
			x = -1;
			break;
		case "w":
			x = 1;
			break;
		case "s":
			y = -1;
			break;
		case "ne":
			y = 1;
			x = -1;
			break;
		case "nw":
			y = 1;
			x = 1;
			break;
		case "se":
			y = -1;
			x = -1;
			break;
		case "sw":
			y = -1;
			x = 1;
			break;

		}

		return new Vector2(x, y).nor();

	}

}
