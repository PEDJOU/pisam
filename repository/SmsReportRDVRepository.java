/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.moov.moovservice.entity.SmsReportRDV;


/**
 * 
 */
public interface SmsReportRDVRepository extends JpaRepository<SmsReportRDV, UUID> {
	
	@Query(value="SELECT * FROM sms_reportrdv n  WHERE n.ctp_sms=0 and n.statut=0 and DATE(n.mrdv_date)>=CURRENT_DATE", nativeQuery = true)
	List<SmsReportRDV> findByNOK();

}
