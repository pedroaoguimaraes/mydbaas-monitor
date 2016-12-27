package main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.machine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.common.AbstractCollector;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.entity.NetworkMetric;

/**
 * @author Daivd Araújo - @araujodavid
 * @version 3.0
 * @since March 13, 2013
 */
public class NetworkCollector extends AbstractCollector<NetworkMetric> {

	private static final Logger logger = Logger.getLogger(NetworkCollector.class);
	
	public NetworkCollector(int identifier, String type) {
		super(identifier, type);
	}

	@Override
	public void loadMetric(Object[] args) throws SigarException {
		Sigar sigar = (Sigar) args[0];
		this.metric = NetworkMetric.getInstance();
		long bytesReceived = 0;
		long bytesSent = 0;
		long packetsSent = 0;
		long packetsReceived = 0;
		String[] netInterfacesList = sigar.getNetInterfaceList();
		
		for (String netInterface : netInterfacesList) {
			NetInterfaceStat netInterfaceStat = sigar.getNetInterfaceStat(netInterface);
			bytesReceived = bytesReceived + netInterfaceStat.getRxBytes();
			packetsReceived = packetsReceived + netInterfaceStat.getRxPackets();
			bytesSent = bytesSent + netInterfaceStat.getTxBytes();
			packetsSent = packetsSent + netInterfaceStat.getTxPackets();
		}
		
		this.metric.setNetworkBytesSent(bytesSent);
		this.metric.setNetworkBytesReceived(bytesReceived);
		this.metric.setNetworkPacketsSent(packetsSent);
		this.metric.setNetworkPacketsReceived(packetsReceived);
	}

	@Override
	public void run() {
		Sigar sigar = new Sigar();
		//Collecting metrics
		try {
			this.loadMetric(new Object[] {sigar});
		} catch (SigarException e2) {
			logger.error("Problem loading the Network metric values (Sigar)", e2);
		}
				
		//Setting the parameters of the POST request
		List<NameValuePair> params = null;
		try {
			params = this.loadRequestParams(new Date(), 0, 0);
		} catch (IllegalAccessException e1) {
			logger.error(e1);
		} catch (IllegalArgumentException e1) {
			logger.error(e1);
		} catch (InvocationTargetException e1) {
			logger.error(e1);
		} catch (NoSuchMethodException e1) {
			logger.error(e1);
		} catch (SecurityException e1) {
			logger.error(e1);
		}
		
		HttpResponse response;
		
		try {
			response = this.sendMetric(params);
			System.out.println(response.getStatusLine());
			if (response.getStatusLine().getStatusCode() != 202) {
				System.out.println("Net request error!");
				EntityUtils.consume(response.getEntity());
			}
			EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		//Release any native resources associated with this sigar instance
		sigar.close();		
	}
}
