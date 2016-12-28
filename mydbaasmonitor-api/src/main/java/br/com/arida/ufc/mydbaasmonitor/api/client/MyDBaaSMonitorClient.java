package main.java.br.com.arida.ufc.mydbaasmonitor.api.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.DBMSPool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.DBaaSPool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.DatabasePool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.HostPool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.VirtualMachinePool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.util.SendResquest;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.DBMS;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.DBaaS;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.Database;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.Host;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.VirtualMachine;

/**
 * Class that handles the access information to the server.
 * @author Daivd Araújo - @araujodavid
 * @version 2.0
 * @since April 28, 2013
 */
public class MyDBaaSMonitorClient {

	private static final Logger logger = Logger.getLogger(MyDBaaSMonitorClient.class);
	private String serverUrl;
	
	/**
	 * Default constructor
	 * @param serverUrl
	 */
	public MyDBaaSMonitorClient(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	/**
	 * Method to check server connection
	 * @return true if exitir, otherwise false
	 */
	public boolean hasConnection() {
		HttpResponse response = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/connection", null);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		if (response != null && response.getStatusLine().getStatusCode() != 200) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method that returns the metrics available for resources
	 * @param typeResource (eg.: host, machine or database)
	 * @return a list of metric about the resource type
	 */
	public List<String> getEnabledMetrics(String typeResource) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("metricsType", typeResource));
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/metrics", params);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		List<String> metrics = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
		return metrics;
	}
	
	public DBaaSPool getMyDBaaSs() {
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/dbaas", null);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		DBaaSPool pool = gson.fromJson(json, DBaaSPool.class);
		pool.setClient(this);
		return pool;
	}
	
	public HostPool getMyHosts() {
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/hosts", null);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		HostPool pool = gson.fromJson(json, HostPool.class);
		pool.setClient(this);
		return pool;
	}
	
	public VirtualMachinePool getMyVirtualMachines() {
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/machines", null);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		VirtualMachinePool pool = gson.fromJson(json, VirtualMachinePool.class);
		pool.setClient(this);
		return pool;
	}
	
	public DBMSPool getMyDBMSs() {
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/dbmss", null);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		DBMSPool pool = gson.fromJson(json, DBMSPool.class);
		pool.setClient(this);
		return pool;
	}
	
	public DatabasePool getMyDatabases() {
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/pool/databases", null);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Gson gson = new Gson();
		DatabasePool pool = gson.fromJson(json, DatabasePool.class);
		pool.setClient(this);
		return pool;	
	}
	
	public Object resourceLookupByID(int resourceId, String resourceType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("resourceId", String.valueOf(resourceId)));
		params.add(new BasicNameValuePair("resourceType", resourceType));
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.serverUrl+"/resource/find", params);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		Gson gson = new Gson();
		switch (resourceType) {
		case "dbaas":
			DBaaS dBaaS = gson.fromJson(json, DBaaS.class);
			return dBaaS;
		case "host":
			Host host = gson.fromJson(json, Host.class);
			return host;
		case "machine":
			VirtualMachine virtualMachine = gson.fromJson(json, VirtualMachine.class);
			return virtualMachine;
		case "dbms":
			DBMS dbms = gson.fromJson(json, DBMS.class);
			return dbms;
		case "database":
			Database database = gson.fromJson(json, Database.class);
			return database;
		}
		return null;
	}

	//Getters and Setters
	
	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}	
	
}
