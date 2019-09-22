package com.pineconeindustries.shared.actions.effects;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.ActionPackage;
import com.pineconeindustries.shared.log.Log;

public class EffectOverTime {

	private float time = 0f;
	private ActionBase action;
	private float duration;
	private float interval;
	private float intervalDuration;
	private int counter = 0;
	private boolean running = true;

	private ActionPackage data;

	public EffectOverTime(ActionBase action, ActionPackage data) {
		this.action = action;
		this.data = data;
		duration = action.getLife();
		interval = action.getInterval();
		intervalDuration = duration / interval;
		Log.debug(data.getCaster().getName() + " cast " + action.getName() + " on " + data.getTarget().getName()
				+ "[ interval = " + interval + ", duration = " + duration + ", delta = " + intervalDuration + " ]");
	}

	public void update(float delta) {

		time += delta;
		float endTime = time;

		if (time >= intervalDuration) {
			time = endTime - intervalDuration;
			counter++;
			if (counter > interval) {
				running = false;
				action.onEnd(data);
			} else {

				action.loop(data);
			}

			return;
		}

	}

	public boolean isRunning() {
		return running;
	}

}
