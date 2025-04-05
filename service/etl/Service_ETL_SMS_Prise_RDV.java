/**
 *
 */
package com.moov.moovservice.service.etl;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.moov.moovservice.entity.SmsPriseRDV;
import com.moov.moovservice.entity.SmsText;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsPriseRDVRepository;
import com.moov.moovservice.repository.SmsTextRepository;
import com.moov.moovservice.utils.UtilsGenerale;
/**
 *
 */
@Service
public class Service_ETL_SMS_Prise_RDV {


	@Autowired
	private SmsPriseRDVRepository cmdPrise;

	@Autowired
	private SmsTextRepository cmdSms;


	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_Prise_RDV.class);


	DaOracle dao=new DaOracle();




	@Scheduled(initialDelay = 2000, fixedRate = 50000)
	@Async
	public void Sms_ETL_Prise_RDV() {



		logger.info("********************DATA d'envoi des SMS de prise de RDV *****************************");

		try {
			 
			String sql =""" 
							SELECT v.mrdv_id, v.mrdv_date,v.nom,v.prenom,COALESCE(v.pattel,'00'),v.ag_lib,v.heure_debut,v.heure_fin 
							FROM PISAM.VUE_SMS v
							WHERE trunc(v.mrdv_date)>trunc(SYSDATE+1) AND v.mrdv_id
							not in (SELECT s.mrdv_id_origine FROM PISAM.vue_sms s WHERE s.mrdv_id_origine is not null) 
								ORDER BY v.mrdv_id
						""";
			
			Optional<SmsText> optionalSmsText =
					cmdSms.findAll().stream().findFirst();

			try (ResultSet resultats = dao.resultSetResult(sql)) {
				if (resultats != null) {
					while (resultats.next())
					{
						
						try {

						SmsPriseRDV cs = SmsPriseRDV.builder()
								.mrdv_id(resultats.getInt(1))
								.mrdv_date(resultats.getDate(2))
								.nom(resultats.getString(3))
								.prenom(resultats.getString(4))
								.tel(UtilsGenerale.tranformTelephone(resultats.getString(5)))
								.consultation(resultats.getString(6))
								.heure_debut(resultats.getString(7))
								.heure_fin(resultats.getString(8))
								.smsformat(optionalSmsText.get())
								.build();
						
					            cmdPrise.save(cs);
						} catch (Exception ex) {
							logger.error("Sms_ETL_Prise_RDV :",ex.getMessage());
						}

				}
					
					logger.info("Rows count ",resultats);
			}
		} catch (Exception ex) {
			logger.error("Sms_ETL_Prise_RDV :",ex.getMessage());
		}
		}catch (Exception ex) {
			logger.error("Sms_ETL_Prise_RDV :",ex.getMessage());
		}

	}


}
