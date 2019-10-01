package com.pineconeindustries.shared.actions;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.ActionBase.type;
import com.pineconeindustries.shared.actions.types.DirectProjectileAction;
import com.pineconeindustries.shared.actions.types.PickupAction;
import com.pineconeindustries.shared.actions.types.SelfEffectAction;
import com.pineconeindustries.shared.actions.types.TargetedEffectAction;
import com.pineconeindustries.shared.log.Log;

public class ActionFactory {

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
