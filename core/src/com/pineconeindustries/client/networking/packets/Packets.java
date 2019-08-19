package com.pineconeindustries.client.networking.packets;

public class Packets {

	public static final int MOVE_REQUEST_PACKET = 1100;
	public static final int MOVE_PACKET = 100;
	public static final int NPC_MOVE_PACKET = 101;

	// start these in the 8000's

	public static final int PLAYER_LIST_PACKET = 8001;
	public static final int NPC_LIST_PACKET = 8002;
	public static final int STRUCTURE_LIST_PACKET = 8004;

	public static final int STRUCTURE_INFO_REQUEST_PACKET = 8100;
	public static final int STRUCTURE_INFO_RESPONSE_PACKET = 8101;

	public static final int HEART_BEAT_PACKET = 9000;
	public static final int VERIFY_PACKET = 9001;
}
