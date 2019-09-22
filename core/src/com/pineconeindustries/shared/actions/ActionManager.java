package com.pineconeindustries.shared.actions;

import java.util.ArrayList;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.files.Files;

public class ActionManager {

	private static ActionManager instance = null;

	private ArrayList<ActionBase> actions;

	private ActionManager() {
		actions = Files.loadActions();

	}

	public static ActionManager getInstance() {
		if (instance == null)
			instance = new ActionManager();

		return instance;

	}

	public ActionBase getActionByID(int id) {

		ActionBase action = null;

		for (ActionBase a : actions) {
			if (a.getID() == id) {
				action = a;
			}
		}

		return action;
	}

}
