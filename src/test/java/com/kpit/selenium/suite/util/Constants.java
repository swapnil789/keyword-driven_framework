/**
 * 
 */
package com.kpit.selenium.suite.util;

import org.json.JSONObject;


/**
 * @author milindw3
 */
public class Constants {

    public static final String TEST_BROWSER        = "Test_Browser";
 
    public static final String FUNCTIONALITY_NAME  = "Functionality";

    public static final String PROJECT_HOME        = "ProjectHome";

    public static final String SHEET_NAME_SEQUENCE = "Sequence";

    public static final String EXECUTE_TEST        = "Execute";

    public static final String BOOLEAN_YES         = "Y";

    public static final String BOOLEAN_NO          = "N";
    
    public static final String BROWSER_FIREFOX          = "Firefox";

    public static final String BROWSER_IE          = "IE";

    public static final String BROWSER_CHROME          = "Chrome";
    
    public static final String Operating_System          = "Android_Chrome";
    
    public static final String BROWSER_SAFARI = "Safari";
    
    public static final String TEST_REPEAT_COUNT        = "TestRepeatCount";
    
    public static final String ANDROID_DRIVER        = "Android";
    
    public static final String EXE_DRIVER        = "Exe";
    
    public static final String LOGIN_ID = "LoginId";
    
    public static final String LOGIN_PWD = "LoginPwd";
    
    public static final String DB_URL = "DB_URL";
    public static final String DB_NAME = "DB_NAME";
    public static final String DB_DRIVER = "DB_DRIVER";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";

    public static final String FILE_TARGET_NAME = "targetFileName";
    public static final String FILE_SOURCE_NAME = "sourceFileName";
    public static final String FILE_DATA = "data";
    public static final String FILE_DATA_BASE64 = "base64";
	public static final String TESTLINK_DEV_KEY = "testlink_devKey";
	public static final String TESTLINK_API_URL = "testlink_api_url";
	public static final String TEST_LINK_BUILD = "TEST_LINK_BUILD";
	public static final String TEST_LINK_TEST_PLAN = "TEST_LINK_TEST_PLAN";
	public static final String TEST_LINK_PROJECT_NAME = "TEST_LINK_PROJECT_NAME";
	public static final String SEND_EMAIL = "SEND_EMAIL";
	public static final String MAX_FAILED_SKIP_COUNT = "MAX_FAILED_SKIP_COUNT";
	public static final String EMAIL_RECIPIENT = "EMAIL_RECIPIENT";
	public static final String MAIL_RECIPIENTS = "Email_Recipients";

	public static final String JIRA_PROJECT_NAME = "Project_Key";
	public static class PRODKEY_Constants {

		public static final String KEY = "KEY";
		public static final String TYPE = "TYPE";
		public static final String PREFIX = "PREFIX";
		public static final String SUFFIX = "SUFFIX";
		public static final String RS_MAP = "RS_MAP";
		public static final String IDX = "IDX";
		
	}
    
    public static class SQL_Constants {
        public static final String SQL = "SQL";
        public static final String RQ_MAP = "RQ_MAP";
        public static final String RS_MAP = "RS_MAP";
        public static final String DEFAULT_OBJECT = new JSONObject().toString();
		public static final String PRODUCT_ID = "PRODUCT_ID";
    }
    
    public static class FIELD_Constants {
        public static final String FIELD_IDS = "FIELD_IDS";
        public static final String ACTIVITY = "ACTIVITY";
        public static final String DEFAULT_OBJECT = new JSONObject().toString();
    }
}
