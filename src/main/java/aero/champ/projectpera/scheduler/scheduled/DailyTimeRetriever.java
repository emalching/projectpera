package aero.champ.projectpera.scheduler.scheduled;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import aero.champ.projectpera.BO.Holiday;
import aero.champ.projectpera.BO.Holidays;
import aero.champ.projectpera.BO.Staff;
import aero.champ.projectpera.BO.TimeInOut;
import aero.champ.projectpera.repository.mongodb.HolidaysRepository;
import aero.champ.projectpera.repository.mongodb.StaffRepository;
import aero.champ.projectpera.sql.dao.FalcoTransactionsDao;

public class DailyTimeRetriever implements TimeRetriever {

	private static final String DEFAULT_START_TIME = "09:00:00";
	private static final String DEFAULT_END_TIME = "18:00:00";

	private static final DateTimeFormatter dateFormatterQuery = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	public void saveTime() throws JobExecutionException {
		LocalDate dateToRetrieve = LocalDate.now(); //GMT
		saveTime(dateToRetrieve.format(dateFormatterQuery));
	}

	public void saveTime(String dateToRetrieve) throws JobExecutionException {
		saveTime(dateToRetrieve, dateToRetrieve);
	}

	public void saveTime(String startDate, String endDate) throws JobExecutionException {
		WebApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		StaffRepository staffRepository = ctx.getBean(StaffRepository.class);
		HolidaysRepository holidaysRepository = ctx.getBean(HolidaysRepository.class);
		List<Holidays> holidaysList = holidaysRepository.findAll();
		List<Holiday> holidayList = new ArrayList<>();
		if (holidaysList.size() > 0) {
			holidayList = holidaysList.get(0).getHolidayList();
		}

		FalcoTransactionsDao ftd = new FalcoTransactionsDao();
		List<Staff> staffNoMissingDateList = ftd.getTimeInOut(startDate, endDate);

		processNonMissingDates(staffNoMissingDateList, staffRepository);

		LocalDate startLdt = LocalDate.parse(startDate, dateFormatterQuery);
		LocalDate endLdt = LocalDate.parse(endDate, dateFormatterQuery).plusDays(1);

		processMissingDates(staffRepository, holidayList, startLdt, endLdt);
		
		sortDates(staffRepository);
		/*
		for (Staff staff : staffList) {			
			List<TimeInOut> tioList = staff.getTimeInOutList();

			for (LocalDate i = startLdt; i.isBefore(endLdt); i = i.plusDays(1)) {
				boolean dateExists = false;

				for (TimeInOut timeInOut : tioList) {
					String workDate = timeInOut.getWorkDate();
					if (workDate.equalsIgnoreCase(i.format(dateFormatterQuery))) {
						dateExists = true;
						break;
					}
				}

				if (!dateExists) {
					TimeInOut tio = new TimeInOut();
					tio.setWorkDate(i.format(dateFormatterQuery));
					if (isWeekdayHoliday(i)) {
						tio.setTimeIn(DEFAULT_START_TIME);
						tio.setTimeOut(DEFAULT_END_TIME);
					}
					tioList.add(tio);
				}
			}

			tioList = tioList.stream().sorted(Comparator.comparing(TimeInOut::getWorkDate)).collect(Collectors.toList());

			Staff currStaff = staffRepository.findByCardNumber(staff.getCardNumber());
			if (null == currStaff) {
				staff.setTimeInOutList(tioList);
				staffRepository.insert(staff);
			}
			else {
				List<TimeInOut> currTioList = currStaff.getTimeInOutList();
				currTioList.addAll(tioList);
				currStaff.setTimeInOutList(currTioList);
				staffRepository.save(currStaff);
			}
		}
		*/
	}

	private void processNonMissingDates(List<Staff> staffList, StaffRepository staffRepository) {
		for (Staff staff : staffList) {
			Staff currStaff = staffRepository.findByCardNumber(staff.getCardNumber());

			if (null == currStaff) {
				staffRepository.insert(staff);
			}
			else {
				currStaff.getTimeInOutList().addAll(staff.getTimeInOutList());
				staffRepository.save(currStaff);
			}
		}
	}

	private void processMissingDates(StaffRepository staffRepository, List<Holiday> holidayList, LocalDate startDt, LocalDate endDt) {
		for (LocalDate i = startDt; i.isBefore(endDt); i = i.plusDays(1)) {
			List<Staff> staffMissingDateList =  staffRepository.findByMissingDate(i.format(dateFormatterQuery));

			for (Staff staff : staffMissingDateList) {
				TimeInOut tio = new TimeInOut();
				tio.setWorkDate(i.format(dateFormatterQuery));
				String weekdayHoliday = weekdayHoliday(holidayList, i);
				if (!weekdayHoliday.isEmpty()) {
					tio.setTimeInOverride(DEFAULT_START_TIME);
					tio.setTimeOutOverride(DEFAULT_END_TIME);
					tio.setRemarks("Holiday - " + weekdayHoliday);
				}
				staff.getTimeInOutList().add(tio);
				staffRepository.save(staff);
			}
		}
	}
	
	private void sortDates(StaffRepository staffRepository) {
		List<Staff> staffList =  staffRepository.findAll();
		
		for (Staff staff : staffList) {
			List<TimeInOut> tioList = staff.getTimeInOutList();
			tioList = tioList.stream().sorted(Comparator.comparing(TimeInOut::getWorkDate)).collect(Collectors.toList());
			
			staff.setTimeInOutList(tioList);
			staffRepository.save(staff);
		}
	}

	private String weekdayHoliday(List<Holiday> holidayList, LocalDate date) {
		String weekdayHoliday = "";

		if (date.getDayOfWeek().getValue() <= DayOfWeek.SATURDAY.getValue()) {
			for (Holiday holiday : holidayList) {
				if (holiday.getdate().equals(date.format(dateFormatterQuery))) {
					weekdayHoliday = holiday.getHoliday();
					break;
				}
			}
		}

		return weekdayHoliday;
	}
}
