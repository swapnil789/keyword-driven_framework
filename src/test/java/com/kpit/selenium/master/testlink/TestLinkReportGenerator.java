package com.kpit.selenium.master.testlink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import com.kpit.selenium.suite.util.Constants;
import com.kpit.selenium.suite.util.SendMail;

/**
 * @author milindw3
 */
public class TestLinkReportGenerator {

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Properties CONFIG = new Properties();
            String targetEnvironment = System.getProperty("targetEnvironment");
            String targetBuildName = System.getProperty("targetBuildName");
            System.out.println("targetEnvironment >> " + targetEnvironment);
            
            FileInputStream fs = null;
            if(null != targetEnvironment && !"".equals(targetEnvironment)){
                String envtVariable = System.getProperty("user.dir") + File.separatorChar + targetEnvironment + ".properties";
                System.out.println("envtVariable >> " + envtVariable);
				fs = new FileInputStream(envtVariable);
            } else {
            	fs = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "config"
                        + File.separatorChar + "config.properties");
            }

            CONFIG.load(fs);
            String buildName = CONFIG.getProperty("TEST_LINK_BUILD");
            if (null != targetBuildName) {
            	buildName = targetBuildName;
			}
            Map<Integer, TestLinkResultBO> testLinkTestCasesMap = prepareTestLinkResults(CONFIG, buildName);
            printReport(testLinkTestCasesMap, CONFIG, buildName);
            
			sendEmail(CONFIG, buildName);
            
		} catch (Exception e) {
			System.err.println("TestLink Report generation failed " + e);
		}

	}

	private static void sendEmail(Properties CONFIG, String buildName) throws MessagingException, IOException {
        String emailRecipient = CONFIG.getProperty("EXTERNAL_EMAIL_RECIPIENT");
        String sendEmailFlag = CONFIG.getProperty(Constants.SEND_EMAIL);
        String path = CONFIG.getProperty("screenshotPath");
        String tomcatBaseURL = CONFIG.getProperty("TOMCAT_BASE_URL");
        if (null != sendEmailFlag && "Y".equals(sendEmailFlag)) {
        	List<String> recipients = new ArrayList<String>();
			if (null != emailRecipient && !"".equals(emailRecipient)) {
				recipients = Arrays.asList(emailRecipient.split("\\s*,\\s*"));
			} else {
				recipients.add("milind.warade@kpit.com");
			}
			
			SendMail.sendTestLinkEmail(path, buildName, recipients, tomcatBaseURL);
		}
		
	}

	private static void printReport(
			Map<Integer, TestLinkResultBO> testLinkTestCasesMap,
			Properties config, String buildName) throws IOException {
		String path = config.getProperty("screenshotPath");
		TestLinkResultBO summaryTestLinkResultBO = null;
		int totalTestCases = 0;
		int passedTestCases = 0;
		int failedTestCases = 0;
		int notRunTestCases = 0;
		StringBuilder reportBuilder = new StringBuilder();
		reportBuilder.append("<HTML>");
		reportBuilder.append("<HEAD>");
        reportBuilder.append(" <TITLE>TestLink Automation Execution Results</TITLE>");
        reportBuilder.append("</HEAD>");
		reportBuilder.append("<BODY>");
		reportBuilder.append("<h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> TestLink Automation Execution Results</u></b></FONT></h4>\n");
		
		StringBuilder mainTableBuilder = new StringBuilder();
		mainTableBuilder.append("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u> Details :</u></FONT></h4>\n");
		mainTableBuilder.append("<TABLE border=1 cellspacing=1 cellpadding=1 width=100% >");
		mainTableBuilder.append("<TR>");
		mainTableBuilder.append("<TD  width=50%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2> Test Suite </FONT></TD>");
		mainTableBuilder.append("<TD  width=30%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2> TestCase Summary </FONT> </TD>");
		mainTableBuilder.append("<TD  width=5%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2> Execution Result </FONT></TD>");
		mainTableBuilder.append("<TD  width=15%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2> Execution Time </FONT></TD>");
		mainTableBuilder.append("</TR>");
		if (!testLinkTestCasesMap.isEmpty()) {
			Collection<TestLinkResultBO> testLinksResultBOs =  testLinkTestCasesMap.values();
			totalTestCases = testLinksResultBOs.size();
			for (TestLinkResultBO testLinkResultBO : testLinksResultBOs) {
				if (null == summaryTestLinkResultBO) {
					summaryTestLinkResultBO = testLinkResultBO;
				}
				mainTableBuilder.append("<TR>");
				mainTableBuilder.append("<TD width=50%  align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + testLinkResultBO.getTestSuite() + "</FONT</TD>");
				mainTableBuilder.append("<TD  width=30%  align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + testLinkResultBO.getTestCaseSummary() + "</FONT</TD>");
				if ("Passed".equals(testLinkResultBO.getExecutionResult())) {
					mainTableBuilder.append("<TD  width=5%  align= center bgcolor=#BCE954><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + testLinkResultBO.getExecutionResult() + "</FONT</TD>");
					passedTestCases++;
				} else if ("Failed".equals(testLinkResultBO.getExecutionResult())){
					mainTableBuilder.append("<TD  width=5%  align= center bgcolor=Red><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + testLinkResultBO.getExecutionResult() + "</FONT</TD>");
					failedTestCases++;
				} else {
					mainTableBuilder.append("<TD  width=5%  align= center bgcolor=Grey><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + testLinkResultBO.getExecutionResult() + "</FONT</TD>");
					notRunTestCases++;
				}
				if (null != testLinkResultBO.getExecutionDate()) {
					mainTableBuilder.append("<TD  width=15%  align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2>"
							+ testLinkResultBO.getExecutionDate() + "</FONT></TD>");
				} else {
					mainTableBuilder.append("<TD  width=15%  align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2> </FONT></TD>");
				}
				mainTableBuilder.append("</TR>");
			}
		}
		mainTableBuilder.append("</TABLE>");
		
		
		StringBuilder summaryTableBuilder = new StringBuilder();
		summaryTableBuilder.append("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u> Summary :</u></FONT></h4>\n");
		summaryTableBuilder.append("<table  border=1 cellspacing=1 cellpadding=1 >\n");
		if (!testLinkTestCasesMap.isEmpty()) {
			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Project Name</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + summaryTestLinkResultBO.getProjectName() + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Test Plan Name</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + summaryTestLinkResultBO.getTestPlanName() + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>TestLink Build</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + summaryTestLinkResultBO.getAutomationBuild() + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Total TestCases</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + totalTestCases + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Pass TestCases</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + passedTestCases + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Fail TestCases</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + failedTestCases + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

			summaryTableBuilder.append("<TR>");
			summaryTableBuilder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Not Run TestCases</b></FONT></td>");
			summaryTableBuilder.append("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>" + notRunTestCases + "</b></FONT></td>");
			summaryTableBuilder.append("</TR>");

		}
		summaryTableBuilder.append("</table>");
		
		reportBuilder.append(summaryTableBuilder);
		reportBuilder.append(mainTableBuilder);
		reportBuilder.append("</BODY>");
		reportBuilder.append("</HTML>");
		File file = new File(path + buildName);
        file.mkdirs();
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path + buildName + File.separator + "testLinkResult.html")));
		bwr.write(reportBuilder.toString());
		bwr.flush();
		bwr.close();
	}

	private static Map<Integer, TestLinkResultBO> prepareTestLinkResults(
			Properties config, String buildName) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String projectName = config.getProperty("TEST_LINK_PROJECT_NAME");
		String testPlanName = config.getProperty("TEST_LINK_TEST_PLAN");
		Map<Integer, TestLinkResultBO> testLinkResultsMap = new LinkedHashMap<Integer, TestLinkResultBO>();
		Map<Integer, String> testSuiteIdNameMap = new LinkedHashMap<Integer, String>();
		StringBuilder allTestCaseSql = new StringBuilder();
		allTestCaseSql.append(" SELECT testProject.name projectName,testPlan.name testPlan,tcv.tcversion_id testId,tvers.summary summary,testSuite.name innerTestSuiteName,testSuite.parent_id innerTestSuiteParentId,");
		allTestCaseSql.append("(CASE   WHEN exec.status = 'p' THEN 'Passed' WHEN exec.status = 'f' THEN 'Failed'   ELSE 'Not Run' END)  status, ");
		allTestCaseSql.append(" exec.execution_ts execTime");
		allTestCaseSql.append(" FROM testplan_tcversions tcv");
		allTestCaseSql.append(" INNER JOIN tcversions tvers ON tcv.tcversion_id = tvers.id");
		allTestCaseSql.append(" LEFT JOIN executions exec  ON     exec.tcversion_id = tcv.tcversion_id  AND exec.build_id = (SELECT build.id FROM builds build WHERE build.name = ?)");
		allTestCaseSql.append(" INNER JOIN testplans tpl ON tcv.testplan_id = tpl.id");
		allTestCaseSql.append(" INNER JOIN testprojects tpr ON tpl.testproject_id = tpr.id");
		allTestCaseSql.append(" INNER JOIN nodes_hierarchy testPlan ON testPlan.id = tpl.id");
		allTestCaseSql.append(" INNER JOIN nodes_hierarchy testProject on testProject.id= tpr.id");
		allTestCaseSql.append(" INNER JOIN nodes_hierarchy testRevision ON tvers.id = testRevision.id");
		allTestCaseSql.append(" INNER JOIN nodes_hierarchy testCase   ON testRevision.parent_id = testCase.id");
		allTestCaseSql.append(" INNER JOIN nodes_hierarchy testSuite   ON testCase.parent_id = testSuite.id");
		allTestCaseSql.append(" WHERE     testProject.name = ? ");
		allTestCaseSql.append(" AND testPlan.name =  ? ");
		allTestCaseSql.append(" order by innerTestSuiteParentId asc, testId asc");
		
		try {
			dbConnection = getDBConnection(config);
			preparedStatement = dbConnection.prepareStatement(allTestCaseSql.toString());
			preparedStatement.setString(1, buildName);
			preparedStatement.setString(2, projectName);
			preparedStatement.setString(3, testPlanName);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				TestLinkResultBO linkResultBO = new TestLinkResultBO();
				
				String projectNameRst = rs.getString("projectName");
				String testPlan = rs.getString("testPlan");
				Integer testCaseId = rs.getInt("testId");
				String summary = rs.getString("summary");
				String status = rs.getString("status");
				String innerTestSuiteName = rs.getString("innerTestSuiteName");
				Integer innerTestSuiteParentId = rs.getInt("innerTestSuiteParentId");
				Timestamp ts = rs.getTimestamp("execTime");
				String testSuite = "";
				if (testSuiteIdNameMap.containsKey(innerTestSuiteParentId)) {
					testSuite = testSuiteIdNameMap.get(innerTestSuiteParentId);
				} else {
					List<String> testSuiteNames = new ArrayList<String>();
					testSuiteNames.add(innerTestSuiteName);
					getRecursiveTestSuiteName(innerTestSuiteParentId, innerTestSuiteName, config, testSuiteNames);
					Collections.reverse(testSuiteNames);
					for (String s : testSuiteNames)
					{
						testSuite += s + "/";
					}
					testSuiteIdNameMap.put(innerTestSuiteParentId, testSuite);
				}
				linkResultBO.setProjectName(projectNameRst);
				linkResultBO.setTestPlanName(testPlan);
				linkResultBO.setTestCaseId(testCaseId);
				linkResultBO.setTestCaseSummary(summary);
				linkResultBO.setTestSuite(testSuite);
				linkResultBO.setExecutionResult(status);
				linkResultBO.setAutomationBuild(buildName);
				if(null != ts) {
					linkResultBO.setExecutionDate(new java.util.Date(ts.getTime()));
				}
				testLinkResultsMap.put(testCaseId, linkResultBO);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return testLinkResultsMap;
	}

	private static void getRecursiveTestSuiteName(Integer innerTestSuiteParentId,
			String innerTestSuiteName, Properties config, List<String> testSuiteNames) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConnection = getDBConnection(config);
			StringBuilder hierarchySql = new StringBuilder();
			hierarchySql.append("select * from nodes_hierarchy where id = ? and node_type_id = ?");
			
			preparedStatement = dbConnection.prepareStatement(hierarchySql .toString());
			preparedStatement.setInt(1, innerTestSuiteParentId);
			preparedStatement.setInt(2, 2);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String testSuiteName =  rs.getString("name");
				Integer parentTestCaseId = rs.getInt("parent_id");
				testSuiteNames.add(testSuiteName);
				getRecursiveTestSuiteName(parentTestCaseId, testSuiteName, config, testSuiteNames);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		
	}

	private static Connection getDBConnection(Properties config) {
		Connection dbConnection = null;
		String DB_DRIVER = config.getProperty("testlink.db.DB_DRIVER");
		String DB_CONNECTION = config.getProperty("testlink.db.DB_URL")
				+ config.getProperty("testlink.db.DB_NAME");
		String DB_USER = config.getProperty("testlink.db.DB_USERNAME");
		String DB_PASSWORD = config.getProperty("testlink.db.DB_PASSWORD");
		try {
			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;
	}

}
