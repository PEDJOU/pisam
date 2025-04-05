/**
 *
 */
package com.moov.moovservice.entity;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class SmsAnnulationRDV {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

    @Column(unique=true)
	private int mrdv_id;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private Date mrdv_date;
	private String nom;
	private String prenom;
	private String tel;
	private String consultation;
	private int Ctp_sms;
	private String message;
	private String heure_debut;
	private String heure_fin;
	private int Statut;

	@ManyToOne
	@JoinColumn(name = "commande", nullable = true)
	private SmsText smsformat;


}
