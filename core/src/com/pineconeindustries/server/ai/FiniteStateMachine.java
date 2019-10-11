package com.pineconeindustries.server.ai;

import java.util.HashMap;
import java.util.Map;

import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.states.CombatState;
import com.pineconeindustries.server.ai.states.DownedState;
import com.pineconeindustries.server.ai.states.IdleState;
import com.pineconeindustries.server.ai.states.MoveState;
import com.pineconeindustries.server.ai.states.SleepState;
import com.pineconeindustries.server.ai.states.State;
import com.pineconeindustries.server.ai.states.WanderState;
import com.pineconeindustries.server.ai.states.WorkState;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.structures.Station;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.professions.Schedule;

public class FiniteStateMachine {

	AStarPath path;
	NPC owner;

	private HashMap<String, State> states;

	State currentState;
	State scheduleState;

	Schedule schedule;

	Structure structure;
	DataPackage data;

	public FiniteStateMachine(NPC owner) {
		this.owner = owner;

		structure = Galaxy.getInstance().getSectorByID(owner.getSectorID()).getStructureByID(owner.getStructureID());
		currentState = new IdleState(this);
		schedule = Schedule.makeSchedule(owner.getProfession().getSchedule());
		states = new HashMap<String, State>();
		registerStates("WANDER", new WanderState(this));
		registerStates("IDLE", new IdleState(this));
		registerStates("WORK", new WorkState(this));
		registerStates("SLEEP", new SleepState(this));
		registerStates("MOVE", new MoveState(this));
		registerStates("DOWNED", new DownedState(this));
		registerStates("COMBAT", new CombatState(this));

		data = new DataPackage(owner, structure);

	}

	public void performAction() {

		checkSchedule();
		checkForCombat();
		checkForDowned();

		currentState.performAction();

	}

	public void checkForCombat() {

		if (owner.inCombat() && currentState != states.get("COMBAT")) {
			changeState(states.get("COMBAT"));
		}

	}

	public void checkForDowned() {

		if (owner.isDowned() && currentState != states.get("DOWNED")) {
			changeState(states.get("DOWNED"));
		}

	}

	public void checkSchedule() {

		if (owner.isDowned() || owner.inCombat()) {
			return;
		}

		scheduleState = states.get(Schedule.getString(schedule.getCurrentSchedule()));

		if (!scheduleState.getKey().equals(currentState.getKey())) {
			Log.aiStateLog(owner.getName() + " changing state to " + scheduleState.getKey());
			changeState(scheduleState);
		}

	}

	public void changeState(State state) {

		currentState.leaveState();
		currentState = state;
		currentState.enterState();
	}

	public void registerStates(String name, State state) {

		states.put(name, state);
	}

	public NPC getOwner() {
		return owner;
	}

	public Structure getStructure() {
		return structure;
	}

	public DataPackage getData() {
		return data;
	}
	
	

}
