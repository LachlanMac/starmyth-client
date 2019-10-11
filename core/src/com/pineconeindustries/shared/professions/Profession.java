package com.pineconeindustries.shared.professions;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.actions.ActionSet;
import com.pineconeindustries.shared.actions.types.Action;

import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.actions.types.DirectProjectileAction;
import com.pineconeindustries.shared.actions.types.TargetedEffectAction;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.GameScript;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public class Profession extends GameScript {

	private ActionSet actions;

	private static final int[] DEFAULT_SCHEDULE = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0 };
	private int[] _schedule;
	private LuaFunction _WORK, _SLEEP, _IDLE, _DOWNED, _RECREATION, _ON_LOAD, _WANDER, _COMBAT;

	public Profession(int id, String name) {
		super(id, name);

		loadScript();
	}

	public void loadScript() {

		script = ScriptInterface.getInstance().loadProfessionScript(name);
		this._schedule = DEFAULT_SCHEDULE;
		load();
		onLoad();

	}

	public void onLoad() {
		if (_ON_LOAD != null) {
			_ON_LOAD.call();
		}
	}

	public void idle(DataPackage data) {
		if (_IDLE != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_IDLE.call(info);
		}
	}

	public void downed(DataPackage data) {
		if (_DOWNED != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_DOWNED.call(info);
		}
	}

	public void wander(DataPackage data) {
		if (_WANDER != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_WANDER.call(info);
		}
	}

	public void work(DataPackage data) {
		if (_WORK != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_WORK.call(info);
		}
	}

	public void combat(DataPackage data) {

		if (_COMBAT != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_COMBAT.call(info);
		}

	}

	public void sleep(DataPackage data) {
		if (_SLEEP != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_SLEEP.call(info);
		}
	}

	public void recreation(DataPackage data) {
		if (_RECREATION != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_RECREATION.call(info);
		}
	}

	public ActionSet getActionSetByProfession() {

		ActionSet actionSet = new ActionSet();
		actionSet.addAction(new Action((DirectProjectileAction) ActionManager.getInstance().getActionByID(1)));
		actionSet.addAction(new Action((TargetedEffectAction) ActionManager.getInstance().getActionByID(4)));
		return actionSet;

	}

	public void load() {

		_schedule = registerLuaArray(_schedule, "_schedule", DEFAULT_SCHEDULE);
		_IDLE = registerLuaFunction(_WORK, "_WORK");
		_WORK = registerLuaFunction(_IDLE, "_IDLE");
		_SLEEP = registerLuaFunction(_SLEEP, "_SLEEP");
		_RECREATION = registerLuaFunction(_RECREATION, "_RECREATION");
		_IDLE = registerLuaFunction(_IDLE, "_IDLE");
		_DOWNED = registerLuaFunction(_DOWNED, "_DOWNED");
		_WANDER = registerLuaFunction(_WANDER, "_WANDER");
		_COMBAT = registerLuaFunction(_COMBAT, "_COMBAT");
		_ON_LOAD = registerLuaFunction(_ON_LOAD, "_ON_LOAD");

	}

	public void loadDefaults() {

	}

	public int[] getSchedule() {
		return _schedule;
	}

}
