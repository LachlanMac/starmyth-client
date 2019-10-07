package com.pineconeindustries.server.clock;

public class Time {

	private int hour, minute, second;

	public Time(float tempHour) {

		this.hour = (int) tempHour;
		this.minute = (int) (60 * (tempHour - hour));
		this.second = (int) (3600 * (tempHour - hour));

	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public String toString() {
		return new String(hour + ":" + minute + ":" + second);
	}

}
