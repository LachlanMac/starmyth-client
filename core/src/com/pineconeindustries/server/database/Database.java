package com.pineconeindustries.server.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class Database {

	private static Database instance = null;
	private Connection conn;

	private Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=root&password=Movingon1");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Database getInstance() {

		if (instance == null) {
			instance = new Database();
		}
		return instance;

	}
	
	
	
	

}
