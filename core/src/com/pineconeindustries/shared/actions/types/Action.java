package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.utils.Base36;

public class Action {

	private ActionBase action;
	private float cooldown, sinceLastCast;
	private boolean ready = false;

	public Action(ActionBase action) {
		this.action = action;
		this.cooldown = this.action.getCooldown();
		this.sinceLastCast = cooldown;

	}

	public void update(float delta) {

		sinceLastCast += delta;

		if (sinceLastCast <= cooldown) {

			ready = false;
		} else {

			ready = true;
		}
	}

	public String getName() {
		return action.getName();
	}

	public void use(ActionPackage data) {
		if (ready) {
			action.use(data);
			sinceLastCast = 0;
			ready = false;
		} else {
			action.onCooldown(data);
			getCooldownRemaining();
		}
	}

	public boolean isReady() {
		return ready;
	}

	public ActionBase getActionBase() {
		return action;
	}

	public int getID() {
		return action.getID();
	}

	public char getCooldownRemaining() {

		if (ready) {
			return '0';
		} else {
			float remaining = (sinceLastCast / cooldown) * 36;
			char x = Base36.parse((int) remaining);
			if (x == '0')
				return '1';
			else
				return x;
		}

	}

}
