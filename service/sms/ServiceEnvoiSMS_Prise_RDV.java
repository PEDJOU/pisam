/**
 * 
 */
package com.moov.moovservice.service.sms;




import static com.moov.moovservice.utils.Constants.CODE;
import static com.moov.moovservice.utils.Constants.DATE_RDV;
import static com.moov.moovservice.utils.Constants.DESTINATIARE;
import static com.moov.moovservice.utils.Constants.DOCTEUR;
import static com.moov.moovservice.utils.Constants.HEURE_DEBUT;
import static com.moov.moovservice.utils.Constants.MSG_SMS;
import static com.moov.moovservice.utils.Constants.NOM;
import static com.moov.moovservice.utils.Constants.PSWD;
import static com.moov.moovservice.utils.Constants.SENDER;
import static com.moov.moovservice.utils.Constants.USER;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.moov.moovservice.entity.ParametreSms;
import com.moov.moovservice.entity.SmsPriseRDV;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.ParametreSmsRepository;
import com.moov.moovservice.repository.SmsPriseRDVRepository;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

/**
 * 
 */

@Service
public class ServiceEnvoiSMS_Prise_RDV {

	@Autowired
	private SendService service;

	@Autowired
	private ParametreSmsRepository param;

	@Autowired
	private SmsPriseRDVRepository cmdPrise;
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEnvoiSMS_Prise_RDV.class);

	DaOracle dao=new DaOracle();
	
	@Scheduled(initialDelay = 4000, fixedRate = 7000)
	@Async
	public void Sms_Prise_RDV() {
		try {
			//String lien="https://smspro.hyperaccesss.com:8443/api/addOneSms?Code={CODE}&Sender={SENDER}&Sms={MSG}&Dest={DEST}&Username={USER}&Password={PSWD}";
			String liens="https://smspro.hyperaccesss.com:8443/api/addOneSms?Code={CODE}&Sender={SENDER}&Sms={MSG}&Dest={DEST}&Username={USER}&Password={PSWD}";
			List<ParametreSms> pars = param.findAll();
			ParametreSms cd = null;
			for(ParametreSms c:pars)
			{
				cd=c;
			}
			if (cd != null) {
				List<SmsPriseRDV> sms = cmdPrise.findByNOK();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			    
				for (SmsPriseRDV s : sms) {
					
					//Declaration de la variable consultation
					String consultation = s.getConsultation();
					
					
					//recuperation de la Date
					String date    = formatter.format(s.getMrdv_date());
					
					//Nom du docteur
					 var nomDocteur = consultation.transform(t -> Arrays.asList(t.split("-")));
					
					String msg= s.getSmsformat().getMessageprise()
							.replace(NOM,s.getNom())
							.replace(DOCTEUR,nomDocteur.get(1).trim())
							.replace(DATE_RDV, date)
							.replace(HEURE_DEBUT, s.getHeure_debut());
					
					System.out.println(msg);

					String url = liens.replace(CODE, cd.getCode())
							.replace(SENDER, cd.getSender())
							.replace(MSG_SMS, msg)
							.replace(DESTINATIARE, s.getTel())
							.replace(USER, cd.getCmptsms())
							.replace(PSWD, cd.getPasswordsms());
					
					System.out.println(url);
				
					HttpResponse<JsonNode> jsonresponse = service.get(url);
					
					logger.error("jsonresponse {}",jsonresponse);
				
					if (jsonresponse.getStatus() == 200) 
					{
						int Etat = jsonresponse.getBody().getObject().getInt("Etat");
						
						if (Etat == 1) {
							s.setMessage(s.getMessage());
							s.setStatut(Etat);
							s.setCtp_sms(Etat);
							s.setCreatedon(new Date());
							s.setMessage(msg);
							cmdPrise.save(s);
						} else {
							logger.error("Echec d'envoi de SMS DE prise de RDV Code ETAT:" + Etat);
						}

					} else {
						logger.error("Echec d'envoi de SMS DE prise de RDV Code HTTP:" + jsonresponse.getStatus());
					}
				}

			} else {
				logger.error("Configuration Introuvable - SMS Prise de RDV");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}
	}

	
	
	

	//@Scheduled(initialDelay = 4000, fixedRate = 7000)
	//@Async
	public void Sms_Prise_RDV_Test() {
		try {
			
					String uri="https://smspro.hyperaccesss.com:8443/api/addOneSms?Code=CMPTO20210201085220&Sender=PISAM&Sms=Hello%20Test&Dest=2250779881920&Sms=TEST&Dest=2250102000236&Username=pisam&Password=Agend@23";
					HttpResponse<JsonNode> jsonresponse = service.get(uri);
					if (jsonresponse.getStatus() == 200) 
					{
						System.out.println("OKKK ol");
						
					} else {
						logger.error("Echec d'envoi de SMS DE prise de RDV Code HTTP:" + jsonresponse.getStatus());
					}
				
		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}
	}

	
}
