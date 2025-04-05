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
public class ResultFaf {
	private String msisdn;
	private int code;
	private String desc;
	private List<String> listmsisdn;
}
