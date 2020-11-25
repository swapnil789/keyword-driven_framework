package com.kpit.selenium.suite.testscript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import com.kpit.selenium.suite.util.Constants;

import junit.framework.Assert;

//String date= (new SimpleDateFormat("hhmmyyssss")).format(new Date());

public class Keywords extends DriverScript {

	public static String xml_final = "";
	private static String IMAGES_ROOT = System.getProperty("user.dir") + File.separatorChar + "data"
			+ File.separatorChar + "IMAGES_ROOT" + File.separatorChar;

	public static String getModifiedData(String data) {
		return data;
	}

	// navigate
	@SuppressWarnings("deprecation")
	public static String navigate() throws MalformedURLException {
		APPICATION_LOGS.debug("Executing Navigate");
		String testBrowser = System.getProperty(Constants.TEST_BROWSER);
		boolean isWbdvClosed = false;
		if (null != wbdv) {
			try {
				wbdv.getCurrentUrl();
				driver.switchTo().window(wbdv.getWindowHandle());
			} catch (Exception e) {
				isWbdvClosed = true;
			}
		}
		if (wbdv == null || isWbdvClosed) {
			if (testBrowser == null || ("").equals(testBrowser)) {
				testBrowser = CONFIG.getProperty("testBrowser");
			}
			if (Constants.BROWSER_FIREFOX.equals(testBrowser)) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + File.separatorChar
						+ "data" + File.separatorChar + "geckodriver" + File.separatorChar + "geckodriver.exe");
				// WebDriver driver = new FirefoxDriver();
				FirefoxProfile profile = new FirefoxProfile();

				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.dir",
						projectRoot + File.separator + "data" + File.separator + "DownloadedReports");
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"text/csv, application/pdf, application/x-msexcel,application/excel,application/x-excel,application/excel,application/x-excel,application/excel, application/vnd.ms-excel,application/x-excel,application/x-msexcel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/excel,text/x-c,application/xls,text/xls,application/csv,text/pdf,application/html,application/octet-stream,application/x-rar-compressed,application/zip");
				profile.setPreference("media.navigator.permission.disabled", true);
				profile.setPreference("geo.prompt.testing", true);
				profile.setPreference("geo.prompt.testing.allow", true);
				profile.setPreference("security.enable_java", true);
				profile.setPreference("plugin.state.java", 2);
				// wbdv = new FirefoxDriver((Capabilities) profile);
				wbdv = new FirefoxDriver((Capabilities) profile);
				wbdv.manage().window().maximize();
				driver = new EventFiringWebDriver(wbdv);
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			} else if (Constants.BROWSER_IE.equals(testBrowser)) {
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + File.separatorChar + "data"
						+ File.separatorChar + "IEDriverServer" + File.separatorChar + "IEDriverServer.exe");
				wbdv = new InternetExplorerDriver();
				driver = new EventFiringWebDriver(wbdv);
				wbdv.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			}

			else if (Constants.BROWSER_CHROME.equals(testBrowser)) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separatorChar
						+ "data" + File.separatorChar + "ChromeDriverServer" + File.separatorChar + "chromedriver.exe");

				ChromeOptions options = new ChromeOptions();
				options.addArguments("user-data-dir=" + projectRoot + File.separator + "data" + File.separator
						+ "ChromeDriverServer" + File.separator + "Chrome_Profile" + File.separator);
				String downloadFilepath = "c:\\download";
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", downloadFilepath);
				HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");
				options.addArguments("--disable-extensions"); // to disable
																// browser
																// extension
																// popup
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);

				wbdv = new ChromeDriver(options);
				driver = new EventFiringWebDriver(wbdv);
				wbdv.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			} else if (Constants.ANDROID_DRIVER.equals(testBrowser)) {
				// navigate_webview();
			}
			if (Constants.BROWSER_SAFARI.equals(testBrowser)) {
				// Platform current = Platform.getCurrent();
				// return Platform.MAC.is(current) ||
				// Platform.WINDOWS.is(current);
				wbdv = new SafariDriver();
				driver = new EventFiringWebDriver(wbdv);
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			}

		}
		if (Constants.ANDROID_DRIVER.equals(testBrowser)) {

		} else {
			driver.navigate().to(CONFIG.getProperty(object));
		}

		return "Pass";

	}

	public static String navigateToExe() {
		try {
			String testBrowser = System.getProperty(Constants.TEST_BROWSER);
			boolean isWbdvClosed = false;
			if (null != wbdv) {
				try {
					wbdv.getCurrentUrl();
					driver.switchTo().window(wbdv.getWindowHandle());
				} catch (Exception e) {
					isWbdvClosed = true;
				}
			}
			// String driverExe =
			// FileReaderManager.getInstance().getConfigReader().getDriverPath();
			// // Set the path
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separatorChar + "data"
					+ File.separatorChar + "KeywordDrivenFramework" + File.separatorChar + "chromedriver.exe");
			// map to put binary of application and other chrome options
			Map<String, Object> chromeOptions = new HashMap<String, Object>();
			// path of electron application
			// chromeOptions.put("binary", "C:\\Program
			// Files\\KPIT\\Diagnostics\\ADCT\\ADCT-win32-x64\\ADCT.exe");
			// chromeOptions.put("binary", "C:\\Program
			// Files\\KPIT\\K-DCP\\KDCPServiceTester\\KDCPServiceTester.exe");
			chromeOptions.put("binary", CONFIG.getProperty(object));
			chromeOptions.put("args", Arrays.asList("--start-maximized", "--disable-infobars", "--disable-gpu"));
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("chromeOptions", chromeOptions);
			capabilities.setBrowserName("chrome");
			driver = new ChromeDriver(capabilities);
			driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
			Thread.sleep(100000);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Pass";
	}

	public static String Sikuli() throws IOException, InterruptedException {
		APPICATION_LOGS.debug("Executing Sikuli_Click Keyword");
		// extract the test data
		try {
			if ("Sikuli_click".equals(object_type)) {
				Screen s2 = new Screen();
				s2.click(IMAGES_ROOT + data_column_name);
			} else if ("Sikuli_doubleClick".equals(object_type)) {
				Screen s2 = new Screen();
				s2.doubleClick(IMAGES_ROOT + data_column_name);
			} else if ("Sikuli_type".equals(object_type)) {
				Screen s2 = new Screen();
				s2.type(data_column_name);
			} else if ("Sikuli_wait_untill_image_appears".equals(object_type)) {
				Screen s2 = new Screen();
				s2.wait(IMAGES_ROOT + data_column_name, 160);
			} 
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting into Sikuli_Click -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}


	private static Object Pattern(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String sendKeys() {
		APPICATION_LOGS.debug("Executing sendKeys Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("xpath_swipeInBetweenObjectAndDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
		} }catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting into sendKeys -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	
	public static String navigate_keys() {
		APPICATION_LOGS.debug("Executing navigate_keys Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("navigate_back".equals(object_type)) {
				driver.navigate().back();
			} else if ("navigate_forward".equals(object_type)) {
				driver.navigate().forward();
			} else if ("navigate_refresh".equals(object_type)) {
				driver.navigate().refresh();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting into sendKeys -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String sendKeysWithEnter() {
		APPICATION_LOGS.debug("Executing sendKeysWithEnter Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(getModifiedData(data_column_name), Keys.ENTER);
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting into sendKeysWithEnter -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String click() {
		APPICATION_LOGS.debug("Executing click Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).click();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).click();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).click();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).click();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).click();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).click();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).click();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).click();
			} else if ("jsscript".equals(object_type)) {
				WebElement elementToClick = driver.findElement(By.xpath(getResolvedObject()));
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0," + elementToClick.getLocation().x + ")");
				elementToClick.click();
			} else if ("containTextClick".equals(object_type)) {
				driver.findElement(By.xpath("//*[normalize-space(text()) = '" + getResolvedObject() + "\']")).click();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).click();
			} else if ("input_ngReflectModelDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath("//input[@ng-reflect-model = '" + data_column_name + "\']")).click();
			} else if ("input_ngReflectModelObjectRead".equals(object_type)) {
				driver.findElement(By.xpath("//input[@ng-reflect-model = '" + getResolvedObject() + "\']")).click();
			} else if ("js_ngReflectModel".equals(object_type)) {
				WebElement el = driver
						.findElement(By.xpath("//input[@ng-reflect-model = '" + getResolvedObject() + "\']"));
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", el);
			} else if ("containTextClickDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath("//*[normalize-space(text()) = '" + data_column_name + "\']")).click();
			} else if ("containTitleDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + data_column_name + "\')]")).click();
			}else if ("moveToElementAndClick".equals(object_type)) {
				WebElement element=driver.findElement(By.xpath(getResolvedObject()));
				Point p= element.getLocation();
				Actions actions = new Actions(driver);
				actions.moveToElement(element).moveByOffset(p.x,p.y).click().perform();
			}else if ("ngReflectResultRead".equals(object_type)) {
				driver.findElement(By.xpath("//*[@ng-reflect-result= '" + getResolvedObject() + "\']")).click();
			} else if ("ngReflectResultDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath("//*[@ng-reflect-result= '" + data_column_name + "\']")).click();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while click -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String NotClickable() {
		APPICATION_LOGS.debug("Executing NotClickable Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).click();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).click();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).click();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).click();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).click();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).click();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).click();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).click();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).click();
			} else if ("input_ngReflectModelDataColumnName".equals(object_type)) {
				driver.findElement(By.xpath("//input[@ng-reflect-model = '" + data_column_name + "\']")).click();
			} else if ("input_ngReflectModelObjectRead".equals(object_type)) {
				driver.findElement(By.xpath("//input[@ng-reflect-model = '" + getResolvedObject() + "\']")).click();
			} else if ("containsTextAnywhere".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(text(), '" + getResolvedObject() + "\')]")).click();
			} else if ("containText".equals(object_type)) {
				driver.findElement(By.xpath("//*[normalize-space(text()) = '" + getResolvedObject() + "\']")).getText();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while NotClickable -" + object + t.getMessage());
			return "Pass - " + t.getMessage();
		}
		return "Fail";
	}

	public String AlertAccept() throws NumberFormatException, InterruptedException {
		APPICATION_LOGS.debug("Executing wait Alert Accept");
		Alert alert = driver.switchTo().alert();
		String actual = alert.getText();
		String expected = data_column_name;
		APPICATION_LOGS.debug(expected);
		APPICATION_LOGS.debug(actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
			alert.accept();
		} catch (Throwable t) {
			// error
			APPICATION_LOGS.debug("Actual - " + actual);
			APPICATION_LOGS.debug("Expected -" + expected);
			return "Fail -" + t.getMessage();
		}
		return "Pass";
	}

	public String Alert() throws NumberFormatException, InterruptedException {
		APPICATION_LOGS.debug("Executing wait Alert keyword");
		try {
			if ("getTextVerifyAndConfirmAlert".equals(object_type)) {
				Alert alert = driver.switchTo().alert();
				String actual = alert.getText();
				String expected = data_column_name;
				APPICATION_LOGS.debug(expected);
				APPICATION_LOGS.debug(actual);
				// error
				APPICATION_LOGS.debug("Actual - " + actual);
				APPICATION_LOGS.debug("Expected -" + expected);
				Assert.assertEquals(expected.trim(), actual.trim());
				alert.accept();
			} else if ("dismissAlert".equals(object_type)) {
				driver.switchTo().alert().dismiss();
			} else if ("acceptAlert".equals(object_type)) {
				driver.switchTo().alert().dismiss();
			} else if ("enterTextToAlert".equals(object_type)) {
				driver.switchTo().alert().sendKeys(data_column_name);
			}
		} catch (Throwable t) {
			APPICATION_LOGS.debug("Error while executing Alert keyword -" + object + t.getMessage());
			return "Fail -" + t.getMessage();
		}
		return "Pass";
	}

	public static String getAttributeValue() {
		APPICATION_LOGS.debug("Executing getAttributeValue Keyword");
		String expected = data_column_name;
		String actual = null;
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getCssValue("background-color");
			} else if ("xpath_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getCssValue("background-color");
			} else if ("name_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getCssValue("background-color");
			} else if ("css_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.cssSelector(getResolvedObject())).getCssValue("background-color");
			} else if ("linkText_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.linkText(getResolvedObject())).getCssValue("background-color");
			} else if ("partialLinkText_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.partialLinkText(getResolvedObject())).getCssValue("background-color");
			} else if ("className_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.className(getResolvedObject())).getCssValue("background-color");
			} else if ("tagName_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.tagName(getResolvedObject())).getCssValue("background-color");
			} else if ("id_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getAttribute("title");
			} else if ("xpath_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("title");
			} else if ("name_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getAttribute("title");
			} else if ("css_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.cssSelector(getResolvedObject())).getAttribute("title");
			} else if ("linkText_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.linkText(getResolvedObject())).getAttribute("title");
			} else if ("partialLinkText_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.partialLinkText(getResolvedObject())).getAttribute("title");
			} else if ("className_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.className(getResolvedObject())).getAttribute("title");
			} else if ("tagName_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.tagName(getResolvedObject())).getAttribute("title");
			} else if ("xpath_ariahidden".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("aria-hidden");
			} else if ("xpath_ariaexpanded".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("aria-expanded");
			} else if ("xpath_comment".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-ng-if");
			} else if ("xpath_color".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getCssValue("color");
			}
			APPICATION_LOGS.debug(expected);
			APPICATION_LOGS.debug(actual);
			boolean compareflag = actual.trim().contains(expected.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing getAttributeValue keyword -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
		}
		return "Pass";
	}

	public static String isSelected() {
		APPICATION_LOGS.debug("Executing isSelected Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			boolean searchElementSelected = true;
			if ("id".equals(object_type)) {
				searchElementSelected = driver.findElement(By.id(getResolvedObject())).isSelected();
			} else if ("xpath".equals(object_type)) {
				searchElementSelected = driver.findElement(By.xpath(getResolvedObject())).isSelected();
			} else if ("name".equals(object_type)) {
				searchElementSelected = driver.findElement(By.name(getResolvedObject())).isSelected();
			} else if ("css".equals(object_type)) {
				searchElementSelected = driver.findElement(By.cssSelector(getResolvedObject())).isSelected();
			} else if ("linkText".equals(object_type)) {
				searchElementSelected = driver.findElement(By.linkText(getResolvedObject())).isSelected();
			} else if ("partialLinkText".equals(object_type)) {
				searchElementSelected = driver.findElement(By.partialLinkText(getResolvedObject())).isSelected();
			} else if ("className".equals(object_type)) {
				searchElementSelected = driver.findElement(By.className(getResolvedObject())).isSelected();
			} else if ("tagName".equals(object_type)) {
				searchElementSelected = driver.findElement(By.tagName(getResolvedObject())).isSelected();
			} else if ("containTitle".equals(object_type)) {
				searchElementSelected = driver
						.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isSelected();
			}
			if (!searchElementSelected) {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isSelected keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String VerifyAttributeValue() {
		APPICATION_LOGS.debug("Executing VerifyAttributeValue Keyword");
		String expected = getResolvedObject();
		String actual = null;
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getCssValue("background-color");
			} else if ("xpath_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getCssValue("background-color");
			} else if ("name_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getCssValue("background-color");
			} else if ("css_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.cssSelector(getResolvedObject())).getCssValue("background-color");
			} else if ("linkText_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.linkText(getResolvedObject())).getCssValue("background-color");
			} else if ("partialLinkText_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.partialLinkText(getResolvedObject())).getCssValue("background-color");
			} else if ("className_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.className(getResolvedObject())).getCssValue("background-color");
			} else if ("tagName_backgroundColour".equals(object_type)) {
				actual = driver.findElement(By.tagName(getResolvedObject())).getCssValue("background-color");
			} else if ("id_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getAttribute("title");
			} else if ("xpath_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("title");
			} else if ("name_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getAttribute("title");
			} else if ("css_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.cssSelector(getResolvedObject())).getAttribute("title");
			} else if ("linkText_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.linkText(getResolvedObject())).getAttribute("title");
			} else if ("partialLinkText_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.partialLinkText(getResolvedObject())).getAttribute("title");
			} else if ("className_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.className(getResolvedObject())).getAttribute("title");
			} else if ("tagName_attributeTitle".equals(object_type)) {
				actual = driver.findElement(By.tagName(getResolvedObject())).getAttribute("title");
			} else if ("xpath_ariahidden".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("aria-hidden");
			} else if ("xpath_ariaexpanded".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("aria-expanded");
			} else if ("xpath_comment".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-ng-if");
			} else if ("xpath_color".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getCssValue("color");
			} else if ("xpath_ngReflectModel".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-model");
			} else if ("xpath_ngReflectModelCSS".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getCssValue("ng-reflect-model");
			} else if ("JS_ngReflectModel".equals(object_type)) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				actual = ((WebElement) js.executeScript(data_column_name)).getAttribute("ng-reflect-model");
			}
			APPICATION_LOGS.debug(expected);
			APPICATION_LOGS.debug(actual);
			boolean compareflag = actual.trim().contains(expected.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing VerifyAttributeValue keyword -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
		}
		return "Pass";
	}

	public static String clear() {
		APPICATION_LOGS.debug("Executing clear Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).clear();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).clear();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).clear();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).clear();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).clear();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).clear();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).clear();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).clear();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while clear -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String wait_Until_ElementPresent() {
		APPICATION_LOGS.debug("Executing wait_Until_ElementPresent Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.id(getResolvedObject())));
			} else if ("xpath".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(getResolvedObject())));
			} else if ("name".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.name(getResolvedObject())));
			} else if ("css".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getResolvedObject())));
			} else if ("linkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.linkText(getResolvedObject())));
			} else if ("partialLinkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(getResolvedObject())));
			} else if ("className".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.className(getResolvedObject())));
			} else if ("tagName".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.elementToBeClickable(By.tagName(getResolvedObject())));
			} else if ("containTitle".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wait_Until_ElementPresent -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String Wait_Until_ElementVisible() {
		APPICATION_LOGS.debug("Executing Wait_Until_ElementVisible Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {

			if ("id".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(getResolvedObject())));
			} else if ("xpath".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getResolvedObject())));
			} else if ("name".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(getResolvedObject())));
			} else if ("css".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(getResolvedObject())));
			} else if ("linkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(getResolvedObject())));
			} else if ("partialLinkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(getResolvedObject())));
			} else if ("className".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(getResolvedObject())));
			} else if ("tagName".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(getResolvedObject())));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while Wait_Until_ElementVisible -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String selectByValue() {
		APPICATION_LOGS.debug("Executing selectByValue Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				select.selectByValue(data_column_name);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				select.selectByValue(data_column_name);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while selectByValue -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String deselectByValue() {
		APPICATION_LOGS.debug("Executing deselectByValue Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				select.deselectByValue(data_column_name);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				select.deselectByValue(data_column_name);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while deselectByValue -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String selectByIndex() {
		APPICATION_LOGS.debug("Executing selectByIndex Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.selectByIndex(index);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while selectByIndex -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String deselectByIndex() {
		APPICATION_LOGS.debug("Executing deselectByIndex Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				int index = Integer.parseInt(data_column_name);
				select.deselectByIndex(index);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while deselectByIndex -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String selectByVisibleText() {
		APPICATION_LOGS.debug("Executing selectByVisibleText Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				select.selectByVisibleText(data_column_name);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while selectByVisibleText -" + object + t.getMessage());
			System.out.println(t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String deselectByVisibleText() {
		APPICATION_LOGS.debug("Executing deselectByVisibleText Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("id".equals(object_type)) {
				Select select = new Select(driver.findElement(By.id(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("xpath".equals(object_type)) {
				Select select = new Select(driver.findElement(By.xpath(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("name".equals(object_type)) {
				Select select = new Select(driver.findElement(By.name(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("css".equals(object_type)) {
				Select select = new Select(driver.findElement(By.cssSelector(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("linkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.linkText(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("partialLinkText".equals(object_type)) {
				Select select = new Select(driver.findElement(By.partialLinkText(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("className".equals(object_type)) {
				Select select = new Select(driver.findElement(By.className(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			} else if ("tagName".equals(object_type)) {
				Select select = new Select(driver.findElement(By.tagName(getResolvedObject())));
				select.deselectByVisibleText(data_column_name);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while deselectByVisibleText -" + object + t.getMessage());
			System.out.println(t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String wait_untill_presenceOfElementLocated() {
		APPICATION_LOGS.debug("Executing wait_untill_presenceOfElementLocated Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.id(getResolvedObject())));
			} else if ("xpath".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(getResolvedObject())));
			} else if ("name".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.name(getResolvedObject())));
			} else if ("css".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(getResolvedObject())));
			} else if ("linkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(getResolvedObject())));
			} else if ("partialLinkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(getResolvedObject())));
			} else if ("className".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.className(getResolvedObject())));
			} else if ("tagName".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				WebElement myDynamicElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.tagName(getResolvedObject())));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS
					.debug("Error while wait_untill_presenceOfElementLocated keyword -" + object + t.getMessage());
			System.out.println(t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String Wait_Until_textToBePresentInElement() {
		APPICATION_LOGS.debug("Executing Wait_Until_textToBePresentInElement Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("id".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(ExpectedConditions.textToBePresentInElement(By.id(getResolvedObject()), data_column_name));
			} else if ("xpath".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(
						ExpectedConditions.textToBePresentInElement(By.xpath(getResolvedObject()), data_column_name));
			} else if ("name".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(ExpectedConditions.textToBePresentInElement(By.name(getResolvedObject()), data_column_name));
			} else if ("css".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(ExpectedConditions.textToBePresentInElement(By.id(getResolvedObject()), data_column_name));
			} else if ("linkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(
						ExpectedConditions.textToBePresentInElement(By.tagName(getResolvedObject()), data_column_name));
			} else if ("partialLinkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(
						ExpectedConditions.textToBePresentInElement(By.tagName(getResolvedObject()), data_column_name));
			} else if ("className".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(
						ExpectedConditions.textToBePresentInElement(By.tagName(getResolvedObject()), data_column_name));
			} else if ("tagName".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 180000);
				wait.until(
						ExpectedConditions.textToBePresentInElement(By.tagName(getResolvedObject()), data_column_name));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while Wait_Until_textToBePresentInElement -" + object + t.getMessage());
			System.out.println(t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int RANDOM_STRING_LENGTH = 7;
	private static final int RANDOM_MAX_STRING_LENGTH = 256;

	public static String generateRandomString() {
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	public static String generateRandomStringMAxlenght() {
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_MAX_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString().toLowerCase();

	}

	private static int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public static String randomAlphanumericValue() {
		APPICATION_LOGS.debug("Executing randomAlphanumericValue keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(generateRandomString() + "_1");
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(generateRandomString() + "_1");
			} else if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(generateRandomString() + "_1");
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while randomAlphanumericValue keyword-" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String randomStringWithMaxLenghth() {
		APPICATION_LOGS.debug("Executing input randomStringWithMaxLenghth");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(generateRandomStringMAxlenght());
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(generateRandomStringMAxlenght());
			} else if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(generateRandomStringMAxlenght());
			} else if ("xpathWithInput".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(data_column_name + generateRandomStringMAxlenght());
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing randomStringWithMaxLenghth" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String randomAlphaValue() {
		APPICATION_LOGS.debug("Executing input randomAlphaValue");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(generateRandomString());
			} else {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(generateRandomString());
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing randomAlphaValue -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String randomNumericValue() {
		APPICATION_LOGS.debug("Executing randomNumericValue");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(getRandomNumber() + "1");
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getRandomNumber() + "1");
			} else if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(getRandomNumber() + "1");
			} else {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getRandomNumber() + "1");
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while randomNumericValue keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String InputWithDateTimeStamp() {
		APPICATION_LOGS.debug("Executing input DateTimeStamp dd-M-yyyy hh:mm:ss");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			DateFormat dateFormat = new SimpleDateFormat("ddMMSSsssss");
			Date date = new Date();
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject()))
						.sendKeys(data_column_name + " " + dateFormat.format(date));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting DateTimeStampdd-M-yyyy hh:mm:ss -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String InputWithDateTimeStampWithoutSpace() {
		APPICATION_LOGS.debug("Executing InputWithDateTimeStampWithoutSpace");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			DateFormat dateFormat = new SimpleDateFormat("ddMMSSsssss");
			Date date = new Date();
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(data_column_name + dateFormat.format(date));
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(data_column_name + dateFormat.format(date));
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(data_column_name + dateFormat.format(date));
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject()))
						.sendKeys(data_column_name + dateFormat.format(date));
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject()))
						.sendKeys(data_column_name + dateFormat.format(date));
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject()))
						.sendKeys(data_column_name + dateFormat.format(date));
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject()))
						.sendKeys(data_column_name + dateFormat.format(date));
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject()))
						.sendKeys(data_column_name + dateFormat.format(date));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug(
					"Error while executing InputWithDateTimeStampWithoutSpace keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String email() {
		APPICATION_LOGS.debug("Executing email Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			String date = (new SimpleDateFormat("mmhhyyssss")).format(new Date());
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(date + "@kpit.com");
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(date + "@kpit.com");
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing email keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String Custom_Email() {
		APPICATION_LOGS.debug("Executing Custom_Email Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		// extract the test data
		try {
			String date3 = (new SimpleDateFormat("mmhhyyssss")).format(new Date());
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject()))
						.sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject()))
						.sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(data_column_name + date3 + "@kpit.com");
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executng Custom_Email keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String close() {
		APPICATION_LOGS.debug("Executing close keyword");
		try {
			driver.close();
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while scrolling -" + object + t.getMessage());
			return "Fail - Close Not Found";
		}
		return "Pass";
	}

	public static String quit() {
		APPICATION_LOGS.debug("Executing Quit keyword");
		try {
			driver.quit();
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while Quit -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String MouseHover() throws InterruptedException {
		APPICATION_LOGS.debug("Executing MouseHover");
		try {
			WebElement myElement = null;
			if ("id".equals(object_type)) {
				myElement = driver.findElement(By.id(getResolvedObject()));
			} else if ("xpath".equals(object_type)) {
				myElement = driver.findElement(By.xpath(getResolvedObject()));
			} else if ("containTitle".equals(object_type)) {
				myElement = driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]"));
			} else if ("containsText".equals(object_type)) {
				myElement = driver.findElement(By.xpath("//span[contains(text(), '" + getResolvedObject() + "\')]"));
			} else if ("containsTextAnywhere".equals(object_type)) {
				myElement = driver.findElement(By.xpath("//*[contains(text(), '" + getResolvedObject() + "\')]"));
			}
			Actions builder = new Actions(driver);
			builder.moveToElement(myElement).build().perform();
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while MouseHover -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}

	public static String Wait() throws NumberFormatException, InterruptedException {
		APPICATION_LOGS.debug("Executing wait Keyword");
		// extract the test data

		Thread.sleep(Long.parseLong(data_column_name));
		return "Pass";
	}

	public static String getText() {

		APPICATION_LOGS.debug("Executing getText Keyword");
		// extract the test data
		try {
			String folder1 = (new SimpleDateFormat("dd-MMM-yyyy")).format(new Date());
			File file = new File(projectRoot + File.separator + "data" + File.separator + "ReadWriteData"
					+ File.separator + folder1);
			file.mkdir();
			String getText = driver.findElement(By.xpath(getResolvedObject())).getAttribute("value");

			FileWriter w = new FileWriter(file + File.separator + "Read_Write_Details.txt", true);
			BufferedWriter out = new BufferedWriter(w);
			out.newLine();
			out.write(getText);
			out.close();
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing getText -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String javascriptTrue() {

		APPICATION_LOGS.debug("start javascriptTrue keyword execution");
		// extract the test data
		String expected = "true";
		String actual = null;
		APPICATION_LOGS.debug(expected);
		APPICATION_LOGS.debug(actual);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		actual = js.executeScript(data_column_name).toString();
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing javascriptTrue keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String javascriptFalse() {

		APPICATION_LOGS.debug("start javascriptFalse keyword execution");
		// extract the test data
		String expected = "false";
		String actual = null;
		APPICATION_LOGS.debug(expected);
		APPICATION_LOGS.debug(actual);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		actual = js.executeScript(data_column_name).toString();
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing javascriptFalse keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String readText() {

		APPICATION_LOGS.debug("Executing readText");
		// extract the test data
		try {
			List<String> lineList = readFile();
			driver.findElement(By.xpath(getResolvedObject()))
					.sendKeys(lineList.get(Integer.parseInt(data_column_name)));

		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing readText -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	private static List<String> readFile() {
		List<String> lineList = new ArrayList<String>();
		try {
			String folder1 = (new SimpleDateFormat("dd-MMM-yyyy")).format(new Date());
			FileReader r = new FileReader(projectRoot + File.separator + "data" + File.separator + "ReadWriteData"
					+ File.separator + folder1 + File.separator + "Read_Write_Details.txt");
			BufferedReader out = new BufferedReader(r);

			String line;
			while ((line = out.readLine()) != null) {
				lineList.add(line);
			}
			out.close();
		} catch (Throwable t) {

		}
		return lineList;
	}

	public static void writeMappedValuesToFile(String key, String value) throws Throwable {
		map.put(key, value);
		writeMappedValuesToFile();
	}

	public static File getReadWriteFile() {
		File folder = new File(System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar
				+ "ReadWriteData" + File.separatorChar);
		folder.mkdir();
		File file = new File(folder, "Read_Write_Details_Map.txt");
		return file;
	}

	public static String clearAllMap() {
		map.clear();
		try {
			writeMappedValuesToFile();
		} catch (Throwable t) {
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String clearValuesFromMap() {
		String[] namesToClear = data_column_name.split(",");
		for (int i = 0; i < namesToClear.length; i++) {
			map.remove(namesToClear[i]);
		}
		try {
			writeMappedValuesToFile();
		} catch (Throwable t) {
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static void writeMappedValuesToFile() throws Throwable {
		APPICATION_LOGS.debug("Executing getLabelTextToMap");
		Properties curretProperties = new Properties();
		File currentFile = getReadWriteFile();
		curretProperties.load(new FileInputStream(currentFile));
		curretProperties.putAll(map);
		PrintWriter pWriter = new PrintWriter(getReadWriteFile());
		Set keys = curretProperties.keySet();
		for (Object key : keys) {
			pWriter.println(key + "=" + curretProperties.get(key));
		}
		pWriter.close();
	}

	public static void AppendMappedValuesToFile(String getText) throws Throwable {
		APPICATION_LOGS.debug("Executing getLabelTextToMap");
		Writer pWriter = new BufferedWriter(new FileWriter(getReadWriteFile(), true));
		Set<String> keys = map.keySet();
		pWriter.append(getText);
		pWriter.append("\n");

		pWriter.close();
	}

	public static void writeValuesToFile() throws Throwable {
		APPICATION_LOGS.debug("Executing getLabelTextToMap");
		Writer pWriter = new BufferedWriter(new FileWriter(getReadWriteFile(), true));
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();
		pWriter.append(sdf.format(cal.getTime()));
		pWriter.append("\n");
		pWriter.close();
	}

	public static void readMappedValuesFromFile() throws Throwable {
		map.clear();
		APPICATION_LOGS.debug("Executing readMappedValuesFromFile");
		File file = new File(System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar
				+ "ReadWriteData" + File.separatorChar + "Read_Write_Details_Map.txt");
		FileReader reader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(reader);
		String line = null;
		while ((line = bReader.readLine()) != null) {
			line = line.trim();
			if (!line.startsWith("#")) {
				String[] lineParts = line.split("=", 2);
				if (lineParts.length > 1) {
					map.put(lineParts[0], lineParts[1]);
				} else if (lineParts.length == 1) {
					map.put(lineParts[0], " ");
				}
			}
		}
		reader.close();
	}

	public static String getTextToMap() {

		APPICATION_LOGS.debug("Executing getText Keyword");
		// extract the test data
		try {
			String getText = null;
			if ("id".equals(object_type)) {
				getText = driver.findElement(By.id(getResolvedObject())).getAttribute("value");
			} else if ("xpath".equals(object_type)) {
				getText = driver.findElement(org.openqa.selenium.By.xpath(getResolvedObject())).getAttribute("value");
			} else if ("name".equals(object_type)) {
				getText = driver.findElement(By.name(getResolvedObject())).getAttribute("value");
			} else if ("xpath_getText".equals(object_type)) {
				getText = driver.findElement(By.xpath(getResolvedObject())).getText();
			} else if ("xpath_singleQuote".equals(object_type)) {
				String test1 = driver.findElement(By.xpath(getResolvedObject())).getText();
				Pattern p = Pattern.compile("\'.*?\'");
				Matcher m = p.matcher(test1);
				while (m.find()) {
					System.out.println(test1.substring(m.start() + 1, m.end() - 1));
					getText = test1.substring(m.start() + 1, m.end() - 1);
				}
			}
			map.put(data_column_name, getText.replaceAll("\n", ""));
			writeMappedValuesToFile();
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing getText -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String readTextFromMap() {

		APPICATION_LOGS.debug("Executing readTextFromMap");
		// extract the test data
		try {
			readMappedValuesFromFile();
			if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(map.get(data_column_name));
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(map.get(data_column_name));
			} else if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(map.get(data_column_name));
			} else {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(map.get(data_column_name));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing readText -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String verifyTextFromExcel() {

		APPICATION_LOGS.debug("verifyTextFromExcel");
		// extract the test data
		String expected = data_column_name;
		String actual = null;
		try {
			if ("id".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getText();
			} else if ("name".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getText();
			} else if ("xpath".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getText();
			} else if ("xpath_value".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("value");
			} else if ("name_value".equals(object_type)) {
				actual = driver.findElement(By.name(getResolvedObject())).getAttribute("value");
			} else if ("xpath_ngReflectModel".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-model");
			} else if ("xpath_ngReflectNgIf".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-ng-if");
			} else if ("containText".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[normalize-space(text()) = '" + data_column_name + "\']"))
						.getText();
			} else if ("containTitle".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]"))
						.getText();
			}
			APPICATION_LOGS.debug(expected);
			APPICATION_LOGS.debug(actual);
			boolean compareflag = actual.trim().equals(expected.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing readText -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
		}
		return "Pass";
	}

	public static String verifyTextFromExcel_contains() {

		APPICATION_LOGS.debug("verifyTextFromExcel");
		// extract the test data
		String expected = data_column_name;
		String actual = null;
		try {
			if ("id".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getText();
			} else if ("xpath".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getText();
			} else if ("xpath_value".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("value");
			} else if ("xpath_ngReflectModel".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-model");
			} else if ("xpath_ngReflectNgIf".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getAttribute("ng-reflect-ng-if");
			} else if ("containText".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[normalize-space(text()) = '" + getResolvedObject() + "\']"))
						.getText();
			}
			APPICATION_LOGS.debug(expected);
			APPICATION_LOGS.debug(actual);
			boolean compareflag = actual.trim().contains(expected.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing readText -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
		}
		return "Pass";
	}

	public static String verifyTextFromGetText() {

		APPICATION_LOGS.debug("executing verifyTextFromGetText keyword");
		// extract the test data
		String expected = getResolvedObject();
		String actual = null;
		try {
			if ("id".equals(object_type)) {
				actual = driver.findElement(By.id(getResolvedObject())).getText();
			} else if ("xpath".equals(object_type)) {
				actual = driver.findElement(By.xpath(getResolvedObject())).getText();
			} else if ("containTitle".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]"))
						.getText();
			} else if ("containTitleValue".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]"))
						.getAttribute("value");
			} else if ("containText".equals(object_type)) {
				actual = driver.findElement(By.xpath("//*[normalize-space(text()) = '" + getResolvedObject() + "\']"))
						.getText();
			} else if ("input_ngReflectModel".equals(object_type)) {
				actual = driver.findElement(By.xpath("//input[@ng-reflect-model = '" + getResolvedObject() + "\']"))
						.getText();
			} else if ("XpathFromDataColumnName".equals(object_type)) {
				actual = driver.findElement(By.xpath(data_column_name)).getAttribute("value");
			} else if ("IdFromDataColumnName".equals(object_type)) {
				actual = driver.findElement(By.id(data_column_name)).getAttribute("value");
			} else if ("NameFromDataColumnName".equals(object_type)) {
				actual = driver.findElement(By.name(data_column_name)).getAttribute("value");
			} else if ("XpathFromDataColumnNameGetText".equals(object_type)) {
				actual = driver.findElement(By.xpath(data_column_name)).getText();
			} else if ("XpathFromDataColumnNameGetValue".equals(object_type)) {
				actual = driver.findElement(By.xpath(data_column_name)).getAttribute("value");
			}
			APPICATION_LOGS.debug(expected);
			APPICATION_LOGS.debug(actual);
			boolean compareflag = actual.trim().contains(expected.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing verifyTextFromGetText keyword -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expected + " " + "but Actual=" + actual;
		}
		return "Pass";
	}

	// keyModifiers used for executing key strokes on Web Element like
	// Keys.ENTER, Keys.DOWN and other user needs to add only Keys value in
	// excel sheet column name
	public static String keyModifiers() {
		APPICATION_LOGS.debug("Executing keyModifiers");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(data_column_name, Keys.ENTER);
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(data_column_name, Keys.ENTER);
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(data_column_name, Keys.ENTER);
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys("Keys." + data_column_name,
						Keys.ENTER);
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys("Keys." + data_column_name, Keys.ENTER);
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys("Keys." + data_column_name,
						Keys.ENTER);
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys("Keys." + data_column_name, Keys.ENTER);
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys("Keys." + data_column_name, Keys.ENTER);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while Executing keyModifiers -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String isDisplayed() {
		APPICATION_LOGS.debug("Executing isDisplayed Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).isDisplayed();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).isDisplayed();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).isDisplayed();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).isDisplayed();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).isDisplayed();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).isDisplayed();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).isDisplayed();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).isDisplayed();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isDisplayed();
			} else if ("containText".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(text()," + getResolvedObject() + ")]")).isDisplayed();
			} else if ("containsTextAnywhere".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(text(),'" + getResolvedObject() + "\')]")).isDisplayed();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isDisplyed keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String ElementIsNotPreset() {
		APPICATION_LOGS.debug("Executing ElementIsNotPreset Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).isDisplayed();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).isDisplayed();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).isDisplayed();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).isDisplayed();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).isDisplayed();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).isDisplayed();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).isDisplayed();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).isDisplayed();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isDisplayed();
			} else if ("containText".equals(object_type)) {
				driver.findElement(By.xpath("//*[normalize-space(text()) = '" + getResolvedObject() + "\']"))
						.isDisplayed();
			} else if ("containsTextAnywhere".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(text(),'" + getResolvedObject() + "\')]")).isDisplayed();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing ElementIsNotPreset keyword -" + object + t.getMessage());
			return "Pass" + t.getMessage();
		}
		return "Fail - ";
	}

	public static String elementIsPresent() {
		APPICATION_LOGS.debug("Executing elementIsPresent Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				boolean a = driver.findElements(By.id(getResolvedObject())).size() != 0;
			} else if ("xpath".equals(object_type)) {
				boolean b = driver.findElements(By.xpath(getResolvedObject())).size() != 0;
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).isEnabled();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).isEnabled();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).isEnabled();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).isEnabled();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).isEnabled();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).isEnabled();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isEnabled();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing elementIsPresent keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String isEnabled() {
		APPICATION_LOGS.debug("Executing isEnabled Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			boolean searchIconEnabled = true;
			if ("id".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.id(getResolvedObject())).isEnabled();
			} else if ("xpath".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.xpath(getResolvedObject())).isEnabled();
			} else if ("name".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.name(getResolvedObject())).isEnabled();
			} else if ("css".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.cssSelector(getResolvedObject())).isEnabled();
			} else if ("linkText".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.linkText(getResolvedObject())).isEnabled();
			} else if ("partialLinkText".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.partialLinkText(getResolvedObject())).isEnabled();
			} else if ("className".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.className(getResolvedObject())).isEnabled();
			} else if ("tagName".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.tagName(getResolvedObject())).isEnabled();
			} else if ("containTitle".equals(object_type)) {
				searchIconEnabled = driver
						.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isEnabled();
			}
			if (!searchIconEnabled) {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isEnabled keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String isDisabled() {
		APPICATION_LOGS.debug("Executing isDisabled Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			boolean searchIconEnabled = true;
			if ("id".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.id(getResolvedObject())).isEnabled();
			} else if ("xpath".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.xpath(getResolvedObject())).isEnabled();
			} else if ("name".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.name(getResolvedObject())).isEnabled();
			} else if ("css".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.cssSelector(getResolvedObject())).isEnabled();
			} else if ("linkText".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.linkText(getResolvedObject())).isEnabled();
			} else if ("partialLinkText".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.partialLinkText(getResolvedObject())).isEnabled();
			} else if ("className".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.className(getResolvedObject())).isEnabled();
			} else if ("tagName".equals(object_type)) {
				searchIconEnabled = driver.findElement(By.tagName(getResolvedObject())).isEnabled();
			} else if ("containTitle".equals(object_type)) {
				searchIconEnabled = driver
						.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).isEnabled();
			}
			if (!searchIconEnabled) {
				return "Pass";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isDisabled keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Fail";
	}

	public static String isCheckBoxSelected() {
		APPICATION_LOGS.debug("Executing isCheckBoxSelected Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			boolean searchIconEnabled = true;
			if ("id".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.id(getResolvedObject())).size() != 0;
			} else if ("xpath".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.xpath(getResolvedObject())).size() != 0;
			} else if ("name".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.name(getResolvedObject())).size() != 0;
			} else if ("css".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.cssSelector(getResolvedObject())).size() != 0;
			} else if ("linkText".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.linkText(getResolvedObject())).size() != 0;
			} else if ("partialLinkText".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.partialLinkText(getResolvedObject())).size() != 0;
			} else if ("className".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.className(getResolvedObject())).size() != 0;
			} else if ("tagName".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.tagName(getResolvedObject())).size() != 0;
			} else if ("containTitle".equals(object_type)) {
				searchIconEnabled = driver
						.findElements(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).size() != 0;
			}
			if (!searchIconEnabled) {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isCheckBoxSelected keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String isCheckBoxDeselected() {
		APPICATION_LOGS.debug("Executing isCheckBoxDeselected Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			boolean searchIconEnabled = true;
			if ("id".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.id(getResolvedObject())).size() != 0;
			} else if ("xpath".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.xpath(getResolvedObject())).size() != 0;
			} else if ("name".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.name(getResolvedObject())).size() != 0;
			} else if ("css".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.cssSelector(getResolvedObject())).size() != 0;
			} else if ("linkText".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.linkText(getResolvedObject())).size() != 0;
			} else if ("partialLinkText".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.partialLinkText(getResolvedObject())).size() != 0;
			} else if ("className".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.className(getResolvedObject())).size() != 0;
			} else if ("tagName".equals(object_type)) {
				searchIconEnabled = driver.findElements(By.tagName(getResolvedObject())).size() != 0;
			} else if ("containTitle".equals(object_type)) {
				searchIconEnabled = driver
						.findElements(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).size() != 0;
			}
			if (!searchIconEnabled) {
				return "Pass";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing isCheckBoxDeselected keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Fail";
	}

	public static String Wait_Until_ElementInvisible() {
		APPICATION_LOGS.debug("Executing Wait_Until_ElementInvisible Keyword");
		try {

			if ("id".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(getResolvedObject())));
			} else if ("xpath".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(getResolvedObject())));
			} else if ("name".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(getResolvedObject())));
			} else if ("css".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(getResolvedObject())));
			} else if ("linkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(getResolvedObject())));
			} else if ("partialLinkText".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText(getResolvedObject())));
			} else if ("className".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(getResolvedObject())));
			} else if ("tagName".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName(getResolvedObject())));
			} else if ("containTitle".equals(object_type)) {
				WebDriverWait wait = new WebDriverWait(driver, 250000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(
						By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")));
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while Wait_Until_ElementInvisible -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String ExecuteJavaScript() throws IOException, InterruptedException {
		APPICATION_LOGS.debug("Executing ExecuteJavaScript Keyword");
		// extract the test data
		try {
			String searchIconEnabled;
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			searchIconEnabled = (String) js.executeScript(data_column_name);
			Thread.sleep(1000);
			if (searchIconEnabled == "true") {
				return "Pass";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing ExecuteJavaScript keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Fail";
	}

	public static String ExecuteJavaScriptAndCompareResult() throws IOException, InterruptedException {
		APPICATION_LOGS.debug("Executing ExecuteJavaScriptAndCompareResult Keyword");
		// extract the test data
		String searchIconEnabled = null;
		try {
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			searchIconEnabled = js.executeScript(getResolvedObject()).toString();
			Thread.sleep(1000);
			APPICATION_LOGS.debug(data_column_name);
			APPICATION_LOGS.debug(searchIconEnabled);
			boolean compareflag = data_column_name.trim().equals(searchIconEnabled.trim());
			if (!compareflag) {
				return "Fail as " + "Expected=" + data_column_name + " " + "but Actual=" + searchIconEnabled;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS
					.debug("Error while executing ExecuteJavaScriptAndCompareResult -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + data_column_name + " " + "but Actual=" + searchIconEnabled;
		}
		return "Pass";
	}
	
	public static String ExecuteJavaScriptClick() throws IOException, InterruptedException {
		APPICATION_LOGS.debug("Executing ExecuteJavaScriptClick Keyword");
		// extract the test data
		try {
			String searchIconEnabled;
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			searchIconEnabled = (String) js.executeScript(data_column_name);
			Thread.sleep(1000);

		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing ExecuteJavaScriptClick keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String ExecuteJavaScriptF() throws IOException, InterruptedException {
		APPICATION_LOGS.debug("Executing ExecuteJavaScriptF Keyword");
		// extract the test data
		try {
			String searchIconEnabled;
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			searchIconEnabled = (String) js.executeScript(data_column_name);
			Thread.sleep(1000);
			if (searchIconEnabled != "true") {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing ExecuteJavaScriptF keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	public static String mobile_number() {

		APPICATION_LOGS.debug("Executing mobile_number Keyword");
		// extract the test data
		try {
			String date2 = (new SimpleDateFormat("mmhhyyssss")).format(new Date());
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).sendKeys(date2);
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(date2);
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(date2);
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(date2);
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(date2);
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(date2);
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(date2);
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(date2);
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).sendKeys(date2);
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing mobile number -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}

	public static String doubleClick() {
		APPICATION_LOGS.debug("Executing DoubleClick Keyword");
		// extract the test data
		try {
			if ("id".equals(object_type)) {
				WebElement elementToInteractWith = driver.findElement(By.id(getResolvedObject()));
				Actions action = new Actions(driver);
				action.moveToElement(elementToInteractWith).doubleClick().perform();
			} else if ("xpath".equals(object_type)) {
				WebElement elementToInteractWith = driver.findElement(By.xpath(getResolvedObject()));
				Actions action = new Actions(driver);
				action.moveToElement(elementToInteractWith).doubleClick().perform();
			} else if ("containsText".equals(object_type)) {
				WebElement elementToInteractWith = driver
						.findElement(By.xpath("//span[contains(text(), '" + getResolvedObject() + "\')]"));
				Actions action = new Actions(driver);
				action.moveToElement(elementToInteractWith).doubleClick().perform();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while wrinting into InformDate -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}

	public static String rightClick() {
		APPICATION_LOGS.debug("Executing rightClick Keyword");
		// extract the test data
		try {
			if ("id".equals(object_type)) {
				WebElement elementToInteractWith = driver.findElement(By.id(getResolvedObject()));
				Actions action = new Actions(driver);
				action.contextClick(elementToInteractWith).perform();
			} else if ("xpath".equals(object_type)) {
				WebElement elementToInteractWith = driver.findElement(By.xpath(getResolvedObject()));
				Actions action = new Actions(driver);
				action.contextClick(elementToInteractWith).perform();
			} else if ("containsText".equals(object_type)) {
				WebElement elementToInteractWith = driver
						.findElement(By.xpath("//span[contains(text(), '" + getResolvedObject() + "\')]"));
				Actions action = new Actions(driver);
				action.contextClick(elementToInteractWith).perform();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing rightClick keyword -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}

	public static String scroll() {
		APPICATION_LOGS.debug("Executing scroll");
		// System.out.println("Executing scoll method");
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)", "");
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while scrolling -" + object + t.getMessage());
			return "Fail - scrolling Not Found";
		}

		return "Pass";

	}

	public static String scrollCustom() {
		APPICATION_LOGS.debug("Executing scrollCustom");
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + data_column_name + ")", "");
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while scrolling -" + object + t.getMessage());
			return "Fail - scrollCustom Not Found";
		}

		return "Pass";

	}

	public static String customScrolls() {
		APPICATION_LOGS.debug("Executing customScrolls Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("scrollDown".equals(object_type)) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + data_column_name + ")", "");
			} else if ("scrollUp".equals(object_type)) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(" + data_column_name + ", 0)", "");
			} else if ("scrollRight".equals(object_type)) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(" + data_column_name + ", 0)", "");
			} else if ("scrollLeft".equals(object_type)) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(-" + data_column_name + ", 0)", "");
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while verifyElementAbsent -" + object + t.getMessage());
			return "Pass";
		}
		return "Fail";
	}

	public static String verifyElementAbsent() {
		APPICATION_LOGS.debug("Executing verifyElementAbsent Keyword");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("id".equals(object_type)) {
				driver.findElement(By.id(getResolvedObject())).click();
			} else if ("xpath".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).click();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).click();
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).click();
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).click();
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).click();
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).click();
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).click();
			} else if ("containTitle".equals(object_type)) {
				driver.findElement(By.xpath("//*[contains(@title, '" + getResolvedObject() + "\')]")).click();
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while verifyElementAbsent -" + object + t.getMessage());
			return "Pass";
		}
		return "Fail";
	}

	public static String waitUntilFileToDownload() throws InterruptedException {
		try {
			File directory = new File("c:\\download");
			boolean downloadinFilePresence = false;
			File[] filesList = null;
			LOOP: while (true) {
				filesList = directory.listFiles();
				for (File file : filesList) {
					downloadinFilePresence = file.getName().startsWith("mblack_");
				}
				if (downloadinFilePresence) {
					for (; downloadinFilePresence;) {
						sleep(5);
						continue LOOP;
					}
				} else {
					break;
				}
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while scrolling -" + object + t.getMessage());
			return "Fail - Close Not Found";
		}
		return "Pass";
	}

	private static void sleep(int i) {
		// TODO Auto-generated method stub

	}

	public static String unzip() {
		try {
			ZipFile zipFile = new ZipFile("C://download//" + getResolvedObject());
			Enumeration<?> enu = zipFile.entries();
			PrintStream o = new PrintStream(new File(data_column_name + "Actual.txt"));
			PrintStream console = System.out;
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.setOut(o);
				System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", name, size, compressedSize);
				System.setOut(console);
				File file = new File(name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Pass";
	}

	public static String compareTextFiles() throws IOException {
		BufferedReader reader1 = new BufferedReader(new FileReader(data_column_name + "Actual.txt"));

		BufferedReader reader2 = new BufferedReader(new FileReader(data_column_name + "Expected.txt"));

		String line1 = reader1.readLine();

		String line2 = reader2.readLine();

		boolean areEqual = true;

		int lineNum = 1;

		while (line1 != null || line2 != null) {
			if (line1 == null || line2 == null) {
				areEqual = false;

				break;
			} else if (!line1.equalsIgnoreCase(line2)) {
				areEqual = false;

				break;
			}

			line1 = reader1.readLine();

			line2 = reader2.readLine();

			lineNum++;
		}

		if (areEqual) {
			System.out.println("Two files have same content.");
			return "Pass";
		} else {
			System.out.println("Two files have different content. They differ at line " + lineNum);

			System.out.println("File1 has " + line1 + " and File2 has " + line2 + " at line " + lineNum);
			APPICATION_LOGS.debug("Two files have different content. They differ at line " + lineNum);
			APPICATION_LOGS.debug("File1 has " + line1 + " and File2 has " + line2 + " at line " + lineNum);
		}
		return "Fail";
	}

	public static String Keys() {
		APPICATION_LOGS.debug("Executing click Keys");
		if (null == object_type || "".equals(object_type)) {
			return "Fail - object type null or blank";
		}
		try {
			if ("xpath_SendKeysWithEnter".equals(object_type)) {
				driver.findElement(By.xpath(getResolvedObject())).sendKeys(getModifiedData(data_column_name),
						Keys.ENTER);
			} else if ("xpath_Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.xpath(getResolvedObject()));
				test.click();
				test.sendKeys(Keys.CONTROL, Keys.END, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE);
			} else if ("xpath_3Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.xpath(getResolvedObject()));
				test.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE);
			} else if ("id_300Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.id(getResolvedObject()));
				test.sendKeys(Keys.CONTROL, Keys.END, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE);
			} else if ("id_20Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.id(getResolvedObject()));
				test.sendKeys(Keys.CONTROL, Keys.END, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE);
			} else if ("xpath_20Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.xpath(getResolvedObject()));
				test.sendKeys(Keys.CONTROL, Keys.END, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE);
			} else if ("xpath_300Backspace".equals(object_type)) {
				WebElement test = driver.findElement(By.xpath(getResolvedObject()));
				test.sendKeys(Keys.CONTROL, Keys.END, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
						Keys.BACK_SPACE);
			}
		} catch (Throwable t) {
			APPICATION_LOGS.debug("Error while Keys -" + object + t.getMessage());
			return "Fail - " + t.getMessage();
		}

		return "Pass";
	}

	public String executebatch_file() throws InterruptedException {
		APPICATION_LOGS.debug("Executing executebatch_file() keyword");
		try {
			String[] cmd = { "cmd", "/c", "start", data_column_name };
			Runtime.getRuntime().exec(cmd);
		} catch (Throwable t) {
			t.printStackTrace();
			// report error
			APPICATION_LOGS.debug("Error while executebatch_file -" + object + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	// keyword "verifyImageLoaded" as a validation function to check if a image
	// is loaded/ displayed or not
	public static String verifyImageLoaded() {
		APPICATION_LOGS.debug("Executing isImageLoaded() keyword");
		try {
			WebElement ImageFile = driver.findElement(By.xpath(getResolvedObject()));
			Boolean ImagePresent = (Boolean) ((JavascriptExecutor) driver).executeScript(
					"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
					ImageFile);
			if (ImagePresent) {
				return "Pass";
			}
		} catch (Throwable t) {
			t.printStackTrace();
			APPICATION_LOGS.debug("Error while Keys -" + object + t.getMessage());
			return "Fail";
		}

		return "Fail";
	}

	public static String renameFile() {
		String folderName = "C://download//"; // Give your folderName
		File[] listFiles = new File(folderName).listFiles();

		for (int i = 0; i < listFiles.length; i++) {

			if (listFiles[i].isFile()) {
				String fileName = listFiles[i].getName();
				if (fileName.startsWith(getResolvedObject())) {
					System.out.println("found file" + " " + fileName);

					File oldfile = new File(folderName + fileName);
					File newfile = new File(folderName + data_column_name);

					if (oldfile.renameTo(newfile)) {
						System.out.println("Rename succesful");
						return "Pass";
					} else {
						System.out.println("Rename failed");
						return "Fail";
					}
				}
			}
		}
		return folderName;

	}

	@SuppressWarnings("unchecked")
	public static String sql() {
		APPICATION_LOGS.debug("Executing DB Connect");
		try {
			Connection conn = null;
			String url = CONFIG.getProperty(object + "." + Constants.DB_URL);
			String dbName = CONFIG.getProperty(object + "." + Constants.DB_NAME);
			String dbdriver = CONFIG.getProperty(object + "." + Constants.DB_DRIVER);
			String userName = CONFIG.getProperty(object + "." + Constants.DB_USERNAME);
			String password = CONFIG.getProperty(object + "." + Constants.DB_PASSWORD);

			JSONObject sqlMapping = new JSONObject(data_column_name);
			String sql = sqlMapping.getString(Constants.SQL_Constants.SQL);
			String rqMapStr = sqlMapping.optString(Constants.SQL_Constants.RQ_MAP,
					Constants.SQL_Constants.DEFAULT_OBJECT);
			String rsMapStr = sqlMapping.optString(Constants.SQL_Constants.RS_MAP,
					Constants.SQL_Constants.DEFAULT_OBJECT);
			JSONObject rqMap = new JSONObject(rqMapStr);
			JSONObject rsMap = new JSONObject(rsMapStr);

			sql = processPlaceholders(sql, rqMap);

			// create db connection and execute the query
			Class.forName(dbdriver);
			conn = DriverManager.getConnection(url + dbName, userName, password);

			Statement stmt = conn.createStatement();
			boolean isSelect = stmt.execute(sql);
			if (isSelect) {
				ResultSet rs = stmt.getResultSet();

				/* define how much data is to be read */

				List<String> rsFieldNames = new ArrayList<String>();
				Iterator<String> rsFieldNameIter = rsMap.keys();
				while (rsFieldNameIter.hasNext()) {
					String columnName = rsFieldNameIter.next();
					rsFieldNames.add(columnName);
				}
				if (rsFieldNames.isEmpty()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					int colCount = rsmd.getColumnCount();
					for (int i = 1; i <= colCount; i++) {
						rsFieldNames.add(rsmd.getColumnName(i));
						rsMap.put(rsmd.getColumnName(i), rsmd.getColumnName(i));
					}
				}
				if (rs.next()) {
					for (String fieldName : rsFieldNames) {
						map.put(String.valueOf(rsMap.get(fieldName)), String.valueOf(rs.getObject(fieldName)));
					}
					writeMappedValuesToFile();
				}

			} else {
				writeMappedValuesToFile(rsMap.optString("UpdateCount", "UpdateCount"),
						String.valueOf(stmt.getUpdateCount()));
			}

			conn.close();
		} catch (Throwable t) {
			APPICATION_LOGS.debug("Error while connecting DB", t);
			return "Fail - " + t.getMessage();
		}
		return "Pass";
	}

	/**
	 * Process placeholders by values.
	 * 
	 * @param sourceString
	 * @param keyMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String processPlaceholders(String sourceString, JSONObject keyMap) {
		Iterator<String> rqFields = keyMap.keys();
		while (rqFields.hasNext()) {
			String rqField = rqFields.next();
			String placeHolder = "${" + rqField + "}";
			while (sourceString.indexOf(placeHolder) >= 0) {
				String rqValue = keyMap.optString(rqField);
				if (rqValue.startsWith("${") && rqValue.endsWith("}")) {
					/*
					 * indicates that we want to pull data from saved property
					 */
					rqValue = rqValue.replaceAll("\\$\\{", "").replaceAll("\\}", "");
					rqValue = map.get(rqValue);
					rqValue = "'" + rqValue + "'";
				}
				sourceString = sourceString.replace(placeHolder, rqValue);
			}
		}
		return sourceString;
	}

	public static String countCharactersOfTextBox() {
		APPICATION_LOGS.debug("Executing countCharactersOfTextBox Keyword");
		String expected = data_column_name;
		int expectedNumber = Integer.parseInt(expected);
		int actualNumber = 0;
		String size = null;
		int i = 0;
		try {
			if ("id".equals(object_type)) {
				size = driver.findElement(By.id(getResolvedObject())).getAttribute("value");
				actualNumber = size.length();
			} else if ("xpath".equals(object_type)) {
				size = driver.findElement(By.xpath(getResolvedObject())).getAttribute("value");
				actualNumber = size.length();
			} else if ("name".equals(object_type)) {
				driver.findElement(By.name(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("css".equals(object_type)) {
				driver.findElement(By.cssSelector(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("linkText".equals(object_type)) {
				driver.findElement(By.linkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("partialLinkText".equals(object_type)) {
				driver.findElement(By.partialLinkText(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("className".equals(object_type)) {
				driver.findElement(By.className(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			} else if ("tagName".equals(object_type)) {
				driver.findElement(By.tagName(getResolvedObject())).sendKeys(getModifiedData(data_column_name));
			}
			APPICATION_LOGS.debug(expectedNumber);
			APPICATION_LOGS.debug(actualNumber);
			if (expectedNumber == actualNumber) {
				return "Pass";
			} else {
				return "Fail as " + "Expected=" + expectedNumber + " " + "but Actual=" + actualNumber;
			}
		} catch (Throwable t) {
			// report error
			APPICATION_LOGS.debug("Error while executing readText -" + object + t.getMessage());
			// return "Fail - " + t.getMessage();
			return "Fail as " + "Expected=" + expectedNumber + " " + "but Actual=" + actualNumber;
		}
	}

}