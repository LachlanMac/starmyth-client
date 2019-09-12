package com.pineconeindustries.shared.actions;

import java.util.ArrayList;

import com.pineconeindustries.shared.files.Files;

public class ActionManager {

	private static ActionManager instance = null;

	private ArrayList<Action> actions;

	private ActionManager() {
		actions = Files.loadActions();

	}

	public static ActionManager getInstance() {
		if (instance == null)
			instance = new ActionManager();

		return instance;

	}

	public Action getActionByID(int id) {

		Action action = null;

		for (Action a : actions) {
			if (a.getID() == id) {
				action = a;
			}
		}

		return action;
	}

}
