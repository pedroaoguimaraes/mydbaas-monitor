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
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.collector.common.AbstractCollector;
import main.java.br.com.arida.ufc.mydbaasmonitor.agent.entity.DiskMetric;

/**
 * @author Daivd Araújo - @araujodavid
 * @version 3.0
 * @since March 13, 2013
 */
public class DiskCollector extends AbstractCollector<DiskMetric> {

	private static final Logger logger = Logger.getLogger(DiskCollector.class);
	
	public DiskCollector(int identifier, String type) {
		super(identifier, type);
	}

	@Override
	public void loadMetric(Object[] args) throws SigarException {
		Sigar sigar = (Sigar) args[0];
		this.metric = DiskMetric.getInstance();
		long diskBytesRead = 0;
		long diskBytesWritten = 0;
		long diskReads = 0;
		long diskWrites = 0;
		long diskFreeBytes = 0;
		long diskUsedBytes = 0;
		long diskTotalBytes = 0;
		FileSystem[] fileSystemList = sigar.getFileSystemList();
		FileSystemUsage fileSystemUsage;

		for (FileSystem fileSystem : fileSystemList) {
			fileSystemUsage = sigar.getFileSystemUsage(fileSystem.getDirName());
			diskBytesRead = diskBytesRead + fileSystemUsage.getDiskReadBytes();
			diskBytesWritten = diskBytesWritten + fileSystemUsage.getDiskWriteBytes();
			diskReads = diskReads + fileSystemUsage.getDiskReads();
			diskWrites = diskWrites + fileSystemUsage.getDiskWrites();
			diskFreeBytes = diskFreeBytes + fileSystemUsage.getAvail();
			diskUsedBytes = diskUsedBytes + fileSystemUsage.getUsed();
			diskTotalBytes = diskTotalBytes + fileSystemUsage.getTotal();
		}
		this.metric.setDiskReads(diskReads);
		this.metric.setDiskWrites(diskWrites);
		this.metric.setDiskBytesRead(diskBytesRead);
		this.metric.setDiskBytesWritten(diskBytesWritten);
		this.metric.setDiskFree(Math.round((diskFreeBytes/1024/1024)*100.0)/100.0);
		this.metric.setDiskUsed(Math.round((diskUsedBytes/1024/1024)*100.0)/100.0);
		this.metric.setDiskTotal(Math.round((diskTotalBytes/1024/1024)*100.0)/100.0);
		this.metric.setDiskPercent(Math.round(((diskUsedBytes*100)/diskTotalBytes)*100.0)/100.0);
	}

	@Override
	public void run() {
		Sigar sigar = new Sigar();
		//Collecting metrics
		try {
			this.loadMetric(new Object[] {sigar});
		} catch (SigarException e2) {
			logger.error("Problem loading the Disk metric values (Sigar)", e2);
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
				System.out.println("Disk request error!");
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
