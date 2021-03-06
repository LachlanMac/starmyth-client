package com.pineconeindustries.server.ai.states;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.shared.components.structures.Station;
import com.pineconeindustries.shared.components.structures.Structure;

public class WanderState extends State {

	public WanderState(FiniteStateMachine fsm) {
		super(fsm);
		// TODO Auto-generated constructor stub
	}

	final String key = "WANDER";

	@Override
	public void leaveState() {
		fsm.getOwner().clearPath();

	}

	@Override
	public void enterState() {
		fsm.getOwner().clearPath();
		fsm.getOwner().findRandomPath();

	}

	@Override
	public void performAction() {

		fsm.getOwner().getProfession().wander(fsm.getData());
	}

	@Override
	public String getKey() {
		return key;
	}

}
