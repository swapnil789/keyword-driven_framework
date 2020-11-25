package com.kpit.selenium.suite.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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

public class SendMail

{
	public static void sendSeleniumTestSuiteEmail(String dateString, String projectName, String functionalityName, String screenshotPath, List<String> recipients, String tomcatBaseURL, String threadName) {
		try {
			String subject =  "Auto-generated Selenium Sanity Suite Results - " + projectName + " - " + functionalityName + " - " + TestUtil.now("yyyy.MM.dd HH:mm:ss");
			

			String body = "Dear Reader,"+ "<br/>"+ "<br/>"+"<b>Note:</b> You need to be within KPIT network or use VPN into KPIT network if connecting from internet." + "<br/>" 
					  + "<a href='"+tomcatBaseURL + dateString + "/index.html'>Test Suite Results</a>" + "<br/>" 	
					  + "Please click on the Test Results link to view posted results of testing for " + projectName + " - " + functionalityName + "<br/>"
					  + "You will see Summary of Test Results , there are hyperlinks on it to take you through details."+ "<br/>"+ "<br/>"
					  + "Thank you!";

			String resultFilePath = screenshotPath + File.separator + dateString +  File.separator + "index.html";
			
			sendEmail(subject, recipients, body, resultFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Session getSession() {
		Authenticator authenticator = new Authenticator();

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.ssl.trust", "smtp.kpit.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.starttls.enable", "true");	
		properties.setProperty("mail.smtp.host", "smtp.kpit.com");
		properties.setProperty("mail.smtp.port", "25");

		return Session.getInstance(properties, authenticator);
	}

	private static class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator() {
//			String username = "automationcdd@gmail.com";
//			String password = "XXXXXX";
			String username = "Diagnostics.Automationtesting@kpit.com";
			String password = "XXXXXX";
			authentication = new PasswordAuthentication(username, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
	
	private static void sendEmail(String subject, List<String> recipients, Object body, String attachmentPath) throws MessagingException, IOException{
		Session session = getSession();
		MimeMessage msg = new MimeMessage(session);
		// set message headers
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");
		

		msg.setFrom(new InternetAddress("Diagnostics.Automationtesting@kpit.com",
				"NoReply-DiagQA"));

		msg.setReplyTo(InternetAddress.parse("Diagnostics.Automationtesting@kpit.com",
				false));

		msg.setSubject(subject, "UTF-8");
		
		msg.setSentDate(new Date());

		
		for (int i = 0; i < recipients.size(); i++) {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recipients.get(i)));
		}

		// creates body part for the message
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, "text/html");
		
		Multipart multipart = new MimeMultipart();
		
		multipart.addBodyPart(messageBodyPart);
		
		
		if (null != attachmentPath) {
			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.attachFile(attachmentPath);
			System.out.println("attachment path - " + attachmentPath);
			multipart.addBodyPart(attachPart);
		}
		
		msg.setContent(multipart);
		System.out.println("Message is ready");
		Transport.send(msg);

		System.out.println("EMail Sent Successfully!!");

	}

	public static void sendTestLinkEmail(String basePath, String buildName, List<String> recipients, String tomcatBaseURL) throws MessagingException, IOException {
		String subject = "Auto-generated TestLink Execution Suite Results for Build - " + buildName;
		
			String body = "Dear Reader,"+ "<br/>"+ "<br/>"+"<b>Note:</b> You need to be within KPIT network or use VPN into KPIT network if connecting from internet." + "<br/>" 
		  + "<a href='"+tomcatBaseURL + buildName + "/testLinkResult.html'>Test Link Consolidated Results</a>" + "<br/>" 	
		  + "Please click on the Test Results link to view results for consolidated TestLink testcases updated by selenium tests."+ "<br/>"+ "<br/>"
		  + "Thank you!";
			
		String resultFilePath =  basePath + buildName + File.separator + "testLinkResult.html";
		
		sendEmail(subject, recipients, body, resultFilePath);
	}

	public static void sendSeleniumConsolidatedEmail(String basePath,
			String buildName, String projectHome, List<String> recipients, String tomcatBaseURL) throws MessagingException, IOException {
		String subject = "Auto-generated Selenium Execution Suite Results for Build - " + projectHome + " - " + buildName;
		

		String body = "Dear Reader,"+ "<br/>"+ "<br/>"
		  +"<b>Note:</b> You need to be within KPIT network or use VPN into KPIT network if connecting from internet." + "<br/>" 
		  + "<a href='"+tomcatBaseURL + buildName + "/" + projectHome +"_SeleniumConsolidatedResult.html'>Selenium Consolidated Results</a>" + "<br/>" 		
		  + "Please click on the Test Results link to view posted consolidated results of testing." + "<br/>"
		  + "You will see Summary of Test Results , there are hyperlinks on it to take you through details."+ "<br/>"+ "<br/>"
		  + "Thank you!";	
		
		String resultFilePath =  basePath + buildName + File.separator + projectHome + "_SeleniumConsolidatedResult.html";
		
		sendEmail(subject, recipients, body, resultFilePath);
	}

}