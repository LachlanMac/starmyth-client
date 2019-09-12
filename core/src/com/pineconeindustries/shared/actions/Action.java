package com.pineconeindustries.shared.actions;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.Entity;
import com.pineconeindustries.shared.objects.GameObject;
import com.pineconeindustries.shared.objects.Projectile;

public class Action {

	public static int projectileID = 1;
	private int id;
	private String name;
	private Globals instance;
	LuaValue actionInstance, sectorInstance, script, call;

	public enum type {
		direct, aoe, buff, heal, item
	};

	private float _speed = 0;
	private float _damage = 0;
	private float _cooldown = 0;
	private float _life = 0;
	private float _range = 0;
	private type _type;

	LuaFunction _ON_CAST, _ON_HIT, _ON_END, _ON_MISS;

	public Action(int id, String name) {
		this.id = id;
		this.name = name;
		instance = JsePlatform.standardGlobals();
		call = instance.loadfile("actions/" + name + ".lua");
		call.call();
		try {
			_ON_CAST = (LuaFunction) instance.get("_ON_CAST");
			_ON_HIT = (LuaFunction) instance.get("_ON_HIT");
			_ON_END = (LuaFunction) instance.get("_ON_END");
			_ON_MISS = (LuaFunction) instance.get("_ON_MISS");
			_type = parseType(instance.get("type").tojstring());
			_cooldown = instance.get("cooldown").tofloat();
			_life = instance.get("life").tofloat();
			_range = instance.get("range").tofloat();
			_damage = instance.get("damage").tofloat();
			_speed = instance.get("speed").tofloat();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public type parseType(String t) {

		switch (t) {

		case "direct":
			return type.direct;
		case "aoe":
			return type.aoe;
		case "buff":
			return type.buff;
		case "heal":
			return type.heal;
		case "item":
			return type.item;
		default:
			return type.direct;
		}

	}

	public void castDirect(Entity caster, GameObject target) {

		instance.set("target", CoerceJavaToLua.coerce(target));
		instance.set("caster", CoerceJavaToLua.coerce(caster));

		if (target == null) {
			return;
		}
		if (caster.isInRange(target, _range)) {
			ON_CAST();
			projectileID++;

			Vector2 tmpLoc = new Vector2(caster.getCenter().x, caster.getCenter().y);
			Vector2 tmpDest = new Vector2(target.getCenter().x, target.getCenter().y);
			Vector2 dir = new Vector2(tmpDest.sub(tmpLoc)).nor();
			Projectile p = new Projectile("pew", new Vector2(caster.getCenter().x, caster.getCenter().y),
					new Vector2(dir.x, dir.y), caster.getLayer(), projectileID, _speed, caster.getSectorID(), _life);
			Galaxy.getInstance().getSectorByID(caster.getSectorID()).addProjectile(p);

		} else {
			//
		}
	}

	public void ON_CAST() {

		_ON_CAST.call();
	}

	public void ON_HIT() {
		_ON_HIT.call();
	}

	public void ON_END() {
		_ON_END.call();
	}

	public int getID() {
		return id;
	}

	public String toString() {

		return new String("Action " + id + ": " + name + "   [ damage :" + _damage + ", cooldown :" + _cooldown
				+ ", range :" + _range);

	}

}
