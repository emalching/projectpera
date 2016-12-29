package aero.champ.projectpera.reports.download;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.champ.projectpera.scheduler.scheduled.SemiMonthlyGenerator;

@RestController
public class RestDownloadController {
	
	@RequestMapping(value = "/reports", params = { "cardNumber", "periodStart", "periodEnd" }, method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> generateAdHocReport(@RequestParam("cardNumber") String cardNumber, @RequestParam("periodStart") String periodStart, @RequestParam("periodEnd") String periodEnd)
	        throws IOException {
		String adHocGenerationDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		SemiMonthlyGenerator smg = new SemiMonthlyGenerator();
		smg.generateAdHocReport(adHocGenerationDateTime, cardNumber, periodStart, periodEnd);
		
	    ClassPathResource pdfFile = new ClassPathResource(cardNumber + "_" + adHocGenerationDateTime + ".zip");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Access-Control-Allow-Origin", "*");
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    headers.add("Content-Disposition", "attachment; filename=" + cardNumber + "_" + periodStart + "-" + periodEnd + "_" + adHocGenerationDateTime + ".zip");
	    
	    return ResponseEntity
	            .ok()
	            .headers(headers)
	            .contentLength(pdfFile.contentLength())
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(new InputStreamResource(pdfFile.getInputStream()));
	}
	
}
