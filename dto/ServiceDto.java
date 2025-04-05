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
public class ServiceDto {
	public int sc_id;
	public String sc_name;
	public String sc_description;
}
