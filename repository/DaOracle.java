/**
 * 
 */
package com.moov.moovservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class DaOracle {

	public Connection conn = null;

	public void connect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException ex) {
			// Logger.getLogger(DaOracle.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.2.15:1521/PISAMPDB", "SYSTEM",
					"PiSam_2023#");
			conn.setAutoCommit(true);
		} catch (SQLException ex) {

			Logger.getLogger(DaOracle.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void connectionBase() {
		try {
			if (conn != null) {
				if (conn.isClosed()) {
					connect();
				} else {

				}
			} else {
				connect();
			}

		} catch (SQLException ex) {
			Logger.getLogger(DaOracle.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void deconnecterBase() {
		try {
			if (conn != null) {

				if (!conn.isClosed()) {
					conn.close();
				} else {

				}
			} else {

			}

		} catch (SQLException ex) {
			Logger.getLogger(DaOracle.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public ResultSet resultSetResult(String sql) throws SQLException {
		connectionBase();
		ResultSet résultats;
		if (!conn.isClosed()) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
			} catch (SQLException ex) {
				conn.close();
				return null;
			}
			try {
				résultats = stmt.executeQuery(sql);

				// conn.close();
				return résultats;
			} catch (SQLException ex) {
				conn.close();
				System.out.println(ex.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public int statmentResult(String sql) throws SQLException {
		connectionBase();
		int i;
		Statement stmt;
		if (!conn.isClosed()) {
			try {
				stmt = conn.createStatement();
			} catch (SQLException ex) {
				conn.close();
				return 0;
			}
			try {
				i = stmt.executeUpdate(sql);
				// conn.close();
				return i;
			} catch (SQLException ex) {
				conn.close();
				return 0;
			}
		} else {
			return 0;
		}
	}

}
