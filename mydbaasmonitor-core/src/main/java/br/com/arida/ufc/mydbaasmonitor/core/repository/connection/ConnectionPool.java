package main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class ConnectionPool {

	private static final Logger logger = Logger.getLogger(ConnectionPool.class);
	private static ConnectionPool uniqueInstance;
	private String schema;
	private int port;
	private String username;
	private String password;
	
	private ConnectionPool() {}

	public static ConnectionPool getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ConnectionPool();
	    }
	    return uniqueInstance;
	}

	public String getSchema() {
		return schema;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setConfig(String schema, int port, String username, String password) {
		this.schema = schema;
		this.port = port;
		this.username = username;
		this.password = password;
	}	
	
	public Connection getConnection() {
		return connect();
	}
	
	private Connection connect() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("The MySQL driver can not be loaded!", e);
		}
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:"+port+"/"+schema, username, password);
		} catch (SQLException e) {
			logger.error("Unable to connect to the database!", e);
		}
		return connection;
	}
}
