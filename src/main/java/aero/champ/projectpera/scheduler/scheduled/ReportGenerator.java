package aero.champ.projectpera.scheduler.scheduled;

import org.quartz.JobExecutionException;

/**
 * The Interface ReportGenerator.
 */
public interface ReportGenerator {


	/**
	 * Generate cut off report.
	 *
	 * @param employeeDetails the employee details
	 */
	public void generateCutOffReport() throws JobExecutionException;

}
