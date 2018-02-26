package com.ibm.test.report.resource;

import java.util.ArrayList;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Define JUnit test suite execution result.
 * - Test suite name
 * - Test count
 * - Error count
 * - Failure count
 * - Execution timestamp
 * - Execution time in seconds
 * - Build
 * - Owner
 * - Due date for fix
 * @author ruifengm
 * @since 2015-Nov-25
 */

public class TestSuiteResult {
	
	private String testSuiteName;
	private String testPackage; 
	private int testCount;
	private int errorCount;
	private int failureCount;
	private String executionTimestamp; 
	private int executionTimeInSeconds;
	private int prepTimeInSeconds; 
	private int teardownTimeInSeconds;
	private Build build;
	private ArrayList<TestCaseResult> testCaseResultList; 
	private String owner;
	private String dueDate;
	
	// constructor
	public TestSuiteResult() {
		super();
	}
	
	public TestSuiteResult(String testSuiteName, String testPackage,
			int testCount, int errorCount, int failureCount,
			String executionTimestamp, int executionTimeInSeconds, 
			int prepTimeInSeconds, int teardownTimeInSeconds, Build build,
			ArrayList<TestCaseResult> testCaseResultList, String owner,
			String dueDate) {
		super();
		this.testSuiteName = testSuiteName;
		this.testPackage = testPackage;
		this.testCount = testCount;
		this.errorCount = errorCount;
		this.failureCount = failureCount;
		this.executionTimestamp = executionTimestamp;
		this.executionTimeInSeconds = executionTimeInSeconds;
		this.prepTimeInSeconds = prepTimeInSeconds;
		this.teardownTimeInSeconds = teardownTimeInSeconds;
		this.build = build;
		this.testCaseResultList = testCaseResultList;
		this.owner = owner;
		this.dueDate = dueDate;
	}
	
	// to JSON
	
	public JSONObject toJSON() {
		JSONObject testSuiteResultJSON = new JSONObject(); 
		
		// put test case results into a JSON Array
		JSONArray testCaseResultJsonArr = new JSONArray(); 
		for (TestCaseResult result: this.testCaseResultList){
			testCaseResultJsonArr.add(result.toJSON()); 
		}
		
		testSuiteResultJSON.put("TestSuiteName", testSuiteName);
		testSuiteResultJSON.put("TestPackage", testPackage);
		testSuiteResultJSON.put("TestCount", testCount);
		testSuiteResultJSON.put("ErrorCount", errorCount);
		testSuiteResultJSON.put("FailureCount", failureCount);
		testSuiteResultJSON.put("ExecutionTimestamp", executionTimestamp);
		testSuiteResultJSON.put("ExecutionTimeInSeconds", executionTimeInSeconds);
		testSuiteResultJSON.put("PreparationTimeInSeconds", prepTimeInSeconds);
		testSuiteResultJSON.put("TeardownTimeInSeconds", teardownTimeInSeconds);
		testSuiteResultJSON.put("Build", build.toJSON());
		testSuiteResultJSON.put("Owner", owner);
		testSuiteResultJSON.put("DueDate", dueDate);
		testSuiteResultJSON.put("TestCaseResults", testCaseResultJsonArr);
		
		return testSuiteResultJSON; 
	}

	public int getExecutionTimeInSeconds() {
		return executionTimeInSeconds;
	}

	public void setExecutionTimeInSeconds(int executionTimeInSeconds) {
		this.executionTimeInSeconds = executionTimeInSeconds;
	}

	public Build getBuild() {
		return build;
	}

	public void setBuild(Build build) {
		this.build = build;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public String getExecutionTimestamp() {
		return executionTimestamp;
	}

	public void setExecutionTimestamp(String executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public ArrayList<TestCaseResult> getTestCaseResultList() {
		return testCaseResultList;
	}

	public void setTestCaseResultList(ArrayList<TestCaseResult> testCaseResultList) {
		this.testCaseResultList = testCaseResultList;
	}

	public String getTestPackage() {
		return testPackage;
	}

	public void setTestPackage(String testPackage) {
		this.testPackage = testPackage;
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
		return "TestSuiteResult [testSuiteName=" + testSuiteName
				+ ", testPackage=" + testPackage + ", testCount=" + testCount
				+ ", errorCount=" + errorCount + ", failureCount="
				+ failureCount + ", executionTimestamp=" + executionTimestamp
				+ ", testCaseResultList=" + testCaseResultList + ", owner="
				+ owner + ", dueDate=" + dueDate + "]";
	}

}
