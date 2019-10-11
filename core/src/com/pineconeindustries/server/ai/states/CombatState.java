package com.pineconeindustries.server.ai.states;

import com.pineconeindustries.server.ai.FiniteStateMachine;

public class CombatState extends State {
	final String key = "COMBAT";

	public CombatState(FiniteStateMachine fsm) {
		super(fsm);

	}

	@Override
	public void leaveState() {

	}

	@Override
	public void enterState() {
		System.out.println("Entering Combat State");
	}

	@Override
	public void performAction() {
		fsm.getOwner().getProfession().combat(fsm.getData());
	}

	@Override
	public String getKey() {

		return key;
	}
}
