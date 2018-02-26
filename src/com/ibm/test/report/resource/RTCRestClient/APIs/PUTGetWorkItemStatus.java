package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to get work item status in RTC
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/rtc/getWorkItemStatus?workItemNumber={int}
 * Payload: RTC config properties
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class PUTGetWorkItemStatus extends RTCClientRestAPIBase {

	public PUTGetWorkItemStatus() {
		super();
	}

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCgetWorkItemStatusURI();
	}
}
