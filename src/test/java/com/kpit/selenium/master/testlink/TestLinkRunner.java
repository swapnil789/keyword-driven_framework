package com.kpit.selenium.master.testlink;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

/**
 * @author milindw3
 */
public class TestLinkRunner {
	public static String DEVKEY = "4639d83c3cbe33bca858c16fad53e978";
	public static String URL = "http://localhost/testlink/index.php";

	public static void reportResult(String TestProject, String TestPlan,
			String Testcase, String Build, String Notes, String Result)
			throws TestLinkAPIException {
		TestLinkAPIClient api = new TestLinkAPIClient(DEVKEY, URL);
		api.reportTestCaseResult(TestProject, TestPlan, Testcase, Build, Notes,
				Result);
	}

	@Test
	public void Test1() throws Exception {
		TestLinkRunner a = new TestLinkRunner();
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, 600);
		String testProject = "TestProject";
		String testPlan = "TP1_TestProduct";
		String testCase = "TC_001";
		String build = "TP_Build1";
		String notes = null;
		String result = null;
		try {
			driver.manage().window().maximize();
			driver.get("http:\\gmail.com");
			driver.findElement(By.id("Email")).sendKeys(
					"warademilind@gmail.com");
			driver.findElement(By.id("Passwd")).sendKeys("milind#$47");
			driver.findElement(By.id("signIn")).click();
			result = TestLinkAPIResults.TEST_PASSED;
			notes = "Executed successfully";
		} catch (Exception e) {
			result = TestLinkAPIResults.TEST_FAILED;
			notes = "Execution failed";
		} finally {

			a.reportResult(testProject, testPlan, testCase, build, notes,
					result);
			driver.quit();
		}
	}
}
