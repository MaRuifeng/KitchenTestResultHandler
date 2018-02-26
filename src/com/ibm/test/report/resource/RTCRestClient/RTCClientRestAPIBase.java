package com.ibm.test.report.resource.RTCRestClient;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.http.HttpStatus;


import com.ibm.json.java.JSONObject;
import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Constants;
import com.ibm.test.report.resource.HandlerConfig;

/**
 * Rest API base class for calls of the customized RTC client REST interfaces
 * RTC Client, Version 1.0, source project found in BPM Test Assets 
 * @author ruifengm
 * @since 2015-Dec-21
 */

public abstract class RTCClientRestAPIBase {
	public String URI;
	private Map<String, String> headers; 
	
	// constructor 
	public RTCClientRestAPIBase() {
		getURI(); 
	}
	
	protected String getURI() {
		URI = getServer() + getRestURI(); 
		return URI; 
	}
	
	protected String getServer() {
		return HandlerConfig.getRTCClientServer();
	}
	
	protected abstract String getRestURI();
	
	/**
	 * Construct Rest Client with connection and read time-out set
	 */
	public RestClient getRestClient() {
		ClientConfig config = new ClientConfig(); 
		config.connectTimeout(30000);
		config.readTimeout(18000000);
		
		RestClient client = new RestClient(config);
		return client; 
	}
	
	/**
	 * Set content type and other headers if any
	 * @param resource
	 * @throws Exception
	 */
	public void setHeaders(Resource resource) throws Exception {
		headers = new HashMap<String, String>(); 
		headers.put("Content-Type", "application/json");
		for (String key: headers.keySet()){
			resource.header(key, headers.get(key));
		}
	}
	
	// API calls
	
	/**
	 * Get request
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public ClientResponse get(MultivaluedMap<String, String> query) throws Exception {
		RestClient client = getRestClient(); 
		Resource resource = client.resource(this.URI);
		setHeaders(resource);
		if (query != null) resource.queryParams(query);
		
		try {
			Log.logVerbose();
			Log.logVerbose(String.format("==> GET Request: " + this.URI));
			Log.logVerbose(String.format("==> Querying Parameters: " + (query!=null?String.valueOf(query):"")));
			Log.logVerbose(String.format("==> Http Headers: " + (headers!=null?String.valueOf(headers):"")));
			// Send request
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get();
			Log.logVerbose("<== Status Code: " + response.getStatusCode());
			Log.logVerbose();
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Put request
	 * @param query
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public ClientResponse put(MultivaluedMap<String, String> query, Object payload) throws Exception {
		RestClient client = getRestClient(); 
		Resource resource = client.resource(this.URI);
		setHeaders(resource);
		if (query != null) resource.queryParams(query);
		
		try {
			Log.logVerbose();
			Log.logVerbose(String.format("==> PUT Request: " + this.URI));
			Log.logVerbose(String.format("==> Querying Parameters: " + (query!=null?String.valueOf(query):"")));
			Log.logVerbose(String.format("==> Http Headers: " + (headers!=null?String.valueOf(headers):"")));
			//Log.logVerbose(String.format("==> Payload: " + (payload!=null?String.valueOf(payload):"")));
			// Send request
			ClientResponse response = resource
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).put(payload);
			Log.logVerbose("<== Status Code: " + response.getStatusCode());
			Log.logVerbose();
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Post request
	 * @param query
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public ClientResponse post(MultivaluedMap<String, String> query, Object payload) throws Exception {
		RestClient client = getRestClient(); 
		Resource resource = client.resource(this.URI);
		setHeaders(resource);
		if (query != null) resource.queryParams(query);
		
		try {
			Log.logVerbose();
			Log.logVerbose(String.format("==> POST Request: " + this.URI));
			Log.logVerbose(String.format("==> Querying Parameters: " + (query!=null?String.valueOf(query):"")));
			Log.logVerbose(String.format("==> Http Headers: " + (headers!=null?String.valueOf(headers):"")));
			//Log.logVerbose(String.format("==> Payload: " + (payload!=null?String.valueOf(payload):"")));
			// Send request
			ClientResponse response = resource
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(payload);
			Log.logVerbose("<== Status Code: " + response.getStatusCode());
			Log.logVerbose();
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Delete request
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public ClientResponse delete(MultivaluedMap<String, String> query) throws Exception {
		RestClient client = getRestClient(); 
		Resource resource = client.resource(this.URI);
		setHeaders(resource);
		if (query != null) resource.queryParams(query);
		
		try {
			Log.logVerbose();
			Log.logVerbose(String.format("==> DELETE Request: " + this.URI));
			Log.logVerbose(String.format("==> Querying Parameters: " + (query!=null?String.valueOf(query):"")));
			Log.logVerbose(String.format("==> Http Headers: " + (headers!=null?String.valueOf(headers):"")));
			// Send request
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).delete();
			Log.logVerbose("<== Status Code: " + response.getStatusCode());
			Log.logVerbose();
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Get API return in JSON format
	 * @param response
	 * @return
	 */
	public JSONObject getResponseAsJSON(ClientResponse response) {
		return response.getEntity(JSONObject.class);
	}
	
	/**
	 * Get API response code
	 * @param response
	 * @return
	 */
	
	public int getResonseCode(ClientResponse response) {
		return response.getStatusCode();
	}
	
	/**
	 * Check if API call is successfully completed (status OK and no error message)
	 * @param response
	 * @return
	 */
	public boolean ranSuccessfully(ClientResponse response) {
		if (getResonseCode(response) == HttpStatus.OK.getCode()) {
			JSONObject responseObj = response.getEntity(JSONObject.class);
			if (!responseObj.keySet().contains(Constants.JSON_KEY_ERROR_MSG)) return true; 
			else return false; 
		}
		else return false; 
	}

}
