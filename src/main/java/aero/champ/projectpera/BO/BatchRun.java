package aero.champ.projectpera.BO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BatchRun {

	@Id
	private String id;

	/** The batch job. */
	private String batchJob;

	/** The batch job start time. */
	private String startTime;

	/** The batch job end time. */
	private String endTime;

	/** The batch job status. */
	private String status;

	/** The batch job error message. */
	private String errorMessage;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the batch job.
	 *
	 * @return the batch job
	 */
	public String getBatchJob() {
		return batchJob;
	}

	/**
	 * Sets the batch job.
	 *
	 * @param batchJob the new batch job
	 */
	public void setBatchJob(String batchJob) {
		this.batchJob = batchJob;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the employee's complete name.
	 *
	 * @return the employee's complete name
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * Sets the employee's complete name.
	 *
	 * @param employeeName the new employee's complete name
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the department.
	 *
	 * @return the department
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the department.
	 *
	 * @param department the new department
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the position
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param position the position to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

