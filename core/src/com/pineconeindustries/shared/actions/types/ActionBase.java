package com.pineconeindustries.shared.actions.types;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.GameScript;
import com.pineconeindustries.shared.scripting.ScriptInterface;

public abstract class ActionBase extends GameScript {

	protected final static boolean DEFAULT_TOGGLE = false;
	protected final static boolean DEFAULT_UNIQUE = true;
	protected final static float DEFAULT_MINIMUM_TOGGLE_TIME = 1f;
	protected final static float DEFAULT_ACCURACY = 50f;
	protected final static float DEFAULT_SPEED = 100f;
	protected final static float DEFAULT_DAMAGE = 10f;
	protected final static float DEFAULT_COOLDOWN = 5f;
	protected final static float DEFAULT_LIFE = 10f;
	protected final static float DEFAULT_COST = 0f;
	protected final static float DEFAULT_RANGE = 200f;
	protected final static float DEFAULT_CAST_TIME = 0f;
	protected final static float DEFAULT_BUFF_TIME = 10f;
	protected final static float DEFAULT_MAGNITUDE = 2f;
	protected final static int DEFAULT_INTERVAL = 5;
	protected float _accuracy, _speed, _damage, _cooldown, _life, _range, _cast_time, _buff_time, _magnitude,
			_minimum_toggle_time, _cost;
	protected int _interval;
	protected boolean aggressive = false;
	protected boolean _unique, _toggle;

	public enum type {
		DIRECT_PROJECTILE, TARGETED_EFFECT, SELF_EFFECT, PICKUP_EFFECT;
	}

	protected LuaFunction _ON_CAST, _ON_HIT_ENTITY, _ON_END, _ON_MISS, _ON_HIT_TILE, _ON_COOLDOWN, _LOOP, _ON_DROP,
			_ON_TOGGLE_OFF, _ON_FAIL, _ON_PICKUP, _ON_NOT_ENOUGH_ENERGY;

	public ActionBase(int id, String name) {
		super(id, name);

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
		_unique = DEFAULT_UNIQUE;
		_minimum_toggle_time = DEFAULT_MINIMUM_TOGGLE_TIME;
		_cost = DEFAULT_COST;

	}

	public void onHitEntity(DataPackage data) {
		if (_ON_HIT_ENTITY != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_HIT_ENTITY.call(info);
		}

	}

	public void onNotEnoughEnergy(DataPackage data) {
		if (_ON_NOT_ENOUGH_ENERGY != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_NOT_ENOUGH_ENERGY.call(info);
		}
	}

	public void onEnd(DataPackage data) {
		if (_ON_END != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_END.call(info);
		}
	}

	public void onMiss(DataPackage data) {
		if (_ON_MISS != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_MISS.call(info);
		}

	}

	public void onPickup(DataPackage data) {
		if (_ON_PICKUP != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_PICKUP.call(info);
		}
	}

	public void onToggleOff(DataPackage data) {
		if (_ON_TOGGLE_OFF != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_TOGGLE_OFF.call(info);
		}
	}

	public void onCast(DataPackage data) {
		if (_ON_CAST != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_CAST.call(info);
		}
	}

	public void onHitTile(DataPackage data) {
		if (_ON_HIT_TILE != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_HIT_TILE.call(info);
		}
	}

	public void onDrop(DataPackage data) {
		if (_ON_DROP != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_DROP.call(info);
		}
	}

	public void onFail(DataPackage data) {
		if (_ON_FAIL != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_FAIL.call(info);
		}
	}

	public void onCooldown(DataPackage data) {
		if (_ON_COOLDOWN != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_ON_COOLDOWN.call(info);
		}
	}

	public void loop(DataPackage data) {
		if (_LOOP != null) {
			LuaValue info = CoerceJavaToLua.coerce(data);
			_LOOP.call(info);
		}
	}

	public static type getType(String identifier) {

		switch (identifier) {
		case "direct":
			return type.DIRECT_PROJECTILE;
		case "targetedeffect":
			return type.TARGETED_EFFECT;
		case "selfeffect":
			return type.SELF_EFFECT;
		case "pickupeffect":
			return type.PICKUP_EFFECT;
		default:
			return type.DIRECT_PROJECTILE;
		}

	}

	public abstract void use(DataPackage data);

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

	public boolean isUnique() {
		return _unique;
	}

	public boolean canToggle() {
		return _toggle;
	}

	public float getMinimumToggleTime() {
		return _minimum_toggle_time;
	}

	public float getCost() {
		return _cost;
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
		_ON_FAIL = registerLuaFunction(_ON_FAIL, "_ON_FAIL");
		_ON_DROP = registerLuaFunction(_ON_DROP, "_ON_DROP");
		_ON_PICKUP = registerLuaFunction(_ON_PICKUP, "_ON_PICKUP");
		_ON_TOGGLE_OFF = registerLuaFunction(_ON_TOGGLE_OFF, "_ON_TOGGLE_OFF");
		_ON_NOT_ENOUGH_ENERGY = registerLuaFunction(_ON_NOT_ENOUGH_ENERGY, "_ON_NOT_ENOUGH_ENERGY");
		_cost = registerLuaFloat(_cost, "cost", DEFAULT_COST);
		_cooldown = registerLuaFloat(_cooldown, "cooldown", DEFAULT_COOLDOWN);
		_accuracy = registerLuaFloat(_accuracy, "accuracy", DEFAULT_ACCURACY);
		_life = registerLuaFloat(_life, "life", DEFAULT_LIFE);
		_range = registerLuaFloat(_range, "range", DEFAULT_RANGE);
		_damage = registerLuaFloat(_damage, "damage", DEFAULT_DAMAGE);
		_speed = registerLuaFloat(_speed, "speed", DEFAULT_SPEED);
		_cast_time = registerLuaFloat(_cast_time, "casttime", DEFAULT_CAST_TIME);
		_buff_time = registerLuaFloat(_buff_time, "bufftime", DEFAULT_BUFF_TIME);
		_magnitude = registerLuaFloat(_magnitude, "magnitude", DEFAULT_MAGNITUDE);
		_interval = registerLuaInt(_interval, "interval");
		_unique = registerLuaBoolean(_unique, "unique");
		_toggle = registerLuaBoolean(_toggle, "toggle");
		_minimum_toggle_time = registerLuaFloat(_minimum_toggle_time, "togglemin", DEFAULT_MINIMUM_TOGGLE_TIME);
		Log.debug("Finished Loading");

	}

	public boolean isAggressive() {
		return aggressive;
	}

}
