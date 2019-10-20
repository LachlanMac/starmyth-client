package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.shared.log.Log;

public class SectorDAO {

	Connection conn;

	public SectorDAO(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<Sector> loadSectors() {

		ArrayList<Sector> sectors = new ArrayList<Sector>();

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sector_name, sector_global_x, sector_global_y, port FROM Sector");
			while (rs.next()) {

				String name = rs.getString("sector_name");
				int globalX = rs.getInt("sector_global_x");
				int globalY = rs.getInt("sector_global_y");
				int port = rs.getInt("port");

				Sector s = new Sector(port, globalX, globalY, name);
				sectors.add(s);

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Sector From Database: " + e.getMessage());
		}


		return sectors;

	}

}
