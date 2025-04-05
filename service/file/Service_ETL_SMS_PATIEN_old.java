/**
 * 
 */
package com.moov.moovservice.service.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.moov.moovservice.entity.SmsAnnulationRDV;
import com.moov.moovservice.entity.Smsgateway;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsAnnulationRDVRepository;
import com.moov.moovservice.repository.SmsgatewayRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 */
@Service
public class Service_ETL_SMS_PATIEN_old {

	@Autowired
	private SmsAnnulationRDVRepository cmdAnnulation;

	@Autowired
	private SmsgatewayRepository cmdgateway;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_PATIEN_old.class);

	DaOracle dao = new DaOracle();

	//@Scheduled(initialDelay = 4000, fixedRate = 7000)
	//@Async
	public void Sms_recuper() {

		try {

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			String ladate = dateFormat.format(new Date());
			String pedro = ladate.substring(6, 10) + ladate.substring(3, 5) + ladate.substring(0, 2);
		
			String formatlog = "SMS_IN_" + pedro + ".log";

			// lien = lien.replace("[FILENAME]", formatlog);

			String url = "C:\\Users\\pouattara.ETISALAT-AFRICA\\Downloads\\CATALOGUE DES ATELIERS JAKA.pdf";
			Path filePath = Paths.get("C:\\Users\\pouattara.ETISALAT-AFRICA\\Downloads\\log.txt");
			Charset charset = StandardCharsets.UTF_8;
			try {
				List<String> lines = Files.readAllLines(filePath, charset);
				for (String line : lines) {
					String donneEs = line.replace("|", "@");
					String[] donne = donneEs.split("@");
					try {
							String date = null, msisdn = null, message = null;
							int i = 0;
							for (String n : donne) {
								i++;
								
								switch (i) {
								case 1:
									date = n.substring(0, 10);
									break;
								case 4:
									msisdn = n;
									break;
								case 5:
									message = n;
									break;
								default:
								}
							}
							
							Smsgateway gt = new Smsgateway();
							gt.setCode(i);
							gt.setMsg(message);
							gt.setMsisdn(msisdn);
							gt.setDatereception(date);
							cmdgateway.save(gt);
							
							msisdn = null;
							message = null;
							date = null;
						
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			} catch (IOException ex) {
				System.out.format("I/O error: %s%n", ex);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private static void downloadUsingStream(String urlStr, String file) throws IOException {
		URL url = new URL(urlStr);
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		FileOutputStream fis = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int count = 0;
		while ((count = bis.read(buffer, 0, 1024)) != -1) {
			fis.write(buffer, 0, count);
		}
		fis.close();
		bis.close();
	}

	private static void downloadUsingNIO(String urlStr, String file) throws IOException {
		URL url = new URL(urlStr);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}

}
