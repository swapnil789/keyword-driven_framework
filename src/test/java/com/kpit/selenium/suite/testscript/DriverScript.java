package com.kpit.selenium.suite.testscript;



import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.testng.AssertJUnit.*;

import com.kpit.selenium.suite.datatable.Xls_Reader;
import com.kpit.selenium.suite.reports.ReportUtil;
import com.kpit.selenium.suite.thread.SingleTestCaseThread;
import com.kpit.selenium.suite.thread.TestLinkStatusThread;
import com.kpit.selenium.suite.util.Constants;
import com.kpit.selenium.suite.util.SendMail;
import com.kpit.selenium.suite.util.TestUtil;

public class DriverScript {
    private static final Logger LOG = Logger.getLogger(DriverScript.class);

    public static Properties           CONFIG;
    public static Properties           OR;
    public static Properties           APPTEXT;
    public static Xls_Reader           controller;

    public static String               currentTest;
    public static String               keyword;
    public static WebDriver            wbdv            = null;
    public static WebDriver driver          = null;
    public static String               object;
//    private static String              screenRecorder;
    public static String               currentTSID;
    public static String               stepDescription;
    public static String               proceedOnFail;
    public static String               testStatus;
    public static String               URLEXE;
    // temp
    public static String               data_column_name;
    public static String               object_type;
    public static int                  testRepeat;
    public static Logger               APPICATION_LOGS = Logger.getLogger("DriverLogger");
    public static String               dateStr         = "";
    public static Map<String, String>           map = new LinkedHashMap<String, String>();
    private static Robot robot = null;
    public static Integer               maxFailSkipCount;
    public static  String screenshotPath = "";
    public static  String projectRoot = "D:\\AUTOMATION\\selenium-base";
    public static String IMAGES_ROOT = "";
  
    
    public ExecutorService      executor              = Executors.newCachedThreadPool();
    
    public static synchronized Robot getRobot() throws AWTException {
    	if (robot == null) {
    		robot = new Robot();
    	}
    	return robot;
    }

    @BeforeClass
    public static void startTesting() throws IOException {

        // load the property fIles
        // load the config prop
        CONFIG = new Properties();
        FileInputStream fs = null;
        String targetEnvironment = System.getProperty("targetEnvironment");
        if(null != targetEnvironment && !"".equals(targetEnvironment)){
            String envtVariable = System.getProperty("user.dir") + File.separatorChar + targetEnvironment + ".properties";
            System.out.println("envtVariable >> " + envtVariable);
			fs = new FileInputStream(envtVariable);
        } else {
        	fs = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "config"
                    + File.separatorChar + "config.properties");
        }
        CONFIG.load(fs);
        screenshotPath = CONFIG.getProperty("screenshotPath");
        if (null != CONFIG.getProperty("ProjectRoot")) {
			projectRoot = CONFIG.getProperty("ProjectRoot");
		}
		Date date = new Date();
        String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-aaa";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        dateStr = sdf.format(date);
        IMAGES_ROOT = projectRoot + File.separator + File.separator + "data" + File.separator + "image" + File.separator;
        File file = new File(screenshotPath + dateStr);
        file.mkdirs();
        file = new File(file, "index.html");
        ReportUtil.startTesting(file.getAbsolutePath(), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), CONFIG.getProperty("EnvironmentName"),
        		CONFIG.getProperty("ReleaseName"));
    }

	private String testProject;

	private String testPlan;
	
	private String jiraProject;

	private String build;

	private HSSFWorkbook finalReportWorkBook;

	private FileInputStream finalReportFile;

    @Before
    public void initialize() throws IOException, URISyntaxException {

        String functionality = System.getProperty(Constants.FUNCTIONALITY_NAME);
        String projectHome = CONFIG.getProperty(Constants.PROJECT_HOME);
        

        String filesPath = System.getProperty("user.dir") + File.separatorChar + "config" + File.separatorChar
                        + projectHome + File.separatorChar + functionality + File.separatorChar;
        
        testProject = CONFIG.getProperty(Constants.TEST_LINK_PROJECT_NAME);
        
        testPlan = CONFIG.getProperty(Constants.TEST_LINK_TEST_PLAN);
        String targetBuildName = System.getProperty("targetBuildName");
        build = CONFIG.getProperty(Constants.TEST_LINK_BUILD);
        if (null != targetBuildName) {
        	build = targetBuildName;
		}

        // LOAD ORfunctionality
        FileInputStream fs = null;
        OR = new Properties();
        fs = new FileInputStream(filesPath + "OR.properties");
        OR.load(fs);
        // app text prop load
        APPTEXT = new Properties();
        fs = new FileInputStream(filesPath + "app_text.properties");
        APPTEXT.load(fs);
        // intialize datatable
        controller = new Xls_Reader(filesPath + "Controller.xls");
        
        //Delete login data file
        String folder1= (new SimpleDateFormat("dd-MMM-yyyy")).format(new Date());
        File loginFolder = new File(projectRoot + File.separator + "data" + File.separator + "ReadWriteData" + File.separator + folder1);
        
        File loginFile = new File(loginFolder + File.separator + "Read_Write_Details.txt");
        if(loginFile.exists()){
            loginFile.delete();
        } 

		
        File offlineFile = new File(projectRoot + File.separator + "data" + File.separator + "OfflineTestCases" + File.separator + "OfflineTestCases.txt");
        offlineFile.getParentFile().mkdirs();
        offlineFile.createNewFile();
        
        String reportXlsFile = System.getProperty("FINAL_REPORT_PATH");
        finalReportFile = new FileInputStream(reportXlsFile);
        finalReportWorkBook = new HSSFWorkbook(finalReportFile);
        finalReportWorkBook.createSheet(functionality);
    }

    @Test
    public void testApp() throws IOException {
        String startTime = null;
        
        ReportUtil.startSuite("Suite 1");
        int rownum = 0;
        for (int tcid = 2; tcid <= controller.getRowCount("Suite1"); tcid++) {
            currentTest = controller.getCellData("Suite1", "TCID", tcid);
            if (null == currentTest || "".equals(currentTest)) {
				continue;
			}
            List<String> testLinkStatusList = new ArrayList<String>();
            String testLinkUpdate = "N";
            String jiraUpdate = "N";
            
            System.out.println("Executing TestCase - " + controller.getCellData("Suite1", "Description", tcid) + " - Start Time - " + new Date());
            HSSFSheet hssfSheet = finalReportWorkBook.getSheet(System.getProperty(Constants.FUNCTIONALITY_NAME));
			HSSFRow row = hssfSheet.createRow(rownum++);
			int totalStepsCount = 0;
			int stepsPassedCount = 0;
			int stepsFailedCount = 0;
			int testCaseStepsFailedCount = 0;
            // initilize start time of test
            if (controller.getCellData("Suite1", "Runmode", tcid).equals("Y")) {
            	//check test link status update 
            	if(controller.getCellData("Suite1", "TestLink_Status_Update", tcid).equals("Y")){
            		testLinkUpdate = "Y";
            	}
            	//check test link status update 
            	if(controller.getCellData("Suite1", "JIRA_Status_Update", tcid).equals("Y")){
            		jiraUpdate = "Y";
        	        
            	}
                // execute the keywords
                // loop again - rows in test data
                int totalSets = Integer.parseInt(CONFIG.getProperty(Constants.TEST_REPEAT_COUNT));
                if (null != CONFIG.getProperty(Constants.MAX_FAILED_SKIP_COUNT)) {
					maxFailSkipCount = Integer.parseInt(CONFIG
							.getProperty(Constants.MAX_FAILED_SKIP_COUNT));
				}
				//int totalSets = controller.getRowCount(currentTest + "1");
                // holds total rows in test data sheet. IF sheet does not exist then 2 by default
                if (totalSets <= 1) {
                    totalSets = 2; // run atleast once
                }

                for (testRepeat = 2; testRepeat <= totalSets; testRepeat++) {
                    startTime = TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");

                    APPICATION_LOGS.debug("Executing the test " + currentTest);
                    // implement keyword . Reflection API
                    System.out.println(controller.getRowCount(currentTest));
                    totalStepsCount = controller.getRowCount(currentTest);
                    int lastFailedIndex = 0;
                    int failedTestsCount = 0;
                    String projectHome = CONFIG.getProperty(Constants.PROJECT_HOME);
                    String buildName = CONFIG.getProperty("TEST_LINK_BUILD");
                    String jiraFuncXML = System.getProperty("JIRA_FUNC_XML");
                    File file = new File(jiraFuncXML);
                    String jiraTestCase = "";
                    String path = CONFIG.getProperty("screenshotPath");
                    String testStepReport = prepareTestStepReport(buildName, path, projectHome, currentTest);
                    FileInputStream testStepRtSt = new FileInputStream(testStepReport);
                    HSSFWorkbook testStepWkBk = new HSSFWorkbook(testStepRtSt);
                	HSSFSheet ctHssfSheet = testStepWkBk.createSheet(currentTest);
                    for (int tsid = 2; tsid <= controller.getRowCount(currentTest); tsid++) {
                        // values from xls
                    	HSSFRow ctRow = ctHssfSheet.createRow(tsid);
                        keyword = controller.getCellData(currentTest, "Keyword", tsid);
                        object = controller.getCellData(currentTest, "Object", tsid);
                        currentTSID = controller.getCellData(currentTest, "TSID", tsid);
                        HSSFCell cell = ctRow.createCell(1);
                		cell.setCellValue(currentTSID);
                        stepDescription = controller.getCellData(currentTest, "Description", tsid);
                        HSSFCell cell2 = ctRow.createCell(2);
                		cell2.setCellValue(stepDescription);
                        proceedOnFail = controller.getCellData(currentTest, "ProceedOnFail", tsid);
                        data_column_name = controller.getCellData(currentTest, "Data_Column_Name", tsid);
                        object_type = controller.getCellData(currentTest, "Object_Type", tsid);
                        String testLink_testCase = controller.getCellData(currentTest, "TestLink_TestCase", tsid);
                        jiraTestCase =  controller.getCellData(currentTest, "Jira_TestCase", tsid);
                        System.out.println("Executing TestStep - " + controller.getCellData("Suite1", "Description", tcid) + " Step - "+ currentTSID + " - Start Time - " + new Date());
                        if (null == keyword || "".equals(keyword)) {
                        	totalStepsCount = totalStepsCount - 1;
							continue;
						}
                        // APPICATION_LOGS.debug(keyword);
                        String result = "";
                        try {
                            LOG.debug(Arrays.toString(new String[] {"CurrentTest Details : ", currentTest, "" + tsid, currentTSID}));
                            //Method method = Keywords.class.getMethod(keyword);
                            //result = (String) method.invoke(method);
                            result = invokeKeywordAsJob();
                            LOG.debug("***Result of execution -- " + result);
                            // take screenshot - every keyword
                            String fileName = "Suite1_TC" + (tcid - 1) + "_TS" + tsid + "_" + keyword + testRepeat
                                            + ".jpg";
                            TestUtil.takeScreenShot(CONFIG.getProperty("screenshotPath") + dateStr + File.separator + fileName);
                            ReportUtil.addKeyword(stepDescription, keyword, result, fileName);
                            HSSFCell cell3 = ctRow.createCell(3);
                    		cell3.setCellValue(result);

                            if (result.startsWith("Fail")) {
                            	stepsFailedCount++;
                            	testCaseStepsFailedCount++;
                            	if (0 == failedTestsCount) {
                            		LOG.debug("***failedTestsCount -- " + failedTestsCount);
                            		failedTestsCount++;
                            		lastFailedIndex = tsid;
                            		LOG.debug("***failedTestsCount  after increment in if -- " + failedTestsCount + "***lastFailedIndex -- " + lastFailedIndex);
								} else {
									LOG.debug("***failedTestsCount -- " + failedTestsCount + "***lastFailedIndex -- " + lastFailedIndex);
									if(lastFailedIndex != 0 && lastFailedIndex + 1 == tsid ) {
										failedTestsCount++;
										lastFailedIndex = tsid;
										LOG.debug("***failedTestsCount after increment in else -- " + failedTestsCount + "***lastFailedIndex -- " + lastFailedIndex);
									}
								}
                            	
                                testStatus = result;
                                if (null != maxFailSkipCount && failedTestsCount >= maxFailSkipCount.intValue()) {
                                	failedTestsCount = 0;
                                	String url = driver.getCurrentUrl();
                                	if(null != url && url.contains("kpit")){
                                		wbdv.close();
                                	}
                                	Thread.sleep(9000);
                                	break;
								}
                                if (proceedOnFail.equalsIgnoreCase("N")) {
                                    break;
                                }
                                

                            } else {
                            	lastFailedIndex = 0;
                            	failedTestsCount = 0;
                            	stepsPassedCount++;
                            }

                        } catch (Throwable t) {
                            APPICATION_LOGS.debug("Error came");
                            if (proceedOnFail.equalsIgnoreCase("N")) {
                                break;
                            }
                        } finally {
                        	if(("Y").equals(testLinkUpdate)){
                        		if(!("".equals(testLink_testCase)) && testCaseStepsFailedCount > 0) {
                        			result = "Fail";
                        			testCaseStepsFailedCount = 0;
                        		}
                        		updateTestLinkStatus(result, testLink_testCase, testLinkStatusList, testProject, testPlan, build);
                        	}
                    		if(("Y").equals(jiraUpdate)){
                        		if (!("".equals(jiraTestCase))) {
									ReportUtil.updateJiraStatus(file, result, startTime, TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
											jiraTestCase);
								} 
                        	}

                        }
                        System.out.println("Finishing TestStep - " + controller.getCellData("Suite1", "Description", tcid) + " Step - "+ currentTSID + " - End Time - " + new Date());
                    }// keywords
                    //mark not executed tests as failed for test Link
//                    if(("Y").equals(testLinkUpdate)){
//                        for (int tsid = 2; tsid <= controller.getRowCount(currentTest); tsid++) {
//                        	String testLink_testCase = controller.getCellData(currentTest, "TestLink_TestCase", tsid);
//                        	updateTestLinkStatus("fail", testLink_testCase, testLinkStatusList, testProject, testPlan, build);
//                        }
//                    }
                     // report pass or fail
                    if (testStatus == null) {
                        testStatus = "Pass";
                    }
                    APPICATION_LOGS.debug("***********************************" + currentTest + " --- " + testStatus);
                    String endTime = TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
                    ReportUtil.addTestCase(controller.getCellData("Suite1", "Description", tcid), startTime,
                    		endTime , testStatus);
                    
                    
                    writeFinalReportCell(tcid, row, totalStepsCount, stepsPassedCount, stepsFailedCount, startTime);
                    
                    //close test report
                    testStepRtSt.close();
        			FileOutputStream outstepFile = new FileOutputStream(new File(
        					testStepReport));
        			testStepWkBk.write(outstepFile);
        			outstepFile.close();

//                    test.log(LogStatus.PASS, "Navigated to the specified URL");
                }// test data

            } else {
                APPICATION_LOGS.debug("Skipping the test " + currentTest);
                testStatus = "Skip";
                // report skipped
                APPICATION_LOGS.debug("***********************************" + currentTest + " --- " + testStatus);
                ReportUtil.addTestCase(controller.getCellData("Suite1", "Description", tcid),
                                TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
                                testStatus);
                writeFinalReportCell(tcid, row, totalStepsCount, stepsPassedCount, stepsFailedCount, startTime);
//                test.log(LogStatus.FAIL, "Test Failed");
            }

            testStatus = null;
            System.out.println("Finishing TestCase - " + controller.getCellData("Suite1", "Description", tcid) + " - End Time - " + new Date());
            
        }
        ReportUtil.endSuite();
        
    }


	/**
	 * @param tcid
	 * @param row
	 * @param stepsFailedCount 
	 * @param stepsPassedCount 
	 * @param totalStepsCount 
	 * @param startTime 
	 */
	private void writeFinalReportCell(int tcid, HSSFRow row, int totalStepsCount, int stepsPassedCount, int stepsFailedCount, String startTime) {
		HSSFCell cell = row.createCell(1);
		cell.setCellValue(currentTest);
		HSSFCell cell2 = row.createCell(2);
		cell2.setCellValue(controller.getCellData("Suite1", "Description", tcid));
		HSSFCell cell3 = row.createCell(3);
		if (null != startTime) {
			cell3.setCellValue(startTime);	
		} else {
			cell3.setCellValue(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
		}
		HSSFCell cell4 = row.createCell(4);
		cell4.setCellValue(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
		HSSFCell cell5 = row.createCell(5);
		cell5.setCellValue(testStatus);
		HSSFCell cell6 = row.createCell(6);
		String currentSuiteName = "Suite 1".replaceAll(" ", "_");;
		cell6.setCellValue(dateStr + "/" + currentSuiteName  + "_TC" + (tcid-1) + "_");
		HSSFCell cell7 = row.createCell(7);
		if (totalStepsCount > 0) {
			cell7.setCellValue(totalStepsCount - 1);
		} else {
			cell7.setCellValue(totalStepsCount);
		}
		HSSFCell cell8 = row.createCell(8);
		cell8.setCellValue(stepsPassedCount);
		HSSFCell cell9 = row.createCell(9);
		cell9.setCellValue(stepsFailedCount);
	}

	private String invokeKeywordAsJob() {
		Future<String> control = Executors.newSingleThreadExecutor().submit(
				new SingleTestCaseThread(keyword));
		String result = "Fail";
		String timeout = "";
		try {
			timeout = CONFIG.getProperty("SINGLE_TEST_CASE_MAX_TIMEOUT", "10");
			result = control.get(Integer.parseInt(timeout), TimeUnit.MINUTES);
		} catch (TimeoutException ex) {
			control.cancel(true);
			LOG.debug("*** Execution of keyword - " + keyword + " from test case " + currentTest + " and TCID " + currentTSID + " took more than "+ timeout + " mins");
			System.out.println("Execution of keyword - " + keyword + " from test case " + currentTest + " and TCID " + currentTSID + " took more than "+ timeout + " mins");
		} catch (InterruptedException ex) {

		} catch (ExecutionException ex) {

		}
		return result;
	}

	private void updateTestLinkStatus(String result, String testLink_testCase,
			List<String> testLinkStatusList, String testProject,
			String testPlan, String build) {
		executor.execute(new TestLinkStatusThread(result, testLink_testCase,
				testLinkStatusList, testProject, testPlan, build, CONFIG));
	}

	/**
	 * 
	 */
	@AfterClass
    public static void endScript() {

        ReportUtil.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
        String projectHome = CONFIG.getProperty(Constants.PROJECT_HOME);
        String sendEmailFlag = CONFIG.getProperty(Constants.SEND_EMAIL);
        String functionality = System.getProperty(Constants.FUNCTIONALITY_NAME);
        //String emailRecipient = CONFIG.getProperty("INTERNAL_EMAIL_RECIPIENT");
        
        //Code for reading recipient email list
        String tomcatBaseURL = CONFIG.getProperty("TOMCAT_BASE_URL");
        Xls_Reader reader = new Xls_Reader(System.getProperty("user.dir") + File.separatorChar + "config"
                            + File.separatorChar + projectHome + File.separatorChar + "Sequential_Test_Config.xls");
        int rowCount = reader.getRowCount(Constants.SHEET_NAME_SEQUENCE);
		if (null != sendEmailFlag && "Y".equals(sendEmailFlag)) {
			for (int i = 2; i <= rowCount; i++) {
				if(reader.getCellData(Constants.SHEET_NAME_SEQUENCE, Constants.EXECUTE_TEST,
						i).equals("Y")){
					String email_recipient = reader.getCellData(Constants.SHEET_NAME_SEQUENCE, Constants.MAIL_RECIPIENTS,
						i);
					List<String> split_recepient = new ArrayList<String>();
					split_recepient = Arrays.asList(email_recipient.split("\\s*,\\s*"));
					SendMail.sendSeleniumTestSuiteEmail(dateStr, projectHome, functionality, screenshotPath, split_recepient, tomcatBaseURL, "T1");
				}
			}
		}
       	 
        /*if (null != sendEmailFlag && "Y".equals(sendEmailFlag)) {
        	
        	List<String> recipients = new ArrayList<String>();
			if (null != emailRecipient && !"".equals(emailRecipient)) {
				recipients = Arrays.asList(emailRecipient.split("\\s*,\\s*"));
			} else {
				recipients.add("milind.warade@kpit.com");
				recipients.add("amol.kapade@kpit.com");
			}
			SendMail.sendSeleniumTestSuiteEmail(dateStr, projectHome, functionality, screenshotPath, recipients, tomcatBaseURL, "T1");
		}*/
//        report.endTest(test);
//        report.flush();
    }

	@After
	public void endMethod() {
        String reportXlsFile = System.getProperty("FINAL_REPORT_PATH");
        try {
        	finalReportFile.close();
			FileOutputStream outFile = new FileOutputStream(new File(
					reportXlsFile));
			finalReportWorkBook.write(outFile);
			outFile.close();
		} catch (Exception e) {
			System.err.println("Writting to final report failed " + e.getMessage());
		}
	}
    protected static String getResolvedObject() {
        return getResolvedValue(object, null);
    }

    /**
     * @param string
     */
    protected static String getResolvedValue(String value, String defaultValue) {
        String valueKey = value;
        int startIndex = value.indexOf("${");
        try {
            while (startIndex >= 0) {
                int endIndex = valueKey.indexOf("}", startIndex);
                String propRef = valueKey.substring(startIndex + 2, endIndex);
                String propVal = map.get(propRef);
                valueKey = valueKey.replace("${" + propRef + "}", propVal);
                defaultValue = valueKey;
                startIndex = valueKey.indexOf("${");
            }
        } catch (Exception e) {
            valueKey = value;
        }
        return OR.getProperty(valueKey, defaultValue);
    }
    protected static void putToORMap(String key, String val){
    	OR.put(key, val);
    }
    
	private static String prepareTestStepReport(String buildName,
			String path, String projectHome, String functionality) throws IOException {
		File folder = new File(path + buildName);
		folder.mkdirs();
		HSSFWorkbook workbook = new HSSFWorkbook();
		String pathname = folder + File.separator + projectHome + "_"+ functionality + "_result.xls";
		FileOutputStream out = 
			new FileOutputStream(new File(pathname));
		workbook.write(out);
		out.close();
		return pathname;
	}    
}
