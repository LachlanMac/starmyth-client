package com.pineconeindustries.server.ai.states;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.server.data.Station;

public class WanderState extends State {

	ArrayList<PathNode> path;
	AStarPath pathFinder;

	LinkedList<PathNode> pathQueue;

	boolean hasPath = false;
	boolean pathReached = false;

	public WanderState(FiniteStateMachine fsm) {
		super(fsm);
		// TODO Auto-generated constructor stub
	}

	final String key = "WANDER";

	@Override
	public void leaveState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterState() {
		System.out.println("GETTING PATH!");

		Station station = fsm.getStructure();
		PathNode start = station.getRandomStartNode();
		PathNode end = station.getRandomEndNode();
		if (!fsm.getOwner().isInitialized()) {
			pathFinder = new AStarPath(station.getGridWidth(), station.getGridHeight(), start, end);
		} else {
			pathFinder = new AStarPath(station.getGridWidth(), station.getGridHeight(),
					new PathNode((int) (fsm.getOwner().getLoc().x / 32), (int) (fsm.getOwner().getLoc().y / 32)), end);
		}

		pathFinder.setBlocks(station.getBlocked());
		path = pathFinder.findPath();
		System.out.println("PATH SIZE " + path.size());
		pathQueue = new LinkedList<PathNode>();
		for (PathNode p : path) {
			pathQueue.add(p);
		}

		hasPath = true;

		int x = pathQueue.peek().getX() * 32;
		int y = pathQueue.peek().getY() * 32;
		if (!fsm.getOwner().isInitialized()) {
			fsm.getOwner().setLocation(new Vector2(x, y));
			fsm.getOwner().initialize();
		} else {

		}
		fsm.getOwner().setDestination(new Vector2(x, y));
		pathQueue.pop();
	}

	@Override
	public void performAction() {

		fsm.getOwner().setSpin(false);

		if (hasPath) {

			if (!fsm.getOwner().isDestinationReached()) {

				fsm.getOwner().move();

			} else {

				PathNode p = pathQueue.pop();
				if (!pathQueue.isEmpty()) {
					int x = p.getX() * 32;
					int y = p.getY() * 32;
					fsm.getOwner().setDestinationReached(false);
					fsm.getOwner().setDestination(new Vector2(x, y));
				} else {
					System.out.println("Path finished");
					hasPath = false;
				}

			}

		}

	}

	@Override
	public String getKey() {
		return key;
	}

}
