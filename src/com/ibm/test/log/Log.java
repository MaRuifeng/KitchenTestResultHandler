package com.ibm.test.log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.wink.client.ClientResponse;

import com.ibm.json.java.JSONObject;

public class Log {
	
	/**
	 * Log file to store BPM REST API call logs
	 */
	// private final static String LOG_FILE_NAME = "TestResultHandler.log";
	
	private final static String LOG_FILE_NAME = generateLogFileName();
	
	public static String generateLogFileName() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return "kitchen_report_" + System.getenv("ENDPOINT_PLATFORM") + "_" + dateFormatter.format(Calendar.getInstance().getTime()) + ".log"; 
	}
	
	/**
	 * Root path of the log file
	 */
	private static String LOG_ROOT_PATH; 
	
	/**
	 * Logger instance to perform logging events
	 */
	private final static Logger logger = Logger.getLogger(Log.class.getName());
	
	/**
	 * Log File handler to handle the general log file
	 */
	private static Handler generalHandler = null; 
	
	/**
	 * Log File handler to handle each test script related log file 
	 */
	private static Handler scriptHandler = null; 
	
	static {
		logger.setLevel(Level.ALL);
		LOG_ROOT_PATH = System.getProperty("user.home") + "/kitchen_report_logs";
		addGeneralLoggerHandler();
	}
	
	/**
	 * Get the log root path
	 */
	public static String getLogRootDir(){
		File logRootDir = new File(LOG_ROOT_PATH);
		return logRootDir.getAbsolutePath();
	}
	
	/**
	 * Add log file handler for the general log file
	 */
	public static void addGeneralLoggerHandler(){
		try {
			generalHandler = createFileHandler(null);
			logger.addHandler(generalHandler);
			logger.setLevel(Level.ALL);
			Log.logVerbose("Logger successfully set up.");
			Log.logVerbose(String.format("Logger path %1s", LOG_ROOT_PATH));
			Log.logVerbose("*********************Log Started*********************");
		} catch (Exception e){
			Log.logException(e);
		}
	}
	
	/**
	 * Create log file and its handler
	 * @param childPath
	 * @return
	 */
	
	public static Handler createFileHandler(String childPath){
		File logDir = null;
		if (childPath == null) {
			logDir = new File(LOG_ROOT_PATH);
		} else {
			logDir = new File(LOG_ROOT_PATH, childPath);
		}
		
		logDir.mkdirs();
		File logFile = new File(logDir, LOG_FILE_NAME);
		Handler handler = null;
		try {
			handler = new FileHandler(logFile.getPath(), 2048000, 100, true);
		} catch (Exception e) {
			Log.logVerbose("Failed to create a file handler for " + logFile.getAbsolutePath());
		}
		handler.setFormatter(new Formatter() {
			
			@Override
			public String format(LogRecord record) {
				String logMsg = record.getMessage();
				return String.format("[%1$s] %2$s %3$d > %4$s", 
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()),
						ManagementFactory.getRuntimeMXBean().getName(),
						Thread.currentThread().getId(),
						logMsg.concat(System.getProperty("line.separator"))
					);
			}
		}); 
		handler.setLevel(Level.ALL);
		return handler;
	}
	
	/**
	 * Add script log handler
	 * @param testScriptName
	 */
	public static void addScriptLoggerHandler(String testScriptName) {
		if (scriptHandler != null) return;
		try {
			scriptHandler = createFileHandler(testScriptName);
			logger.addHandler(scriptHandler);
			Log.logVerbose("*******Test Script [" + testScriptName + "]*******");
		} catch (Exception e) {
			Log.logException(e);
		}
	}
	
	/**
	 * Remove the log file handler for the script related log file
	 * */
	public static void removeScriptLoggerHandler() {
		if (scriptHandler != null) {
			Log.logVerbose("****************End of Test Script****************");
			scriptHandler.close();
			logger.removeHandler(scriptHandler);
			scriptHandler = null;
		}
	}
	
	/**
	 * Get logger instance
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * Log error
	 */
	public static void logError(String err){
		logger.severe(err);
	}
	
	/**
	 * Log exception
	 */
	public static void logException(Throwable cause) {
		StringWriter sw = new StringWriter();
		cause.printStackTrace(new PrintWriter(sw));
		logVerbose(sw.toString());
	}
	
	/**
	 * Log message
	 */
	public static void logVerbose(String message) {
		logInfo(message);
	}
	
	/**
	 * Log an empty line
	 */
	public static void logVerbose() {
		logVerbose("");
	}
	
	/**
	 * Log information
	 */
	public static void logInfo(String info){
		if((info != null) && (!info.isEmpty()))
			System.out.println(info);
		else 
			System.out.println();
		logger.fine(info);
	}
	
	/**
	 * Log the response message from a REST API Response
	 * @param response the client response returned from REST API
	 * */
	public static void logResponse(ClientResponse response) {
		try {
			Log.logVerbose("Return code: " + response.getStatusCode());
			Log.logVerbose("Return message: " + response.getMessage());
			Object obj = response.getEntity(Object.class);
			if (obj != null) {
				if (obj instanceof JSONObject) {
					Log.logVerbose("Response data: "
							+ ((JSONObject) obj).toString());
				} else {
					Log.logVerbose("Response data: " + obj);
				}
			}
		} catch (Exception e) {
			Log.logException(e);
		}
	}
	
	/**
	 * Log a severe message to indicate failed test case
	 * @param message the message to log
	 * */
	public static void logFailure(String message) {
		logger.severe("xxxxxxxx-failure-xxxxxxx");
		logger.severe(message);
	}
	
	/**
	 * Log an entering message of source class and function
	 * @param sourceClass the source class entered
	 * @param sourceMethod the source method entered
	 * */
	public static void entering(String sourceClass, String sourceMethod) {
		logger.entering(sourceClass, sourceMethod);
	}
	

	/**
	 * Log an entering message of source class and function
	 * @param sourceClass the source class entered
	 * @param sourceMethod the source method entered
	 * @param param1 the log record casted in Object
	 * */
	public static void entering(String sourceClass, String sourceMethod,
			Object param1) {
		logger.entering(sourceClass, sourceMethod, param1);
	}
	
	/**
	 * Log an entering message of source class and function
	 * @param sourceClass the source class entered
	 * @param sourceMethod the source method entered
	 * @param param1 the log record casted in Object Array
	 * */
	public static void entering(String sourceClass, String sourceMethod,
			Object[] params) {
		logger.entering(sourceClass, sourceMethod, params);
	}
	
	/**
	 * Log an exiting message of source class and function
	 * @param sourceClass the source class entered
	 * @param sourceMethod the source method entered
	 * */
	public static void exiting(String sourceClass, String sourceMethod) {
		logger.exiting(sourceClass, sourceMethod);
	}
	
	/**
	 * Log an exiting message of source class and function
	 * @param sourceClass the source class entered
	 * @param sourceMethod the source method entered
	 * @param result the log record casted in Object
	 * */
	public static void exiting(String sourceClass, String sourceMethod,
			Object result) {
		logger.exiting(sourceClass, sourceMethod, result);
	}
}
