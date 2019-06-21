package com.pineconeindustries.client.networking.listeners;

import java.io.BufferedReader;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.shared.log.Log;

public class TCPListener extends Thread {

	private ArrayBlockingQueue<String> tcpQueue;
	private BufferedReader in;
	private boolean isRunning = false;

	public TCPListener(BufferedReader in) {
		this.in = in;
	}

	@Override
	public void run() {

		while (isRunning) {

			String data;

			try {
				while ((data = in.readLine()) != null) {

					tcpQueue.add(data);
				}
			} catch (Exception e) {
				Log.serverLog("Error in client TCP Listener " + e.getMessage());
			}

		}

	}

	public void startListener(ArrayBlockingQueue<String> tcpQueue) {
		this.tcpQueue = tcpQueue;
		isRunning = true;
		this.start();
	}

	public void stopListener() {
		isRunning = false;
		tcpQueue.clear();

	}

}
