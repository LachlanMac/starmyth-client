package com.pineconeindustries.server.ai.states;

import com.pineconeindustries.server.ai.FiniteStateMachine;

public class IdleState extends State {

	public IdleState(FiniteStateMachine fsm) {
		super(fsm);
		// TODO Auto-generated constructor stub
	}

	final String key = "IDLE";
	
	@Override
	public String getKey() {
		return key;
	}

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
		// TODO Auto-generated method stub

	}

}
