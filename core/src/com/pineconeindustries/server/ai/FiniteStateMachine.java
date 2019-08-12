package com.pineconeindustries.server.ai;

import java.util.HashMap;
import java.util.Map;

import com.pineconeindustries.client.objects.NPC;
import com.pineconeindustries.server.ai.states.IdleState;
import com.pineconeindustries.server.ai.states.State;

public class FiniteStateMachine {

	NPC owner;

	private static HashMap<String, State> states;

	State currentState;
	
	public FiniteStateMachine(NPC owner) {
		this.owner = owner;
		currentState = new IdleState(this);
	}

	public void performAction() {

		currentState.performAction();

	}

	public void changeState(String state) {

		currentState.leaveState();
		currentState = states.get(state);
		currentState.enterState();
	}

	public static void registerStates(String name, State state) {
		states = new HashMap<String, State>();
		states.put(name, state);
	}

	public NPC getOwner() {
		return owner;
	}

}
