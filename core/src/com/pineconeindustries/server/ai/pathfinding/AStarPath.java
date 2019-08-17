package com.pineconeindustries.server.ai.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.pineconeindustries.shared.objects.GridTile;
import com.pineconeindustries.shared.objects.Tile;

/**
 * A Star Algorithm
 *
 * @author Marcelo Surriabre
 * @version 2.1, 2017-02-23
 */
public class AStarPath {
	private static int DEFAULT_HV_COST = 10; // Horizontal - Vertical Cost
	private static int DEFAULT_DIAGONAL_COST = 14;
	private int hvCost;
	private int diagonalCost;
	private PathNode[][] searchArea;
	private PriorityQueue<PathNode> openList;
	private Set<PathNode> closedSet;
	private PathNode initialNode;
	private PathNode finalNode;

	public AStarPath(int rows, int cols, PathNode initialNode, PathNode finalNode, int hvCost, int diagonalCost) {
		this.hvCost = hvCost;
		this.diagonalCost = diagonalCost;
		setInitialNode(initialNode);
		setFinalNode(finalNode);
		this.searchArea = new PathNode[rows][cols];
		this.openList = new PriorityQueue<PathNode>(new Comparator<PathNode>() {
			@Override
			public int compare(PathNode node0, PathNode node1) {
				return Float.compare(node0.getF(), node1.getF());
			}
		});
		setNodes();
		this.closedSet = new HashSet<>();
	}

	public AStarPath(int rows, int cols, PathNode initialNode, PathNode finalNode) {
		this(rows, cols, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST);
	}

	private void setNodes() {
		for (int i = 0; i < searchArea.length; i++) {
			for (int j = 0; j < searchArea[0].length; j++) {
				PathNode node = new PathNode(i, j);
				node.calculateHeuristic(getFinalNode());
				this.searchArea[i][j] = node;
			}
		}
	}

	public void setBlocks(ArrayList<GridTile> blocks) {

		for (GridTile node : blocks) {
			
			setBlock(node.getX(), node.getY());
		}
	}

	public ArrayList<PathNode> findPath() {
		openList.add(initialNode);
		while (!isEmpty(openList)) {
			PathNode currentNode = openList.poll();
			closedSet.add(currentNode);
			if (isFinalNode(currentNode)) {
				return getPath(currentNode);
			} else {
				addAdjacentNodes(currentNode);
			}
		}
		return new ArrayList<PathNode>();
	}

	private ArrayList<PathNode> getPath(PathNode currentNode) {
		ArrayList<PathNode> path = new ArrayList<PathNode>();
		path.add(currentNode);
		PathNode parent;
		while ((parent = currentNode.getParent()) != null) {
			path.add(0, parent);
			currentNode = parent;
		}
		return path;
	}

	private void addAdjacentNodes(PathNode currentNode) {
		addAdjacentUpperRow(currentNode);
		addAdjacentMiddleRow(currentNode);
		addAdjacentLowerRow(currentNode);
	}

	private void addAdjacentLowerRow(PathNode currentNode) {
		int row = currentNode.getX();
		int col = currentNode.getY();
		int lowerRow = row + 1;
		if (lowerRow < getSearchArea().length) {
			if (col - 1 >= 0) {
				checkNode(currentNode, col - 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements
																				// are not allowed
			}
			if (col + 1 < getSearchArea()[0].length) {
				checkNode(currentNode, col + 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements
																				// are not allowed
			}
			checkNode(currentNode, col, lowerRow, getHvCost());
		}
	}

	private void addAdjacentMiddleRow(PathNode currentNode) {
		int row = currentNode.getX();
		int col = currentNode.getY();
		int middleRow = row;
		if (col - 1 >= 0) {
			checkNode(currentNode, col - 1, middleRow, getHvCost());
		}
		if (col + 1 < getSearchArea()[0].length) {
			checkNode(currentNode, col + 1, middleRow, getHvCost());
		}
	}

	private void addAdjacentUpperRow(PathNode currentNode) {
		int row = currentNode.getX();
		int col = currentNode.getY();
		int upperRow = row - 1;
		if (upperRow >= 0) {
			if (col - 1 >= 0) {
				checkNode(currentNode, col - 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are
																				// not allowed
			}
			if (col + 1 < getSearchArea()[0].length) {
				checkNode(currentNode, col + 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are
																				// not allowed
			}
			checkNode(currentNode, col, upperRow, getHvCost());
		}
	}

	private void checkNode(PathNode currentNode, int col, int row, int cost) {
		PathNode adjacentNode = getSearchArea()[row][col];
		if (!adjacentNode.getBlocked() && !getClosedSet().contains(adjacentNode)) {
			if (!getOpenList().contains(adjacentNode)) {
				adjacentNode.setNodeData(currentNode, cost);
				getOpenList().add(adjacentNode);
			} else {
				boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
				if (changed) {
					// Remove and Add the changed node, so that the PriorityQueue can sort again its
					// contents with the modified "finalCost" value of the modified node
					getOpenList().remove(adjacentNode);
					getOpenList().add(adjacentNode);
				}
			}
		}
	}

	private boolean isFinalNode(PathNode currentNode) {
		return currentNode.equals(finalNode);
	}

	private boolean isEmpty(PriorityQueue<PathNode> openList) {
		return openList.size() == 0;
	}

	private void setBlock(int row, int col) {
		this.searchArea[row][col].setBlocked(true);
	}

	public PathNode getInitialNode() {
		return initialNode;
	}

	public void setInitialNode(PathNode initialNode) {
		this.initialNode = initialNode;
	}

	public PathNode getFinalNode() {
		return finalNode;
	}

	public void setFinalNode(PathNode finalNode) {
		this.finalNode = finalNode;
	}

	public PathNode[][] getSearchArea() {
		return searchArea;
	}

	public void setSearchArea(PathNode[][] searchArea) {
		this.searchArea = searchArea;
	}

	public PriorityQueue<PathNode> getOpenList() {
		return openList;
	}

	public void setOpenList(PriorityQueue<PathNode> openList) {
		this.openList = openList;
	}

	public Set<PathNode> getClosedSet() {
		return closedSet;
	}

	public void setClosedSet(Set<PathNode> closedSet) {
		this.closedSet = closedSet;
	}

	public int getHvCost() {
		return hvCost;
	}

	public void setHvCost(int hvCost) {
		this.hvCost = hvCost;
	}

	private int getDiagonalCost() {
		return diagonalCost;
	}

	private void setDiagonalCost(int diagonalCost) {
		this.diagonalCost = diagonalCost;
	}
}