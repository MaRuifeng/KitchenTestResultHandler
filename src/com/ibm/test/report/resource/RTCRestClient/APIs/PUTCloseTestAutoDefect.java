package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to close test automation defect in RTC and TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/rtc/closeTestAutoDefect?defectNumber={int}&comment={string}
 * Payload: RTC config properties
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class PUTCloseTestAutoDefect extends RTCClientRestAPIBase {

	public PUTCloseTestAutoDefect() {
		super();
	}

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCcloseTestAutoDefectURI();
	}
	

}
