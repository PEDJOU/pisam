/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.moov.moovservice.entity.SmsText;


/**
 * 
 */
public interface SmsTextRepository extends JpaRepository<SmsText, UUID> {


}
