package com.pineconeindustries.server.ai.states;

import com.pineconeindustries.server.ai.FiniteStateMachine;

public class WanderState extends State {

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
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction() {
		fsm.getOwner().setSpin(false);
	}

	@Override
	public String getKey() {
		return key;
	}

}
