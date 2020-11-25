package com.kpit.selenium.suite.testscript;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author milindw3
 */
public class JiraStatusThread implements Runnable {

	private String result;
	private String jiraNo;
	private String testProject;
	private String testPlan;
	private String build;
	private Properties properties;
	public static Logger APPICATION_LOGS = Logger.getLogger("DriverLogger");

	public JiraStatusThread(String result, String jiraNo,
			 String testProject,
			String testPlan, String build, Properties properties) {
		this.result = result;
		this.jiraNo = jiraNo;
		this.testProject = testProject;
		this.testPlan = testPlan;
		this.build = build;
		this.properties = properties;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
