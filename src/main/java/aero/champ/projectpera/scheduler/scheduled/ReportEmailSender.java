package aero.champ.projectpera.scheduler.scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import aero.champ.projectpera.BO.Staff;
import aero.champ.projectpera.BO.Users;
import aero.champ.projectpera.repository.mongodb.StaffRepository;
import aero.champ.projectpera.repository.mongodb.UsersRepository;

public class ReportEmailSender extends QuartzJobBean {

	private static final String PDF_FILE_TYPE = "pdf";
	private static final String DUMP_FOLDER_WINDOWS = "dump_folder_windows";
	private static final String FILENAME = "filename";

	private static final DateTimeFormatter dateFormatterFilename = DateTimeFormatter.ofPattern("yyyyMMdd");

	private LocalDate generationDate;

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public MailSender getMailSender() {
		return this.mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public SimpleMailMessage getTemplateMessage() {
		return this.templateMessage;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		send();
	}

	public void send() throws JobExecutionException {
		WebApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
		StaffRepository staffRepository = ctx.getBean(StaffRepository.class);
		UsersRepository usersRepository = ctx.getBean(UsersRepository.class);
		Properties prop = ctx.getBean(Properties.class);

		String dumpFolderWindows = prop.getProperty(DUMP_FOLDER_WINDOWS);
		String filename = prop.getProperty(FILENAME);

		generationDate = LocalDateTime.now().plus(8, ChronoUnit.HOURS).toLocalDate();

		List<String> teamLeadNames = mongoTemplate.getCollection("staff").distinct("teamLeadName");
		for (String teamLeadName : teamLeadNames) {
			List<Users> users = usersRepository.findByDisplayName(teamLeadName);
			if (!users.isEmpty()) {
				String teamLeadEmailAddress = users.get(0).getEmailAddress();

				List<Staff> staffList = staffRepository.findByTeamLeadName(teamLeadName);

				StringBuffer sb = new StringBuffer();
				sb.append("Dear " + teamLeadName + ",");
				sb.append("\n\n");
				sb.append("Below are the direct links containing the daily time in/out of your subordinates:");
				sb.append("\n\n");
				
				for (Staff staff : staffList) {
					String employeeName = staff.getEmployeeName();
					String pdfLink = generateFilename(dumpFolderWindows, filename, employeeName, PDF_FILE_TYPE);
					sb.append("<" + pdfLink + ">");
					sb.append("\n\n");
				}
				
				sb.append("\n\n");
				sb.append("Love,");
				sb.append("\n");
				sb.append("Project Pera Team");

				// Create a thread safe "copy" of the template message and customize it
				SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
				msg.setTo(teamLeadEmailAddress);
				msg.setSubject("[PERA] Subordinate's Falco Extract for " + generationDate.format(dateFormatterFilename));
				msg.setText(sb.toString());
				try{
					this.mailSender.send(msg);
				}
				catch (MailException ex) {
					handleException(ex);
				}
			}
		}
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
		JobExecutionException jee = new JobExecutionException(ex);
		jee.refireImmediately();
		throw jee;
	}
}
