/**
 * 
 */
package com.moov.moovservice.dto;

import java.util.List;

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
public class SmsBlock {
	private String Code;
	private String Username;
	private String Password;
	private List<Msisdn> Mssg;
}
