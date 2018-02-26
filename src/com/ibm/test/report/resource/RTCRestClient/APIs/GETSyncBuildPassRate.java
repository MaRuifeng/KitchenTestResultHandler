package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to sync application build pass rates in the TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/syncBuildPassRate?buildName={string}
 * Payload: test results in JSONArray format
 * @author ruifengm
 * @since 2016-Jul-29
 */

public class GETSyncBuildPassRate extends RTCClientRestAPIBase {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCsyncBuildPassRateURI();
	}

}
