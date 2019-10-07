package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.actions.effects.EffectOverTime;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public class TargetedEffectAction extends ActionBase {

	public TargetedEffectAction(int id, String name) {
		super(id, name);
		init();
	}

	public void init() {
		script = ScriptInterface.getInstance().loadActionScript(name);
		load();
	}

	@Override
	public void use(DataPackage data) {
		if (data.getTarget() == null) {
			data.setTarget(data.getCaster());
		}
		data.getCaster().getStats().changeCurrentEnergy(-_cost);

		EffectOverTime e = new EffectOverTime(this, data);
		data.getTarget().addEffectOverTime(e);

	}

}
