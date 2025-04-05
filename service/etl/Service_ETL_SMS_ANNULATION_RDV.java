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
import com.moov.moovservice.entity.SmsAnnulationRDV;
import com.moov.moovservice.entity.SmsText;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsAnnulationRDVRepository;
import com.moov.moovservice.repository.SmsTextRepository;
import com.moov.moovservice.utils.UtilsGenerale;

/**
 * 
 */
@Service
public class Service_ETL_SMS_ANNULATION_RDV {

	@Autowired
	private SmsAnnulationRDVRepository cmdAnnulation;

	@Autowired
	private SmsTextRepository cmdSms;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_ANNULATION_RDV.class);

	DaOracle dao = new DaOracle();

	@Scheduled(initialDelay = 4000, fixedRate = 7000)
	@Async
	public void Sms_Anulation_RDV() {
		logger.info("********************DATA d'envoi des SMS d'Annulation de RDV *****************************");

		try {

			
			String sql = "SELECT v.mrdv_id, v.mrdv_date,v.nom,v.prenom,COALESCE(v.pattel,'00'),v.ag_lib,v.heure_debut,v.heure_fin \r\n"
					+ "FROM PISAM.VUE_SMS v\r\n"
					+ "WHERE trunc(v.mrdv_date)>=trunc(SYSDATE) and v.MRDV_ETAT=2 and v.mrdv_id not in (select n.MRDV_ID_ORIGINE from PISaM.ag_mrdv n WHERE n.MRDV_ID_ORIGINE is not null)";

			try (ResultSet résultats = dao.resultSetResult(sql)) {
				if (résultats != null) {
					while (résultats.next()) {
						try
						{
						SmsAnnulationRDV cs = new SmsAnnulationRDV();
						cs.setConsultation(résultats.getString(6));
						// cs.setCreatedOn(new Date());
						cs.setMrdv_id(résultats.getInt(1));
						cs.setNom(résultats.getString(3));
						cs.setPrenom(résultats.getString(4));
						cs.setTel(UtilsGenerale.tranformTelephone(résultats.getString(5)));
						
						cs.setHeure_debut(résultats.getString(7));
						cs.setHeure_fin(résultats.getString(8));

						Optional<SmsText> optionalSmsText = 
								cmdSms.findAll().stream().findFirst();
						
						if(optionalSmsText.isPresent()) cs.setSmsformat(optionalSmsText.get());
						
						cs.setMrdv_date(résultats.getDate(2));
						try {
							cmdAnnulation.save(cs);
						} catch (Exception ex) {
							logger.error(ex.getMessage());
						}
					}
					
					catch(Exception x)
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
