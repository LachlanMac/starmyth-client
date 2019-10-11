package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.components.gameobjects.Entity;
import com.pineconeindustries.shared.utils.Base36;

public class Action {

	private ActionBase action;
	private float cooldown, sinceLastCast, cost;
	private float toggleTime;
	private boolean ready = false;
	private boolean toggle, toggled;

	public Action(ActionBase action) {
		this.action = action;
		this.cooldown = action.getCooldown();
		this.sinceLastCast = cooldown;
		this.toggle = action.canToggle();
		this.toggleTime = action.getMinimumToggleTime();
		this.toggled = false;
		this.cost = action.getCost();
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

	public void use(DataPackage data) {


		if (ready) {

			if (hasEnoughEnergy(data.getCaster())) {
				action.use(data);
				sinceLastCast = 0;
				toggled = false;
				ready = false;
			} else {
				action.onNotEnoughEnergy(data);
			}
		} else {
			if (toggle && !toggled) {
				if (sinceLastCast >= toggleTime) {
					action.onToggleOff(data);
					sinceLastCast = 0.01f;
					toggled = true;
				}

			} else {
				if (sinceLastCast >= 0) {
					action.onCooldown(data);
				}
			}

		}
	}

	public boolean hasEnoughEnergy(Entity caster) {

		return caster.getStats().getCurrentEnergy() >= cost;

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
