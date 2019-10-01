package com.pineconeindustries.shared.actions.effects;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.ActionPackage;
import com.pineconeindustries.shared.log.Log;

public class EffectOverTime {

	protected boolean passive = false;
	protected float time = 0f;
	protected ActionBase action;
	protected float duration;
	protected float interval;
	protected float intervalDuration;
	protected int counter = 0;

	protected boolean running = true;

	protected ActionPackage data;

	public EffectOverTime(ActionBase action, ActionPackage data) {
		this.action = action;
		this.data = data;
		duration = action.getLife();
		interval = action.getInterval();

		if (duration == 0) {
			intervalDuration = interval;
			passive = true;
		} else {
			intervalDuration = duration / interval;
		}

	}

	public void update(float delta) {

		time += delta;
		float endTime = time;

		if (time >= intervalDuration) {
			time = endTime - intervalDuration;
			counter++;

			if (counter > interval) {
				if (!passive) {
					running = false;
					action.onEnd(data);
				} else {
					counter = 0;
					action.loop(data);
				}
			} else {

				action.loop(data);
			}

			return;
		}

	}

	public boolean isRunning() {
		return running;
	}

	public String getActionName() {
		return action.getName();
	}

}
