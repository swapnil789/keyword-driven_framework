package com.kpit.selenium.master.testlink;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.kpit.selenium.suite.util.Constants;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;

/**
 * @author milindw3
 */
public class TestLinkBuildCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.setProperty("jsse.enableSNIExtension", "false");
			Properties CONFIG = new Properties();
			String targetEnvironment = System.getProperty("targetEnvironment");
			System.out.println("targetEnvironment >> " + targetEnvironment);

			String targetBuildName = System.getProperty("targetBuildName");
			System.out.println("targetBuildName >> " + targetBuildName);

			FileInputStream fs = null;
			if (null != targetEnvironment && !"".equals(targetEnvironment)) {
				String envtVariable = System.getProperty("user.dir")
						+ File.separatorChar + targetEnvironment
						+ ".properties";
				System.out.println("envtVariable >> " + envtVariable);
				fs = new FileInputStream(envtVariable);
			} else {
				fs = new FileInputStream(System.getProperty("user.dir")
						+ File.separatorChar + "config" + File.separatorChar
						+ "config.properties");
			}

			CONFIG.load(fs);

			String createNewBuild = CONFIG
					.getProperty("TEST_LINK_CREATE_BUILD");
			if (null != createNewBuild || !"".equals(createNewBuild)) {
				if ("Y".equals(createNewBuild)) {
					createNewBuild(CONFIG, targetBuildName);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String createNewBuild(Properties CONFIG, String targetBuildName)
			throws TestLinkAPIException {
		String test_link_dev_key = CONFIG
				.getProperty(com.kpit.selenium.suite.util.Constants.TESTLINK_DEV_KEY);
		String dev_key = CONFIG
				.getProperty(com.kpit.selenium.suite.util.Constants.TESTLINK_API_URL);
		TestLinkAPIClient api = new TestLinkAPIClient(
				test_link_dev_key, dev_key);
		String projectName = CONFIG.getProperty(Constants.TEST_LINK_PROJECT_NAME);
		
		String planName = CONFIG.getProperty(Constants.TEST_LINK_TEST_PLAN);

		String buildSuffix = CONFIG.getProperty("TEST_LINK_BUILD_SUFFIX");
		if (null == targetBuildName) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			targetBuildName = sdf.format(date) + buildSuffix; 
		}
		api.createBuild(projectName, planName, targetBuildName, targetBuildName);
		return targetBuildName;
	}

}
