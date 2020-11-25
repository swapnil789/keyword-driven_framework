
package com.kpit.selenium.suite.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

/**
 * @author milindw3
 */
public class TestLinkStatusThread implements Runnable {
	private String result;
	private String testLink_testCase;
	private List<String> testLinkStatusList;
	private String testProject;
	private String testPlan;
	private String build;
	private Properties properties;
	public static Logger APPICATION_LOGS = Logger.getLogger("DriverLogger");

	public TestLinkStatusThread(String result, String testLink_testCase,
			List<String> testLinkStatusList, String testProject,
			String testPlan, String build, Properties properties) {
		this.result = result;
		this.testLink_testCase = testLink_testCase;
		this.testLinkStatusList = testLinkStatusList;
		this.testProject = testProject;
		this.testPlan = testPlan;
		this.build = build;
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if (null != testLink_testCase && !"".equals(testLink_testCase)
					&& !testLinkStatusList.contains(testLink_testCase)) {
				String test_link_result = TestLinkAPIResults.TEST_FAILED;
				String notes = "Execution failed";
				String test_link_dev_key = properties
						.getProperty(com.kpit.selenium.suite.util.Constants.TESTLINK_DEV_KEY);
				String dev_key = properties
						.getProperty(com.kpit.selenium.suite.util.Constants.TESTLINK_API_URL);
				TestLinkAPIClient api = new TestLinkAPIClient(
						test_link_dev_key, dev_key);
				if ("Pass".equals(result)) {
					notes = "Executed successfully";
					test_link_result = TestLinkAPIResults.TEST_PASSED;
				}

				try {
					if (isOnline(properties)) {
						System.out.println("build used to update testlink -> " + build);
						TestLinkAPIResults reportTestCaseResult = api.reportTestCaseResult(testProject, testPlan,
								testLink_testCase, build, notes,
								test_link_result);
						testLinkStatusList.add(testLink_testCase);
					} else {
						offlineUpdates(testProject, testPlan,
								testLink_testCase, build, notes,
								test_link_result);
					}
				} catch (TestLinkAPIException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			APPICATION_LOGS.debug("Error in Test Link Sttaus Update >> "
					+ e.getLocalizedMessage());
		}
	}

	private void offlineUpdates(String testProject, String testPlan,
			String testLink_testCase, String build, String notes,
			String test_link_result) throws IOException {
		File file = new File(System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar + "OfflineTestCases");
		FileWriter w = new FileWriter(file + "\\" + "OfflineTestCases.txt", true);
        BufferedWriter out = new BufferedWriter(w);
        out.newLine();
        out.write(result + "||" + testProject + "||" + testPlan + "||" + testLink_testCase + "||" + build + "||" + notes + "||" + test_link_result);
        out.close();
	}

	private boolean isOnline(Properties properties) {
		boolean connectivity = false;
		try {
			String serverUrl = properties.getProperty("testSiteURL1");
			URL url = new URL(serverUrl);
			URLConnection conn = url.openConnection();
			conn.connect();
			connectivity = true;
		} catch (Exception e) {
			connectivity = false;
			System.out.println("Internet Not Working");

		}
		return connectivity;
	}

}
