package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;


/** 
 * API to get latest build information from TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/getLatestBuildTestResults
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2016-Jul-29
 */


public class GETGetLatestBuilds extends RTCClientRestAPIBase  {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCgetLatestBuildTestResultsURI();
	}

}
