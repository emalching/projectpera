package aero.champ.projectpera.BO;

public class TimeInOut {

	/** The date of work. */
	private String workDate;

	/** The time in. */
	private String timeIn;

	/** The time out. */
	private String timeOut;

	/** The new time in. */
	private String timeInOverride;

	/** The new time out. */
	private String timeOutOverride;

	/** The remark. */
	private String remarks;

	/**
	 * @return the workDate
	 */
	public String getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate the workDate to set
	 */
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	/**
	 * @return the timeIn
	 */
	public String getTimeIn() {
		return timeIn;
	}

	/**
	 * @param timeIn the timeIn to set
	 */
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * @return the timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return the timeInOverride
	 */
	public String getTimeInOverride() {
		return timeInOverride;
	}

	/**
	 * @param timeInOverride the timeInOverride to set
	 */
	public void setTimeInOverride(String timeInOverride) {
		this.timeInOverride = timeInOverride;
	}

	/**
	 * @return the timeOutOverride
	 */
	public String getTimeOutOverride() {
		return timeOutOverride;
	}

	/**
	 * @param timeOutOverride the timeOutOverride to set
	 */
	public void setTimeOutOverride(String timeOutOverride) {
		this.timeOutOverride = timeOutOverride;
	}

	/**
	 * @return the remark
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
