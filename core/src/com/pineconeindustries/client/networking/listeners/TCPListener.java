package com.pineconeindustries.client.networking.listeners;

import java.io.BufferedReader;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;

public class TCPListener extends Thread {

	private ArrayBlockingQueue<String> tcpQueue;
	private BufferedReader in;
	private boolean isRunning = false;

	public int ERROR_CODE = 0;

	public TCPListener(BufferedReader in) {
		this.in = in;
	}

	public void setErrorCode(int i) {
		this.ERROR_CODE = i;
	}

	@Override
	public void run() {

		while (isRunning) {

			try {
				Thread.sleep(Global.THREAD_WAIT_TIME);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			String data;

			try {
				while ((data = in.readLine()) != null) {

					tcpQueue.add(data);
				}
			} catch (Exception e) {
				Log.serverLog("Error in client TCP Listener " + e.getMessage() + " ERROR FROM " + ERROR_CODE);
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
