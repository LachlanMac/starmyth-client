package com.pineconeindustries.shared.actions;

import java.util.Random;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.components.gameobjects.Entity;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.components.gameobjects.Projectile;
import com.pineconeindustries.shared.log.Log;

public class Action {

	public static int projectileID = 1;
	private int id;
	private String name;
	private Globals instance;
	LuaValue actionInstance, sectorInstance, script, call;
	private boolean ready = false;

	private Random random;

	public enum type {
		direct, aoe, buff, heal, item
	};

	private float _accuracy = 0;
	private float timePassed = 0;
	private float _speed = 0;
	private float _damage = 0;
	private float _cooldown = 0;
	private float _life = 0;
	private float _range = 0;
	private type _type;

	LuaFunction _ON_CAST, _ON_HIT, _ON_END, _ON_MISS;

	public Action(int id, String name) {
		random = new Random(System.currentTimeMillis());
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
			_accuracy = instance.get("accuracy").tofloat();
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

			float accuracyX = (100 - _accuracy);
			float accuracyY = (100 - _accuracy);
			float accuracy = caster.getStats().getAccuracy();
			float xOff = accuracy + random.nextFloat() * (accuracyX - accuracy);
			float yOff = accuracy + random.nextFloat() * (accuracyY - accuracy);
			int flipX = random.nextInt(2);
			int flipY = random.nextInt(2);

			if (flipX == 1) {
				xOff = -xOff;
			}
			if (flipY == 1) {
				yOff = -yOff;
			}

			Vector2 tmpLoc = new Vector2(caster.getCenter().x, caster.getCenter().y);
			Vector2 tmpDest = new Vector2(target.getCenter().x + xOff, target.getCenter().y + yOff);
			Vector2 dir = new Vector2(tmpDest.sub(tmpLoc)).nor();
			Projectile p = new Projectile("pew", new Vector2(caster.getCenter().x, caster.getCenter().y),
					new Vector2(dir.x, dir.y), caster.getLayer(), projectileID, _speed, caster.getSectorID(),
					caster.getStructureID(), _life, this, caster);
			Galaxy.getInstance().getSectorByID(caster.getSectorID()).addProjectile(p);
			ready = false;
		} else {
			//
		}
	}

	public void update(float delta) {
		if (!ready) {
			this.timePassed += delta;
		}

		if (timePassed >= _cooldown) {
			timePassed = 0;
			ready = true;
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

	public boolean isReady() {
		return ready;
	}
}
