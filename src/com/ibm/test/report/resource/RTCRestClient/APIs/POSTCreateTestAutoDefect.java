package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to create test automation defect in RTC and TEST DB
 * Type: POST
 * URL: /RTCWebClient/v0.1/api/rtc/createTestAutoDefect?testCategory={string}&testPhase={string}&defectSeverity={string}
 * Payload: RTC config properties and test suite results in JSONObject format
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class POSTCreateTestAutoDefect extends RTCClientRestAPIBase {

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCcreateTestAutoDefectURI();
	}
	
	
}
