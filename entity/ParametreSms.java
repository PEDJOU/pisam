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
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParametreSms {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String sender;
	private String Code;
	private String cmptsms;
	private String passwordsms;
	private String cmptgayway;
	private String passwordgatway;
	private String urlsms;
	private String urlgatway;
	private String bd;
	private String pswdbd;
	private String ipbd;
}
