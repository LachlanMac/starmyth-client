package com.pineconeindustries.server.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class Database {

	private static Database instance = null;
	private Connection conn;
	private PlayerDAO playerDAO;
	private NPCDAO npcDAO;
	private StructureDAO structureDAO;

	public static boolean useDatabase = false;

	private Database() {
		try {
			if (useDatabase) {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/starmyth?" + "user=root&password=Movingon1");

			}
			playerDAO = new PlayerDAO(conn);
			npcDAO = new NPCDAO(conn);
			structureDAO = new StructureDAO(conn);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			useDatabase = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			useDatabase = false;
		}

	}

	public static Database getInstance() {

		if (instance == null) {
			instance = new Database();

		}
		return instance;

	}

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	public NPCDAO getNPCDAO() {
		return npcDAO;
	}

}
