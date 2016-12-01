package aero.champ.projectpera.scheduler.scheduled;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import aero.champ.projectpera.BO.Staff;
import aero.champ.projectpera.BO.TimeInOut;
import aero.champ.projectpera.reports.datasource.TimeInOutDataSource;
import aero.champ.projectpera.repository.mongodb.StaffRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * The Class SemiMonthlyGenerator.
 */
public class SemiMonthlyGenerator implements ReportGenerator {

	private static final Log LOG = LogFactory.getLog(SemiMonthlyGenerator.class);

	private static final String XLSX_FILE_TYPE = "xlsx";
	private static final String PDF_FILE_TYPE = "pdf";
	private static final String REPORTS_PATH = "/WEB-INF/reports/";
	private static final String DUMP_FOLDER_UNIX = "dump_folder_unix";
	private static final String DUMP_FOLDER_WINDOWS = "dump_folder_windows";
	private static final String XLSX_TEMPLATE = "xlsx_template";
	private static final String PDF_TEMPLATE = "pdf_template";
	private static final String FILENAME = "filename";
	private static final String STAFF_CARD_NO_LIST = "cardNoList";

	private static final DateTimeFormatter dateFormatterReport = DateTimeFormatter.ofPattern("dd-MMM-yy");
	private static final DateTimeFormatter dateFormatterQuery = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	private static final DateTimeFormatter dateFormatterFilename = DateTimeFormatter.ofPattern("yyyyMMdd");

	private LocalDate generationDate;
	private LocalDate startDate = LocalDate.now();
	private LocalDate endDate = LocalDate.now();

	@Override
	public void generateCutOffReport() throws JobExecutionException {		
		LOG.info("start");

		WebApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		StaffRepository staffRepository = ctx.getBean(StaffRepository.class);

		String realPath = ctx.getServletContext().getRealPath(REPORTS_PATH);
		Properties prop = ctx.getBean(Properties.class);

		String dumpFolder = prop.getProperty(DUMP_FOLDER_UNIX);
		if (SystemUtils.IS_OS_WINDOWS) {
			dumpFolder = prop.getProperty(DUMP_FOLDER_WINDOWS);
		}
		String xlsxTemplatePath = realPath + prop.getProperty(XLSX_TEMPLATE);
		String pdfTemplatePath = realPath + prop.getProperty(PDF_TEMPLATE);
		String filename = prop.getProperty(FILENAME);
		String cardNoList = realPath + prop.getProperty(STAFF_CARD_NO_LIST);

		if (computeCutOffDates()) {
			List<Integer> userCardNoList = new ArrayList<>();
			try (BufferedReader userReader = new BufferedReader(new FileReader(cardNoList))) {
				String userLine;
				while ((userLine = userReader.readLine()) != null)
				{
					userCardNoList.add(Integer.parseInt(userLine.split("\\|")[0]));
				}
			}
			catch (FileNotFoundException e)
			{
				handleException(e);
			}
			catch (IOException e)
			{
				handleException(e);
			}

			try {
				JasperDesign xlsxJasperDesign = JRXmlLoader.load(xlsxTemplatePath);
				JasperReport xlsxJasperReport = JasperCompileManager.compileReport(xlsxJasperDesign);

				JasperDesign pdfJasperDesign = JRXmlLoader.load(pdfTemplatePath);
				JasperReport pdfJasperReport = JasperCompileManager.compileReport(pdfJasperDesign);

				List<Staff> staffList = staffRepository.findByDate(startDate.format(dateFormatterQuery), endDate.format(dateFormatterQuery));

				for (Staff staff : staffList) {
					if (!userCardNoList.contains(new Integer(staff.getCardNumber()))) {
						continue;
					}

					String employeeName = staff.getEmployeeName();
					List<TimeInOut> tioList = staff.getTimeInOutList().stream()
							.filter(s -> LocalDate.parse(s.getWorkDate(), dateFormatterQuery).isAfter(startDate.minusDays(1)))
							.filter(s -> LocalDate.parse(s.getWorkDate(), dateFormatterQuery).isBefore(endDate.plusDays(1)))
							.collect(Collectors.toList());

					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("employeeName", employeeName);
					parameters.put("teamLeadName", staff.getTeamLeadName());
					parameters.put("department_position", staff.getDepartment() + "/" + staff.getPosition());
					parameters.put("projectName", staff.getProject());
					parameters.put("periodCovered", startDate.format(dateFormatterQuery) + " - " + endDate.format(dateFormatterQuery));
					parameters.put("realPath", realPath);
					parameters.put("generationDate", generationDate.format(dateFormatterReport));

					JasperPrint xlsxJasperPrint = JasperFillManager.fillReport(xlsxJasperReport, parameters, new TimeInOutDataSource(tioList));
					JasperPrint pdfJasperPrint = JasperFillManager.fillReport(pdfJasperReport, parameters, new TimeInOutDataSource(tioList));

					ByteArrayOutputStream xlsxReport = new ByteArrayOutputStream();
					JRXlsxExporter xlsxExporter = new JRXlsxExporter();
					xlsxExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, xlsxJasperPrint);
					xlsxExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsxReport);
					xlsxExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);              
					xlsxExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE); 
					xlsxExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
					xlsxExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.FALSE);
					xlsxExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					xlsxExporter.exportReport();			

					FileUtils.writeByteArrayToFile(new File(generateFilename(dumpFolder, filename, employeeName, XLSX_FILE_TYPE)), xlsxReport.toByteArray());
					JasperExportManager.exportReportToPdfFile(pdfJasperPrint, generateFilename(dumpFolder, filename, employeeName, PDF_FILE_TYPE));
				}
			} catch (JRException ex) {
				handleException(ex);
			} catch (IOException ex) {
				handleException(ex);
			}   
		}
		else {
			Exception ex = new Exception("Invalid execution date - " + generationDate.format(dateFormatterQuery));
			handleException(ex);
		}

		LOG.info("done");
	}

	private boolean computeCutOffDates() {
		generationDate = LocalDateTime.now().plus(8, ChronoUnit.HOURS).toLocalDate();

		boolean isValidExecutionDate = true;

		endDate = generationDate.minusDays(1);

		if (1 == generationDate.getDayOfMonth()) {
			startDate = endDate.withDayOfMonth(16);
		}
		else if (16 == generationDate.getDayOfMonth()) {
			startDate = generationDate.minusDays(15); //1st
		}
		else {
			isValidExecutionDate = false;
		}

		return isValidExecutionDate;
	}

	private String generateFilename(String targetFolder, String filename, String employeeName, String fileType) {
		StringBuffer sb = new StringBuffer();
		sb.append(targetFolder);
		sb.append(generationDate.format(dateFormatterFilename));
		sb.append("/");
		sb.append(filename);
		sb.append("_");
		sb.append(employeeName);
		sb.append("_");
		sb.append(generationDate.format(dateFormatterFilename));
		sb.append(".");
		sb.append(fileType);

		return sb.toString();
	}

	private void handleException(Exception ex) throws JobExecutionException {
		LOG.error(ex);

		JobExecutionException jee = new JobExecutionException(ex);
		jee.refireImmediately();
		throw jee;
	}

}
