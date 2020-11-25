package com.kpit.selenium.master.testlink;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author milindw3
 */
public class TestLinkResultBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String testPlanName;
	private Integer testCaseId;
	private String testCaseSummary;
	private String executionResult;
	private String automationBuild;
	private Date executionDate;
	private String testSuite;
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTestPlanName() {
		return testPlanName;
	}
	public void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCaseSummary() {
		return testCaseSummary;
	}
	public void setTestCaseSummary(String testCaseSummary) {
		this.testCaseSummary = testCaseSummary;
	}
	public String getExecutionResult() {
		return executionResult;
	}
	public void setExecutionResult(String executionResult) {
		this.executionResult = executionResult;
	}
	public String getAutomationBuild() {
		return automationBuild;
	}
	public void setAutomationBuild(String automationBuild) {
		this.automationBuild = automationBuild;
	}
	public Date getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}
	public String getTestSuite() {
		return testSuite;
	}

}
