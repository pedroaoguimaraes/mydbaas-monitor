package main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.receiver;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.host.DomainStatus;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.host.HostDomains;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.host.HostInfo;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.machine.Cpu;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.machine.Disk;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.machine.Memory;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.machine.Network;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.machine.Partition;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.receiver.common.AbstractReceiver;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.HostRepository;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.MetricRepository;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.view.DefaultStatus;

/**
 * Class that handles requests sent by the monitoring agents about host.
 * @author Daivd Araújo
 * @version 1.0
 * @since May 14, 2013 
 */

@Resource
@Path("/host")
public class HostReceiverController extends AbstractReceiver {

	private static final Logger logger = Logger.getLogger(HostReceiverController.class);
	private HostRepository hostRepository;
	
	public HostReceiverController(DefaultStatus status, MetricRepository repository, HostRepository hostRepository) {
		super(status, repository);
		this.hostRepository = hostRepository;
	}
	
	@Post("/info")
	public void information(HostInfo metric, int host) {
		if (hostRepository.updateHostInformation(metric, host)) {
			status.accepted();
		}
	}
	
	@Post("/cpu")
	public void cpu(List<Cpu> metric, int host, String recordDate) {
		for (Cpu cpu : metric) {
			try {
				if (repository.saveMetric(cpu, recordDate, 0, host, 0, 0)) {
					status.accepted();
				}
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		}				
	}
	
	@Post("/memory")
	public void memory(Memory metric, int host, String recordDate) {
		try {
			if (repository.saveMetric(metric, recordDate, 0, host, 0, 0)) {
				status.accepted();
			}
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	@Post("/disk")
	public void disk(Disk metric, int host, String recordDate) {
		try {
			if (repository.saveMetric(metric, recordDate, 0, host, 0, 0)) {
				status.accepted();
			}
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	@Post("/partition")
	public void partition(List<Partition> metric, int host, String recordDate) {
		for (Partition partition : metric) {
			try {
				if (repository.saveMetric(partition, recordDate, 0, host, 0, 0)) {
					status.accepted();
				}
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		}				
	}
	
	@Post("/network")
	public void network(Network metric, int host, String recordDate) {
		try {
			if (repository.saveMetric(metric, recordDate, 0, host, 0, 0)) {
				status.accepted();
			}
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	@Post("/domainstatus")
	public void domainStatus(List<DomainStatus> metric, int host, String recordDate) {
		for (DomainStatus domainStatus : metric) {
			try {
				if (repository.saveMetric(domainStatus, recordDate, 0, host, 0, 0)) {
					status.accepted();
				}
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		}				
	}
	
	@Post("/hostdomains")
	public void hostDomains(HostDomains metric, int host, String recordDate) {
		try {
			if (repository.saveMetric(metric, recordDate, 0, host, 0, 0)) {
				status.accepted();
			}
		} catch (NoSuchMethodException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}

}
