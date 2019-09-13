package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.shared.data.GameData;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.objects.PlayerMP;
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

	public void loadStats(PlayerMP player) {

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(
					"SELECT hp, energy, strength, stamina, accuracy, reflexes, logic FROM PlayerCharacterStats WHERE character_id=?");
			stmt.setInt(1, player.getID());
			ResultSet rs = stmt.executeQuery();

			System.out.println("Loading stats for player " + player.getID());
			while (rs.next()) {

				float hp = rs.getFloat("hp");
				float energy = rs.getFloat("energy");
				int strength = rs.getInt("strength");
				int stamina = rs.getInt("stamina");
				int accuracy = rs.getInt("accuracy");
				int reflexes = rs.getInt("reflexes");
				int logic = rs.getInt("logic");

				player.setStats(new Stats(hp, energy, strength, stamina, accuracy, reflexes, logic));
			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Player From Database: " + e.getMessage());
		}

	}

	public PlayerMP loadPlayerByID(int id) {
		if (!Global.useDatabase) {
			Log.debug("Loading Default Player");
			return getDefaultPlayer(id);
		}

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

				player = new PlayerMP(name, new Vector2(localX, localY), factionID, structureID, id, sectorID, layer);

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

	public PlayerMP getDefaultPlayer(int id) {
		return new PlayerMP("Default Player", new Vector2(100, 100), 0, 0, id, 7780, 1);
	}

}
