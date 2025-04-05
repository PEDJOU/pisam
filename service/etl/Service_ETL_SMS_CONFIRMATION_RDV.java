/**
 *
 */
package com.moov.moovservice.service.etl;

import java.sql.ResultSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.moov.moovservice.entity.SmsConfirmationRDV;
import com.moov.moovservice.entity.SmsText;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsConfirmationRDVRepository;
import com.moov.moovservice.repository.SmsTextRepository;
import com.moov.moovservice.utils.UtilsGenerale;;

/**
 *
 */
@Service
public class Service_ETL_SMS_CONFIRMATION_RDV {

	@Autowired
	private SmsConfirmationRDVRepository cmdConfirmation;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_CONFIRMATION_RDV.class);

	@Autowired
	private SmsTextRepository cmdSms;

	DaOracle dao = new DaOracle();

	@Scheduled(initialDelay = 4000, fixedRate = 7000)
	@Async
	public void Sms_Confirmation_RDV() {
		logger.info("********************DATA d'envoi des SMS de Confirmation de RDV *****************************");

		try {


			String sql = "SELECT v.mrdv_id, v.mrdv_date,v.nom,v.prenom,COALESCE(v.pattel,'00'),v.ag_lib,v.heure_debut,v.heure_fin \r\n"
					+ "FROM PISAM.vue_sms v\r\n"
					+ "WHERE v.mrdv_id not in (SELECT s.mrdv_id_origine FROM PISAM.vue_sms s WHERE s.mrdv_id_origine is not null)\r\n"
					+ "AND TRUNC(v.mrdv_date -1)=trunc(sysdate)";
			

			Optional<SmsText> optionalSmsText =
					cmdSms.findAll().stream().findFirst();
			
			try (ResultSet résultats = dao.resultSetResult(sql)) {
				if (résultats != null) {
					while (résultats.next()) {
						try{

						SmsConfirmationRDV cs = new SmsConfirmationRDV();
						cs.setConsultation(résultats.getString(6));
						// cs.setCreatedOn(new Date());
						cs.setMrdv_id(résultats.getInt(1));
						cs.setNom(résultats.getString(3));
						cs.setPrenom(résultats.getString(4));
						cs.setTel(UtilsGenerale.tranformTelephone(résultats.getString(5)));

						cs.setMrdv_date(résultats.getDate(2));
						cs.setHeure_debut(résultats.getString(7));
						cs.setHeure_fin(résultats.getString(8));


						if(optionalSmsText.isPresent()) cs.setSmsformat(optionalSmsText.get());
						try {
							cmdConfirmation.save(cs);
						} catch (Exception ex) {
							logger.error(ex.getMessage());
						}
					}
						catch(Exception ex)
						{

						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

}
