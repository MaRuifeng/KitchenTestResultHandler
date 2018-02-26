package com.ibm.test.report.resource;

/**
 * Define constants used in the Test Results Reader
 * @author ruifengm
 * @since 2015-Nov-25
 */

public class Constants {
	// test case execution status
	public static enum STATUS {SUCCESS, FAILURE, ERROR};
	
	public static final String ALL_TEST_SUITES = "AllTestSuites";
	
	public static final int DUE_WORKING_DAYS = 4; 
	
	//RTC defect severity levels
	public static final String RTC_SEVERITY_BLOCKER = "Critical"; 
	public static final String RTC_SEVERITY_MAJOR = "Major"; 
	public static final String RTC_SEVERITY_NORMAL = "Moderate"; 
	public static final String RTC_SEVERITY_MINOR = "Minor"; 
	
	// test phases 
	public static final String TEST_PHASE_BVT = "BVT";
	public static final String TEST_PHASE_IVT = "IVT";
	
	// test category
	// public static final String TEST_CTG_KITCHEN = "Kitchen Test";
	public static final String TEST_CTG_KITCHEN_LINUX = "KitchenTest_Linux";
	public static final String TEST_CTG_KITCHEN_WINDOWS = "KitchenTest_Windows";
	public static final String TEST_CTG_KITCHEN_AIX = "KitchenTest_AIX";
	public static final String TEST_CTG_KITCHEN_SUSE = "KitchenTest_SUSE";
	public static final String TEST_CTG_KITCHEN_UBUNTU = "KitchenTest_Ubuntu";
	
	// RTC Client API parameters
	public static final String PARAM_TEST_CATEGORY                  ="testCategory";
	public static final String PARAM_TEST_PHASE                     ="testPhase";
	public static final String PARAM_DEFECT_SEVERITY                ="defectSeverity";
	public static final String PARAM_DEFECT_NUM                     ="defectNumber";
	public static final String PARAM_COMMENT                        ="comment";
	public static final String PARAM_WORK_ITEM_NUM                  ="workItemNumber";
	public static final String PARAM_TEST_SUITE_NAME                ="testSuiteName";
	public static final String PARAM_BUILD_NAME                     ="buildName";
	
	// RTC Client API REST types
	public static final String REST_TYPE_PUT                  ="PUT";
	public static final String REST_TYPE_GET                  ="GET";
	public static final String REST_TYPE_POST                 ="POST";
	public static final String REST_TYPE_DELETE               ="DELETE";
	
	// RTC Client API response JSON field keys
	public static final String JSON_KEY_RESULT                  ="Result";
	public static final String JSON_KEY_IDENTIFIER              ="Identifier";
	public static final String JSON_KEY_ERROR_MSG               ="Error Message";
}
