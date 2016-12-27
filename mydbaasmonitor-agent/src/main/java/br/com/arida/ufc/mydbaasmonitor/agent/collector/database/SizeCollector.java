package main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.database;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.common.AbstractCollector;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.entity.SizeMetric;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.util.DatabaseConnection;

/**
 * @author David Araújo - @araujodavid
 * @version 2.0
 * @since April 26, 2013
 */
public class SizeCollector extends AbstractCollector<SizeMetric> {

	private static final Logger logger = Logger.getLogger(SizeCollector.class);
	public SizeCollector(int identifier, String type) {
		super(identifier, type);
	}

	@Override
	public void loadMetric(Object[] args) throws ClassNotFoundException, SQLException {
		this.metric = SizeMetric.getInstance();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection = null;
		ResultSet resultSet = null;
		Object[] params;
		
		switch (String.valueOf(args[0])) {
		case "dbms":
			params = databaseConnection.getConnection(Integer.valueOf((String) args[1]), null);
			connection = (Connection) params[1];
			//Checking database type to make the SQL
			if (params[0].equals("MySQL")) {
				resultSet = databaseConnection.executeSQL(connection, "select sum(data_length + index_length)/1024/1024 as size from information_schema.tables;", null);
			} else if (params[0].equals("PostgreSQL")) {
				resultSet = databaseConnection.executeSQL(connection, "select (sum(pg_database_size(pg_database.datname))/1024/1024) as size from pg_database;", null);
			}
			while (resultSet != null && resultSet.next()) {
				this.metric.setSizeUsed(Math.round(resultSet.getDouble("size")*100.0)/100.0);
            }
			break;
		case "database":
			params = databaseConnection.getConnection(null, Integer.valueOf((String) args[1]));
			connection = (Connection) params[1];
			//Checking database type to make the SQL
			if (params[0].equals("MySQL")) {
				resultSet = databaseConnection.executeSQL(connection, "select sum(data_length + index_length)/1024/1024 as size from information_schema.tables where table_schema = database();", null);
			} else if (params[0].equals("PostgreSQL")) {
				resultSet = databaseConnection.executeSQL(connection, "select (pg_database_size('"+params[2]+"')/1024/1024) as size;", null);
			}
			while (resultSet != null && resultSet.next()) {
				this.metric.setSizeUsed(Math.round(resultSet.getDouble("size")*100.0)/100.0);
            }
			break;
		}
		if(resultSet != null){
			resultSet.close();
		}
		if(connection != null){
			connection.close();
		}
	}

	@Override
	public void run() {
		this.metric = SizeMetric.getInstance();
		HttpResponse response;
		List<NameValuePair> params = null;
		Object[] args = new Object[2];
		//Checking the DBMSs enabled for collection
		if (this.metric.getDBMSs().length > 0) {
			for (String dbms : this.metric.getDBMSs()) {
				args[0] = "dbms";
				args[1] = dbms;
				try {
					this.loadMetric(args);
				} catch (ClassNotFoundException e) {
					logger.error(e);
				} catch (SQLException e) {
					logger.error("Problem loading the ActiveConnections metric value (DBMS)", e);
				}
				
				try {
					params = this.loadRequestParams(new Date(), Integer.parseInt(dbms), 0);
				} catch (NumberFormatException e) {
					logger.error(e);
				} catch (IllegalAccessException e) {
					logger.error(e);
				} catch (IllegalArgumentException e) {
					logger.error(e);
				} catch (InvocationTargetException e) {
					logger.error(e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
				} catch (SecurityException e) {
					logger.error(e);
				}
				
				try {
					response = this.sendMetric(params);
					System.out.println(response.getStatusLine());
					if (response.getStatusLine().getStatusCode() != 202) {
						System.out.println("Active connections request error!");
						EntityUtils.consume(response.getEntity());
					}
					EntityUtils.consume(response.getEntity());
				} catch (ClientProtocolException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		//Checking the Databases enabled for collection
		if (this.metric.getDatabases().length > 0) {
			for (String database : this.metric.getDatabases()) {
				args[0] = "database";
				args[1] = database;
				try {
					this.loadMetric(args);
				} catch (ClassNotFoundException e) {
					logger.error(e);
				} catch (SQLException e) {
					logger.error("Problem loading the ActiveConnections metric value (DBMS)", e);
				}
				
				try {
					params = this.loadRequestParams(new Date(), 0, Integer.parseInt(database));
				} catch (NumberFormatException e) {
					logger.error(e);
				} catch (IllegalAccessException e) {
					logger.error(e);
				} catch (IllegalArgumentException e) {
					logger.error(e);
				} catch (InvocationTargetException e) {
					logger.error(e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
				} catch (SecurityException e) {
					logger.error(e);
				}
				
				try {
					response = this.sendMetric(params);
					System.out.println(response.getStatusLine());
					if (response.getStatusLine().getStatusCode() != 202) {
						System.out.println("Active connections request error!");
						EntityUtils.consume(response.getEntity());
					}
					EntityUtils.consume(response.getEntity());
				} catch (ClientProtocolException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}		
	}
}
