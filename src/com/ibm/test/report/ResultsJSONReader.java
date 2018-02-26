package com.ibm.test.report;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Build;
import com.ibm.test.report.resource.Constants;
import com.ibm.test.report.resource.Constants.STATUS;
import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.TestCaseResult;
import com.ibm.test.report.resource.TestSuiteResult;

/**
 * This utility class reads JSON files with Chef recipe kitchen test results 
 * and produces a customized hash map that contains a list of test
 * case execution results for RTC defect creation and result XML writing.
 * 
 * 
 * @author ruifengm
 * @since 2016-Jul-27
 */

public class ResultsJSONReader {
	
	private static final String className = ResultsJSONReader.class.getName();
	
	protected JSONArray kitchenTestResultJsonArray = new JSONArray();
	protected ArrayList<TestSuiteResult> kitchenTestResultList = new ArrayList<TestSuiteResult>();
	
	/**
	 * Construct the kitchen test result list
	 * @param resultList
	 * @param jsonFilePath
	 * @param logFilePath
	 * @param buildName
	 */
	protected void constructKitchenTestResultList (JSONObject resultJson, String jsonFilePath, ArrayList<String> logFileList, String buildName) {
		String methodName = "constructKitchenTestResultList";
		Log.getLogger().entering(className, methodName);
		Log.logVerbose("Constructing kitchen test result list...");
		
		Matcher matcher = null; // regex matcher
		
		String testSuiteName = ((JSONObject)resultJson.get("cookbookInfor")).get("projectName").toString();
		// Parse test case results
		ArrayList<TestCaseResult> testCaseResultList = new ArrayList<TestCaseResult>();
		JSONArray testCases = (JSONArray)resultJson.get("testCases");
		for (Object item: testCases) {
			JSONObject testCase = (JSONObject)item;
			TestCaseResult testCaseResult = new TestCaseResult();
			testCaseResult.setTestCaseName(testCase.get("name").toString());
			testCaseResult.setTestCaseParentSuite(testSuiteName);
			testCaseResult.setTestCasePath(testSuiteName);
			testCaseResult.setRqmTestCaseId(0);	 // TODO
			testCaseResult.setExecutionTimeInSeconds(Integer.parseInt(testCase.get("executionTime").toString()));
			String status = (String)testCase.get("result"); 
			if (status.equalsIgnoreCase("fail")) {
				testCaseResult.setStatus(STATUS.FAILURE); 
				// failure type
				testCaseResult.setFailureType(status);
				// failure message
				String failureMsg = ((String) testCase.get("errorStackTrace")).replaceAll("[\u0000-\u001f]", ""); // remove unrecognizable control characters in XML
				if (failureMsg.length() > 500 ) failureMsg = failureMsg.substring(0, 500) + "[Too long, not displayed, check log]";
				testCaseResult.setFailureMsg(failureMsg);
			}
			if (status.equalsIgnoreCase("unknown")) {
				testCaseResult.setStatus(STATUS.ERROR);
				// error type
				testCaseResult.setErrorType(status);
				// error message
				String errorMsg = ((String) testCase.get("errorStackTrace")).replaceAll("[\u0000-\u001f]", ""); // remove unrecognizable control characters in XML
				if (errorMsg.length() > 500 ) errorMsg = errorMsg.substring(0, 500) + "[Too long, not displayed, check log]";
				testCaseResult.setErrorMsg(errorMsg);
			}
			if (status.equalsIgnoreCase("pass")) testCaseResult.setStatus(STATUS.SUCCESS);
			for (String logFilePath: logFileList) {
				//if (logFilePath.contains(testSuiteName)) {
				if (logFilePath.matches("(?i)kitchen_" + testSuiteName + "_automation.log")) {
					testCaseResult.setTestLogLink(logFilePath);
					break;
				}
			}
			if (null == testCaseResult.getTestLogLink()) testCaseResult.setTestLogLink(jsonFilePath);
			testCaseResult.setTestEdptIP(testCase.get("targetServerIp").toString());
			testCaseResult.setTestEdptOS(testCase.get("targetServerOs").toString());
			testCaseResult.setPrepTimeInSeconds(Integer.parseInt(testCase.get("setupTime").toString()));
			testCaseResult.setTeardownTimeInSeconds(Integer.parseInt(testCase.get("teardownTime").toString()));
			testCaseResultList.add(testCaseResult);
		}
		
		// Parse build info
			// sprint & git branch
		Pattern sprintPattern = Pattern.compile("ivt_[0-9]+\\.[0-9]");
		matcher = sprintPattern.matcher(buildName);
		matcher.find();
		String sprint = matcher.group(0).replace("ivt_", "");
		String gitBranch = matcher.group(0); 
			// version
		Pattern versionPattern = Pattern.compile("[0-9]+$");
		matcher = versionPattern.matcher(buildName);
		matcher.find();
		String version = matcher.group(0);
			// timestamp
		Pattern timePattern = Pattern.compile("[0-9]{8}-[0-9]{4}");
		matcher = timePattern.matcher(buildName);
		matcher.find();
		int year = Integer.parseInt(matcher.group(0).substring(0,4));
		int month = Integer.parseInt(matcher.group(0).substring(4,6)) - 1; // Calendar month starts from 0
		int day = Integer.parseInt(matcher.group(0).substring(6,8));
		int hour = Integer.parseInt(matcher.group(0).substring(9,11));
		int minute = Integer.parseInt(matcher.group(0).substring(11,13));
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, 0);
		String timestamp = generateTimeStampString(cal.getTime());
			// build
		Build build = new Build(buildName, version, timestamp, gitBranch, sprint);
		
		// Parse test suites 
		TestSuiteResult testSuiteResult = new TestSuiteResult(); 
		// test suite name
		testSuiteResult.setTestSuiteName(testSuiteName);
		// build
		testSuiteResult.setBuild(build);
		// package
		testSuiteResult.setTestPackage(testSuiteName);
		// owner 
		for (HashMap<String, String> testSuiteOwner: HandlerConfig.testSuiteOwnerList){
			if (testSuiteName.toLowerCase().contains(testSuiteOwner.get("Platform"))){
				testSuiteResult.setOwner(testSuiteOwner.get("Owner"));
				break;
			}
		}
		// execution timestamp
		Date executionTime = Calendar.getInstance().getTime(); // take reporting time
		testSuiteResult.setExecutionTimestamp(generateTimeStampString(executionTime));
		// due date
		testSuiteResult.setDueDate(generateTimeStampString(getDueDate(executionTime)));
		// test case result list
		int testCount = 0; 
		int failureCount = 0; 
		int errorCount = 0; 
		int executionTimeInSeconds = 0; 
		int prepTimeInSeconds = 0; 
		int teardownTimeInSeconds = 0; 
		for (Iterator<TestCaseResult> i = testCaseResultList.iterator(); i.hasNext();) {
			TestCaseResult testCaseResult = i.next();
			testCount++; 
			if (testCaseResult.getStatus().equals(STATUS.FAILURE)) failureCount++;
			if (testCaseResult.getStatus().equals(STATUS.ERROR)) errorCount++; 
			executionTimeInSeconds += testCaseResult.getExecutionTimeInSeconds();
			prepTimeInSeconds += testCaseResult.getPrepTimeInSeconds();
			teardownTimeInSeconds += testCaseResult.getTeardownTimeInSeconds();
		}
		if (testCount == 0) Log.logVerbose("No test cases found for test suite " + testSuiteResult.getTestSuiteName() + ". Skipping it...");
		else {
			testSuiteResult.setTestCaseResultList(testCaseResultList);
			testSuiteResult.setTestCount(testCount);
			testSuiteResult.setErrorCount(errorCount);
			testSuiteResult.setFailureCount(failureCount);
			testSuiteResult.setExecutionTimeInSeconds(executionTimeInSeconds);
			testSuiteResult.setPrepTimeInSeconds(prepTimeInSeconds);
			testSuiteResult.setTeardownTimeInSeconds(teardownTimeInSeconds);
			
			kitchenTestResultJsonArray.add(testSuiteResult.toJSON());
			kitchenTestResultList.add(testSuiteResult);
		}

		Log.logVerbose("Completed.");
		Log.getLogger().exiting(className, methodName);
	}

	/**
	 * Read data from JSON and put them into an ArrayList of HashMap objects
	 * 
	 * @param inputFileStream
	 * @throws IOException 
	 */
	protected JSONObject readDataFromJSON(String inputFilePath) throws IOException {
		InputStream inputStream = new FileInputStream(inputFilePath);
		JSONObject resultJson = new JSONObject();
		try {
			resultJson = JSONObject.parse(inputStream);
		} finally {
			inputStream.close();
		}
		return resultJson;
	}
	
	/** 
	 * Generate timestamp string in yyyy-MM-dd'T'HH:mm:ss format
	 * @param timeValue
	 * @return
	 */
	private String generateTimeStampString(Date timeValue) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return dateFormatter.format(timeValue); 
	}

	/**
	 * Get Due date
	 * @return
	 */
	private Date getDueDate(Date executionTimestamp) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(executionTimestamp);
		
		// add only business working days
		for (int i=0; i<Constants.DUE_WORKING_DAYS; i++){
			do {
				cal.add(Calendar.DAY_OF_MONTH, 1); 
			} while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
					 cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
		}
		return cal.getTime();
	}

}
