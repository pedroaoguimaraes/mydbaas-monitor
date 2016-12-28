package main.java.br.com.arida.ufc.mydbaasmonitor.api.entity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.entity.common.AbstractPool;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.util.SendResquest;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.DBMS;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.VirtualMachine;

/**
 * @author Daivd Araújo
 * @version 3.0
 * @since April 1, 2013
 */

public class VirtualMachinePool extends AbstractPool<VirtualMachine> {

	private static final Logger logger = Logger.getLogger(VirtualMachinePool.class);
	
	@Override
	public boolean save(VirtualMachine resource) {
		List<NameValuePair> params = null;
		HttpResponse response;
		try {
			params = loadRequestParams(resource);
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
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/machines/add", params);
			if (response.getStatusLine().getStatusCode() != 202) {
				EntityUtils.consume(response.getEntity());
				return true;
			}
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public boolean update(VirtualMachine resource) {
		List<NameValuePair> params = null;
		HttpResponse response;
		try {
			params = loadRequestParams(resource);
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
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/machines/update", params);
			if (response.getStatusLine().getStatusCode() != 202) {
				EntityUtils.consume(response.getEntity());
				return true;
			}
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * Method to retrieve the dbmss of a particular Virtual Machine
	 * @param resource- Virtual Machine object owner
	 * @return a list of dbmss of the Virtual Machine
	 */
	public List<DBMS> getDBMSs(VirtualMachine resource) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("identifier", String.valueOf(resource.getId())));
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/dbmss", params);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		Gson gson = new Gson();
		List<DBMS> dbmss = gson.fromJson(json, new TypeToken<List<DBMS>>(){}.getType());
		return dbmss;
	}

}
