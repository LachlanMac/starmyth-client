package com.pineconeindustries.server.clock;

import com.badlogic.gdx.Gdx;

public class Clock {

	private float galacticHour;
	private float speed = 0.05f;
	private static Clock instance = null;

	public static Clock getInstance() {

		if (instance == null) {
			instance = new Clock();
		}
		return instance;
	}

	private Clock() {

	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setGalacticHour(float galacticHour) {
		this.galacticHour = galacticHour;
	}

	public void tick() {

		galacticHour += Gdx.graphics.getDeltaTime() * speed;
		if (galacticHour >= 24) {
			galacticHour = 0;
		}
	}

	public Time getTime() {
		return new Time(galacticHour);
	}

}
