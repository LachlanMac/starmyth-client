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
import com.pineconeindustries.shared.objects.Elevator;
import com.pineconeindustries.shared.objects.NPC;
import com.pineconeindustries.shared.objects.PlayerMP;
import com.pineconeindustries.shared.objects.Ship;
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

	public ArrayList<Elevator> loadElevatorsByStructure(Structure structure) {

		ArrayList<Elevator> elevators = new ArrayList<Elevator>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("SELECT elevator_id, tile_x, tile_y, wall FROM Elevator WHERE structure_id=?");
			stmt.setInt(1, structure.getStructureID());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				int elevatorID = rs.getInt("elevator_id");
				int x = rs.getInt("tile_x");
				int y = rs.getInt("tile_y");
				String wall = rs.getString("wall");

				elevators.add(new Elevator(elevatorID, x, y, structure, wall));

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Station From Database: " + e.getMessage());
		}
		Log.dbLog("Adding elevators to " + structure.getStructureName());
		return elevators;

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
					"SELECT structure_id, structure_name, faction_id, local_x, local_y, global_x, global_y, type, layers, state FROM Structure WHERE sector_id=?");
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
				String state = rs.getString("state");

				if (type == 1) {
					Station s = new Station(name, structureID, id, factionID, localX, localY, globalX, globalY, layers, Structure.getStateByString(state));
					s.loadElevators(loadElevatorsByStructure(s));
					structures.add(s);
					Log.dbLog("Adding station : [" + structureID + "]" + name);
				}
				if (type == 2) {
					Ship s = new Ship(name, structureID, id, factionID, localX, localY, globalX, globalY, layers, Structure.getStateByString(state));
					s.loadElevators(loadElevatorsByStructure(s));
					structures.add(s);
					Log.dbLog("Adding Ship : [" + structureID + "]" + name);
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
