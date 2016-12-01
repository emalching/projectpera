package aero.champ.projectpera.BO;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Holidays {

	/** The list of holidays. */
	private List<Holiday> holidayList;

	/**
	 * @return the holidayList
	 */
	public List<Holiday> getHolidayList() {
		return holidayList;
	}

	/**
	 * @param holidayList the holidayList to set
	 */
	public void setHolidayList(List<Holiday> holidayList) {
		this.holidayList = holidayList;
	}

}

