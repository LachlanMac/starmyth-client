package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.actions.effects.EffectOverTime;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public class SelfEffectAction extends ActionBase {

	public SelfEffectAction(int id, String name) {
		super(id, name);
		init();
	}

	public void init() {
		script = ScriptInterface.getInstance().loadActionScript(name);
		load();
	}
	
	@Override
	public void use(DataPackage data) {
		data.getCaster().getStats().changeCurrentEnergy(-_cost);
		EffectOverTime e = new EffectOverTime(this, data);
		data.getTarget().addEffectOverTime(e);

	}

}
