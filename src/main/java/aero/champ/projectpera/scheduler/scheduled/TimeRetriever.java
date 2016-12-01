package aero.champ.projectpera.scheduler.scheduled;

import org.quartz.JobExecutionException;

public interface TimeRetriever {

	public void saveTime() throws JobExecutionException;
}
