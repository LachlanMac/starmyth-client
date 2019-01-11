package com.pineconeindustries.client.networking;

import java.text.DecimalFormat;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pineconeindustries.client.data.ShipData;
import com.pineconeindustries.client.log.Log;
import com.pineconeindustries.client.manager.Game;
import com.pineconeindustries.client.objects.NPC;
import com.pineconeindustries.client.objects.Player;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.client.objects.PlayerMPLight;
import com.pineconeindustries.client.objects.Ship;
import com.pineconeindustries.client.ui.UserInterface;
import com.pineconeindustries.client.zones.Zone;

public class NetworkLayer {

	static int ROOM_INFO_ATTEMPTS = 0;

	DecimalFormat df = new DecimalFormat("#.00");

	Player player;
	Zone zone;
	Connection conn;
	Game game;
	UserInterface ui;

	Executor executor;
	
	public NetworkLayer(Player player, Zone zone, Connection conn, Game game, UserInterface ui) {
		this.player = player;
		this.zone = zone;
		this.conn = conn;
		this.game = game;
		this.ui = ui;
		this.ui.getChat().setLnet(this);
		executor = Executors.newFixedThreadPool(5);
		sendMove(player.getPlayerID(), new Vector2(0, 0));
	}

	public void receiveMove(int id, Vector2 newLoc, Vector2 lastDir, float velocity) {

		// check if move is for this player
		if (player.getPlayerID() == id) {

			player.setLoc(newLoc);
			player.setLastDirectionFaced(lastDir);
			player.setVelocity(velocity);
			player.setFramesSinceLastMove(0);

		} else {

			PlayerMP playerMP = zone.getPlayerByID(id);

			if (playerMP != null) {
				playerMP.setLoc(newLoc);
				playerMP.setLastDirectionFaced(lastDir);
				playerMP.setVelocity(velocity);
				playerMP.setFramesSinceLastMove(0);
			}
		}

	}

	public void sendShipLayoutRequest(Ship s) {

		conn.send(
				new Packet(player.getPlayerID(), Integer.toString(s.getData().getShipID()), Packet.SHIP_LAYOUT_PACKET));
		executor.execute(new WaitForShipData(s));

	}

	public void sendMove(int id, Vector2 mov) {

		float clampedX = mov.x * Gdx.graphics.getDeltaTime();
		float clampedY = mov.y * Gdx.graphics.getDeltaTime();

		String data = clampedX + "=" + clampedY;
		conn.send(new Packet(id, data, Packet.MOVE_PACKET));

	}

	public void sendRoomDataRequest(Ship s) {

		conn.send(new Packet(player.getPlayerID(), Integer.toString(s.getData().getShipID()),
				Packet.SHIP_ROOM_INFO_PACKET));

		executor.execute(new WaitForRoomData(s, this));

	}

	public void sendLocalChat(String message) {

		conn.send(new Packet(player.getPlayerID(), message, Packet.LOCAL_CHAT_PACKET));

	}

	public void sendAdminCmd(String message) {
		conn.send(new Packet(player.getPlayerID(), message, Packet.ADMIN_PACKET));
	}

	public void processPacket(Packet p) {

		if (!p.decode()) {

			Log.print("ERROR PARSING PACKET");
			return;
		}

		switch (p.getType()) {

		case Packet.NPC_INFO_PACKET:

			// Console: [425=1=4150.0=710.0=100]
			String[] npcInfoSplit = p.getData().split("=");

			int npcID = Integer.parseInt(npcInfoSplit[0]);
			String npcName = npcInfoSplit[1];
			int npcFaction = Integer.parseInt(npcInfoSplit[2]);
			float xNPCLoc = Float.parseFloat(npcInfoSplit[3]);
			float yNPCLoc = Float.parseFloat(npcInfoSplit[4]);
			int structureID = Integer.parseInt(npcInfoSplit[5]);

			NPC npc = zone.getNPCByID(npcID);

			if (npc == null) {
				NPC n = new NPC(npcID, npcName, new Vector2(xNPCLoc, yNPCLoc), game, npcFaction, structureID);
				zone.addNPC(n);
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						zone.addNPC(n);

					}
				});

			} else {

				npc.setLoc(new Vector2(xNPCLoc, yNPCLoc));

			}

			break;

		case Packet.SHIP_ROOM_INFO_PACKET:

			RXShipRoomInfoPacket(p.getData());

			break;

		case Packet.SHIP_INFO_PACKET:

			String[] shipInfoSplit = p.getData().split("=");

			for (int i = 0; i < shipInfoSplit.length; i++) {

				String[] shipData = shipInfoSplit[i].split("-");

				int shipID = Integer.parseInt(shipData[0]);

				Ship s = zone.getShipByID(shipID);

				if (s == null) {
					String shipName = shipData[1];
					String shipClass = shipData[2];
					int localX = Integer.parseInt(shipData[3]);
					int localY = Integer.parseInt(shipData[4]);

					int checksum = Integer.parseInt(shipData[5]);

					String path = shipID + "-" + shipName + ".txt";

					ShipData d = new ShipData(shipID, zone.getSectorID(), shipName, shipClass, localX, localY, path,
							checksum);
					Ship ship = new Ship(d.getShipName(), new Vector2(0, 0), game, d);

					sendRoomDataRequest(ship);

					Gdx.app.postRunnable(new Runnable() {

						@Override
						public void run() {
							zone.addShip(ship);

						}
					});

				}

			}

			break;

		case Packet.SHIP_LAYOUT_PACKET:

			String[] layoutSplit = p.getData().split("=");

			int shipID = Integer.parseInt(layoutSplit[0]);

			Ship s = zone.getShipByID(shipID);
			if (s != null) {

				s.getData().writeShipData(layoutSplit[1]);
				s.getData().loadShipLayout();

			}

			break;

		case Packet.MOVE_PACKET:

			String[] dataSplit = p.getData().split("=");

			try {

				float x = Float.parseFloat(dataSplit[0]);
				float y = Float.parseFloat(dataSplit[1]);

				float xDir = Float.parseFloat(dataSplit[2]);
				float yDir = Float.parseFloat(dataSplit[3]);

				float velocity = Float.parseFloat(dataSplit[4]);

				int structID = Integer.parseInt(dataSplit[5]);

				player.setStructureID(structID);

				receiveMove(p.getPlayerID(), new Vector2(x, y), new Vector2(xDir, yDir), velocity);

			} catch (NumberFormatException e) {

				e.printStackTrace();
				return;
			}

			break;

		case Packet.ZONE_PLAYER_INFO_PACKET:

			String[] infoSplit = p.getData().split("=");

			for (int i = 0; i < infoSplit.length; i++) {

				String[] playerData = infoSplit[i].split(",");

				int id = Integer.parseInt(playerData[0]);

				String name = playerData[1];

				if (id == player.getPlayerID()) {
					// do nothing because its the player
				} else {

					PlayerMPLight mp = new PlayerMPLight();
					mp.setPlayerID(id);
					mp.setPlayerName(name);

					boolean playerExists = false;

					for (PlayerMP player : zone.getPlayers()) {
						if (player.getPlayerID() == mp.getPlayerID()) {

							playerExists = true;
							player.refresh();

						}
					}

					if (!playerExists) {

						Gdx.app.postRunnable(new AddPlayerThread(name, new Vector2(20, 20), game, id) {
							@Override
							public void run() {
								zone.addPlayer(new PlayerMP(playerName, loc, game, 1, 0, playerID));

							}
						});

					}

				}

			}

			break;

		case Packet.LOCAL_CHAT_PACKET:

			String msg = p.getData().trim();

			if (p.getPlayerID() == player.getPlayerID()) {

				ui.getChat().addMsg("[LOCAL] : " + msg);

			} else {

				String userName = zone.getPlayerByID(p.getPlayerID()).getName();

				ui.getChat().addMsg("[LOCAL] " + userName + " : " + msg);

			}

			break;

		default:
			System.out.println("UNKNONW PACKET " + p.getData() + " ID : " + p.getPlayerID());
			break;
		}

	}

	public void RXShipRoomInfoPacket(String data) {

		String dataSplit[] = data.split("=");

		int shipID = Integer.parseInt(dataSplit[0]);

		Ship ship = zone.getShipByID(shipID);

		if (ship != null) {

			ship.getData().loadRoomData(data);

		}

	}

	public Game getGame() {
		return game;
	}

	public Connection getConnection() {
		return conn;
	}
}

class WaitForShipData implements Runnable {

	Ship s;

	public WaitForShipData(Ship s) {
		this.s = s;
	}

	@Override
	public void run() {

		s.getData().setPendingDataRequest(true);

		try {
			Thread.sleep(3000);

			s.getData().setPendingDataRequest(false);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

}

class WaitForRoomData implements Runnable {

	Ship s;
	NetworkLayer lnet;

	public WaitForRoomData(Ship s, NetworkLayer lnet) {
		this.s = s;
		this.lnet = lnet;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(2000);

			if (!s.getData().roomDataLoaded()) {
				lnet.sendRoomDataRequest(s);
			} else {

				if (NetworkLayer.ROOM_INFO_ATTEMPTS > 10) {
					lnet.getConnection().disconnect("Failed to get Room Info Attempts");
				}

				NetworkLayer.ROOM_INFO_ATTEMPTS++;
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

}

class AddPlayerThread implements Runnable {

	String playerName;
	Vector2 loc;
	Game game;
	int playerID;

	public AddPlayerThread(String playerName, Vector2 loc, Game game, int playerID) {

		this.playerName = playerName;
		this.loc = loc;
		this.game = game;
		this.playerID = playerID;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
