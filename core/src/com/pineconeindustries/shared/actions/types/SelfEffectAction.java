package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public class SelfEffectAction extends ActionBase {

	public SelfEffectAction(int id, String name) {
		super(id, name);
		init();
	}

	public void init() {
		script = ScriptInterface.getInstance().loadScript(name);
		load();
	}

	@Override
	public void use(ActionPackage data) {

	}

}
