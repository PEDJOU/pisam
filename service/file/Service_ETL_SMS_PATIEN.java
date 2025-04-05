/**
 * 
 */
package com.moov.moovservice.service.file;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.moov.moovservice.entity.Smsgateway;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsgatewayRepository;
import com.moov.moovservice.utils.Constants;
import com.moov.moovservice.utils.UtilsGenerale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 */
@Service
public class Service_ETL_SMS_PATIEN {



	

	@Autowired
	private SmsgatewayRepository cmdgateway;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_PATIEN.class);

	

	@Scheduled(initialDelay = 4000, fixedRate = 7000)
	@Async
	public void Sms_recuper() {
		
		logger.info("*************************************Service_ETL_SMS_PATIEN : Sms_recuper*************************************");

		try {

			DateFormat dateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);
			
			String ladate = dateFormat.format(new Date());
			String dateDuJour = ladate.substring(6, 10) + ladate.substring(3, 5) + ladate.substring(0, 2);
            logger.info("Date du jour {}",dateDuJour);
	
			String fichierlog = "SMS_IN_" + dateDuJour + ".log";
			logger.info("FIchier log du jour {}",fichierlog);
			
			String url = "http://172.16.46.156/cb/SMS-Logs.php?password=admin&domain=sms&action=get&filename=[FILENAME]&filetype=cdr&external=0&format=text";
			
			 url = url.replace("[FILENAME]", fichierlog);
			
			String path="C:\\Users\\Administrateur\\Documents\\"+fichierlog+"log.txt";
			downloadUsingNIO(url, path);

			Path filePath = Paths.get(path);
			
			//Nombre enregistrement 
		//	int nombreEnregitements = cmdgateway.findAll().size();
			
			try {
				List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				for (String line : lines) {
					String donnees = line.replace("|", "@");
					String[] donne = donnees.split("@");
					try {
							String date = null, msisdn = null, message = null,fullDate = null;
							
							
					//		int i = nombreEnregitements == 0 ? 0 : nombreEnregitements;
							
							date = donne[0].substring(0, 19);
							
						    //fullDate = donne[0].substring(0, 19);
							
							msisdn = (String) donne[3];
							message = donne[4];
							
						 //  i++;
							
							
							Smsgateway gt = new Smsgateway();
							gt.setDatereception(date);
							gt.setIsactive(true);
							gt.setMsg(message);
							gt.setMsisdn(msisdn);
							try
							{
							cmdgateway.save(gt);
							}
							catch(Exception ex)
							{
								
							}
							
							/*
							 * var nouveauIdentifiant = UtilsGenerale.creerIdentifiant(fullDate, msisdn);
							 * gt.setCode(i); gt.setIdentifiant(nouveauIdentifiant); gt.setMsg(message);
							 * gt.setMsisdn(msisdn); gt.setDatereception(date); gt.setIsactive(true);
							 * 
							 * List<Smsgateway> smsgateways =
							 * cmdgateway.findByIdentifiant(nouveauIdentifiant);
							 * if(Objects.nonNull(smsgateways)) { cmdgateway.save(gt); }
							 */
							
							
							
						
					} catch (Exception ex) {
						logger.error(ex.getMessage());
					}
				}
			} catch (IOException ex) {
				System.out.format("I/O error: %s%n", ex);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
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
