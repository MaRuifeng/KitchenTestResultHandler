package com.ibm.test.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ibm.test.log.Log;
import com.ibm.test.report.resource.Constants;
import com.ibm.test.report.resource.HandlerConfig;

/**
 * This utility class Emails abstracted test results to distribution list.
 * 
 * @author ruifengm
 * @since 2016-Aug-22
 */


public class ResultsEmailer {
	private static final String className = ResultsEmailer.class.getName();
	
	private Message message;
	private Session session;
	private InternetAddress sender;
	private InternetAddress[] receivers;
	
	public String build;
	public String testPassRate;
	public String platform;
	
	// Override the getPasswordAuthentication method in Authenticator class to pass in username and password
	public class SMTPAuthenticator extends Authenticator {
		private String username, password; 
		
		public SMTPAuthenticator(String username, String password) {
			this.username = username;
			this.password = password; 
		}
		
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.username, this.password);
		}
	}
	
	// constructor
	public ResultsEmailer(String build, String platform) {
		super();
		this.build = build;
		this.platform = platform;
	}

	/**
	 * Construct Email
	 * @param emailSubject
	 * @param emailContent
	 * @throws IOException
	 * @throws MessagingException
	 */
	
	public void constructEmail (String emailSubject, String emailContent) throws IOException, MessagingException {
		String methodName = "constructEmail";
		Log.getLogger().entering(className, methodName);
		Log.logVerbose("Constructing Email...");
		// force javax.mail to use IPv4
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		ArrayList<String> receiverList = HandlerConfig.getEmailReceiverList();
		sender = new InternetAddress(HandlerConfig.getSMTPFromAddress(), "SDAD BPM Build");
		
		String smtpServer = HandlerConfig.getSMTPServer();
		String smtpPort = HandlerConfig.getSMTPServerPort();
		String smtpAuthUser = HandlerConfig.getSMTPAuthUser();
		String smtpAuthPass = HandlerConfig.getSMTPAuthPass();
		
		Properties props = new Properties(); 
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", smtpServer);
		props.put("mail.smtp.port", smtpPort);
		
		Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPass);
		
		session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);
		
		message = new MimeMessage(session);
		// sender
		message.setFrom(sender);
		// recipients
		receivers = new InternetAddress[receiverList.size()];
		Log.logVerbose("Email recipients:");
		for (int i=0; i<receiverList.size(); i++) {
			receivers[i] = new InternetAddress(receiverList.get(i));
			Log.logVerbose(receiverList.get(i));
		}
		message.setRecipients(Message.RecipientType.TO, receivers);
		// date
		message.setSentDate(new Date());
		// subject
		message.setSubject(emailSubject);
		Log.logVerbose("Email subject: " + emailSubject);
		// body
		MimeBodyPart mbpEmailContent = new MimeBodyPart();
		mbpEmailContent.setContent(emailContent, "text/html");
		Multipart mp = new MimeMultipart(); 
		mp.addBodyPart(mbpEmailContent);
		message.setContent(mp);
		
		Log.logVerbose("Email constructed.");
		Log.getLogger().exiting(className, methodName);
	}
	
	/**
	 * Send Email
	 * @throws MessagingException
	 */
	public void sendEmail() throws MessagingException {
		String methodName = "sendEmail";
		Log.getLogger().entering(className, methodName);
		
		Log.logVerbose("Sending Email...");
		Transport transport = session.getTransport("smtp");
		transport.connect();
		Transport.send(message);
		Log.logVerbose("Email sent.");
		
		Log.getLogger().exiting(className, methodName);
	}
	
	/**
	 * Construct Email content
	 * @param htmlReportFilePath
	 * @return
	 * @throws IOException
	 */
	public String constructEmailContent(String htmlReportFilePath) throws IOException {
		File file = new File(htmlReportFilePath);
		String result = "";
		String header = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String line = "";
			while(!(line=in.readLine()).contains("TestSuite Details")){
				result = result + line;
			}
			String lastTableHeader = "<hr size=\"1\" width=\"95%\" align=\"left\">";
			int last = result.lastIndexOf(lastTableHeader);
			result = result.substring(0, last);
			// find pass rate info
			int index = result.indexOf("%</td>");
			String temp = result.substring(0, index + 1);
			last = temp.lastIndexOf("td>");
			this.testPassRate = temp.substring(last + 3);
			header = "<div><h3 align=\"left\"><a href=\"http://" + HandlerConfig.getReportHostPub() 
					+ "/" + HandlerConfig.getReportMainDir()
					+ "/" + this.build
					+ "/" + Constants.TEST_PHASE_BVT.toLowerCase()
					+ "/" + HandlerConfig.getReportKitchenDir() + "/" + platform + "/" + file.getName()
					+ "\">Click Here To View Detailed Report</a></h3></div>";
		} finally {
			if (in!=null){
				in.close();
			}
		}
		return header + result;
	}
}
