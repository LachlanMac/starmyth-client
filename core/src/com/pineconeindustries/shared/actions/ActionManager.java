package com.pineconeindustries.shared.actions;

import java.util.ArrayList;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.DirectProjectileAction;
import com.pineconeindustries.shared.actions.types.PickupAction;
import com.pineconeindustries.shared.actions.types.SelfEffectAction;
import com.pineconeindustries.shared.actions.types.TargetedEffectAction;
import com.pineconeindustries.shared.actions.types.ActionBase.type;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.log.Log;

public class ActionManager {

	private static ActionManager instance = null;

	private ArrayList<ActionBase> actions;

	private ActionManager() {
		loadActions();
	}

	public static ActionManager getInstance() {
		if (instance == null)
			instance = new ActionManager();

		return instance;

	}

	public void loadActions() {
		actions = Files.loadActions();
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

	public static ActionBase getAction(int id, String name, String identifier) {

		type t = ActionBase.getType(identifier);
		switch (t) {
		case DIRECT_PROJECTILE:
			return new DirectProjectileAction(id, name);
		case TARGETED_EFFECT:
			return new TargetedEffectAction(id, name);
		case SELF_EFFECT:
			return new SelfEffectAction(id, name);
		case PICKUP_EFFECT:
			return new PickupAction(id, name);
		default:
			Log.debug("No Action Type Found");
			return null;
		}

	}

}
