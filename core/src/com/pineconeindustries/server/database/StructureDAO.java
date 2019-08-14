package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;

public class StructureDAO {
	Connection conn;

	public StructureDAO(Connection conn) {
		this.conn = conn;
	}

	public void saveStructure(Structure structure) {

		if (!Database.useDatabase) {
			return;
		}

	}

	public ArrayList<Structure> loadStructuressBySectorID(int id) {
		if (!Database.useDatabase) {
			Log.debug("Loading Default Structures");
			return getDefaultStructures(id);
		}
		ArrayList<Structure> structures = new ArrayList<Structure>();

		return structures;

	}

	public ArrayList<Structure> getDefaultStructures(int sectorID) {

		ArrayList<Structure> structures = new ArrayList<Structure>();

		// structures.add(new Ship("NPC1", new Vector2(300, 200),
		// GameData.getInstance(), 0, 0, 444,
		// Galaxy.getInstance().getSectorByID(sectorID)));
		// structures.add(new Station("NPC2", new Vector2(600, 800),
		// GameData.getInstance(), 0, 0, 455,
		// Galaxy.getInstance().getSectorByID(sectorID)));

		return structures;

	}

}
