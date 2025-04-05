/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.moov.moovservice.entity.Smsgateway;
import java.util.List;



/**
 * 
 */
public interface SmsgatewayRepository extends JpaRepository<Smsgateway, UUID> {
	
	
	List<Smsgateway> findByIdentifiant(String identifiant);
	


}
