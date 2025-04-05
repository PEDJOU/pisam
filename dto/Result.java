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
public class Result {
	private String msisdn;
	private int respCode;
	private String respDescription;
}
