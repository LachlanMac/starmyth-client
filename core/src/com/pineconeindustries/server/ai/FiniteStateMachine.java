package com.pineconeindustries.server.ai;

import java.util.HashMap;
import java.util.Map;

import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.states.IdleState;
import com.pineconeindustries.server.ai.states.MoveState;
import com.pineconeindustries.server.ai.states.SleepState;
import com.pineconeindustries.server.ai.states.State;
import com.pineconeindustries.server.ai.states.WanderState;
import com.pineconeindustries.server.ai.states.WorkState;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.data.Station;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.files.Files;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.NPC;

public class FiniteStateMachine {

	AStarPath path;
	NPC owner;

	private HashMap<String, State> states;
	private String schedule[];

	State currentState;
	State scheduleState;

	Structure structure;

	public FiniteStateMachine(NPC owner) {
		this.owner = owner;

		structure = (Station) Galaxy.getInstance().getSectorByID(owner.getSectorID())
				.getStructureByID(owner.getStructureID());

		currentState = new IdleState(this);
		schedule = Files.loadAIScript("scripts/ai/default_ai");
		states = new HashMap<String, State>();
		registerStates("WANDER", new WanderState(this));
		registerStates("IDLE", new IdleState(this));
		registerStates("WORK", new WorkState(this));
		registerStates("SLEEP", new SleepState(this));
		registerStates("MOVE", new MoveState(this));
		Clock.getInstance().setSpeed(0.3f);
	}

	public void performAction() {
		checkSchedule();

		currentState.performAction();

	}

	public void checkSchedule() {
		int hour = Clock.getInstance().getTime().getHour();

		scheduleState = states.get(schedule[hour]);

		if (!scheduleState.getKey().equals(currentState.getKey())) {
			Log.print("Changing state to " + scheduleState.getKey() + " because its hour " + hour);
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

	public Station getStructure() {
		return (Station) structure;
	}

}
