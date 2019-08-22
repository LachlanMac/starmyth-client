package com.pineconeindustries.client.networking.packets;

import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.server.net.packets.types.TCPPacket;

public class PacketRequester extends Thread {

	private boolean successful = false;
	private boolean failed = false;

	private int delay, attempts;

	TCPPacket packet;

	public PacketRequester(TCPPacket packet, int delay, int attempts) {
		this.packet = packet;
		this.delay = delay;
		this.attempts = attempts;
	}

	public void run() {

		int counter = 0;

		while (successful == false) {

			try {
				LogicController.getInstance().getConnection().sendTCP(packet);
				Thread.sleep(delay * 1000);
				checkValidity();
				counter++;
				if (counter >= attempts) {
					successful = false;
					failed = true;
					return;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void kill() {
		successful = true;
	}

	public void checkValidity() {
	}

	public void receivedGoodData() {
		successful = true;
		failed = false;
	}

	public void receivedBadData() {
		successful = false;
		failed = true;
	}

	public boolean wasSucessful() {
		return successful;
	}

	public boolean requestFailed() {
		return failed;
	}

}
