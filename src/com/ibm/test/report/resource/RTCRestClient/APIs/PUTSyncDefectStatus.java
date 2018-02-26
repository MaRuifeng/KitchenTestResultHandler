package com.ibm.test.report.resource.RTCRestClient.APIs;

import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;

/** 
 * API to sync defect status in RTC to the TEST DB
 * Type: PUT
 * URL: /RTCWebClient/v0.1/api/testResult/syncRTCDefectStatus
 * Payload: RTC config JSON
 * @author ruifengm
 * @since 2015-Dec-21
 */

public class PUTSyncDefectStatus extends RTCClientRestAPIBase {

	public PUTSyncDefectStatus() {
		super();
	}

	@Override
	protected String getRestURI() {
		return HandlerConfig.getRTCsyncRTCDefectStatusURI();
	}
}
