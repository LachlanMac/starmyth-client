package com.pineconeindustries.shared.actions.types;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public abstract class ActionBase {

	protected final static float DEFAULT_ACCURACY = 50f;
	protected final static float DEFAULT_SPEED = 100f;
	protected final static float DEFAULT_DAMAGE = 10f;
	protected final static float DEFAULT_COOLDOWN = 5f;
	protected final static float DEFAULT_LIFE = 10f;
	protected final static float DEFAULT_RANGE = 700f;
	protected final static float DEFAULT_CAST_TIME = 0f;
	protected final static float DEFAULT_BUFF_TIME = 10f;
	protected final static float DEFAULT_MAGNITUDE = 2f;
	protected final static int DEFAULT_INTERVAL = 5;
	protected float _accuracy, _speed, _damage, _cooldown, _life, _range, _cast_time, _buff_time, _magnitude;
	protected int _interval;

	public enum type {
		DIRECT_PROJECTILE, TARGETED_EFFECT, SELF_EFFECT;
	}

	protected String name;
	protected int id;
	protected Globals script;

	protected LuaFunction _ON_CAST, _ON_HIT_ENTITY, _ON_END, _ON_MISS, _ON_HIT_TILE, _ON_COOLDOWN, _LOOP;

	public ActionBase(int id, String name) {
		this.name = name;
		this.id = id;

		_accuracy = DEFAULT_ACCURACY;
		_speed = DEFAULT_SPEED;
		_damage = DEFAULT_DAMAGE;
		_cooldown = DEFAULT_COOLDOWN;
		_life = DEFAULT_LIFE;
		_range = DEFAULT_RANGE;
		_cast_time = DEFAULT_CAST_TIME;
		_buff_time = DEFAULT_BUFF_TIME;
		_magnitude = DEFAULT_MAGNITUDE;
		_interval = DEFAULT_INTERVAL;

	}

	public void onHitEntity(ActionPackage data) {
		if (_ON_HIT_ENTITY != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_HIT_ENTITY.call(info);
		}

	}

	public void onEnd(ActionPackage data) {
		if (_ON_END != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_END.call(info);
		}
	}

	public void onMiss(ActionPackage data) {
		if (_ON_MISS != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_MISS.call(info);
		}

	}

	public void onCast(ActionPackage data) {
		if (_ON_CAST != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_CAST.call(info);
		}
	}

	public void onHitTile(ActionPackage data) {
		if (_ON_HIT_TILE != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_HIT_TILE.call(info);
		}
	}

	public void onCooldown(ActionPackage data) {
		if (_ON_COOLDOWN != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_COOLDOWN.call(info);
		}
	}

	public void loop(ActionPackage data) {
		if (_LOOP != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_LOOP.call(info);
		}
	}

	public LuaFunction registerLuaFunction(LuaFunction function, String name) {

		try {
			return function = (LuaFunction) script.get(name);

		} catch (Exception e) {
			Log.debug("Could not load function " + name + " in " + this.name);
			return null;
		}

	}

	public float registerLuaFloat(float value, String name) {

		float initialValue = value;

		try {
			return value = script.get(name).tofloat();
		} catch (Exception e) {
			Log.debug("Could not parse variable " + e.getMessage());
			return initialValue;
		}

	}

	public int registerLuaInt(int value, String name) {
		int initialValue = value;
		try {
			return value = script.get(name).toint();
		} catch (Exception e) {
			Log.debug("Could not parse variable " + e.getMessage());
			return initialValue;
		}

	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static type getType(String identifier) {

		switch (identifier) {

		case "direct":
			return type.DIRECT_PROJECTILE;
		case "targetedeffect":
			return type.TARGETED_EFFECT;
		case "selfeffect":
			return type.SELF_EFFECT;
		default:
			return type.DIRECT_PROJECTILE;
		}

	}

	public abstract void use(ActionPackage data);

	public float getAccuracy() {
		return _accuracy;
	}

	public float getSpeed() {
		return _speed;
	}

	public float getDamage() {
		return _damage;
	}

	public float getCooldown() {
		return _cooldown;
	}

	public float getLife() {
		return _life;
	}

	public float getRange() {
		return _range;
	}

	public int getInterval() {
		return _interval;
	}

	public float getMagnitude() {
		return _magnitude;
	}

	public void load() {
		Log.debug("Loading Functions and Variables for " + name);
		_ON_CAST = registerLuaFunction(_ON_CAST, "_ON_CAST");
		_ON_HIT_ENTITY = registerLuaFunction(_ON_HIT_ENTITY, "_ON_HIT_ENTITY");
		_ON_END = registerLuaFunction(_ON_END, "_ON_END");
		_ON_MISS = registerLuaFunction(_ON_MISS, "_ON_MISS");
		_ON_HIT_TILE = registerLuaFunction(_ON_HIT_TILE, "_ON_HIT_TILE");
		_LOOP = registerLuaFunction(_LOOP, "_LOOP");
		_ON_COOLDOWN = registerLuaFunction(_ON_COOLDOWN, "_ON_COOLDOWN");

		_cooldown = registerLuaFloat(_cooldown, "cooldown");
		_accuracy = registerLuaFloat(_accuracy, "accuracy");
		_life = registerLuaFloat(_life, "life");
		_range = registerLuaFloat(_range, "range");
		_damage = registerLuaFloat(_damage, "damage");
		_speed = registerLuaFloat(_speed, "speed");
		_cast_time = registerLuaFloat(_cast_time, "casttime");
		_buff_time = registerLuaFloat(_buff_time, "bufftime");
		_magnitude = registerLuaFloat(_magnitude, "magnitude");
		_interval = registerLuaInt(_interval, "interval");
		Log.debug("Finished Loading");

	}

}
