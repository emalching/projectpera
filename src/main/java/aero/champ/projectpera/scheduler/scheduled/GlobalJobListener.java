package aero.champ.projectpera.scheduler.scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import aero.champ.projectpera.BO.BatchRun;
import aero.champ.projectpera.repository.mongodb.BatchRunRepository;

public class GlobalJobListener extends JobListenerSupport {

	private static final String LISTENER_NAME = "globalJobListener";
	private static final String SUCCESS = "SUCCESS";
	private static final String ERROR = "ERROR";

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime startTime;

	@Override
	public String getName() {
		return LISTENER_NAME;
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		startTime = LocalDateTime.now();
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		WebApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		BatchRunRepository batchRunRepository = ctx.getBean(BatchRunRepository.class);

		String jobName = context.getJobDetail().getKey().getName();

		BatchRun batchRun = new BatchRun();
		batchRun.setBatchJob(jobName);
		batchRun.setStartTime(startTime.format(formatter));
		batchRun.setEndTime(LocalDateTime.now().format(formatter));

		if (jobException == null) {
			batchRun.setStatus(SUCCESS);
		}
		else {
			batchRun.setStatus(ERROR);
			batchRun.setErrorMessage(jobException.getMessage());
		}

		batchRunRepository.save(batchRun);
	}

}
