package com.pineconeindustries.shared.actions;

import java.util.HashMap;

public class ActionSet {

	private HashMap<Integer, Action> actions;

	public ActionSet() {
		actions = new HashMap<Integer, Action>();
	}

	public void addAction(Action action) {
		actions.put(action.getID(), action);
	}

	public void removeAction(Action action) {
		actions.remove(action.getID());
	}

	public Action getActionByID(int id) {
		return actions.get(id);
	}

}
