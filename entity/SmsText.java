/**
 *
 */
package com.moov.moovservice.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SmsText {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String messageprise;
	private String messageconfirmation;
	private String messagereport;
	private String messagemodification;
	private String messageannulation;
}
