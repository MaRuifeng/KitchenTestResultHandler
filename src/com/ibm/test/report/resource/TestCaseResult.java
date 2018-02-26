package com.ibm.test.report.resource;

import com.ibm.json.java.JSONObject;
import com.ibm.test.report.resource.Constants.STATUS;

/**
 * Define JUnit test case execution result.
 * @author ruifengm
 * @since 2015-Nov-25
 */

public class TestCaseResult {
	
	private String testCasePath; 
	private String testCaseName;
	private String testCaseParentSuite;
	private int executionTimeInSeconds;
	private int prepTimeInSeconds;
	private int teardownTimeInSeconds;
	private int rqmTestCaseId; 
	private STATUS status; 
	private String failureType;
	private String failureMsg;
	private String errorType; 
	private String errorMsg;
	private String testLogLink;
	
	private String testEdptOS; 
	private String testEdptIP;
	
	//Constructor
	public TestCaseResult() {
		super();
	}
	
	//Constructor with fields
	public TestCaseResult(String testCasePath, String testCaseName,
			String testCaseParentSuite, int executionTimeInSeconds,
			int prepTimeInSeconds, int teardownTimeInSeconds,
			int rqmTestCaseId, STATUS status, String failureType,
			String failureMsg, String errorType, String errorMsg,
			String testLogLink, String testEdptOS, String testEdptIP) {
		super();
		this.testCasePath = testCasePath;
		this.testCaseName = testCaseName;
		this.testCaseParentSuite = testCaseParentSuite;
		this.executionTimeInSeconds = executionTimeInSeconds;
		this.prepTimeInSeconds = prepTimeInSeconds;
		this.teardownTimeInSeconds = teardownTimeInSeconds;
		this.rqmTestCaseId = rqmTestCaseId;
		this.status = status;
		this.failureType = failureType;
		this.failureMsg = failureMsg;
		this.errorType = errorType;
		this.errorMsg = errorMsg;
		this.testLogLink = testLogLink;
		this.testEdptOS = testEdptOS;
		this.testEdptIP = testEdptIP;
	}

	// To JSON
	public JSONObject toJSON() {
		JSONObject testCaseResultJSON = new JSONObject(); 
		testCaseResultJSON.put("TestCaseClassPath", this.testCasePath);
		testCaseResultJSON.put("TestCaseName", this.testCaseName);
		testCaseResultJSON.put("TestSuite", this.testCaseParentSuite);
		testCaseResultJSON.put("RQMTestCaseId", this.rqmTestCaseId);
		testCaseResultJSON.put("ExecutionTimeInSeconds", this.executionTimeInSeconds);
		testCaseResultJSON.put("PreparationTimeInSeconds", this.prepTimeInSeconds);
		testCaseResultJSON.put("TeardownTimeInSeconds", this.teardownTimeInSeconds);
		testCaseResultJSON.put("Status", this.status.toString());
		testCaseResultJSON.put("FailureType", this.failureType);
		testCaseResultJSON.put("FailureMessage", this.failureMsg);
		testCaseResultJSON.put("ErrorType", this.errorType);
		testCaseResultJSON.put("ErrorMessage", this.errorMsg);
		testCaseResultJSON.put("TestLogLink", this.testLogLink);
		testCaseResultJSON.put("TestEndpointOS", this.testEdptOS);
		testCaseResultJSON.put("TestEndpointIP", this.testEdptIP);
		return testCaseResultJSON;
	}
	
	public String getTestCasePath() {
		return testCasePath;
	}

	public void setTestCasePath(String testCasePath) {
		this.testCasePath = testCasePath;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getFailureType() {
		return failureType;
	}

	public void setFailureType(String failureType) {
		this.failureType = failureType;
	}

	public String getFailureMsg() {
		return failureMsg;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setFailureMsg(String failureMsg) {
		this.failureMsg = failureMsg;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getTestCaseParentSuite() {
		return testCaseParentSuite;
	}

	public void setTestCaseParentSuite(String testCaseParentSuite) {
		this.testCaseParentSuite = testCaseParentSuite;
	}

	public int getExecutionTimeInSeconds() {
		return executionTimeInSeconds;
	}

	public void setExecutionTimeInSeconds(int executionTimeInSeconds) {
		this.executionTimeInSeconds = executionTimeInSeconds;
	}

	public int getRqmTestCaseId() {
		return rqmTestCaseId;
	}

	public void setRqmTestCaseId(int rqmTestCaseId) {
		this.rqmTestCaseId = rqmTestCaseId;
	}

	public String getTestLogLink() {
		return testLogLink;
	}

	public void setTestLogLink(String testLogLink) {
		this.testLogLink = testLogLink;
	}

	public String getTestEdptOS() {
		return testEdptOS;
	}

	public void setTestEdptOS(String testEdptOS) {
		this.testEdptOS = testEdptOS;
	}

	public String getTestEdptIP() {
		return testEdptIP;
	}

	public void setTestEdptIP(String testEdptIP) {
		this.testEdptIP = testEdptIP;
	}

	public int getPrepTimeInSeconds() {
		return prepTimeInSeconds;
	}

	public void setPrepTimeInSeconds(int prepTimeInSeconds) {
		this.prepTimeInSeconds = prepTimeInSeconds;
	}

	public int getTeardownTimeInSeconds() {
		return teardownTimeInSeconds;
	}

	public void setTeardownTimeInSeconds(int teardownTimeInSeconds) {
		this.teardownTimeInSeconds = teardownTimeInSeconds;
	}

	@Override
	public String toString() {
		return "TestCaseResult [testCasePath=" + testCasePath
				+ ", testCaseName=" + testCaseName + ", testCaseParentSuite="
				+ testCaseParentSuite + ", executionTimeInSeconds="
				+ executionTimeInSeconds + ", rqmTestCaseId=" + rqmTestCaseId
				+ ", prepTimeInSeconds=" + prepTimeInSeconds + ", teardownTimeInSeconds=" + teardownTimeInSeconds
				+ ", status=" + status + ", failureType=" + failureType
				+ ", failureMsg=" + failureMsg + ", errorType=" + errorType
				+ ", errorMsg=" + errorMsg + ", testLogLink=" + testLogLink
				+ "]";
	}
}
