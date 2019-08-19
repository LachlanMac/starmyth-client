package com.pineconeindustries.client.galaxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.Entity;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.Person;
import com.pineconeindustries.shared.objects.Player;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Station;
import com.pineconeindustries.shared.objects.Structure;

public class Sector {

	private int port;

	private ArrayBlockingQueue<NPC> npcs;
	private ArrayBlockingQueue<PlayerMP> players;
	private ArrayBlockingQueue<Structure> structures;
	private Player player;

	private CopyOnWriteArrayList<Person> renderList;

	public Sector(Player player) {
		this.port = player.getSectorID();
		players = new ArrayBlockingQueue<PlayerMP>(64);
		npcs = new ArrayBlockingQueue<NPC>(128);
		structures = new ArrayBlockingQueue<Structure>(16);
		renderList = new CopyOnWriteArrayList<Person>();
		this.player = player;
		renderList.add(player);

	}

	public void render(SpriteBatch b) {

		Structure s = getStructureByID(player.getStructureID());
		if (s != null)
			s.render(b);

		Collections.sort(renderList);
		for (Entity e : renderList) {

			if (e.getLayer() == player.getLayer())
				e.render(b);
		}

	}

	public void update() {

		player.update();

		for (PlayerMP p : players) {
			p.update();
		}
		for (NPC n : npcs) {
			n.update();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayBlockingQueue<PlayerMP> getPlayers() {
		return players;
	}

	public PlayerMP getPlayerByID(int id) {
		PlayerMP player = null;

		for (PlayerMP p : players) {
			if (p.getID() == id)
				player = p;
		}

		return player;
	}

	public NPC getNPCByID(int id) {
		NPC npc = null;

		for (NPC n : npcs) {
			if (n.getID() == id)
				npc = n;
		}

		return npc;
	}

	public boolean npcExists(int id) {

		boolean exists = false;

		for (NPC n : npcs) {
			if (n.getID() == id)
				exists = true;
		}

		return exists;

	}

	public boolean structureExists(int id) {
		boolean exists = false;
		for (Structure s : structures) {
			if (s.getStructureID() == id)
				exists = true;
		}

		return exists;

	}

	public void cleanPlayerList(ArrayList<Integer> ids) {

		for (PlayerMP p : players) {
			boolean playerExists = false;
			for (Integer i : ids) {
				if (p.getID() == i) {
					playerExists = true;
				}
			}

			if (!playerExists) {
				removePlayer(getPlayerByID(p.getID()));
			}
		}

	}

	public void cleanNPCList(ArrayList<Integer> ids) {

		for (NPC npc : npcs) {
			boolean npcExists = false;
			for (Integer i : ids) {
				if (npc.getID() == i) {
					npcExists = true;
				}
			}

			if (!npcExists) {
				removeNPC(getNPCByID(npc.getID()));
			}
		}

	}

	public void cleanStructureList(ArrayList<Integer> ids) {

		for (Structure structure : structures) {
			boolean structureExists = false;
			for (Integer i : ids) {
				if (structure.getStructureID() == i) {
					structureExists = true;
				}
			}

			if (!structureExists) {
				removeStructure(getStructureByID(structure.getStructureID()));
			}
		}

	}

	public void addNPC(NPC npc) {
		System.out.println("Adding " + npc.getName());
		npcs.add(npc);
		renderList.add(npc);
	}

	public void addStructure(Structure structure) {
		Log.print("ADDING ! Name : " + structure.getStructureName() + " : " + structure.getStructureID());
		structures.add(structure);

	}

	public void removeNPC(NPC npc) {
		npcs.remove(npc);
		renderList.remove(npc);

	}

	public void removeStructure(Structure structure) {
		structures.remove(structure);

	}

	public void addPlayer(PlayerMP player) {
		players.add(player);
		renderList.add(player);

	}

	public void removePlayer(PlayerMP player) {
		players.remove(player);
		renderList.remove(player);

	}

	public void loadSector() {

	}

	public PlayerMP getPlayerMPByID(int id) {

		PlayerMP pmp = null;

		for (PlayerMP p : players) {
			if (p.getID() == id) {
				pmp = p;
			}
		}
		return pmp;

	}

	public Structure getStructureByID(int id) {

		Structure structure = null;

		for (Structure s : structures) {
			if (s.getStructureID() == id) {
				structure = s;
			}
		}
		return structure;

	}

	public int getPort() {
		return port;
	}

}
