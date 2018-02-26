package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;


/** 
 * API to sync application builds listed in the test results to the TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/syncAppBuilds
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2016-Jul-29
 */


public class PUTSyncAppBuilds extends RTCClientRestAPIBase {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCsyncAppBuildsURI();
	}
	
	

}
