package com.pineconeindustries.server.ai.roles;
import com.pineconeindustries.server.clock.Clock;
import com.pineconeindustries.server.clock.Time;

public class RoleSchedule {
	public static final int SLEEP = 0;
	public static final int GUARD = 1;
	public static final int PATROL = 2;
	public static final int WORK = 3;
	public static final int RECREATION = 4;
	public static final int GYM = 5;
	public static final int IDLE = 6;
	public static final int WANDER = 7;

	private int[] schedule;

	private RoleSchedule(int[] schedule) {
		this.schedule = schedule;
	}

	public static RoleSchedule makeSchedule(int[] schedule) {
		return new RoleSchedule(schedule);
	}

	public int getCurrentSchedule() {

		Time t = Clock.getInstance().getTime();

		int i = t.getHour();
		if (t.getMinute() >= 30) {
			i++;
		}

		return schedule[i];

	}

	public static String getString(int id) {
		switch (id) {

		case SLEEP:
			return "SLEEP";
		case IDLE:
			return "IDLE";
		case WORK:
			return "WORK";
		case GUARD:
			return "GUARD";
		case WANDER:
			return "WANDER";
		default:
			return "IDLE";
		}
	}

}
