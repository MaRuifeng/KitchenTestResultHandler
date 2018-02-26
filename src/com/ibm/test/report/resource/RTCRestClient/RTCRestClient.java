package com.ibm.test.report.resource.RTCRestClient;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.common.internal.MultivaluedMapImpl;

import com.ibm.json.java.JSONObject;

/**
 * RTC Rest Client
 * @author ruifengm
 * @since 2015-Dec-21
 */
public class RTCRestClient {
	public static enum REST {GET, DELETE, PUT, POST};
	protected REST restType;
	protected RTCClientRestAPIBase API = null; 
	protected Object payload = null; 
	protected MultivaluedMapImpl<String, String> queryParams = new MultivaluedMapImpl<String, String>();
	protected int statusCode; 
	protected JSONObject returnJson = new JSONObject(); 
	protected boolean runSucessfully; 
	
	// constructor
	public RTCRestClient(String restType, RTCClientRestAPIBase API,
			Object payload, MultivaluedMapImpl<String, String> queryParams) {
		super();
		this.restType = (REST) Enum.valueOf(REST.class, restType);
		this.API = API;
		this.payload = payload;
		this.queryParams = queryParams;
	}

	public void runAPI() throws Exception{
		boolean typeWrong = false; 
		ClientResponse response = null;
		// send request
		switch(restType) {
		case GET:
			try {
				response = API.get(queryParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case POST:
			try {
				response = API.post(queryParams, payload); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case PUT:
			try {
				response = API.put(queryParams, payload); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case DELETE:
			try {
				response = API.delete(queryParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			typeWrong = true; 
		}
		if (!typeWrong){
			returnJson = API.getResponseAsJSON(response);
			statusCode = API.getResonseCode(response);
			runSucessfully = API.ranSuccessfully(response);
		}
	}

	// getters
	public REST getRestType() {
		return restType;
	}

	public RTCClientRestAPIBase getAPI() {
		return API;
	}

	public Object getPayload() {
		return payload;
	}

	public MultivaluedMapImpl<String, String> getQueryParams() {
		return queryParams;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public JSONObject getReturnJson() {
		return returnJson;
	}

	public boolean isRunSucessfully() {
		return runSucessfully;
	}

	// setters
	public void setRestType(REST restType) {
		this.restType = restType;
	}

	public void setAPI(RTCClientRestAPIBase API) {
		this.API = API;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public void setQueryParams(MultivaluedMapImpl<String, String> queryParams) {
		this.queryParams = queryParams;
	}
}
