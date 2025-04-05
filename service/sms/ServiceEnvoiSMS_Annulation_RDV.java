/**
 * 
 */
package com.moov.moovservice.service.sms;

import static com.moov.moovservice.utils.Constants.CODE;
import static com.moov.moovservice.utils.Constants.DATE_RDV;
import static com.moov.moovservice.utils.Constants.DESTINATIARE;
import static com.moov.moovservice.utils.Constants.DOCTEUR;
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
import com.moov.moovservice.entity.SmsAnnulationRDV;
import com.moov.moovservice.repository.ParametreSmsRepository;
import com.moov.moovservice.repository.SmsAnnulationRDVRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

/**
 * 
 */
@Service
public class ServiceEnvoiSMS_Annulation_RDV {

	@Autowired
	private SendService service;

	@Autowired
	private ParametreSmsRepository param;

	@Autowired
	private SmsAnnulationRDVRepository cmdAnnulation;

	private static final Logger logger = LoggerFactory.getLogger(ServiceEnvoiSMS_Annulation_RDV.class);

	 @Scheduled(initialDelay = 4000, fixedRate = 7000)
	 @Async
	public void Sms_Anulation_RDV() {
		try {
			List<ParametreSms> pars = param.findAll();
			ParametreSms cd = null;
			for(ParametreSms c:pars)
			{
				cd=c;
			}
			if (cd != null) {
				String liens = "https://smspro.hyperaccesss.com:8443/api/addOneSms?Code={CODE}&Sender={SENDER}&Sms={MSG}&Dest={DEST}&Username={USER}&Password={PSWD}";

				List<SmsAnnulationRDV> sms = cmdAnnulation.findByNOK();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
				for (SmsAnnulationRDV s : sms) {
					
					//Declaration de la variable consultation
					String consultation = s.getConsultation();
					
					//recuperation de la Date
					String date    = formatter.format(s.getMrdv_date());
					
					//Nom du docteur
					 var nomDocteur = consultation.transform(t -> Arrays.asList(t.split("-")));
					
					String msg= s.getSmsformat().getMessageannulation()
							.replace(NOM,s.getNom())
							.replace(DOCTEUR,nomDocteur.get(1).trim())
							.replace(DATE_RDV, date);

					String url = liens.replace(CODE, cd.getCode())
							.replace(SENDER, cd.getSender())
							.replace(MSG_SMS, msg)
							.replace(DESTINATIARE, s.getTel())
							.replace(USER, cd.getCmptsms())
							.replace(PSWD, cd.getPasswordsms());
					
					HttpResponse<JsonNode> jsonresponse = service.get(url);
					
					logger.error("jsonresponse {}",jsonresponse);
					
					if (jsonresponse.getStatus() == 200) {
						int Etat = jsonresponse.getBody().getObject().getInt("Etat");
						if (Etat == 1) {
							// Mise a jour
							s.setMessage(s.getMessage());
							s.setStatut(Etat);
							s.setCtp_sms(Etat);
							s.setCreatedon(new Date());
							s.setMessage(msg);
							cmdAnnulation.save(s);
						} else {
							logger.error("Echec d'envoi de SMS D'Annulation de RDV Code ETAT:" + Etat);
						}

					} else {
						logger.error("Echec d'envoi de SMS D'Annulation de RDV Code HTTP:" + jsonresponse.getStatus());
					}
				}

			} else {
				logger.error("Configuration Introuvable - SMS D'Annulation de RDV");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}
	}

}
