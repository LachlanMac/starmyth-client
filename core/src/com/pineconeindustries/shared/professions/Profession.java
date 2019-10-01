package com.pineconeindustries.shared.professions;

import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.actions.ActionSet;
import com.pineconeindustries.shared.actions.types.Action;
import com.pineconeindustries.shared.actions.types.DirectProjectileAction;
import com.pineconeindustries.shared.actions.types.TargetedEffectAction;

public class Profession {

	public static ActionSet getActionSetByProfession() {

		ActionSet actionSet = new ActionSet();
		actionSet.addAction(new Action((DirectProjectileAction) ActionManager.getInstance().getActionByID(1)));
		actionSet.addAction(new Action((TargetedEffectAction) ActionManager.getInstance().getActionByID(4)));
		return actionSet;

	}

}
