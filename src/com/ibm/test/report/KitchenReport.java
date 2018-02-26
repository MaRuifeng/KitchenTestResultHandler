package com.ibm.test.report;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Constants;
import com.ibm.test.report.resource.HandlerConfig;

public class KitchenReport {
	
	private static void printUsage(){
		System.out.println("Designed usage of KitchenReport: \n" +
				"com.ibm.test.report.KitchenReport arg1 arg2 arg3 arg4\n" +
				"where arg1 is the source JSON file directory\n" +
				"and arg2 is the source log file directory\n" +
				"and arg3 is the output directory for XML and HTML reports\n" +
				"and arg4 is the build name in <ivt_xx.x.yyyymmdd-hhmm.xxx> format\n" + 
				"and arg5 is the platform of the endpoint server to be tested.\n\n" +
				"Example: com.ibm.cc.test.KitchenReport C:/kitchen_test/json C:/kitchen_test/log C:/kitchen_result ivt_11.4.20160719-1900.166 windows");
	}
	
	public static void main(String[] args) {
		try {
			// process arguments
			if(args == null || args.length != 5){
				printUsage();
				return;
			}
			String inputJsonDir = args[0];
			String inputLogDir = args[1];
			String outputDir = args[2];
			String buildName = args[3];
			String serverPlatform = args[4];
			
			String testCategory = "";
			if(serverPlatform.toLowerCase().contains("windows")) {
				testCategory = Constants.TEST_CTG_KITCHEN_WINDOWS;
			}
			else if(serverPlatform.toLowerCase().contains("linux")) {
				testCategory = Constants.TEST_CTG_KITCHEN_LINUX;
			}
			else if(serverPlatform.toLowerCase().contains("aix")) {
				testCategory = Constants.TEST_CTG_KITCHEN_AIX;
			}
			else if(serverPlatform.toLowerCase().contains("suse")) {
				testCategory = Constants.TEST_CTG_KITCHEN_SUSE;
			}
			else if(serverPlatform.toLowerCase().contains("ubuntu")) {
				testCategory = Constants.TEST_CTG_KITCHEN_UBUNTU;
			}
			else {
				throw new Exception("Given server platform " + serverPlatform + " is either not valid or not supported.");
			}
			
			ResultsJSONReader jsonReader = new ResultsJSONReader();
			ResultsRTCPublisher resultsPublisher = new ResultsRTCPublisher(); 
			ResultsXMLWriter xmlWriter = new ResultsXMLWriter(); 
			ResultsHTMLTransformer htmlWriter = new ResultsHTMLTransformer();
			ResultsEmailer mailer = new ResultsEmailer(buildName, serverPlatform);
			
			// Read JSON and log files 
			HandlerConfig.getKitchenTestJsonList(inputJsonDir);
			HandlerConfig.getKitchenTestLogList(inputLogDir);
			for (String jsonFilePath : HandlerConfig.kitchenTestJsonList) {
				jsonReader.constructKitchenTestResultList(jsonReader.readDataFromJSON(jsonFilePath), jsonFilePath, HandlerConfig.kitchenTestLogList, buildName);
			}
			// System.out.println(jsonReader.kitchenTestResultJsonArray.toString());
			
			// Update RTC & DB
			resultsPublisher.syncTestSuites(testCategory,  jsonReader.kitchenTestResultJsonArray);
		    resultsPublisher.syncTestCases(jsonReader.kitchenTestResultJsonArray);
		    resultsPublisher.syncAppBuilds(jsonReader.kitchenTestResultJsonArray);
			resultsPublisher.addTestResults(Constants.TEST_PHASE_BVT, jsonReader.kitchenTestResultJsonArray);
			resultsPublisher.syncBuildPassRate(jsonReader.kitchenTestResultJsonArray);
			resultsPublisher.syncRTCDefectStatus();
			resultsPublisher.publishTestResultsToRTC(testCategory, Constants.TEST_PHASE_BVT, jsonReader.kitchenTestResultJsonArray, buildName);
			
			ArrayList<HashMap<String, String>> testSuiteDefectList = resultsPublisher.getLatestDefectsForXMLPublish(); 
			ArrayList<HashMap<String, String>> testSuiteBuildList = resultsPublisher.getLatestBuildsForXMLPublish(); 
			
			// Write XML & HTML 
			String outputXML = outputDir + "/" + testCategory + "-summary.xml"; 
			String logsLink = "/" + HandlerConfig.getReportMainDir() + "/" + buildName + "/" +
					Constants.TEST_PHASE_BVT.toLowerCase() + "/" + 
					HandlerConfig.getReportKitchenDir() + "/" + serverPlatform + "/" + HandlerConfig.getReportLogDir() + "/";
			xmlWriter.writeXMLFile(jsonReader.kitchenTestResultList, testSuiteDefectList, testSuiteBuildList, logsLink, outputXML, testCategory.replace("KitchenTest_", ""));
			String outputHtml = outputDir + "/" + testCategory + "-summary.html";
			htmlWriter.transformXMLtoHTML(outputXML, outputHtml, HandlerConfig.getHTMLStyleSheetPath());
			
			// Upload
			ResultsUploader resultsUploader = new ResultsUploader(outputDir, buildName, Constants.TEST_PHASE_BVT, serverPlatform);
			resultsUploader.upload();
			
			// Email 
			String content = mailer.constructEmailContent(outputHtml);
			String subject = "[" + testCategory.replace("KitchenTest_", "") + "] Chef Recipe Kitchen Test (" + mailer.build + "): " + mailer.testPassRate + " passed.";
		    mailer.constructEmail(subject, content);
			mailer.sendEmail();
			
			Log.logVerbose("*********************Log Ended*********************");
		} catch (Exception e) {
			Log.logException(e);
		}
	}

}
