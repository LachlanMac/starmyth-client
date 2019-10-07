package com.pineconeindustries.shared.actions.effects;

import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.log.Log;

public class EffectPickup extends EffectOverTime {

	public EffectPickup(ActionBase action, DataPackage data) {
		super(action, data);

		if (data.getTarget().isHoldable()) {
			action.onPickup(data);
			GameObject.pickup(data.getCaster(), data.getTarget());
		} else {
			data.setError("Target cannot be picked up");
			action.onFail(data);
		}
	}

	public void update(float delta) {

		time += delta;
		float endTime = time;

		if (time >= intervalDuration) {
			time = endTime - intervalDuration;
			counter++;

			if (data.getTarget().getHolder() == null || data.getCaster().getHeld() == null) {
				running = false;
				action.onDrop(data);
				GameObject.drop(data.getCaster(), data.getTarget());
				action.onEnd(data);
				return;
			}
			if (counter > interval) {
				running = false;
				action.onDrop(data);
				GameObject.drop(data.getCaster(), data.getTarget());
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
