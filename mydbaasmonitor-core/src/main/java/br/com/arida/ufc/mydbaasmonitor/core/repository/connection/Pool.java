package main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.connection;

import java.sql.Connection;  
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import javax.naming.Context;  
import javax.naming.InitialContext;  

/**
 * @author David Araújo
 * 
 */

public class Pool {
	
	private static final Logger logger = Logger.getLogger(Pool.class);
	public static final String JDBC_MySQL = "mysql/mydbaasmonitor";
	public static final String JDBC_PostgreSQL = "postgres/mydbaasmonitor";
	
	public Pool() {}  
	
	public static Connection getConnection(String selectedConnection) {  
		Connection connection = null;  
		try {  
			Context context = (Context) new InitialContext().lookup("java:comp/env");
			connection = ((DataSource) context.lookup(selectedConnection)).getConnection();
		} catch (Exception e) {
			logger.error(e);
		}
		return connection;  
	}

}
