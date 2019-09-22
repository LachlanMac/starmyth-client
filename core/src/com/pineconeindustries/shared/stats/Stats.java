package com.pineconeindustries.shared.stats;

public class Stats {

	private float hp, energy;
	private int strength, stamina, logic, accuracy, reflexes;
	private float currentHP, currentEnergy;

	public Stats() {
		this.hp = 100.0f;
		this.energy = 100.0f;
		this.strength = 10;
		this.stamina = 10;
		this.logic = 8;
		this.accuracy = 5;
		this.reflexes = 4;
		this.currentHP = hp;
		this.currentEnergy = energy;

	}

	public Stats(float hp, float energy, int strength, int stamina, int logic, int accuracy, int reflexes) {
		this.hp = hp;
		this.energy = energy;
		this.strength = strength;
		this.stamina = stamina;
		this.logic = logic;
		this.accuracy = accuracy;
		this.reflexes = reflexes;
		this.currentHP = hp;
		this.currentEnergy = energy;
	}

	public void setStats(float hp, float energy, int strength, int stamina, int logic, int accuracy, int reflexes) {
		this.hp = hp;
		this.energy = energy;
		this.strength = strength;
		this.stamina = stamina;
		this.logic = logic;
		this.accuracy = accuracy;
		this.reflexes = reflexes;
		this.currentHP = hp;
		this.currentEnergy = energy;
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

	public float getCurrentHP() {
		return currentHP;
	}

	public float getCurrentEnergy() {
		return currentEnergy;
	}

	public void changeCurrentHP(float val) {
		currentHP += val;
	}

	public void changeCurrentEnergy(float val) {
		currentEnergy += val;
		if (currentEnergy < 0)
			currentEnergy = 0;
		if (currentEnergy > energy)
			currentEnergy = energy;
	}

	public void setCurrentHP(float val) {
		this.currentHP = val;
		if (currentHP < 0)
			currentHP = 0;
		if (currentHP > hp)
			currentHP = hp;
	}

	public void setCurrentEnergy(float val) {
		this.currentEnergy = val;
	}
}
