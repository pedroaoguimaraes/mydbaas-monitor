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
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.DBaaS;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.Host;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.VirtualMachine;

/**
 * @author Daivd Araújo - @araujodavid
 * @version 3.0
 * @since April 1, 2013
 */
public class DBaaSPool extends AbstractPool<DBaaS> {
	
	private static final Logger logger = Logger.getLogger(DBaaSPool.class);
	
	@Override
	public boolean save(DBaaS resource) {
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
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/dbaas/add", params);
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
	public boolean update(DBaaS resource) {
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
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/dbaas/update", params);
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
	 * Method to retrieve the hosts of a particular DBaaS
	 * @param resource - DBaaS object owner
	 * @return a list of hosts of the DBaaS
	 */
	public List<Host> getHosts(DBaaS resource) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("identifier", String.valueOf(resource.getId())));
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/hosts", params);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		Gson gson = new Gson();
		List<Host> hosts = gson.fromJson(json, new TypeToken<List<Host>>(){}.getType());
		return hosts;
	}
	
	/**
	 * Method to retrieve the virtual machines of a particular DBaaS
	 * @param resource- DBaaS object owner
	 * @return a list of virtual machines of the DBaaS
	 */
	public List<VirtualMachine> getMachines(DBaaS resource) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("identifier", String.valueOf(resource.getId())));
		params.add(new BasicNameValuePair("ownerType", resource.toString()));
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.getClient().getServerUrl()+"/resource/machines", params);
			json = SendResquest.getJsonResult(response);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		Gson gson = new Gson();
		List<VirtualMachine> virtualMachines = gson.fromJson(json, new TypeToken<List<VirtualMachine>>(){}.getType());
		return virtualMachines;
	}

}
