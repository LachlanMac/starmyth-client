package com.pineconeindustries.shared.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import com.pineconeindustries.shared.log.Log;

public abstract class GameScript {

	protected int id;
	protected String name;
	protected Globals script;

	public GameScript(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public LuaFunction registerLuaFunction(LuaFunction function, String name) {

		try {
			return function = (LuaFunction) script.get(name);

		} catch (Exception e) {
			Log.debug("Could not load function " + name + " in " + this.name);
			return null;
		}

	}

	public boolean registerLuaBoolean(boolean value, String name) {

		boolean initialValue = value;
		try {
			return value = script.get(name).toboolean();
		} catch (Exception e) {
			Log.debug("Could not parse variable " + e.getMessage());
			return initialValue;
		}

	}

	public int[] registerLuaArray(int[] value, String name) {

		int[] initialValue = value;
		System.out.println("XXX   ");
		for (int i = 0; i < initialValue.length; i++) {
			System.out.print(" " + value[i]);
		}
		try {
			LuaTable v = script.get(name).checktable();

			System.out.println("registering " + name);
			for (int i = 0; i < initialValue.length; i++) {

				initialValue[i] = v.get(i).toint();
			}

			return initialValue;
		} catch (LuaError e) {
			Log.debug("Lua Error");

			return initialValue;
		} catch (Exception e) {

			Log.debug("Could not parse variable " + e.getMessage());
			return initialValue;
		}

	}

	public float registerLuaFloat(float value, String name, float DEFAULT_VALUE) {

		try {
			return value = script.get(name).tofloat();
		} catch (Exception e) {

			Log.debug("Could not parse variable " + e.getMessage());
			return DEFAULT_VALUE;
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

	public int[] registerLuaArray(int[] value, String name, int[] defaultValue) {

		int[] initialValue = new int[defaultValue.length];
		try {
			LuaTable v = script.get(name).checktable();
			for (int i = 0; i < initialValue.length; i++) {

				initialValue[i] = v.get(i).toint();
			}

			return initialValue;
		} catch (LuaError e) {

			return defaultValue;

		} catch (Exception e) {

			Log.debug("Could not parse variable " + e.getMessage());
			return initialValue;
		}
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}
}
