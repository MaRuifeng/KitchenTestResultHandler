package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to add test results to the TEST DB
 * Type: POST
 * URL: /RTCWebClient/v0.1/api/testResult/addTestResults?testPhase={string}
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class POSTAddTestResults extends RTCClientRestAPIBase  {

	public POSTAddTestResults() {
		super();
	}

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCaddTestResultsURI();
	}
}
