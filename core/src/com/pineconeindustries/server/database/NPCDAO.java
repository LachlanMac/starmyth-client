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

public class NPCDAO {
	Connection conn;

	public NPCDAO(Connection conn) {
		this.conn = conn;
	}

	public void savePlayer(PlayerMP player) {

		if (!Global.useDatabase) {
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

	public ArrayList<NPC> loadNPCsBySectorID(int id) {
		if (!Global.useDatabase) {
			Log.debug("Loading Default Player");
			return getDefaultNPCs(id);
		}
		ArrayList<NPC> npcs = new ArrayList<NPC>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(
					"SELECT npc_id, npc_name, faction_id, local_x, local_y, structure_id FROM NPC WHERE sector_id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int npcID = rs.getInt("npc_id");
				String name = rs.getString("npc_name");

				int factionID = rs.getInt("faction_id");
				int structureID = rs.getInt("structure_id");
				float localX = rs.getFloat("local_x");
				float localY = rs.getFloat("local_y");
				npcs.add(new NPC(name, new Vector2(localX, localY), GameData.getInstance(), factionID, structureID, id,
						Galaxy.getInstance().getSectorByID(id)));
				Log.dbLog("Loaded NPC [" + npcID + ":" + name + "]");
			}

		} catch (SQLException e) {
			Log.dbLog("Error Loading Player From Database: " + e.getMessage());
		}

		return npcs;

	}

	public ArrayList<NPC> getDefaultNPCs(int sectorID) {

		ArrayList<NPC> npcs = new ArrayList<NPC>();

		npcs.add(new NPC("NPC1", new Vector2(300, 200), GameData.getInstance(), 0, 0, 444,
				Galaxy.getInstance().getSectorByID(sectorID)));
		npcs.add(new NPC("NPC2", new Vector2(600, 800), GameData.getInstance(), 0, 0, 455,
				Galaxy.getInstance().getSectorByID(sectorID)));

		return npcs;

	}

	public PlayerMP getDefaultPlayer(int id) {
		return new PlayerMP("Default Player", new Vector2(100, 100), GameData.getInstance(), 0, 0, id, 7780);
	}

}
