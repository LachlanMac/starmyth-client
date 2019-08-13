package com.pineconeindustries.server.net.players;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.log.Log;

public class PlayerConnectionListener extends Thread {

	private static final int THREAD_WAIT_TIME = 1000;

	private ServerSocket socket;
	private Sector sector;
	private boolean isRunning = false, shutdown = false;

	public PlayerConnectionListener(Sector sector) {
		this.sector = sector;

	}

	@Override
	public void run() {

		startListener();

		while (!shutdown) {

			if (isRunning) {

				try {

					Socket playerSocket = socket.accept();
					playerSocket.setSoTimeout(5000);
					PlayerConnection pc = new PlayerConnection(playerSocket, sector);
					sector.connectPlayer(pc);

				} catch (IOException e) {

					e.printStackTrace();
				}

			} else {
				try {
					Thread.sleep(THREAD_WAIT_TIME);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

		}

	}

	public void startListener() {

		Log.connection("Listening for Player Connections on port " + sector.getPort());

		stopListener();

		try {
			socket = new ServerSocket(sector.getPort());
			isRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopListener() {

		if (socket != null) {
			try {
				socket.close();
				socket = null;
				isRunning = false;
			} catch (IOException e) {

				Log.connection("Player Connection Listener stopped " + e.getMessage());
			}

		}

	}

	public void shutdownListener() {
		stopListener();
		shutdown = true;
	}

}
