/**
 * 
 */
package com.moov.moovservice.service.sms;

import org.springframework.stereotype.Service;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * 
 */

@Service
public class SendService {
	
	//private String root = "https://smspro.hyperaccesss.com:8443/api/addBulkSms";
	
	public HttpResponse<JsonNode> get(String url)
	{
		try
		{
		 HttpResponse<JsonNode> jsonResponse = null;
	        try {
	            jsonResponse = Unirest.get(url)
	                    .header("accept", "application/json")
	                    .header("content-type", "application/json")
	                    .asJson();
	        } catch (UnirestException e) {
	            return null;
	        }
	        return jsonResponse;
		}
		catch(Exception ex)
		{
			 return null;
		}	
		
	}
	

}
