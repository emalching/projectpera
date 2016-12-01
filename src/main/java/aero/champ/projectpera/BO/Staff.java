package aero.champ.projectpera.BO;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Staff {

	@Id
	private String id;

	/** The card number. */
	private int cardNumber;

	/** The first name. */
	private String firstName;

	/** The last name. */
	private String lastName;

	/** The employee's complete name. */
	private String employeeName;

	/** The department. */
	private String department;

	/** The position. */
	private String position;

	/** The project. */
	private String project;

	/** The team lead name. */
	private String teamLeadName;

	/** The Falco-generated time in and out. */
	private List<TimeInOut> timeInOutList;

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
	 * @return the cardNumber
	 */
	public int getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the employee's complete name.
	 *
	 * @return the employee's complete name
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * Sets the employee's complete name.
	 *
	 * @param employeeName the new employee's complete name
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * Gets the department.
	 *
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * Sets the department.
	 *
	 * @param department the new department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * Gets the team lead name.
	 *
	 * @return the team lead name
	 */
	public String getTeamLeadName() {
		return teamLeadName;
	}

	/**
	 * Sets the team lead name.
	 *
	 * @param teamLeadName the new team lead name
	 */
	public void setTeamLeadName(String teamLeadName) {
		this.teamLeadName = teamLeadName;
	}

	/**
	 * @return the timeInOutList
	 */
	public List<TimeInOut> getTimeInOutList() {
		return timeInOutList;
	}

	/**
	 * @param timeInOutList the timeInOutList to set
	 */
	public void setTimeInOutList(List<TimeInOut> timeInOutList) {
		this.timeInOutList = timeInOutList;
	}

}

