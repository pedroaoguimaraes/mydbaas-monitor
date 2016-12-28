package main.java.br.com.arida.ufc.mydbaasmonitor.api.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import main.java.br.com.arida.ufc.mydbaasmonitor.api.util.SendResquest;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.common.AbstractMetric;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * Class that controls access to resource metrics.
 * @author Daivd Araújo - @araujodavid
 * @version 2.0
 * @since May 8, 2013
 */
public class MyMetrics {
	
	private static final Logger logger = Logger.getLogger(MyMetrics.class);
	public final static String RESOURCE_TYPE_HOST = "host";
	public final static String RESOURCE_TYPE_VM = "machine";
	public final static String RESOURCE_TYPE_DBMS = "dbms";
	public final static String RESOURCE_TYPE_DATABASE = "database";
	
	private MyDBaaSMonitorClient client;
	
	/**
	 * Default constructor
	 * @param client - MyDBaaSMonitorClient object
	 */
	public MyMetrics(MyDBaaSMonitorClient client) {
		this.client = client;
	}
	
	/**
	 * Method to requests about the last collection of a particular metric
	 * @param metricName
	 * @param resourceType
	 * @param resourceID
	 * @param startDatetime
	 * @param endDatetime
	 * @return an metric object
	 */
	public Object getMetricSingle(String metricName, String resourceType, int resourceID, String startDatetime, String endDatetime) {		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("metricName", metricName));
		parameters.add(new BasicNameValuePair("resourceType", resourceType));
		parameters.add(new BasicNameValuePair("metricType", "0"));
		parameters.add(new BasicNameValuePair("resourceID", String.valueOf(resourceID)));
		
		if (startDatetime != null && !startDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("startDatetime", startDatetime));
		}
		if (endDatetime != null && !endDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("endDatetime", endDatetime));
		}
		
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.client.getServerUrl()+"/metric/single", parameters);
			json = SendResquest.getJsonResult(response);
			System.out.println(json);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		Class<?> metricClass = this.getMetricClass(metricName, resourceType);
		Gson gson = new Gson();
		Object metric = gson.fromJson(json, metricClass);
		return metric;
	}
	
	/**
	 * Method to requests collections of a particular metric
	 * @param metricName
	 * @param resourceType
	 * @param resourceID
	 * @param startDatetime
	 * @param endDatetime
	 * @return json of the metric
	 */
	public List<Object> getMetricMulti(String metricName, String resourceType, int resourceID, String startDatetime, String endDatetime) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("metricName", metricName));
		parameters.add(new BasicNameValuePair("resourceType", resourceType));
		parameters.add(new BasicNameValuePair("metricType", "1"));
		parameters.add(new BasicNameValuePair("resourceID", String.valueOf(resourceID)));
		
		if (startDatetime != null && !startDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("startDatetime", startDatetime));
		}
		if (endDatetime != null && !endDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("endDatetime", endDatetime));
		}
		
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.client.getServerUrl()+"/metric/single", parameters);
			json = SendResquest.getJsonResult(response);
			System.out.println(json);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}

		Class<?> metricClass = this.getMetricClass(metricName, resourceType);
		List<Object> metricList = null;
		try {
			Object metric = metricClass.newInstance();
			Method jsonToListMethod = AbstractMetric.class.getDeclaredMethod("jsonToList", String.class);
			metricList = (List<Object>) jsonToListMethod.invoke(metric, json);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (SecurityException e) {
			logger.error(e);
		} catch (IllegalArgumentException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
		return metricList;
	}
	
	/**
	 * Method to requests collections of a particular metric
	 * @param metricName
	 * @param resourceType
	 * @param resourceID
	 * @param startDatetime
	 * @param endDatetime
	 * @return a json of the metric list
	 */
	public List<Object> getMetricCollection(String metricName, String resourceType, int resourceID, String startDatetime, String endDatetime) {		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("metricName", metricName));
		parameters.add(new BasicNameValuePair("resourceType", resourceType));
		parameters.add(new BasicNameValuePair("resourceID", String.valueOf(resourceID)));
		
		if (startDatetime != null && !startDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("startDatetime", startDatetime));
		}
		if (endDatetime != null && !endDatetime.trim().equals("")) {
			parameters.add(new BasicNameValuePair("endDatetime", endDatetime));
		}
		
		HttpResponse response;
		String json = null;
		try {
			response = SendResquest.postRequest(this.client.getServerUrl()+"/metric/multi", parameters);
			json = SendResquest.getJsonResult(response);
			System.out.println(json);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		Class<?> metricClass = this.getMetricClass(metricName, resourceType);
		List<Object> metricList = null;
		try {
			Object metric = metricClass.newInstance();
			Method jsonToListMethod = AbstractMetric.class.getDeclaredMethod("jsonToList", String.class);
			metricList = (List<Object>) jsonToListMethod.invoke(metric, json);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (SecurityException e) {
			logger.error(e);
		} catch (IllegalArgumentException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
		return metricList;
	}
	
	private Class<?> getMetricClass(String metricName, String resourceType) {
		Class<?> metricClass = null;
		try {
			if (resourceType.equals("dbms") || resourceType.equals("database")) {			
				metricClass = Class.forName("main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.database."+metricName);						
			} else {
				metricClass = Class.forName("main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric."+resourceType+"."+metricName);
			}
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		return metricClass;
	}
}
