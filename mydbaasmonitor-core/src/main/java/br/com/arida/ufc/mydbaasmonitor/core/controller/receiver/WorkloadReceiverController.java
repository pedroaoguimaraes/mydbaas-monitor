package main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.receiver;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.metric.database.WorkloadStatus;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.receiver.common.AbstractReceiver;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.MetricRepository;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.view.DefaultStatus;

@Resource
@Path("/workload")
public class WorkloadReceiverController extends AbstractReceiver {

	private static final Logger logger = Logger.getLogger(WorkloadReceiverController.class);
	
	public WorkloadReceiverController(DefaultStatus status, MetricRepository repository) {
		super(status, repository);
	}
	
	@Post("/workloadstatus")
	public void workloadStatus(WorkloadStatus metric, int database, String recordDate) {
		try {
			if (repository.saveMetric(metric, recordDate, 0, 0, 0, database)) {
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

