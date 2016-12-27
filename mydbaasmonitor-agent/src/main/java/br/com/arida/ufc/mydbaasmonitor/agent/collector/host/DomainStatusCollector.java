package main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.host;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.common.AbstractCollector;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.entity.DomainStatusMetric;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.util.ShellCommand;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.host.DomainStatus;

/**
 * @author David Araújo - @araujodavid
 * @version 3.0
 * @since May 2, 2013
 */
public class DomainStatusCollector extends AbstractCollector<DomainStatusMetric> {

	private static final Logger logger = Logger.getLogger(DomainStatusCollector.class);
	private List<DomainStatus> domainStatusMetrics;
	
	public DomainStatusCollector(int identifier, String type) {
		super(identifier, type);
		this.domainStatusMetrics = new ArrayList<DomainStatus>();
	}

	@Override
	public void loadMetric(Object[] args) throws LibvirtException {
		this.metric = DomainStatusMetric.getInstance();
		Connect connect = (Connect) args[0];
		int[] domains = connect.listDomains();
		for (int domainId : domains) {
			DomainStatus domainStatus = new DomainStatus();
			Domain domain = connect.domainLookupByID(domainId);
			domainStatus.setDomainStatusHostIdentifier(domain.getName());
			long domainPid = ShellCommand.getDomainPid(domain.getName());
			String[] results = ShellCommand.getProcessStatus(domainPid);
			domainStatus.setDomainStatusCpuPercent(Double.valueOf(results[0]));
			domainStatus.setDomainStatusMemoryPercent(Double.valueOf(results[1]));
			this.domainStatusMetrics.add(domainStatus);
		}
		
	}

	@Override
	public void run() {
		Connect connect = null;
		try {
			connect = new Connect(null);
			this.loadMetric(new Object[] {connect});
		} catch (LibvirtException e) {
			logger.error("Problem loading the DomainStatus metric values (Libvirt)", e);
		}
		
		//Setting the parameters of the POST request
		List<NameValuePair> params = null;
		try {
			params = this.loadRequestParams(new Date(), domainStatusMetrics, 0, 0);
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
				System.out.println("Domain Status request error!");
				EntityUtils.consume(response.getEntity());
			}
			EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		//Release any native resources associated with this sigar instance
		this.domainStatusMetrics.clear();
		try {
			connect.close();
		} catch (LibvirtException e) {
			logger.error("Problem to close the Libvirt connection.", e);
		}
	}
}
