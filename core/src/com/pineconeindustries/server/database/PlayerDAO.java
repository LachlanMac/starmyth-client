package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.components.gameobjects.PlayerMP;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.stats.Stats;

public class PlayerDAO {

	Connection conn;

	public PlayerDAO(Connection conn) {
		this.conn = conn;
	}

	public void savePlayer(PlayerMP player) {

		if (!Global.useDatabase) {
			return;
		}

		Log.dbLog("Saving Player [" + player.getID() + ":" + player.getName() + "]");

		String savePlayerSQL = "UPDATE PlayerCharacter SET sector_id=?, faction_id=?, structure_id=?, local_x=?, local_y=?, layer=?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(savePlayerSQL);

			pstmt.setInt(1, player.getSectorID());
			pstmt.setInt(2, player.getFactionID());
			pstmt.setInt(3, player.getStructureID());
			pstmt.setFloat(4, player.getLoc().x);
			pstmt.setFloat(5, player.getLoc().y);
			pstmt.setInt(6, player.getLayer());
			pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			Log.dbLog("Error Saving Player to Database: " + e.getMessage());
		}

	}

	public void createStats(PlayerMP player) {

		String createStats = "INSERT INTO PlayerCharacterStats (character_id, hp, energy, strength, stamina, accuracy, reflexes, logic) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(createStats);

			pstmt.setInt(1, player.getID());
			pstmt.setFloat(2, 100.0f);
			pstmt.setFloat(3, 100.0f);
			pstmt.setInt(4, 5);
			pstmt.setInt(5, 5);
			pstmt.setInt(6, 5);
			pstmt.setInt(7, 5);
			pstmt.setInt(8, 5);

			player.setStats(new Stats(100, 100, 5, 5, 5, 5, 5));

			pstmt.execute();

		} catch (SQLException e) {
			Log.dbLog("Error Saving Player to Database: " + e.getMessage());
		}

	}

	public void loadStats(PlayerMP player) {

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(
					"SELECT hp, energy, strength, stamina, accuracy, reflexes, logic FROM PlayerCharacterStats WHERE character_id=?");
			stmt.setInt(1, player.getID());
			ResultSet rs = stmt.executeQuery();

			System.out.println("Loading stats for player " + player.getID());

			int rows = 0;

			while (rs.next()) {
				rows++;
				float hp = rs.getFloat("hp");
				float energy = rs.getFloat("energy");
				int strength = rs.getInt("strength");
				int stamina = rs.getInt("stamina");
				int accuracy = rs.getInt("accuracy");
				int reflexes = rs.getInt("reflexes");
				int logic = rs.getInt("logic");

				player.setStats(new Stats(hp, energy, strength, stamina, accuracy, reflexes, logic));
			}

			if (rows == 0) {

				createStats(player);

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Player From Database: " + e.getMessage());
		}

	}

	public PlayerMP loadPlayerByID(int id) {

		PlayerMP player = null;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(
					"SELECT character_name, sector_id, faction_id, local_x, local_y, structure_id, layer FROM PlayerCharacter WHERE character_id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				String name = rs.getString("character_name");
				int sectorID = rs.getInt("sector_id");
				int factionID = rs.getInt("faction_id");
				int structureID = rs.getInt("structure_id");
				float localX = rs.getFloat("local_x");
				float localY = rs.getFloat("local_y");
				int layer = rs.getInt("layer");

				player = new PlayerMP(id, name, new Vector2(localX, localY), sectorID, structureID, layer, factionID);

			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Player From Database: " + e.getMessage());
		}

		if (player != null) {
			Log.dbLog("Loaded Player [" + player.getID() + ":" + player.getName() + "]");
			loadStats(player);
		}
		return player;

	}

}
