/**
 *
 */
package com.moov.moovservice.entity;



import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Smsgateway {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(unique=true)
	private String datereception;
	
	private String msisdn;
	private String msg;
	private int code;
	private boolean isactive=true;
	private String identifiant;
}
