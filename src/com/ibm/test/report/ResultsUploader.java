package com.ibm.test.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.ibm.test.log.Log;
import com.ibm.test.report.resource.HandlerConfig;

/**
 * This utility class uploads the test results (report and logs) to a remote HTTP server. 
 * 
 * @author ruifengm
 * @since 2016-Aug-10
 */

public class ResultsUploader {
	private static final String className = ResultsUploader.class.getName();
	
	private String reportDir;
	private String build;
	private String testPhase;
	private String platform;
	

	// Constructor
    public ResultsUploader(String reportDir, String build, String testPhase, String platform) {
		super();
		this.reportDir = reportDir;
		this.build = build;
		this.testPhase = testPhase.toLowerCase();
		this.platform = platform;
	}
	
	/**
	 * Set up FTP and upload
	 * @return
	 * @throws IOException 
	 */
	public void upload() throws IOException{
		String methodName = "upload";
		Log.getLogger().entering(className, methodName);
		
		FTPClient ftpClient = new FTPClient();
		
		ftpClient.connect(HandlerConfig.getReportHost(), 21);
		ftpClient.login(HandlerConfig.getReportUser(), HandlerConfig.getReportPass());
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		String parentFTPWorkingDir = ftpClient.printWorkingDirectory();
		
		Log.logVerbose("Report server FTP info: " + HandlerConfig.getReportHost() + " " +  HandlerConfig.getReportUser());

		// making directories in sequence on the report server
		String reportMainDir = HandlerConfig.getReportMainDir(); 
		String buildDir = reportMainDir + "/" + build; 
		String testPhaseDir = buildDir + "/" + testPhase;
		String kitchenResultDir = testPhaseDir + "/" + HandlerConfig.getReportKitchenDir(); 
		String platformDir = kitchenResultDir + "/" + platform;
		
		makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, reportMainDir);
		makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, buildDir);
		makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, testPhaseDir);
		makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, kitchenResultDir);
		makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, platformDir);
		
		// recursively upload
		recursivelyUpload(ftpClient, parentFTPWorkingDir, reportDir, platformDir);
		
		Log.logVerbose("Upload completed.");
		Log.getLogger().exiting(className, methodName);
	}
	
	/**
	 * Recursively upload everything in the source directory (excluding the source dir itself) to the FTP target directory
	 * @param ftp
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException 
	 */
	private void recursivelyUpload(FTPClient ftpClient, String parentFTPWorkingDir, String sourceDir, String targetDir) throws IOException {
		Log.logVerbose("Navigating into source directory: " + sourceDir);
		
		File sourceFolder = new File(sourceDir); 
		for (File item: sourceFolder.listFiles()) {
			if (item.isDirectory()) {
				Log.logVerbose("Navigating into sub source directory: " + item);
				String subTargetDir = targetDir + "/" + item.getName();
				makeDirOnFtpServer(ftpClient, parentFTPWorkingDir, subTargetDir); 
				recursivelyUpload(ftpClient, parentFTPWorkingDir, item.getAbsolutePath(), subTargetDir); 
			}
			else if (item.isFile()) {
				Log.logVerbose("Uploading file: " + item.getAbsolutePath());
				InputStream inputStream = new FileInputStream(item);
				String targetFile = parentFTPWorkingDir + "/" + targetDir + "/" + item.getName();
				ftpClient.storeFile(targetFile, inputStream);
				inputStream.close();
				Log.logVerbose(String.format("Successfully uploaded file %1s", targetFile));
			}
		}
	}
	
	/**
	 * Make directories on FTP server if not exist
	 * @param dir
	 * @throws IOException 
	 */
	private void makeDirOnFtpServer(FTPClient ftpClient, String parentFTPWorkingDir, String dir) throws IOException {
		String absolute_dir_path = parentFTPWorkingDir + "/" + dir;
		ftpClient.changeWorkingDirectory(absolute_dir_path); 
		int returnCode = ftpClient.getReplyCode(); 
		if (returnCode == 550 ) {
			Log.logVerbose("Making FTP server directory: " + absolute_dir_path);
			ftpClient.makeDirectory(absolute_dir_path);
		}
		ftpClient.changeWorkingDirectory(parentFTPWorkingDir); // restore to default
	}
}

