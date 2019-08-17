package com.pineconeindustries.server.ai.pathfinding;

public class PathNode {

	float g, h, f;

	private PathNode parent;
	private int x, y;
	private boolean blocked;

	public PathNode(int x, int y) {
		this.g = 0;
		this.f = 0;
		this.h = 0;
		this.x = x;
		this.y = y;
	}

	public void calculateHeuristic(PathNode finalNode) {
		this.h = Math.abs(finalNode.getX() - getX()) + Math.abs(finalNode.getY() - getY());
	}

	private void calculateFinalCost() {
		float finalCost = getG() + getH();
		setF(finalCost);
	}

	public void setNodeData(PathNode currentNode, float cost) {

		float gCost = currentNode.getG() + cost;
		setParent(currentNode);
		setG(gCost);
		calculateFinalCost();
	}

	public boolean checkBetterPath(PathNode currentNode, float cost) {

		float gCost = currentNode.getG() + cost;
		if (gCost < getG()) {
			setNodeData(currentNode, cost);
			return true;
		}
		return false;

	}

	public PathNode getParent() {
		return parent;
	}

	@Override
	public boolean equals(Object arg) {
		PathNode other = (PathNode) arg;
		return this.getX() == other.getX() && this.getY() == other.getY();
	}

	@Override
	public String toString() {
		return "N: (" + getX() + ", " + getY() + ")";
	}

	public void setF(float f) {
		this.f = f;
	}

	public void setG(float g) {
		this.g = g;
	}

	public void setParent(PathNode parent) {
		this.parent = parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float getG() {
		return g;
	}

	public float getF() {
		return f;
	}

	public float getH() {
		return h;
	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
