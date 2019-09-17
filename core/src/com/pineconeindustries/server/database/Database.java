package com.pineconeindustries.server.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.pineconeindustries.shared.data.Global;

import java.sql.Connection;

public class Database {

	private static Database instance = null;
	private Connection conn;
	private PlayerDAO playerDAO;
	private NPCDAO npcDAO;
	private StructureDAO structureDAO;
	private SectorDAO sectorDAO;

	public static final String WINDOWS_URL = "jdbc:mysql://localhost:3306/starmyth?user=root&password=Movingon1?characterEncoding=latin1&useConfigs=maxPerformance";
	public static final String UNIX_URL = "jdbc:mysql://localhost/starmyth?user=root&password=Movingon1";

	private Database() {
		try {
			if (Global.useDatabase) {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starmyth?characterEncoding=latin1",
						"root", "Movingon1");
			}
			playerDAO = new PlayerDAO(conn);
			npcDAO = new NPCDAO(conn);
			structureDAO = new StructureDAO(conn);
			sectorDAO = new SectorDAO(conn);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Global.useDatabase = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Global.useDatabase = false;
		}

	}

	public static Database getInstance() {

		if (instance == null) {
			instance = new Database();

		}
		return instance;

	}

	public StructureDAO getStructureDAO() {
		return structureDAO;
	}

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	public NPCDAO getNPCDAO() {
		return npcDAO;
	}

	public SectorDAO getSectorDAO() {
		return sectorDAO;
	}

}
