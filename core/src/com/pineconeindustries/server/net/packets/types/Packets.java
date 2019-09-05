package com.pineconeindustries.server.net.packets.types;

public class Packets {

	public static final int MOVE_REQUEST_PACKET = 1100;

	public static final int MOVE_PACKET = 100;
	public static final int NPC_MOVE_PACKET = 101;
	public static final int INPUT_CHANGE_PACKET = 102;

	public static final int SHIP_HIT_PACKET = 110;

	public static final int SHIP_STOP_PACKET = 111;
	public static final int SHIP_START_PACKET = 112;
	public static final int SHIP_EMERGENCY_PACKET = 113;

	public static final int CHAT_SAY_PACKET = 200;
	public static final int CHAT_GROUP_PACKET = 201;
	public static final int CHAT_INTERCOM_PACKET = 202;
	public static final int CHAT_TELL_PACKET = 203;
	public static final int CHAT_COMMAND_PACKET = 204;

	// start these in the 8000's

	public static final int PLAYER_LIST_PACKET = 8001;
	public static final int NPC_LIST_PACKET = 8002;
	public static final int STRUCTURE_LIST_PACKET = 8004;

	public static final int STRUCTURE_ELEVATOR_REQUEST_PACKET = 8100;
	public static final int STRUCTURE_ELEVATOR_RESPONSE_PACKET = 8101;
	public static final int STRUCTURE_LAYER_REQUEST_PACKET = 8102;
	public static final int STRUCTURE_LAYER_RESPONSE_PACKET = 8103;

	public static final int HEART_BEAT_PACKET = 9000;
	public static final int VERIFY_PACKET = 9001;
}
