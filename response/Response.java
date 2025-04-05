/**
 * 
 */
package com.moov.moovservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	public enum ResponseStatusEnum {
		SUCCESS, ERROR, WARNING, NO_ACCESS, NO_CONTENT, INTERNAL_ERROR
	};

	private ResponseStatusEnum status;
	private Object data;
	private String message;
	private boolean isSuccess;
}
