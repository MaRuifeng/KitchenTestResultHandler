package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to sync test suites listed in the test results to the TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/syncTestSuites?testCategory={string}
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2015-Dec-21
 */
public class PUTSyncTestSuites extends RTCClientRestAPIBase {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCsyncTestSuitesURI();
	}

	
}
