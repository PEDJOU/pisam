/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.moov.moovservice.entity.SmsConfirmationRDV;



/**
 * 
 */
public interface SmsConfirmationRDVRepository extends JpaRepository<SmsConfirmationRDV, UUID> {
	@Query(value="SELECT * FROM sms_confirmationrdv n  WHERE n.ctp_sms=0 and n.statut=0 and DATE(n.mrdv_date)=CURRENT_DATE+1", nativeQuery = true)
	List<SmsConfirmationRDV> findByNOK();

}
