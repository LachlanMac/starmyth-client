package com.pineconeindustries.client.networking;

import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;

public class Net {

	public static boolean isLocalPlayer(int playerID) {

		if (LogicController.getInstance().getPlayer().getID() == playerID)
			return true;
		else
			return false;

	}

	public static Player getLocalPlayer() {

		return LogicController.getInstance().getPlayer();

	}

	public static PlayerMP getPlayerMP(int id) {

		return LogicController.getInstance().getSector().getPlayerMPByID(id);

	}

	public static Connection getConnection() {
		return LogicController.getInstance().getConnection();
	}

}
