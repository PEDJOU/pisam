/**
 * 
 */
package com.moov.moovservice.dto;

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
public class ResponseDto {
	private String Ref;
	private String Sender;
	private String Dest;
	private String Sms;
	private int Cpt_sms;
	private String Statut;
}
