package com.ibm.test.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Constants.STATUS;
import com.ibm.test.report.resource.HandlerConfig;
import com.ibm.test.report.resource.TestCaseResult;
import com.ibm.test.report.resource.TestSuiteResult;

/**
 * This utility class contains a writer that uses a DOM XML parser 
 * to write kitchen test results into a single XML file.
 * 
 * @author ruifengm
 * @since 2016-Jul-28
 */

public class ResultsXMLWriter {
	private static final String className = ResultsXMLWriter.class.getName();
	
	/**
	 * Write XML file from the test suite result list
	 * @param testSuiteResultList
	 * @param xmlFilePath
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void writeXMLFile(ArrayList<TestSuiteResult> testSuiteResultList, 
			ArrayList<HashMap<String, String>> testSuiteDefectList,
			ArrayList<HashMap<String, String>> testSuiteBuildList,
			String logsLink,
			String xmlFilePath,
			String testCategory) 
			   throws ParserConfigurationException, TransformerException {
		String methodName = "writeXMLFile";
		Log.getLogger().entering(className, methodName);
		
		Log.logVerbose("Writing kitchen test results into a single XML file...");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder(); 
		
		// root element
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("testsuites");
		Attr logAttr = doc.createAttribute("logs");
		//logAttr.setValue(HandlerConfig.getKitchenTestCsvDir());
		logAttr.setValue(logsLink);
		root.setAttributeNode(logAttr);
		Attr timestampAttr = doc.createAttribute("timestamp");
		timestampAttr.setValue(generateTimeStampString(Calendar.getInstance().getTime()));
		root.setAttributeNode(timestampAttr);
		root.setAttribute("testEdptProvider", "vSphere ESXi Bare-Metal Hypervisor - VMware");
		root.setAttribute("testCat", testCategory);
		doc.appendChild(root);
		
		int index = 0; 
		for (TestSuiteResult tsr: testSuiteResultList) {	
			// test suite element
			Element ts = doc.createElement("testsuite");
			ts.setAttribute("failures", String.valueOf(tsr.getFailureCount()));
			ts.setAttribute("errors", String.valueOf(tsr.getErrorCount()));
			ts.setAttribute("tests", String.valueOf(tsr.getTestCount()));
			ts.setAttribute("time", String.valueOf(tsr.getExecutionTimeInSeconds()));
			ts.setAttribute("prepTime", String.valueOf(tsr.getPrepTimeInSeconds()));
			ts.setAttribute("teardownTime", String.valueOf(tsr.getTeardownTimeInSeconds()));
			ts.setAttribute("time", String.valueOf(tsr.getExecutionTimeInSeconds()));
			ts.setAttribute("name", tsr.getTestSuiteName());
			for (HashMap<String, String> testSuiteOwner: HandlerConfig.testSuiteOwnerList){
				if (tsr.getOwner().equals(testSuiteOwner.get("Owner"))){
					ts.setAttribute("owner", testSuiteOwner.get("Owner Name"));
					break;
				}				
			}
//			ts.setAttribute("build", tsr.getBuild().getBuildName());
//			ts.setAttribute("sprint", tsr.getBuild().getSprint());
//			ts.setAttribute("git_branch", tsr.getBuild().getGitBranch());
			ts.setAttribute("package", tsr.getTestPackage());
			ts.setAttribute("id", String.valueOf(index));
				// defect info
			for (HashMap<String, String> tsd: testSuiteDefectList){
				if (tsd.get("Test Suite").equals(tsr.getTestSuiteName())) {
					for (String key: tsd.keySet()) {
						if (key != "Test Suite") ts.setAttribute(key, tsd.get(key));
					}
				}
			}
			
			ts.setAttribute("log", logsLink + tsr.getTestCaseResultList().get(0).getTestLogLink());
				// build info
			for (HashMap<String, String> tsb: testSuiteBuildList){
				if (tsb.get("Test Suite").equals(tsr.getTestSuiteName())) {
					for (String key: tsb.keySet()) {
						if (key != "Test Suite") ts.setAttribute(key, tsb.get(key));
					}
				}
			}
			index++; 
			
			for (TestCaseResult tcr: tsr.getTestCaseResultList()) {
				// test case element
				Element tc = doc.createElement("testcase");
				tc.setAttribute("classname", tcr.getTestCaseParentSuite());
				tc.setAttribute("name", tcr.getTestCaseName());
				tc.setAttribute("time", String.valueOf(tcr.getExecutionTimeInSeconds()));
				tc.setAttribute("prepTime", String.valueOf(tcr.getPrepTimeInSeconds()));
				tc.setAttribute("teardownTime", String.valueOf(tcr.getTeardownTimeInSeconds()));
				//tc.setAttribute("log", tcr.getTestLogLink());
				tc.setAttribute("log", logsLink + tcr.getTestLogLink());
				tc.setAttribute("testEdptOS", tcr.getTestEdptOS());
				tc.setAttribute("testEdptIP", tcr.getTestEdptIP());
				
				// failure element
				if (tcr.getStatus().equals(STATUS.FAILURE)) {
					Element f = doc.createElement("failure");
					f.setAttribute("message", "Failure Type: " + tcr.getFailureType());
					f.appendChild(doc.createCDATASection(tcr.getFailureMsg()));
					tc.appendChild(f);
				}
				
			    // error element
				if (tcr.getStatus().equals(STATUS.ERROR)) {
					Element f = doc.createElement("error");
					f.setAttribute("message", "Error Type: " + tcr.getErrorType());
					f.appendChild(doc.createCDATASection(tcr.getErrorMsg()));
					tc.appendChild(f);
				}
				
				ts.appendChild(tc);
			}
			root.appendChild(ts);
		}

		// write the document content to XML file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xmlFilePath));
		
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		transformer.transform(source, result);
		
		Log.logVerbose("Completed. File saved to " + xmlFilePath + ".");
		Log.getLogger().exiting(className, methodName);
	}
	
	/** 
	 * Generate timestamp string in yyyy-MM-dd HH:mm:ss format
	 * @param timeValue
	 * @return
	 */
	private String generateTimeStampString(Date timeValue) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormatter.format(timeValue); 
	}
}
