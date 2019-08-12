package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.objects.PlayerMP;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.log.Log;

public class PlayerDAO {

	Connection conn;

	public PlayerDAO(Connection conn) {
		this.conn = conn;
	}

	public void savePlayer(PlayerMP player) {

		if (!Database.useDatabase) {
			return;
		}

		Log.dbLog("Saving Player [" + player.getID() + ":" + player.getName() + "]");

		String savePlayerSQL = "UPDATE PlayerCharacter SET sector_id=?, faction_id=?, structure_id=?, local_x=?, local_y=?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(savePlayerSQL);

			pstmt.setInt(1, player.getSectorID());
			pstmt.setInt(2, player.getFactionID());
			pstmt.setInt(3, player.getStructureID());
			pstmt.setFloat(4, player.getLoc().x);
			pstmt.setFloat(5, player.getLoc().y);

			pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			Log.dbLog("Error Saving Player to Database: " + e.getMessage());
		}

	}

	public PlayerMP loadPlayerByID(int id) {
		if (!Database.useDatabase) {
			Log.debug("Loading Default Player");
			return getDefaultPlayer(id);
		}

		PlayerMP player = null;

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT character_name, sector_id, faction_id, local_x, local_y, structure_id FROM PlayerCharacter");
			while (rs.next()) {

				String name = rs.getString("character_name");
				int sectorID = rs.getInt("sector_id");
				int factionID = rs.getInt("faction_id");
				int structureID = rs.getInt("structure_id");
				float localX = rs.getFloat("local_x");
				float localY = rs.getFloat("local_y");
				player = new PlayerMP(name, new Vector2(localX, localY), GameData.getInstance(), factionID, structureID,
						id, sectorID);

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Player From Database: " + e.getMessage());
		}

		Log.dbLog("Loaded Player [" + player.getID() + ":" + player.getName() + "]");

		return player;

	}

	public PlayerMP getDefaultPlayer(int id) {
		return new PlayerMP("Default Player", new Vector2(100, 100), GameData.getInstance(), 0, 0, id, 7780);
	}

}
