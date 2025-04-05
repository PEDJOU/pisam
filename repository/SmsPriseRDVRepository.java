/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.moov.moovservice.entity.SmsPriseRDV;


/**
 * 
 */
public interface SmsPriseRDVRepository extends JpaRepository<SmsPriseRDV, UUID> {
	@Query(value="SELECT * FROM sms_priserdv n  WHERE n.ctp_sms=0 and n.statut=0 and DATE(n.mrdv_date)>CURRENT_DATE", nativeQuery = true)
	List<SmsPriseRDV> findByNOK();
}
