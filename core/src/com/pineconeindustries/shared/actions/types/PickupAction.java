package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.actions.effects.EffectPickup;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public class PickupAction extends ActionBase {

	public PickupAction(int id, String name) {
		super(id, name);
		init();
	}

	public void init() {
		script = ScriptInterface.getInstance().loadScript(name);
		load();
	}

	@Override
	public void use(ActionPackage data) {
		data.getCaster().getStats().changeCurrentEnergy(-_cost);

		EffectPickup e = new EffectPickup(this, data);
		data.getTarget().addEffectOverTime(e);

	}

}
