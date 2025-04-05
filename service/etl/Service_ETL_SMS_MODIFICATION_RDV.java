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

import com.moov.moovservice.entity.SmsModificationRDV;
import com.moov.moovservice.entity.SmsText;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsModificationRDVRepository;
import com.moov.moovservice.repository.SmsTextRepository;
import com.moov.moovservice.utils.UtilsGenerale;;
/**
 *
 */
@Service
public class Service_ETL_SMS_MODIFICATION_RDV {

	@Autowired
	private SmsModificationRDVRepository cmdModification;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_MODIFICATION_RDV.class);

	@Autowired
	private SmsTextRepository cmdSms;

	DaOracle dao=new DaOracle();


	  @Scheduled(initialDelay = 4000, fixedRate = 7000)
	  @Async
		public void Sms_Modification_RDV() {
			logger.info("********************DATA d'envoi des SMS de Modification de RDV *****************************");

			try {
				String sql="";

				
				Optional<SmsText> optionalSmsText =
						cmdSms.findAll().stream().findFirst();


				try (ResultSet résultats = dao.resultSetResult(sql)) {
					if (résultats != null) {
						while (résultats.next()) {
							try
							{
							SmsModificationRDV cs = new SmsModificationRDV();
							cs.setConsultation(résultats.getString(6));
							// cs.setCreatedOn(new Date());
							cs.setMrdv_id(résultats.getInt(0));
							cs.setNom(résultats.getString(2));
							cs.setPrenom(résultats.getString(3));
							cs.setTel(UtilsGenerale.tranformTelephone(résultats.getString(5)));

							cs.setMrdv_date(résultats.getDate(1));

							if(optionalSmsText.isPresent()) cs.setSmsformat(optionalSmsText.get());
							try
							{
								cmdModification.save(cs);
							}
							catch(Exception ex)
							{
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
