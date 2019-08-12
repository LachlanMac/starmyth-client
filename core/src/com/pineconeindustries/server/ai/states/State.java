package com.pineconeindustries.server.ai.states;

import com.pineconeindustries.server.ai.FiniteStateMachine;

public abstract class State {

	FiniteStateMachine fsm;
	
	public State(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}

	public abstract void leaveState();

	public abstract void enterState();

	public abstract void performAction();

	public abstract String getKey();
	
	public FiniteStateMachine getFSM() {
		return fsm;
	}

}
