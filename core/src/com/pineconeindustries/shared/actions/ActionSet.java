package com.pineconeindustries.shared.actions;

import java.util.ArrayList;

import com.pineconeindustries.shared.actions.types.Action;

public class ActionSet {

	private ArrayList<Action> actions;

	public ActionSet() {
		actions = new ArrayList<Action>();
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public void removeAction(Action action) {
		actions.remove(action);
	}

	public Action getActionByID(int id) {
		
		for (Action a : actions) {
			if (a.getID() == id)
				return a;
		}

		return null;

	}

	
	public void update(float delta) {

		for (Action a : actions)
			a.update(delta);

	}

}
