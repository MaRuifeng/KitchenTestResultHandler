package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to sync test cases listed in the test results to the TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/syncTestCases
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class PUTSyncTestCases extends RTCClientRestAPIBase {

	public PUTSyncTestCases() {
		super();
	}

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCsyncTestCasesURI();
	}
}
