package com.pineconeindustries.shared.stats;

public class Stats {

	private float hp, energy;
	private int strength, stamina, logic, accuracy, reflexes;

	public Stats() {
		this.hp = 100.0f;
		this.energy = 100.0f;
		this.strength = 10;
		this.stamina = 10;
		this.logic = 8;
		this.accuracy = 5;
		this.reflexes = 4;

	}

	public Stats(float hp, float energy, int strength, int stamina, int logic, int accuracy, int reflexes) {
		this.hp = hp;
		this.energy = energy;
		this.strength = strength;
		this.stamina = stamina;
		this.logic = logic;
		this.accuracy = accuracy;
		this.reflexes = reflexes;
	}
	
	public void setStats(float hp, float energy, int strength, int stamina, int logic, int accuracy, int reflexes) {
		this.hp = hp;
		this.energy = energy;
		this.strength = strength;
		this.stamina = stamina;
		this.logic = logic;
		this.accuracy = accuracy;
		this.reflexes = reflexes;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getLogic() {
		return logic;
	}

	public void setLogic(int logic) {
		this.logic = logic;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getReflexes() {
		return reflexes;
	}

	public void setReflexes(int reflexes) {
		this.reflexes = reflexes;
	}

}
