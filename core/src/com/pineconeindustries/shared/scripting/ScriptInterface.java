package com.pineconeindustries.shared.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ScriptInterface {

	private static ScriptInterface instance = null;

	private ScriptInterface() {

	}

	public static ScriptInterface getInstance() {

		if (instance == null) {
			instance = new ScriptInterface();
		}
		return instance;

	}

	public Globals loadActionScript(String name) {
		Globals globals = JsePlatform.standardGlobals();
		LuaValue v = globals.loadfile("actions/" + name + ".lua");
		v.call();
		return globals;
	}

	public Globals loadProfessionScript(String name) {
		Globals globals = JsePlatform.standardGlobals();
		LuaValue v = globals.loadfile("roles/" + name + ".lua");
		v.call();
		return globals;
	}

}
