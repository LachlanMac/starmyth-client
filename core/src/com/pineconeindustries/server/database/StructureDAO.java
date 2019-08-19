package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Station;
import com.pineconeindustries.shared.objects.Structure;

public class StructureDAO {
	Connection conn;

	public StructureDAO(Connection conn) {
		this.conn = conn;
	}

	public void saveStructure(Structure structure) {

		if (!Global.useDatabase) {
			return;
		}

	}

	public ArrayList<Structure> loadStructuressBySectorID(int id) {
		if (!Global.useDatabase) {
			Log.debug("Loading Default Structures");
			return getDefaultStructures(id);
		}
		ArrayList<Structure> structures = new ArrayList<Structure>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(
					"SELECT structure_id, structure_name, faction_id, local_x, local_y, global_x, global_y, type, layers FROM Structure WHERE sector_id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int structureID = rs.getInt("structure_id");
				String name = rs.getString("structure_name");
				int factionID = rs.getInt("faction_id");
				int localX = rs.getInt("local_x");
				int localY = rs.getInt("local_y");
				float globalX = rs.getFloat("global_x");
				float globalY = rs.getFloat("global_y");
				int type = rs.getInt("type");
				int layers = rs.getInt("layers");

				if (type == 1) {
					structures.add(
							new Station(name, structureID, id, factionID, localX, localY, globalX, globalY, layers));
					Log.dbLog("Adding station : [" + structureID + "]" + name);
				}

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Station From Database: " + e.getMessage());
		}

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
