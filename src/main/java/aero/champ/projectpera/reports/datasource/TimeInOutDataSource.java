package aero.champ.projectpera.reports.datasource;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

import aero.champ.projectpera.BO.TimeInOut;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class TimeInOutDataSource  implements JRDataSource{

	private final Iterator<TimeInOut> iter;
	private final DecimalFormat df = new DecimalFormat("#.00");

	private TimeInOut item;

	public TimeInOutDataSource(List<TimeInOut> timeInOutList){
		iter = timeInOutList.iterator();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		if (jrField.getName().equals("workDate")) {
			return item.getWorkDate();
		}
		
		if (jrField.getName().equals("timeIn")) {
			return item.getTimeIn();
		}

		if (jrField.getName().equals("timeOut")) {
			return item.getTimeOut();
		}
		
		if (jrField.getName().equals("timeInOverride")) {
			return getTimeInOverride();
		}

		if (jrField.getName().equals("timeOutOverride")) {
			return getTimeOutOverride();
		}

		if (jrField.getName().equals("remarks")) {
			return item.getRemarks();
		}

		if (jrField.getName().equals("totalTime")) {
			String totalTime = "0";

			if (null != item.getTimeIn() && null != item.getTimeOut()) {
				LocalTime timeIn = LocalTime.parse(item.getTimeIn());
				LocalTime timeOut = LocalTime.parse(item.getTimeOut());

				totalTime = df.format((Duration.between(timeIn, timeOut).get(ChronoUnit.SECONDS) / 3600.00) - 1);
			}

			return totalTime;
		}
		
		if (jrField.getName().equals("totalTimeOverride")) {
			String totalTime = "0";

			String timeInOverride = getTimeInOverride();
			String timeOutOverride = getTimeOutOverride();
			
			if (null != timeInOverride && null != timeOutOverride) {
				LocalTime timeIn = LocalTime.parse(timeInOverride);
				LocalTime timeOut = LocalTime.parse(timeOutOverride);

				totalTime = df.format((Duration.between(timeIn, timeOut).get(ChronoUnit.SECONDS) / 3600.00) - 1);
			}

			return totalTime;
		}

		if (jrField.getName().equals("billableTime")) {
			String billableTime = "0";

			if (null != getTimeInOverride() && null != getTimeOutOverride()) {
				billableTime =  "8.0";
			}

			return billableTime;
		}

		return null;
	}

	@Override
	public boolean next() throws JRException {
		final boolean hasNext = iter.hasNext();

		if (hasNext) {
			item = iter.next();
		}

		return hasNext;
	}
	
	private String getTimeInOverride() {
		String timeInOverride = item.getTimeInOverride();

		if (timeInOverride == null || timeInOverride.isEmpty()) { 
			return item.getTimeIn();
		}
		else {
			return timeInOverride;
		}
	}
	
	private String getTimeOutOverride() {
		String timeOutOverride = item.getTimeOutOverride();

		if (timeOutOverride == null || timeOutOverride.isEmpty()) { 
			return item.getTimeOut();
		}
		else {
			return timeOutOverride;
		}
	}

}
