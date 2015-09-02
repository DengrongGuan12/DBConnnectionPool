package com.sap.nic.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author  steve.hu@sap.com
 * @version created at　Aug 17, 2015 7:33:00 PM
 * 
 */
public class ConnectionPool {
	private static String dbUrl= ""; 
	private final static ArrayList<Connection> conns = new ArrayList<Connection>();

	private static ConnectionPool instance;
	
	public static void setDBUrl(String ip,String port,String user,String passwd){
		dbUrl = "jdbc://"+ip+":"+port+"/?user="+user+"&password="+passwd;
//		System.out.println(dbUrl);
	}
	
	
	private static int maxSize = 0;
	
	public ConnectionPool(int poolSize) {
		maxSize = poolSize;
	}
	
	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool(100);
		}
		return instance;
	}
	
	public synchronized Connection getConnection() throws SQLException {
		if (conns.size() == 0 )
		{
			return java.sql.DriverManager.getConnection(dbUrl);
		}
		else
		{
			int index = conns.size() - 1;
			return conns.remove(index);
		}
	}
	
	public synchronized void closeConnection(Connection conn) throws SQLException {
		if (conns.size() == maxSize) {
			conn.close();
		}
		else
		{
			conns.add(conn);
		}
	}
}
