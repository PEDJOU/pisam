/**
 * 
 */
package com.moov.moovservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.moov.moovservice.entity.ParametreSms;


/**
 * 
 */
public interface ParametreSmsRepository extends JpaRepository<ParametreSms, UUID> {


}
