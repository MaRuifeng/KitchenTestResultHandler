package com.ibm.test.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.common.internal.MultivaluedMapImpl;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Constants;
import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.RTCRestClient.RTCClientRestAPIBase;
import com.ibm.test.report.resource.RTCRestClient.RTCRestClient;
import com.ibm.test.report.resource.RTCRestClient.APIs.GETGetAllLatestRTCDefects;
import com.ibm.test.report.resource.RTCRestClient.APIs.GETGetLatestBuilds;
import com.ibm.test.report.resource.RTCRestClient.APIs.GETSyncBuildPassRate;
import com.ibm.test.report.resource.RTCRestClient.APIs.POSTAddTestResults;
import com.ibm.test.report.resource.RTCRestClient.APIs.POSTCreateTestAutoDefect;
import com.ibm.test.report.resource.RTCRestClient.APIs.POSTUpdateTestAutoDefect;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTCloseTestAutoDefect;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTGetOpenTestAutoDefect;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTSyncAppBuilds;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTSyncDefectStatus;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTSyncTestCases;
import com.ibm.test.report.resource.RTCRestClient.APIs.PUTSyncTestSuites;

/**
 * This utility class contains an publisher that manages the RTC defects created upon the JSON package produced by the 
 * test results CSV reader.
 * The publisher works via the RTC Client REST APIs deployed to a server (RTC Web Proxy).
 * - Create a defect for a test suite whose results contain failures/errors
 * - Update its content upon subsequent test suite executions, until the defect is closed 
 * @author ruifengm
 * @since 2015-Dec-22
 */

public class ResultsRTCPublisher {
	private static final String className = ResultsRTCPublisher.class.getName();
	
	// RTC Client params
	private RTCClientRestAPIBase API; 
	private MultivaluedMapImpl<String, String> queryParams;
	private Object payload; 
	
	/**
	 * Sync test suites between the results obtained and TEST DB record
	 * @param testCategory
	 * @param testPhase
	 * @param testSuiteResultList
	 * @throws Exception
	 */
 	public void syncTestSuites(String testCategory, JSONArray testSuiteResultList) throws Exception {
		String methodName = "syncTestSuites"; 
		Log.getLogger().entering(className, methodName);
		
		// set up RTC client
		API = new PUTSyncTestSuites(); 
		payload = testSuiteResultList; 
		queryParams = new MultivaluedMapImpl<String, String>(); 
		queryParams.add(Constants.PARAM_TEST_CATEGORY, testCategory); 
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, queryParams);
		
		// run API
		Log.logVerbose("Syncing test suites for test category " + testCategory + "..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else Log.logVerbose("Sync completed."); 
		Log.getLogger().exiting(className, methodName); 
	}
	
	/**
	 * Sync test cases between the results obtained and TEST DB record
	 * @param testSuiteResultList
	 */
	public void syncTestCases(JSONArray testSuiteResultList) throws Exception{
		String methodName = "syncTestCases"; 
		Log.getLogger().entering(className, methodName);
		// set up RTC client
		API = new PUTSyncTestCases(); 
		payload = testSuiteResultList;  
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, null);
		
		// run API
		Log.logVerbose("Syncing test cases..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else Log.logVerbose("Sync completed."); 
		Log.getLogger().exiting(className, methodName); 
	}
	
	
	/**
	 * Sync application builds between the results obtained and TEST DB record
	 * @param testSuiteResultList
	 */
	public void syncAppBuilds(JSONArray testSuiteResultList) throws Exception{
		String methodName = "syncAppBuilds"; 
		Log.getLogger().entering(className, methodName);
		// set up RTC client
		API = new PUTSyncAppBuilds();
		payload = testSuiteResultList;  
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, null);
		
		// run API
		Log.logVerbose("Syncing app builds..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else Log.logVerbose("Sync completed."); 
		Log.getLogger().exiting(className, methodName); 
	}
	
	/**
	 * Add test results to the DB
	 * @param testSuiteResultList
	 */
	public void addTestResults(String testPhase, JSONArray testSuiteResultList) throws Exception {
		String methodName = "addTestResults"; 
		Log.getLogger().entering(className, methodName);
		// set up RTC client
		API = new POSTAddTestResults(); 
		payload = testSuiteResultList;  
		queryParams.add(Constants.PARAM_TEST_PHASE, testPhase); 
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_POST, API, payload, queryParams);
		
		// run API
		Log.logVerbose("Adding test results for test phase " + testPhase + "..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else Log.logVerbose("Add completed."); 
		Log.getLogger().exiting(className, methodName); 
	}
	
	/**
	 * Sync application build pass rates between the results obtained and TEST DB record
	 * @param testSuiteResultList
	 */
	public void syncBuildPassRate(JSONArray testSuiteResultList) throws Exception{
		String methodName = "syncBuildPassRate"; 
		Log.getLogger().entering(className, methodName);
		Set<String> buildSet = new HashSet<String>(); 
		for (Object obj: testSuiteResultList) {
			JSONObject testSuiteResult = (JSONObject) obj; 
			buildSet.add( (String) ((JSONObject)testSuiteResult.get("Build")).get("BuildName"));
		}
		for (String buildName: buildSet) {
			// set up RTC client
			API = new GETSyncBuildPassRate();
			payload = testSuiteResultList;  
			queryParams.add(Constants.PARAM_BUILD_NAME, buildName);
			RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_GET, API, payload, queryParams);
			
			// run API
			Log.logVerbose("Syncing pass rates for application build " + buildName + "..."); 
			rtcRestClient.runAPI(); 
			if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
					+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
			else Log.logVerbose("Sync completed.");
		}
		Log.getLogger().exiting(className, methodName); 
	}

	/**
	 * Publish test results to RTC by creating/updating defects for test suites with failures/errors, and then store the defects to the TEST DB.
	 * @param testCategory
	 * @param testPhase
	 * @param testSuiteResultList
	 * @throws Exception
	 */
	public void publishTestResultsToRTC (String testCategory, String testPhase, JSONArray testSuiteResultList, String buildName) throws Exception {
		String methodName = "publishTestResultsToRTC"; 
		Log.getLogger().entering(className, methodName);
		
		for (Object suiteObj: testSuiteResultList){
			JSONObject testSuiteResult = (JSONObject) suiteObj; 
			String testSuiteName = (String)testSuiteResult.get("TestSuiteName");
			int testCount = (int) testSuiteResult.get("TestCount");
			int errorCount = (int) testSuiteResult.get("ErrorCount");
			int failureCount = (int) testSuiteResult.get("FailureCount");
			
			double passRate = 1 - (double)(errorCount + failureCount)/testCount;
			
			RTCRestClient rtcRestClient = null; 
			JSONObject input = new JSONObject(); 
			input.put("RTCConfig", constructRTCConfig()); 

			if (passRate < HandlerConfig.getMinorThreshold()) { // create or update defect
				String defectSev = null;
				if (passRate < HandlerConfig.getBlockerThreshold()) defectSev = Constants.RTC_SEVERITY_BLOCKER;
				else if (passRate < HandlerConfig.getMajorThreshold()) defectSev = Constants.RTC_SEVERITY_MAJOR;
				else if (passRate < HandlerConfig.getNormalThreshold()) defectSev = Constants.RTC_SEVERITY_NORMAL;
				else defectSev = Constants.RTC_SEVERITY_MINOR; 
				
				/* Check from the TEST DB whether there is already an open defect for this test suite
				   if yes, update it
				   if no, create a new one
				*/
				
				// set up RTC client for /getOpenTestAutoDefect API
				Log.logVerbose("Pass rate beneath standard. Update existing open defect or create a new one."); 
				Log.logVerbose("Looking for open defect for test suite " + testSuiteName + "..."); 
				API = new PUTGetOpenTestAutoDefect(); 
				payload = input; 
				queryParams = new MultivaluedMapImpl<String, String>();
				queryParams.add(Constants.PARAM_TEST_SUITE_NAME, testSuiteName);
				rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, queryParams);
				// run /getOpenTestAutoDefect API
				rtcRestClient.runAPI(); 
				if (rtcRestClient.isRunSucessfully()) {
					JSONObject response = rtcRestClient.getReturnJson(); 
					queryParams = new MultivaluedMapImpl<String, String>(); 
					queryParams.add(Constants.PARAM_TEST_CATEGORY, testCategory); 
					queryParams.add(Constants.PARAM_TEST_PHASE, testPhase); 
					queryParams.add(Constants.PARAM_DEFECT_SEVERITY, defectSev);
					if (response.get(Constants.JSON_KEY_RESULT) != null){
						// open defect found, update it
						Log.logVerbose("Open defect found. Updating its content...");
						// set up RTC client for /updateTestAutoDefect API
						API = new POSTUpdateTestAutoDefect(); 
						input.put("TestSuiteResult", testSuiteResult);
						payload = input; 
						queryParams.add(Constants.PARAM_DEFECT_NUM, 
								((JSONObject)response.get(Constants.JSON_KEY_RESULT)).get("Defect Number").toString()); 
						rtcRestClient = new RTCRestClient(Constants.REST_TYPE_POST, API, payload, queryParams);
						// run /updateTestAutoDefect API
						rtcRestClient.runAPI(); 
						if (rtcRestClient.isRunSucessfully()) {
							Log.logVerbose("Updated the open defect.");
							Log.logVerbose("RTC Client Response: " + rtcRestClient.getReturnJson().toString());
						} 
						else throw new Exception("RTC Client API Failure: " + API.URI 
								+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG));
					}
					else {
						// no open defect, create a new one
						Log.logVerbose("Open defect not found. Creating a new one...");
						// set up RTC client for /createTestAutoDefect API
						API = new POSTCreateTestAutoDefect();  
						input.put("TestSuiteResult", testSuiteResult);
						payload = input; 
						rtcRestClient = new RTCRestClient(Constants.REST_TYPE_POST, API, payload, queryParams);
						// run /createTestAutoDefect API
						rtcRestClient.runAPI(); 
						if (rtcRestClient.isRunSucessfully()) {
							Log.logVerbose("Created a new defect.");
							Log.logVerbose("RTC Client Response: " + rtcRestClient.getReturnJson().toString());
						} 
						else throw new Exception("RTC Client API Failure: " + API.URI 
								+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG));
					}
				}
				else throw new Exception("RTC Client API Failure: " + API.URI 
						+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG));
			}
			else {  // pass rate reached standard, close open defect
				
				/* Check from the TEST DB whether there is already an open defect for this test suite
				   if yes, close it
				   if no, do nothing, you are good
				*/
				
				// set up RTC client for /getOpenTestAutoDefect API
				Log.logVerbose("Pass rate reached standard. Close open defect.");
				Log.logVerbose("Looking for open defect for test suite " + testSuiteName + "..."); 
				API = new PUTGetOpenTestAutoDefect(); 
				payload = input; 
				queryParams = new MultivaluedMapImpl<String, String>();
				queryParams.add(Constants.PARAM_TEST_SUITE_NAME, testSuiteName);
				rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, queryParams);
				// run /getOpenTestAutoDefect API
				rtcRestClient.runAPI(); 
				if (rtcRestClient.isRunSucessfully()) {
					JSONObject response = rtcRestClient.getReturnJson(); 
					if (response.get(Constants.JSON_KEY_RESULT) != null){
						// open defect found, close it
						Log.logVerbose("Open defect found. Closing it...");
						// set up RTC client for /closeTestAutoDefect API
						API = new PUTCloseTestAutoDefect();
						queryParams = new MultivaluedMapImpl<String, String>(); 
						queryParams.add(Constants.PARAM_DEFECT_NUM, 
								((JSONObject)response.get(Constants.JSON_KEY_RESULT)).get("Defect Number").toString());
						queryParams.add(Constants.PARAM_COMMENT, "Closed defect on test automation built and run at " 
								+ testSuiteResult.get("ExecutionTimestamp").toString() 
								+ " with an acceptable pass rate of " + passRate + ".");
						queryParams.add(Constants.PARAM_BUILD_NAME, buildName);
						rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, queryParams);
						// run /updateTestAutoDefect API
						rtcRestClient.runAPI(); 
						if (rtcRestClient.isRunSucessfully()) {
							Log.logVerbose("Closed the open defect.");
							Log.logVerbose("RTC Client Response: " + rtcRestClient.getReturnJson().toString());
						} 
						else throw new Exception("RTC Client API Failure: " + API.URI 
								+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG));
					}
					else {
						// do nothing
					}
				}
				else throw new Exception("RTC Client API Failure: " + API.URI 
						+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG));
			}
		}
		Log.getLogger().exiting(className, methodName);
	}
	

	/**
	 * Get RTC defect info to be published to respective test suite execution result XML files for reporting
	 * @throws Exception
	 */
 	public ArrayList<HashMap<String, String>> getLatestDefectsForXMLPublish() throws Exception {
		String methodName = "getLatestDefectsForXMLPublish";
		Log.getLogger().entering(className, methodName);
		
		ArrayList<HashMap<String, String>> testSuiteDefectList = new ArrayList<HashMap<String, String>>();
		// set up RTC client
		API = new GETGetAllLatestRTCDefects();
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_GET, API, null, null);
		
		// run API
		Log.logVerbose("Get latest defects of all test suites..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else {
			Log.logVerbose("All latest defects retrieved. Constructing test suite defect list..."); 
			for (Object obj: (JSONArray) rtcRestClient.getReturnJson().get(Constants.JSON_KEY_RESULT)){
				JSONObject defectInfo = (JSONObject) obj; 
				HashMap<String, String> testSuiteDefect = new HashMap<String, String>(); 
				testSuiteDefect.put("defect_number", defectInfo.get("Defect Number").toString());
				testSuiteDefect.put("defect_status", defectInfo.get("Defect Status").toString());
				testSuiteDefect.put("defect_url", defectInfo.get("Defect Link").toString());
				testSuiteDefect.put("defect_filing_date", defectInfo.get("Defect Filed Timestamp").toString().substring(0,10));
				testSuiteDefect.put("Test Suite", defectInfo.get("Test Suite").toString());
				
				testSuiteDefectList.add(testSuiteDefect);
			}
			Log.logVerbose("Test suite defect list constructed."); 
		}
		Log.getLogger().exiting(className, methodName); 
		return testSuiteDefectList; 
	}
 	
 	
	/**
	 * Get test suite builds to be published to respective test suite execution result XML files for reporting
	 * @throws Exception
	 */
 	public ArrayList<HashMap<String, String>> getLatestBuildsForXMLPublish() throws Exception {
		String methodName = "getLatestBuildForXMLPublish";
		Log.getLogger().entering(className, methodName);
		
		ArrayList<HashMap<String, String>> testSuiteBuildList = new ArrayList<HashMap<String, String>>();
		// set up RTC client
		API = new GETGetLatestBuilds();
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_GET, API, null, null);
		
		// run API
		Log.logVerbose("Get latest build test results of all test suites..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else {
			Log.logVerbose("All latest build results retrieved. Constructing test suite build list..."); 
			for (Object obj: (JSONArray) ((JSONObject) rtcRestClient.getReturnJson().get(Constants.JSON_KEY_RESULT)).get("Latest Test Suite Results")){
				JSONObject buildInfo = (JSONObject) obj; 
				HashMap<String, String> testSuiteBuild = new HashMap<String, String>(); 
				testSuiteBuild.put("build", ((JSONObject)buildInfo.get("Build")).get("Build Name").toString());
				testSuiteBuild.put("git_branch", ((JSONObject)buildInfo.get("Build")).get("Git Branch").toString());
				testSuiteBuild.put("sprint", ((JSONObject)buildInfo.get("Build")).get("Sprint").toString());
				testSuiteBuild.put("Test Suite", ((JSONObject)buildInfo.get("Test Suite")).get("Name").toString());
				
				testSuiteBuildList.add(testSuiteBuild);
			}
			Log.logVerbose("Test suite build list constructed."); 
		}
		Log.getLogger().exiting(className, methodName); 
		return testSuiteBuildList; 
	}
 	
	/**
	 * Sync status of all RTC defects
	 * @throws Exception
	 */
 	public void syncRTCDefectStatus() throws Exception {
		String methodName = "syncRTCDefects"; 
		Log.getLogger().entering(className, methodName);
		
		// set up RTC client
		API = new PUTSyncDefectStatus(); 
		JSONObject input = new JSONObject(); 
		input.put("RTCConfig", constructRTCConfig());
		payload = input; 
		RTCRestClient rtcRestClient = new RTCRestClient(Constants.REST_TYPE_PUT, API, payload, null);
		
		// run API
		Log.logVerbose("Syncing all RTC defect status..."); 
		rtcRestClient.runAPI(); 
		if (!rtcRestClient.isRunSucessfully()) throw new Exception("RTC Client API Failure: " + API.URI 
				+ " " + rtcRestClient.getReturnJson().get(Constants.JSON_KEY_ERROR_MSG)); 
		else Log.logVerbose("Sync completed."); 
		
		Log.getLogger().exiting(className, methodName); 
	}
 	
 	/**
 	 * Construct RTC config properties
 	 * @return
 	 */
 	public JSONObject constructRTCConfig() {
 		JSONObject rtcConfig = new JSONObject(); 
 		
 		rtcConfig.put("RepositoryURI", HandlerConfig.getRepositoryURI());
 		rtcConfig.put("UserId", HandlerConfig.getUser());
 		rtcConfig.put("Password", HandlerConfig.getPassword());
 		rtcConfig.put("ProjectArea", HandlerConfig.getProjectArea());
 		rtcConfig.put("DevLineId", HandlerConfig.getDevelopmentLineID());
 		
 		JSONArray subscribers = new JSONArray(); 
		for (String sub: HandlerConfig.getkitchenTestSubscribers())
			subscribers.add(sub);
 		
 		rtcConfig.put("Subscribers", subscribers);
 		return rtcConfig;
 	}
}
