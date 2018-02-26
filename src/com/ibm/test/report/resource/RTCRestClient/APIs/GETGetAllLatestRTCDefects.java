package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to get latest defects of all test suites from the TEST DB
 * Type: GET
 * URL: RTCWebClient/v0.1/api/rtc/getAllLatestRTCDefects
 * @author ruifengm
 * @since 2015-Dec-24
 */

public class GETGetAllLatestRTCDefects extends RTCClientRestAPIBase {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCgetAllLatestRTCDefectsURI();
	}

}
