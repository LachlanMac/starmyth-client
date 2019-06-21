package com.pineconeindustries.server.tests;


import com.pineconeindustries.server.net.players.PacketWriter;
import com.pineconeindustries.server.net.players.PlayerConnection;

public class TestSender extends Thread {

	PacketWriter writer;
	PlayerConnection conn;

	public TestSender(PlayerConnection conn) {
		this.conn = conn;

	}

	public void sendUDP(String data) {

	}

	public void sendTCP(String data) {

	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				sendTCP("This is from Test Sender (TCP)");
				sendUDP("This is from Test Sender (UDP)");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
