package com.ibm.test.report;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.ibm.test.log.Log;


/**
 * This utility class contains an XSLT transformer
 * that converts the single XML file with kitchen test results to HTML. 
 * 
 * @author ruifengm
 * @since 2016-Jul-28
 */

public class ResultsHTMLTransformer {
	private static final String className = ResultsHTMLTransformer.class.getName();
	
	public void transformXMLtoHTML(String sourcePath, String targetPath, String styleSheetPath) 
			throws TransformerFactoryConfigurationError, TransformerException{
		String methodName = "transformXMLtoHTML";
		Log.getLogger().entering(className, methodName);
		
		Log.logVerbose("Transforming XML to HTML..");
		Source xmlInput = new StreamSource(new File(sourcePath));
		Source xsl = new StreamSource(ResultsHTMLTransformer.class.getResourceAsStream(styleSheetPath));
		Result xmlOutput = new StreamResult(new File(targetPath));

		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
		transformer.transform(xmlInput, xmlOutput);	
		
		Log.logVerbose("Transform completed. Filed saved to " + targetPath + ".");
		Log.getLogger().exiting(className, methodName);
	}
}
