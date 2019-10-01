package com.pineconeindustries.server.ai.states;

import com.pineconeindustries.server.ai.FiniteStateMachine;

public class DownedState extends State {
	final String key = "DOWNED";

	public DownedState(FiniteStateMachine fsm) {
		super(fsm);

	}

	@Override
	public void leaveState() {

		fsm.getOwner().setHoldable(false);
		fsm.getOwner().beDropped();
	}

	@Override
	public void enterState() {

		fsm.getOwner().setHoldable(true);

	}

	@Override
	public void performAction() {

	}

	@Override
	public String getKey() {

		return key;
	}

}
