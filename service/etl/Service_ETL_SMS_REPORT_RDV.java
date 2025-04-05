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
import com.moov.moovservice.entity.SmsReportRDV;
import com.moov.moovservice.entity.SmsText;
import com.moov.moovservice.repository.DaOracle;
import com.moov.moovservice.repository.SmsReportRDVRepository;
import com.moov.moovservice.repository.SmsTextRepository;
import com.moov.moovservice.utils.UtilsGenerale;

/**
 * 
 */
@Service
public class Service_ETL_SMS_REPORT_RDV {

	@Autowired
	private SmsReportRDVRepository cmdReport;

	@Autowired
	private SmsTextRepository cmdSms;

	private static final Logger logger = LoggerFactory.getLogger(Service_ETL_SMS_REPORT_RDV.class);

	DaOracle dao = new DaOracle();

	@Scheduled(initialDelay = 4000, fixedRate = 7000)
	@Async
	public void Sms_Report_RDV() {
		logger.info("********************DATA d'envoi des SMS de Report de RDV *****************************");

		try {

			Optional<SmsText> optionalSmsText =
					cmdSms.findAll().stream().findFirst();

			String sql = "SELECT v.mrdv_id, v.mrdv_date,v.nom,v.prenom,COALESCE(v.pattel,'00'),v.ag_lib,g.mrdv_date as date_origine,v.heure_debut,v.heure_fin \r\n"
					+ "FROM PISAM.vue_sms v\r\n" + "INNER JOIN PISAM.AG_MRDV g on G.mrdv_id=v.mrdv_id_origine\r\n"
					+ "WHERE v.mrdv_id_origine is not null And trunc(v.mrdv_date)>trunc(SYSDATE)";

			try (ResultSet résultats = dao.resultSetResult(sql)) {
				if (résultats != null) {
					while (résultats.next()) {
						try {
							SmsReportRDV cs = new SmsReportRDV();
							cs.setConsultation(résultats.getString(6));
							cs.setMrdv_date(résultats.getDate(2));
							// cs.setCreatedOn(new Date());
							cs.setMrdv_id(résultats.getInt(1));
							cs.setNom(résultats.getString(3));
							cs.setPrenom(résultats.getString(4));
							cs.setTel(UtilsGenerale.tranformTelephone(résultats.getString(5)));
							cs.setMrdv_date(résultats.getDate(2));
							if(optionalSmsText.isPresent()) cs.setSmsformat(optionalSmsText.get());
							cs.setMrdv_date_origine(résultats.getDate(7));
							
							cs.setHeure_debut(résultats.getString(8));
							cs.setHeure_fin(résultats.getString(9));

							try {
								cmdReport.save(cs);
							} catch (Exception ex) {
								// logger.error(ex.getMessage());
							}
						} catch (Exception ex) {
							// logger.error(ex.getMessage());
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

}
