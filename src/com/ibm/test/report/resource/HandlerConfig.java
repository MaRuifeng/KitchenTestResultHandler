package com.ibm.test.report.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This configuration class does below jobs for the test results handler 
 * - Read test suite owner information from the TestSuiteOwners.csv file
 * - Obtain test suite execution log file links
 * - Get other required client configuration properties
 * @author ruifengm
 * @since 2015-Nov-25
 */

public class HandlerConfig {
	private static Properties props = new Properties();
	public static ArrayList<HashMap<String, String>> testSuiteOwnerList = new ArrayList<HashMap<String, String>>();
	// public static ArrayList<String> kitchenTestCsvList = new ArrayList<String>();
	public static ArrayList<String> kitchenTestJsonList = new ArrayList<String>();
	public static ArrayList<String> kitchenTestLogList = new ArrayList<String>();
	static { //static initializer: executed when the class is loaded and a good place to put initialization of static variables
		try {
			System.out.println("Loading HandlerConfig.properties file from current class directory... ");
			props.load(HandlerConfig.class.getResourceAsStream("HandlerConfig.properties"));
			System.out.println("HandlerConfig.properties file successfully loaded.");
		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("Failed to load the HandlerConfig.properties file.");
		}
		
		// get test suite owners
		try {
			System.out.println("Loading test suite owner list from current class directory... ");
			readTestSuiteOwnerListFromCSV(HandlerConfig.class.getResourceAsStream("TestSuiteOwners.csv"));
			System.out.println("Test suite owner list successfully loaded.");
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Failed to load the test suite owner list.");
		}
		
		// create XML ouput folder
//		String dir = props.getProperty("KITCHEN_TEST_OUPUT_XML_DIR"); 
//		File theDir = new File(dir);
//		// if the directory does not exist, create it
//		if (!theDir.exists()) {
//		    System.out.println("Creating directory: " + dir);
//		    boolean result = false;
//		    try{
//		        theDir.mkdir();
//		        result = true;
//		    } 
//		    catch(SecurityException e){
//				e.printStackTrace();
//				System.out.println("Failed to create.");
//		    }        
//		    if(result) {    
//		        System.out.println(dir + " created");  
//		    }
//		}
	}
	
	/**
	 * Read CSV file data to String
	 * @param inputFilePath
	 * @return
	 * @throws IOException
	 */
	
	public static String readCsvToString (InputStream inputFileStream) throws IOException {
		BufferedReader in = null;
		String csvString = "";
		try {
			in = new BufferedReader(new InputStreamReader(inputFileStream));
			int c;
			while ((c=in.read())!=-1){
				if ( c!=13 /* && c!=10  && c!=32 */){ // Trim Carriage Return '\r', Line Feed '\n' and space characters 
					csvString=csvString+(char)c;
				}
			}
		} finally {
			if (in!=null){
				in.close();
			}
		}
		return csvString;
	}
	
	/**
	 * Get test suite owner list from CSV file
	 * @param filePath
	 */
	public static void readTestSuiteOwnerListFromCSV(InputStream inputFileStream) {
		String fileContent = ""; 
		try {
			fileContent = readCsvToString(inputFileStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] strList = fileContent.split("\n");
		String valueStr = ""; 
		String[] keys = strList[0].split(","); // first row to be header row and to contain keys by default
		for (int i=1; i<strList.length; i++){
			HashMap<String, String> entry = new HashMap<String, String>();
			String[] values = strList[i].split(",", keys.length+1);
			for (int j=0; j<keys.length; j++){
				if(keys[j].length()==0) continue;
				if (values[j].equalsIgnoreCase("")) valueStr = values[j];
				else valueStr=values[j].replaceAll("\"", "");
				entry.put(keys[j].replaceAll("\"", ""), valueStr);
			}
			testSuiteOwnerList.add(entry);
		}
	}
	
	
	public static void getKitchenTestJsonList(String dir) {
		// get kitchen test JSON file list
		try {
			System.out.println("Loading kitchen test JSON report files... ");
			File folder = new File(dir); 
			for (File item: folder.listFiles()) {
				if (item.getName().matches("(?i)KITCHEN_(.+)_report.json")) kitchenTestJsonList.add(item.getAbsolutePath());
			}
			System.out.println("kitchen test JSON report file list successfully loaded.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load the kitchen test JSON file list.");
		}
	}
	
	public static void getKitchenTestLogList(String dir) {
		// get kitchen test log file list
		try {
			System.out.println("Loading kitchen test log files... ");
			File folder = new File(dir); 
			for (File item: folder.listFiles()) {
				// if (item.getName().matches("(?i)KITCHEN_(.+)_automation.log")) kitchenTestLogList.add(item.getAbsolutePath());
				if (item.getName().matches("(?i)KITCHEN_(.+)_automation.log")) kitchenTestLogList.add(item.getName());
			}
			System.out.println("kitchen test log file list successfully loaded.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load the kitchen test log file list.");
		}
	}
	
	public static String getHTMLStyleSheetPath() {
		return props.getProperty("HTML_STYLE_SHEET_PATH");
	}
	
	public static String getRepositoryURI() {
		return props.getProperty("REPOSITORY_URI"); 
	}
	
	public static String getUser() {
		return props.getProperty("USER_ID"); 
	}
	
	public static String getPassword() {
		return props.getProperty("PASSWORD"); 
	}
	
	public static String getProjectArea() {
		return props.getProperty("PROJECT_AREA"); 
	}
	
	public static String getDevelopmentLineID() {
		return props.getProperty("DEV_LINE_ID"); 
	}
	
	public static ArrayList<String> getkitchenTestSubscribers() {
		String subscriberStr = props.getProperty("KITCHEN_TEST_SUBSCRIBERS");
		ArrayList<String> subscriberList = new ArrayList<String>();
		String[] subscriberArr = subscriberStr.split(","); 
		for (String subscriber: subscriberArr) {
			if (subscriber.contains("@")) subscriberList.add(subscriber.trim());
		}
		return subscriberList;
	}
	
	/**
	 * Get blocker defect severity threshold based on test suite execution pass rate
	 * @return
	 */
	public static double getBlockerThreshold() {
		String threshold = props.getProperty("BLOCKER_THRESHOLD"); 
		return Double.parseDouble(threshold.replace("%", ""))/100;
	}
	
	/**
	 * Get major defect severity threshold based on test suite execution pass rate
	 * @return
	 */
	public static double getMajorThreshold() {
		String threshold = props.getProperty("MAJOR_THRESHOLD"); 
		return Double.parseDouble(threshold.replace("%", ""))/100;
	}
	
	/**
	 * Get normal defect severity threshold based on test suite execution pass rate
	 * @return
	 */
	public static double getNormalThreshold() {
		String threshold = props.getProperty("NORMAL_THRESHOLD"); 
		return Double.parseDouble(threshold.replace("%", ""))/100;
	}
	
	/**
	 * Get minor defect severity threshold based on test suite execution pass rate
	 * @return
	 */
	public static double getMinorThreshold() {
		String threshold = props.getProperty("MINOR_THRESHOLD"); 
		return Double.parseDouble(threshold.replace("%", ""))/100;
	}

	public static String getRTCClientServer() {
		return props.getProperty("RTC_CLIENT_SERVER"); 
	}
	
	public static String getRTCsyncTestSuitesURI() {
		return props.getProperty("RTC_REST_URI_syncTestSuites"); 
	}
	
	public static String getRTCsyncTestCasesURI() {
		return props.getProperty("RTC_REST_URI_syncTestCases"); 
	}
	
	
	public static String getRTCaddTestResultsURI() {
		return props.getProperty("RTC_REST_URI_addTestResults"); 
	}
	
	public static String getRTCsyncAppBuildsURI() {
		return props.getProperty("RTC_REST_URI_syncAppBuilds"); 
	}
	
	public static String getRTCsyncBuildPassRateURI() {
		return props.getProperty("RTC_REST_URI_syncBuildPassRate"); 
	}
	
	public static String getRTCgetLatestBuildTestResultsURI() {
		return props.getProperty("RTC_REST_URI_getLatestBuildTestResults"); 
	}
	
	
	public static String getRTCsyncRTCDefectStatusURI() {
		return props.getProperty("RTC_REST_URI_syncRTCDefectStatus"); 
	}
	
	
	public static String getRTCcreateTestAutoDefectURI() {
		return props.getProperty("RTC_REST_URI_createTestAutoDefect"); 
	}
	
	public static String getRTCupdateTestAutoDefectURI() {
		return props.getProperty("RTC_REST_URI_updateTestAutoDefect"); 
	}
	
	
	public static String getRTCcloseTestAutoDefectURI() {
		return props.getProperty("RTC_REST_URI_closeTestAutoDefect"); 
	}
	
	public static String getRTCgetWorkItemStatusURI() {
		return props.getProperty("RTC_REST_URI_getWorkItemStatus"); 
	}
	
	public static String getRTCgetOpenTestAutoDefectURI() {
		return props.getProperty("RTC_REST_URI_getOpenTestAutoDefect"); 
	}
	
	public static String getRTCgetAllLatestRTCDefectsURI() {
		return props.getProperty("RTC_REST_URI_getAllLatestRTCDefects"); 
	}
	
	public static String getReportHost() {
		return props.getProperty("REPORT_HOST"); 
	}
	
	public static String getReportHostPub() {
		return props.getProperty("REPORT_HOST_PUB"); 
	}
	
	public static String getReportUser() {
		return props.getProperty("REPORT_USER"); 
	}
	
	public static String getReportPass() {
		return props.getProperty("REPORT_PASS"); 
	}
	
	public static String getReportMainDir() {
		return props.getProperty("REPORT_MAIN_DIR"); 
	}
	
	public static String getReportKitchenDir() {
		return props.getProperty("REPORT_KITCHEN_DIR"); 
	}
	
	public static String getReportLogDir() {
		return props.getProperty("REPORT_LOG_DIR"); 
	}
	
	public static String getSMTPServer() {
		return props.getProperty("SMTP_SERVER"); 
	}
	
	public static String getSMTPServerPort() {
		return props.getProperty("SMTP_PORT"); 
	}
	
	public static String getSMTPFromAddress() {
		return props.getProperty("SMTP_FROM_ADDRESS"); 
	}
	
	/**
	 * Get Email receiver list from txt file
	 * @param filePath
	 * @throws IOException 
	 */
	public static ArrayList<String> getEmailReceiverList() throws IOException {
		ArrayList<String> receiverList = new ArrayList<String>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(HandlerConfig.class.getResourceAsStream("EmailAddress.txt")));
			String address = "";
			while((address=in.readLine()) != null){
				receiverList.add(address.trim());
			}
		} finally {
			if (in!=null){
				in.close();
			}
		}
		return receiverList;
	}
	
	public static String getSMTPAuthUser() {
		return props.getProperty("SMTP_AUTH_USER"); 
	}
	
	public static String getSMTPAuthPass() {
		return props.getProperty("SMTP_AUTH_PASS"); 
	}
}
