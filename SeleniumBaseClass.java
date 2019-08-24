package ReusableUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import Modules.Entity.Entity;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

@Listeners({ ConfigurationListener.class, MethodListener.class })
public class SeleniumBaseClass{

	//public String mergedObjMapPath = mergedObjLibraryPath();	
	public String objmapPath = System.getProperty("user.dir")+"\\src\\test\\java\\ObjectLib\\objectMap.properties";	
	public String logfilePath = "C://UleafSelenium/logs/";
	public String testDataFN = readConfigFile("RegFileName");
	public String testDataFilePath = System.getProperty("user.dir") + "\\" + testDataFN;
	public String incidentIdFull = getCurrentIncidentIDFull();
	public String baseWindow;

	protected String username = getUserName();
	protected String usernameUpperCase = username.toUpperCase();

	public static final String HOMEPAGE_URL = "/group/holmes/home";

	/*protected String username;*/
	protected String password = getPassword();
	public String baseUrl = readConfigFile("TestRunAppURL");
	public WebDriver driver;
	public SoftAssert softAssert;
	public int softAssertCount = 0;
	public static Alert alert = null;
	private static boolean acceptNextAlert = true;

	{
		System.setProperty("atu.reporter.config", System.getProperty("user.dir") + "\\Resources\\atu\\atu.properties");
	}

	//public ObjectLib map = new ObjectLib(mergedObjMapPath);
	public ObjectLib map = new ObjectLib(objmapPath);

	private static final String JQUERY_LOAD_SCRIPT =System.getProperty("user.dir") + "\\Resources\\Jqueries\\Jquerify.js";
	public String jQueryLoader;
	public JavascriptExecutor jQuery;

	/*public String mergedObjLibraryPath(){
		try{
			MergeObjectMapPropertyFiles mergedObjMap = new MergeObjectMapPropertyFiles();
			mergedObjMapPath = mergedObjMap.mergeObjectFiles();

		}
		catch(Exception e){
		}
		return mergedObjMapPath; 

	}*/


	public void openBrowserLaunhApp() 
	{
		try{
			baseUrl=readConfigFile("TestRunAppURL");			
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile Uleafprofile = profile.getProfile("Selenium_User");					
			/*final String firebugPath = "F:\\Projects\\Uleaf\\Software\\Firefox Selenium Plugins\\extensions\\firebug@software.joehewitt.com.xpi";
			final String fireXpath = "F:\\Projects\\Uleaf\\Software\\Firefox Selenium Plugins\\extensions\\FireXPath@pierre.tholence.com.xpi";
			Uleafprofile.addExtension(new File(firebugPath));
			Uleafprofile.addExtension(new File(fireXpath));*/
		//	driver = new FirefoxDriver(Uleafprofile);
			ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
			appSpecificMethods.handleSecurityWindow3();
			sleep(3000);
			driver.get(baseUrl);
			sleep(2000);			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().pageLoadTimeout(getPageLoadWaitTime(), TimeUnit.SECONDS);
			//			driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);			
			Report.pass("Open Browser and Application is launched");	

		}
		catch(Exception e)
		{			
			Report.fail("Application is not launched " + e);			
		}
	}	

	public void openIEBrowserLaunhApp() 
	{
		try{
			baseUrl=readConfigFile("TestRunAppURL");

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// to clear cache in IE
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("unexpectedAlertBehaviour", "accept");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("disable-popup-blocking", true);
			capabilities.setCapability("enablePersistentHover", true);

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\Resources\\Drivers\\IEDriverServer1.exe");
			driver = new InternetExplorerDriver(capabilities);
			ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
			appSpecificMethods.handleSecurityWindow1();
			sleep(3000);
			driver.get(baseUrl);


			sleep(2000);			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			//			driver.manage().timeouts().implicitlyWait(getTotalWaitTimeInSecs(),TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(getPageLoadWaitTime(), TimeUnit.SECONDS);
			Report.pass("Opened IEBrowser and Application is launched");

		}
		catch(Exception e)
		{			
			Report.fail("Application is not launched " + e);			
		}
	}	

	public void openIEBrowserLaunhAppThroughSeleniumGrid(String testAppURL) 
	{
		try{


			baseUrl=testAppURL;

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// to clear cache in IE
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("unexpectedAlertBehaviour", "accept");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("disable-popup-blocking", true);
			capabilities.setCapability("enablePersistentHover", true);

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\Resources\\Drivers\\IEDriverServer1.exe");

			driver = new RemoteWebDriver(new URL(readConfigFile("RemoteWebdriverPath")),capabilities);
			ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
			appSpecificMethods.handleSecurityWindow1();
			driver.get(baseUrl);


			//driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			//			driver.manage().timeouts().implicitlyWait(getTotalWaitTimeInSecs(),TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(getPageLoadWaitTime(), TimeUnit.SECONDS);
			Report.pass("Opened IEBrowser and Application is launched");

			sleep(5000);	
			driver.navigate().to(baseUrl);

		}
		catch(Exception e)
		{			
			Report.fail("Application is not launched " + e);			
		}
	}	

	public void openIEBrowserLaunhAppOnRemoteMachine() 
	{
		try{


			baseUrl=readConfigFile("TestRunAppURL");

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// to clear cache in IE
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("unexpectedAlertBehaviour", "accept");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("disable-popup-blocking", true);
			capabilities.setCapability("enablePersistentHover", true);

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\Resources\\Drivers\\IEDriverServer1.exe");

			driver = new RemoteWebDriver(new URL(readConfigFile("RemoteWebdriverPath")),capabilities);
			ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
			appSpecificMethods.handleSecurityWindow1();
			driver.get(baseUrl);


			//driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			//			driver.manage().timeouts().implicitlyWait(getTotalWaitTimeInSecs(),TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(getPageLoadWaitTime(), TimeUnit.SECONDS);
			Report.pass("Opened IEBrowser and Application is launched");

		}
		catch(Exception e)
		{			
			Report.fail("Application is not launched " + e);			
		}
	}	
	
	public String getCurrentIncidentIDFull(){
		return readConfigFile("incidentID_full");
	}

	public void launchApp()
	{
		display_Execution_DateTime();
		openBrowserLaunhApp();
	}

	public void launchAppInIE()
	{
		display_Execution_DateTime();
		openIEBrowserLaunhApp();
	}

	public void launchAppInIEOnRemote()
	{
		display_Execution_DateTime();
		openIEBrowserLaunhAppOnRemoteMachine();
	}
	
	public void launchAppThroughSeleniumGrid(String testAppURL){
		display_Execution_DateTime();
		openIEBrowserLaunhAppThroughSeleniumGrid(testAppURL);
	}

	
	public void closeApp() throws Exception
	{
		try {
			driver.close();
			Report.pass("Browser closed properly");
		} catch (Exception e) {
			Report.fail("Browser did not closed properly");
		}
	}


	public void clickAndAcceptAlert(String objname) {
		try
		{
			//				driver.findElement(map.getLocator(objname)).click();
			//				fluentWaitAndGetWebElementAndClick(getTotalWaitTimeInSecs(), getPoolWaitTime(), objname);
			/*			explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
				driver.findElement(map.getLocator(objname)).click();*/
			explicityWaitForAnElementAndClick(objname, getTotalWaitTimeInSecs());
			Report.pass("clicked on '"+objname+"'");
			try {
				driver.switchTo().alert().accept();
			} catch (Exception e) {
				System.out.println("unexpected alert not present");  
			}


		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}

	}


	public String readConfigFile(String value)
	{
		try{
			Properties props=new Properties();
			FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"\\Config.properties");
			props.load(fis);
			return props.getProperty(value);			
		}
		catch(Exception e)
		{
			Report.info("Config file is not read");
			return "";
		}
	}


	/*** Send console result to text file */
	public void logResult(String testScriptName){	
		try	{
			System.setOut(new  PrintStream(new FileOutputStream(logfilePath+"/"+testScriptName+".txt",true)));
		}
		catch(Exception E1)
		{
			Report.info("The Result file has not been found. Please check the path of Results File and Re-Execute");
		}
	}

	public void sleep(int miliSec) throws InterruptedException
	{
		Thread.sleep(miliSec);
	}

	/** print current system date and time*/
	public void display_Execution_DateTime()

	{
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy: HH:mm:ss");
		String myDateString = sdf.format(myDate);
		Report.info("............................");
		Reporter.log("............................");
		Report.info("Execution Date/Time:");
		Report.info(myDateString);
		Reporter.log("Execution Startes @ :" + myDateString);
		Report.info("............................");
		Reporter.log("............................");
	}

	/** Prints current time, seconds.This method is useful to append current time seconds with any string*/
	public String getSecondsStamp()
	{
		Date myDate = new Date();
		SimpleDateFormat seconds = new SimpleDateFormat("ss");
		String mySecondsString = seconds.format(myDate);
		return mySecondsString;
	}


	public String getCurrentDataStamp(String dateFormat)
	{
		Date myDate = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String mySecondsString = date.format(myDate);
		return mySecondsString;
	}

	//Get current date Time and add 1 day to get future date
	public String getTomorrowDateStamp(String dateFormat)
	{
		Date myDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(myDate); 
		c.add(Calendar.DATE, 1);
		myDate = c.getTime();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String mySecondsString = date.format(myDate);
		return mySecondsString;

	}

	/**
	 * Get past date and time using amount value.
	 * if you want 5 days old pass amount value as -5
	 * @param dateFormat
	 * @param amount
	 * @return
	 */
	public String getPastDateStamp(String dateFormat, int amount)
	{
		Date myDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(myDate); 
		c.add(Calendar.DATE, amount);
		myDate = c.getTime();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String mySecondsString = date.format(myDate);
		return mySecondsString;

	}

	//Get future Date in dd/MM/YYYY MM:SS Format
	public String getFutureDateStamp(String dateFormat)
	{
		Date myDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(myDate); 
		c.add(Calendar.DATE, 7);
		myDate = c.getTime();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String mySecondsString = date.format(myDate);
		return mySecondsString;

	}

	public String getCurrentDataStampwithoutSlash()
	{
		Date myDate = new Date();
		SimpleDateFormat date = new SimpleDateFormat("ddMMyyyyhhmmss");
		String mySecondsString = date.format(myDate);
		return mySecondsString;
	}
	
	public String getCurrentDataStampwithSlash()
	{
		Date myDate = new Date();
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		String mySecondsString = date.format(myDate);
		return mySecondsString;
	}
	public String randomChar()
	{
		final String alphabet = "0123456789ABCDE";
		final int N = alphabet.length();
		Random r = new Random();

		String randomString=Character.toString(alphabet.charAt(r.nextInt(N)));

		return randomString;
	}

	public String randomNum()
	{
		final String alphabet = "0123456789";
		final int N = alphabet.length();
		Random r = new Random();

		String randomString=Character.toString(alphabet.charAt(r.nextInt(N)));

		return randomString;
	}

	public String randomAlphaChar() {
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		return Character.toString(c);
	}

	public int generateRandomNumber() {
		Random rnum = new Random();
		return rnum.nextInt(500);
	}

	public void sendKeys(String objname, String value)
	{
		try
		{
			/*System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\AutoItfiles\\IEDriverServer32bit.exe");
			Report.info("System webdriver ie driver value is:" +System.getProperty("webdriver.ie.driver"));*/
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			/*e.click();
			e.clear();
			e.sendKeys(value);*/
			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			/*WebElement searchbox = driver.findElement(By.xpath("//input[@name='q']"));*/
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", e);

			Report.pass("'"+value+"' is entered in '"+objname+"'");
			/*System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\AutoItfiles\\IEDriverServer32bit.exe");
			Report.info("System webdriver ie driver value is:" +System.getProperty("webdriver.ie.driver"));*/
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void sendKeysWithDriverNoClick(String objname, String value)
	{
		try
		{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.clear();
			e.sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}


	public void sendKeysWithDriver(String objname, String value)
	{
		try
		{
			/*System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\AutoItfiles\\IEDriverServer32bit.exe");
			Report.info("System webdriver ie driver value is:" +System.getProperty("webdriver.ie.driver"));*/
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			e.sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void sendKeysAndEnter(String objname, String value)
	{
		try
		{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			/*e.sendKeys(value);*/
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", e);
			e.sendKeys(Keys.RETURN);

			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{	e.printStackTrace();
		Report.fail("element '"+objname+"' is not displayed");
		Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void sendKeysAndKeyDown(String objname, String value)
	{
		try
		{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			/*e.sendKeys(value);*/
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", e);
			e.sendKeys(Keys.DOWN);

			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void sendKeysAndArrowDown(String objname, String value)
	{
		try
		{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			/*e.sendKeys(value);*/
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", e);
			String selectAll = Keys.chord(Keys.CONTROL, "a");
			e.sendKeys(selectAll);
			sleep(4000);
			Actions builder = new Actions(driver);
			/*Action arrowdown = builder.keyDown(Keys.ARROW_DOWN).build();
			arrowdown.perform();*/

			Action moveCursorLeft = builder.moveToElement(e).moveByOffset(-2, 0).build();
			moveCursorLeft.perform();
			/*e.sendKeys(Keys.CONTROL);
			e.sendKeys(Keys.ARROW_DOWN);*/

			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void sendKeysAndEnterWithDriver(String objname, String value)
	{
		try
		{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			e.sendKeys(value);
			e.sendKeys(Keys.RETURN);

			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}
	public void sendKeysAndTab(String objname, String value){
		try{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			/*e.sendKeys(value + Keys.TAB);*/
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", e);
			e.sendKeys(Keys.TAB);
			e.sendKeys(Keys.RETURN);

			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		} catch(Exception e){
			Report.fail("element '"+ objname +"' is not displayed");
			Assert.fail("element '"+ objname +"' is not displayed");
		}
	}

	public void sendKeysAndTabWithDriver(String objname, String value){
		try{
			WebElement e = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			e.click();
			e.clear();
			e.sendKeys(value + Keys.TAB);
			e.sendKeys(Keys.TAB);
			e.sendKeys(Keys.RETURN);
			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("'"+value+"' is entered in '"+objname+"'");
		} catch(Exception e){
			Report.fail("element '"+ objname +"' is not displayed");
			Assert.fail("element '"+ objname +"' is not displayed");
		}
	}

	public void selectOptionWithText(String textToSelect, String autoCompleteDivIdentiferInfo) {
		try {

			Report.info("Option to select:'" + textToSelect + "'" + " Or '" + textToSelect.toLowerCase() + "'" + " Or '" + textToSelect.toUpperCase() + "'" );
			WebElement autoOptions = explicitlyWaitAndGetWebElement(autoCompleteDivIdentiferInfo, getTotalWaitTimeInSecs());
			WebDriverWait wait = new WebDriverWait(driver,	getTotalWaitTimeInSecs());
			wait.until(ExpectedConditions.visibilityOf(autoOptions));
			List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("div"));
			// To find the different suggestion values
			for(WebElement option : optionsToSelect){
				Report.info("Values: "+ "'" + option.getAttribute("val") + "'");
			}
			for(WebElement option : optionsToSelect){
				String attVal = option.getAttribute("val");
				/*Report.info("Att Value '" + attVal + "'");
				Report.info("value evalution '" + attVal.contains(textToSelect));
				Report.info("value evalution '" + attVal.contains(textToSelect.toLowerCase()));
				Report.info("value evalution '" + attVal.contains(textToSelect.toUpperCase()));
				Report.info("value evalution '" + textToSelect.equalsIgnoreCase(attVal));*/
				if(attVal.contains(textToSelect)	|| 
						attVal.contains(textToSelect.toLowerCase()) || 
						attVal.contains(textToSelect.toUpperCase()) || 
						textToSelect.equalsIgnoreCase(attVal)) {
					Report.info("Trying to select: "+textToSelect);
					option.click();
					Report.info("Clicked the required option");
					break;
				}
			}

		} catch (NoSuchElementException e) {
			System.out.println(e.getStackTrace());
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void doubleClickAndWait(String objname){
		try{
			explicityWaitForAnElementAndDoubleClick(objname, getTotalWaitTimeInSecs());
			Report.pass("Double Clicked on '"+ objname +"'");
			waitForPageLoad();
		}catch(Exception e){
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}


	public void doubleClick(String objname)
	{
		try
		{
			explicityWaitForAnElementAndDoubleClick(objname, getTotalWaitTimeInSecs());
			Report.pass("Double Clicked on '"+ objname +"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}


	public void click(String objname)
	{
		try
		{
			//			driver.findElement(map.getLocator(objname)).click();
			//			fluentWaitAndGetWebElementAndClick(getTotalWaitTimeInSecs(), getPoolWaitTime(), objname);
			/*			explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
			driver.findElement(map.getLocator(objname)).click();*/
			explicityWaitForAnElementAndClick(objname, getTotalWaitTimeInSecs());
			Report.pass("clicked on '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}
	
	 //This method clicks on element
	 //element identifier is directly passed
	 public void clickElement(String identifier_xpath) throws Exception
     {
    	 try{
    		 explicitlyWaitForDynamicElement(identifier_xpath, getTotalWaitTimeInSecs());
    		 driver.findElement(By.xpath(identifier_xpath)).click();
    	 }catch(Exception e){
    		 Report.fail("element '"+identifier_xpath+"' is not displayed");
 			 Assert.fail("element '"+identifier_xpath+"' is not displayed");
    	 }
     }

	public void sendKeyEnterWithOutTargetElement()
	{
		try
		{
			//			pressReturnKey("button_Entity_Search_body");
			WebElement e = explicitlyWaitAndGetWebElement("button_Entity_Search_body", getTotalWaitTimeInSecs());


			/*JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].value='Keys.ENTER';",e);*/


			e.submit();
			/*e.click();*/
			/*e.sendKeys(Keys.TAB);
			e.sendKeys(Keys.ENTER);*/
			//			driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);

			/*Actions builder = new Actions(driver);
			builder.keyDown(Keys.RETURN).keyUp(Keys.RETURN).build().perform();*/

			Report.pass("clicked on default focused elment using Enter key");
		}
		catch(Exception e)
		{
			Report.fail("Failed to click on default focused element with enter key");
			Assert.fail("Failed to click on default focused element with enter key");
		}
	}

	public void clickAndWait(String objname)
	{
		try
		{
			//			fluentWaitAndGetWebElementAndClick(getTotalWaitTimeInSecs(), getPoolWaitTime(), objname);
			explicityWaitForAnElementAndClick(objname, getTotalWaitTimeInSecs());
			//			driver.findElement(map.getLocator(objname)).click();
			Report.pass("clicked on '"+objname+"'");
			waitForJQuery(driver);

		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void clickAndWaitForPageLoad(String objname)
	{
		try
		{
			explicityWaitForAnElementAndClick(objname, getTotalWaitTimeInSecs());
			//			driver.findElement(map.getLocator(objname)).click();
			Report.pass("clicked on '"+objname+"'");
			waitForPageLoad();

		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public void clickMenu(String objname)
	{
		try
		{
			//			driver.findElement(map.getLocator(objname)).click();
			//			fluentWaitAndGetWebElementAndClick(getTotalWaitTimeInSecs(), getPoolWaitTime(), objname);
			/*			explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
			driver.findElement(map.getLocator(objname)).click();*/
			explicityWaitForAnElementAndClick(objname, getTotalWaitTimeInSecs());
			Report.pass("clicked on '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}


	/*	protected boolean IsElementPresent(String objname) throws Exception {

		try {
			driver.findElement(map.getLocator(objname));
			return true;
		} catch (NoSuchElementException e)
		{
			return false;
		}
	}*/

	public boolean isElementPresent(String objname) throws Exception {

		try {
			if(driver.findElement(map.getLocator(objname)) != null){
				//Report.info("Element '" + objname + "' is present");
				return true;
			}else{
				Report.info("Element '" + objname + "' is NOT present");
				return false;
			}

		} catch (NoSuchElementException e){
			return false;
		} catch (Exception e){
			return false;
		}
	}

	public boolean isElementPresent(WebElement element) {
		boolean flag = false;
		try {
			if (element.isDisplayed() || element.isEnabled())
				flag = true;
			Report.info("Element '" + element + "' is present");
		} catch (NoSuchElementException e) {
			flag = false;
			//			e.printStackTrace();
			Report.fail("Element '" + element + "' is NOT present");
		} catch (StaleElementReferenceException e) {
			flag = false;
			//			e.printStackTrace();
			Report.fail("Element '" + element + "' is NOT present");
		}
		return flag;
	}

	public boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		String tagName = element.getTagName();
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				Report.info("Attribute '" + attribute + "' for Element '" + tagName + "' is present");
				result = true;
			}
		} catch (Exception e) {
			Report.info("Got in exception for the is attribute present");
		}	    
		Report.info("Attribute '" + attribute + "' for Element '" + tagName + "' is NOT present");
		return result;
	}

	public boolean isAttribtuePresent(String objName, String attribute) {
		Boolean result = false;
		WebElement element = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
		String tagName = "";
		try {
			tagName = element.getTagName();
		} catch (StaleElementReferenceException e1) {
			//e1.printStackTrace();
			element = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
			tagName = element.getTagName();
		} catch (Exception e1) {
			//e1.printStackTrace();
			Report.warn("could not able to get the TagName");
		}
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				Report.info("Attribute '" + attribute + "' for Element '" + tagName + "' is present");
				result = true;
			}
		} catch (Exception e) {
			Report.info("Got in exception for the is attribute present");
		}	    
		Report.info("Attribute '" + attribute + "' for Element '" + tagName + "' is NOT present");
		return result;
	}

	public boolean verifyElementDisplayed(String objname) throws Exception {

		try {
			WebElement element = driver.findElement(map.getLocator(objname));
			if(element != null && element.isDisplayed()){
				Report.info("Element '" + objname + "' is displayed");
				return true;
			}else{
				Report.info("Element '" + objname + "' is NOT displayed");
				Report.fail("Element '" + objname + "' is NOT displayed");
				return false;
			}

		} catch (NoSuchElementException e){
			return false;
		}
	}
	
	public boolean verifyElementNotDisplayed(String objname) throws Exception {

		try {
			WebElement element = driver.findElement(map.getLocator(objname));
			if( !(element.isDisplayed())){
				Report.info("Element '" + objname + "' is Not displayed");
				return true;
			}else{
				Report.info("Element '" + objname + "' is displayed");
				Report.fail("Element '" + objname + "' is displayed");				
				return false;
			}

		} catch (NoSuchElementException e){
			return false;
		}
	}



	public void assertTrue (String objname) throws Exception{
		try{
			Assert.assertTrue(isElementPresent(objname));
			Report.pass("'"+objname+"' is Present");
		} catch (Error e){
			//			Report.fail("'"+objname+"' is not Present");
			Assert.fail("'"+objname+"' is not Present");
		}

	}

	public void verifyTrue (String objname) throws Exception{
		softAssert.assertTrue(isElementPresent(objname));
		if(isElementPresent(objname)){
			Report.pass("'"+objname+"' is Present");
		} else {
			softAssertCount++;
			Report.warn("'"+objname+"' is not Present");
			softAssert.fail("'"+objname+"' is not Present");
		}

	}

	public boolean isElementDisplayed(String objName) {
		try {
			if (driver.findElement(map.getLocator(objName)).isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Report.fail("Locator type is not defined!!");
		}
		return false;
	}

	public void verifyElementPresent(String objname) throws Exception {
		try{
			verifyTrue(objname);
		} 
		catch (Error e){
			Report.fail("'" + objname + "' is not Present");
			//verificationErrors.append(e.toString());
		}
	}

	public boolean verifyNonObjectMapElementDisplayed(String eleIndentifier, String identifierType) throws Exception {


		try {
			WebElement element;
			if (identifierType.equals("id")) {
				element = driver.findElement(By.id(eleIndentifier));
			}else if(identifierType.equals("xpath")) {
				element = driver.findElement(By.xpath(eleIndentifier));
			}else if (identifierType.equals("name")) {
				element = driver.findElement(By.name(eleIndentifier));
			}else if (identifierType.equals("class")) {
				element=driver.findElement(By.className(eleIndentifier));
			}else{
				throw new Exception("identifier type '" + identifierType + "'is not defined!!");
			}
			if(element != null && element.isDisplayed()){
				Report.info("Element '" + eleIndentifier + "' is displayed");
				return true;
			}else{
				Report.info("Element '" + eleIndentifier + "' is NOT displayed");
				return false;
			}

		} catch (NoSuchElementException e){
			return false;
		}
	}
	
	public void assertElementPresent(String objname) throws Exception {
		try 
		{
			assertTrue(objname);
		} 
		catch (Error e)
		{
			Report.fail("'" + objname + "' is not displayed");
			//verificationErrors.append(e.toString());
		}
	}

	public void verifyElementIsMandatory(String objname) throws Exception {
		explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
		if (getText(objname).contains("*")) {
			Report.pass("The element '"+ objname +"' is marked as mandatory");	
		} else{
			Report.warn("'"+ objname +"' is Not marked as mandatory: "+ getText(objname));
		}
	}

	public void verifyElementNotPresent(String objname) throws Exception {
		Boolean flag = false;		

		try{
			flag = (isElementPresent(objname));
			if (flag==true){
				Report.fail("'"+objname+"' is displayed");
			}else
			{
				Report.pass("'" + objname + "' is not displayed");
			}

		} 
		catch (Error e)
		{
			Report.pass("'" + objname + "' is not displayed");
			//verificationErrors.append(e.toString());
		}
	}

	/** getText method (objname = the object name from objectmap file) */
	public String getText(String objname) throws Exception{
		//		WebElement element = driver.findElement(map.getLocator(objname));
		WebElement element = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
		/*WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.elementToBeClickable(element));*/

		/*.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element))*/

		/*new WebDriverWait(driver, getTotalWaitTimeInSecs())
		.ignoring(StaleElementReferenceException.class)		
		.until(new Predicate<WebDriver>(){
			@Override
			public boolean apply(@Nullable WebDriver driver) {
				try {
					driver.findElement(map.getLocator(objname)).getText();
				} catch (Exception e) {
					//e.printStackTrace();
					Report.fail("Could not able to find the element in wait time: '"+ objname + "' " + getTotalWaitTimeInSecs() + " Seconds");
					Assert.fail("Could not able to find the element in wait time: '"+ objname + "' " +getTotalWaitTimeInSecs() + " Seconds");

				}
				return true;
			}

		});*/

		String tagName = explicitlyWaitAndGetWebElementTagName(objname, getTotalWaitTimeInSecs());
		String elementText = explicitlyWaitAndGetWebElementText(objname, getTotalWaitTimeInSecs());

		if(tagName.equals("input")){
			Report.info(element.getTagName() + "," + element.getAttribute("type") + ",");
			if((elementText.trim().equals("")) && (element.getAttribute("type").equals("text"))){
				return element.getAttribute("value");
			} else{
				return elementText;
			}
		} else {
			return elementText;
		}
	}

	public String getTextFrmTextfieldWhileInserting(String objname) throws Exception{
		WebElement element = driver.findElement(map.getLocator(objname));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript(" return jQuery(arguments[0]).contents().filter(function() { return this.nodeType == Node.TEXT_NODE;}).text();", element);
	}

	public void validateAllVerificationPoints() {
		if (softAssert == null) {
			softAssert = new SoftAssert();
			softAssert.assertAll();

		} else{
			try {
				softAssert.assertAll();
			} catch (AssertionError e) {
				if(softAssertCount > 0){
					try {
						e.printStackTrace();
						Assert.fail("Failed because of a warning noticed Above:");
					} catch (Exception e2) {
					}
				}
			}
		}
	}

	/** verifyTextPresent method (objname = the object name from objectmap file) */
	public void verifyText(String string_to_compare, String objname) throws Exception{
		Report.info("string_to_compare="+string_to_compare);
		explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
		String elementText = getText(objname);
		System.out.println("elementText="+elementText);
		softAssert.assertEquals(string_to_compare, elementText,"Comparsion failed: expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
		if(string_to_compare.equals(elementText)){
			Report.pass("The text '"+string_to_compare+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("For object '"+ objname + "' expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
			softAssert.fail("For object '"+ objname + "' expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
		}
	}

	/**
	 * Used to compare element text with required text.
	 * @param string_to_compare is required text, and can be a subset text to @param elementText field
	 * @param elementText is usually the text content of an element. 
	 * @throws Exception
	 */
	public void verifyTextCompare(String string_to_compare, String elementText)throws Exception{
		softAssert.assertEquals(string_to_compare, elementText,"Comparsion failed: expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
		if(elementText.contains(string_to_compare)){
			Report.pass("The text '"+string_to_compare+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
			softAssert.fail("expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
		}
	}

	/**
	 * Used to compare element text with required text.
	 * @param string_to_compare is required text, and can be a subset text to @param elementText field
	 * @param elementText is usually the text content of an element. 
	 * @throws Exception
	 */
	public void verifyTextCompareText(String string_to_compare, String elementText)throws Exception{
		if(elementText.contains(string_to_compare)){
			Report.pass("The text '"+string_to_compare+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
			softAssert.fail("expected text '" + string_to_compare +"' is not same as actual text '" + elementText + "'");
		}
	}


	/** verifyTextPresent method (objname = the object name from objectmap file) */
	public void verifyTextNotEquals(String string_to_compare, String objname) throws Exception{
		explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
		String elementText = getText(objname);
		//softAssert.assertNotEquals(string_to_compare, elementText,"Comparsion failed: expected text '" + string_to_compare +"' is same as actual text '" + getText(objname) + "'");
		if(!string_to_compare.equals(elementText)){
			Report.pass("The text '"+string_to_compare+"' is not maching with " +  getText(objname)+"'");
		}else{
			softAssertCount++;
			Report.warn("For object '"+ objname + " expected text '" + string_to_compare +"' is same as actual text '" + getText(objname) + "'");
			softAssert.fail("For object '"+ objname + " expected text '" + string_to_compare +"' is same as actual text '" + getText(objname) + "'");
		}
	}

	public void verifyTextOnlyNotEquals(String string_to_compare, String elementText) throws Exception{
		//softAssert.assertNotEquals(string_to_compare, elementText,"Comparsion failed: expected text '" + string_to_compare +"' is same as element text '" + elementText + "'");
		System.out.println("elementText="+elementText);
		if(!string_to_compare.equals(elementText)){
			Report.pass("The text '"+string_to_compare+"' is not maching with " + elementText);
		}else{
			softAssertCount++;
			Report.warn("expected text '" + string_to_compare +"' is same as element text '" + elementText + "'");
			softAssert.fail(" expected text '" + string_to_compare +"' is same as element text '" + elementText + "'");
		}
	}
	/**
	 * 
	 * @param expectedElementColour : hexadecimal value of colour
	 * @param Objname : Objname of webelement
	 * @throws Exception
	 */
	public void verifyElementColour(String expectedElementColour, String Objname, String color)throws Exception{
		WebElement elementColr = explicitlyWaitAndGetWebElement(Objname, getTotalWaitTimeInSecs());
		String rgacolor = elementColr.getCssValue(color);
		String hexcolor = convertrgbaElementColour(rgacolor);
		softAssert.assertEquals(expectedElementColour, hexcolor,"Comparsion failed: expected colour '" + hexcolor +"' is not same as actual colour '" + hexcolor + "'");
		if(expectedElementColour.contains(hexcolor)){
			Report.pass("The colour '"+hexcolor+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("expected colour '" + expectedElementColour +"' is not same as actual colour '" + hexcolor + "'");
			softAssert.fail("expected colour '" + expectedElementColour +"' is not same as actual colour '" + hexcolor + "'");
		}
	}

	/**
	 * 
	 * @param rgbavalue : hexa decimal value from getCSSValue method and then convert it to expected value
	 * @return
	 */

	private String convertrgbaElementColour(String rgbavalue){

		String[] hexValue = rgbavalue.replace("rgba(", "").replace(")", "").split(",");
		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);
		//		Report.info(hexValue[1]);
		//		Report.info(hexValue[2]);
		//		Report.info(hexValue[3]);
		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		return actualColor;
		//System.out.println(actualColor);

	}

	public void assertText(String string_to_compare, String objname) throws Exception{
		try {
			if (isElementPresent(objname)) {
				Assert.assertEquals(string_to_compare, getText(objname));
				Report.pass("The text '"+string_to_compare+"' is verified");
			} else {
				explicitlyWaitForElement(objname, getTotalWaitTimeInSecs());
				Assert.assertEquals(string_to_compare, getText(objname));
				Report.pass("The text '"+string_to_compare+"' is verified");
			}

		} 
		catch (Error e) 
		{
			Report.fail("'"+ objname +"' is not as expected and found to be as: "+ getText(objname));
			Assert.fail("'"+ objname +"' is not as expected and found to be as: "+ getText(objname));

		}
	}

	/** verifyTextPresent method (objname = the object name from objectmap file) */
	public void verifyTextPresent(String string_to_compare, String objname, long waitTimeInSec) throws Exception
	{
		try {
			//			Assert.assertEquals(string_to_compare, GetText(objname));
			Assert.assertEquals(string_to_compare, explicitlyWaitAndGetWebElementText(objname, waitTimeInSec));
			Report.pass("The text '"+string_to_compare+"' is verified");
		} 
		catch (Error e) 
		{
			Report.fail("'"+objname+"' is not as expected and found to be as:"+ getText(objname));

		}
	}


	/** verifyTextContains method (objname = the object name from objectmap file) */
	public void verifyTextContains(String string_to_compare, String objname) throws Exception
	{
		String actualText = "";
		
			actualText = getText(objname);
			if(actualText.contains(string_to_compare)){
			Report.pass("The text '"+string_to_compare+"' is verified");
			}
			else {
				softAssertCount++;
				Report.fail("'"+objname+"' is not as expected" + "Expected Text:'" + string_to_compare + "' ActualText:'" + actualText + "'");
				softAssert.fail("For object '"+ objname + "' expected text '" + string_to_compare +"' is not same as actual text '" + actualText + "'");
			}
			
	}

	

	public String getTitle(){

		String title=driver.getTitle();
		return title;
	}

	public void verifyTitle(String title_to_be_matched) throws Exception
	{
		String actulTitel=getTitle();

		try {
			Assert.assertEquals(actulTitel, title_to_be_matched);
			//actulTitel.equalsIgnoreCase(title_to_be_matched);
			Report.pass("The title '"+title_to_be_matched+"' is verified");
		} catch (Error e) {

			Report.fail("The given title: "+title_to_be_matched+" is not matched and found to be: "+actulTitel);
			Assert.fail("The given title: "+title_to_be_matched+" is not matched and found to be: "+actulTitel);
		}

	}

	public int getTableRowCount(String objname){

		try {
			return driver.findElements(map.getLocator(objname)).size();
		} catch (Exception e) {
			Report.fail("Table '"+objname+"' is not displayed");
			Assert.fail("Table '"+objname+"' is not displayed");
			return 0;
		}

	}

	public void verifyTableRowCount(String objname, int expValue ){
		int actualVaule=0;
		try {
			actualVaule = getTableRowCount(objname);
			Report.info("Table contains"+actualVaule+" rows");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Report.fail("Table '"+objname+"' is not displayed");
			Assert.fail("Table '"+objname+"' is not displayed");
		}
		if (actualVaule == expValue){

			Report.pass("Verified table rows and the Table contains:"+actualVaule+" row");
		} else {

			Report.fail("Actual table row count:"+actualVaule+" and expected table row count:"+expValue+" did not match");
			Assert.fail("Table '"+objname+"' is not displayed");
		}

	}

	public void waitForAlertTextAndClose(String text_tobe_verified){

		try {

			WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
			wait.until(ExpectedConditions.alertIsPresent());

			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			alert.accept();

			try {
				alertText.contains(text_tobe_verified);
				Report.pass("The text '"+text_tobe_verified+"' in the alert is verified");
			} catch (Error e) {

				Report.fail("The given text : "+text_tobe_verified+" is not matched in the alert or the alert may not be present");
			}
		} catch (Error e) {
			Report.fail("Alert is not present to verify: "+text_tobe_verified);
		}

	}

	public void waitForAlertTextAndDismiss(String text_tobe_verified){

		try {

			WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
			wait.until(ExpectedConditions.alertIsPresent());

			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			alert.dismiss();

			try {
				alertText.contains(text_tobe_verified);
				Report.pass("The text '"+text_tobe_verified+"' in the alert is verified");
			} catch (Error e) {

				Report.fail("The given text : "+text_tobe_verified+" is not matched in the alert or the alert may not be present");
			}
		} catch (Error e) {
			Report.fail("Alert is not present to verify: "+text_tobe_verified);
		}

	}

	public boolean isAlertPresent(){

		boolean ispresent = false;

		try{
			alert = driver.switchTo().alert();
			ispresent= true;
		} catch(NoAlertPresentException e){
			ispresent = false;
		}
		return ispresent;
	}

	/**
	 * <p>
	 * The method finds existence of an Alert in the web page, and returns 
	 * either true or false
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @return Returns boolean true or false
	 */	
	public boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	/**
	 * <p>
	 * The method gets text of an existing alert in webpage and cancels the alert
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 */	
	public String closeAlertAndGetItsText(WebDriver driver) {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	/**
	 * <p>
	 * The method accepts the alert box that is currently displayed.
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 */
	public void acceptAlertBox(WebDriver driver) {
		driver.switchTo().alert().accept();
	}

	public void uploadFile(String objname, String value)
	{
		try
		{

			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.info("'"+value+"' is entered in '"+objname+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	public String getTextboxValue(String objname){

		try {

			return driver.findElement(map.getLocator(objname)).getAttribute("value");
		} catch (Exception e) {
			Report.fail("element '"+objname+"' is not displayed");
			return "";
		}

	}

	public void verifyTextboxValuePresent(String string_to_compare, String objname) throws Exception
	{
		String str = "";
		try {
			str = getTextboxValue(objname);
			Assert.assertEquals(string_to_compare, str );
			Report.pass("The value '"+string_to_compare+"' in texbox "+objname+" is verified");
		} 
		catch (Error e) 
		{
			Report.fail("'"+objname+"' is not present in the textbox: "+ objname+" and found to be '"+ getTextboxValue(objname)+"'");

		}
	}

	public void verifyTitleContains(String title_to_be_matched) throws Exception
	{
		String actulTitel=getTitle();

		try {
			actulTitel.contains(title_to_be_matched);
			Report.pass("Title is verified and it contains '"+title_to_be_matched+"'.");
		} catch (Error e) {

			Report.fail("In the title: "+title_to_be_matched+" is not present");
		}

	}


	/**
	 * <p>
	 * The method The method fluent waits for an {@link WebElement} based on
	 * <code>identifier</code>, and click on the {@link WebElement}
	 * 
	 * </p>
	 * 
	 * @param driver
	 *            The {@link WebDriver } Instance
	 * @param identifier
	 *            The <code>identifier</code> to locate the WebElement.
	 * @param totalWaitTimeInSecs
	 *            The total time to wait for an existence of {@link WebElement}
	 *            in seconds
	 * @param poolWaitTimeInSecs
	 *            The amount of time to wait and recursively check for
	 *            <p>
	 *            The <code>identifier</code> value can be an Xpath or CSS or ID
	 *            or name or linktext or partial link text or classname or
	 *            tagname
	 *            </p>
	 *            existence of {@link WebElement} in seconds
	 * @param identifierType
	 *            The value type of <code>identifier</code> is specified in this
	 *            argument
	 *            <p>
	 *            See {@link IdentifierTypes } to know about different Identifier
	 *            type values
	 *            </p>
	 * @throws NoSuchFieldException 
	 */
	public void fluentWaitAndGetWebElementAndClick(long totalWaitTimeInSecs,
			long poolWaitTimeInSecs, String objName) throws NoSuchFieldException {
		WebElement webElement = fluentWaitAndGetWebElement(totalWaitTimeInSecs, poolWaitTimeInSecs,
				objName);
		try {
			webElement.click();
			Report.info("Clicked on'" + objName + "'");
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("Could not able to load element" + webElement.getText()
			// );
			Report.fail("Could not able to load element '" + objName + "'");
			Assert.fail("Could not able to load element '" + objName + "'");
		}
	}

	/**
	 * <p>
	 * The method The method fluent waits for an {@link WebElement} based on
	 * <code>identifier</code>
	 * </p>
	 * 
	 * @param driver
	 *            The {@link WebDriver } Instance
	 * @param identifier
	 *            The <code>identifier</code> to locate the WebElement.
	 *            <p>
	 *            The <code>identifier</code> value can be an Xpath or CSS or ID
	 *            or name or linktext or partial link text or classname or
	 *            tagname
	 *            </p>
	 * @param totalWaitTimeInSecs
	 *            The total time to wait for an existence of {@link WebElement}
	 *            in seconds
	 * @param poolWaitTimeInSecs
	 *            The amount of time to wait and recursively check for existence
	 *            of {@link WebElement} in seconds
	 * @param identifierType
	 *            The value type of <code>identifier</code> is specified in this
	 *            argument
	 *            <p>
	 *            See {@link IdentifierTypes } to know about different Identifier
	 *            type values
	 *            </p>
	 * @return WebElement Returns {@link WebElement}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WebElement fluentWaitAndGetWebElement(final long totalWaitTimeInSecs, long poolWaitTimeInSecs, final String objName)
			throws NoSuchFieldException {

		// Waiting 30 seconds for an element to be present on the page, checking
		// for its presence once every 5 seconds.
		Wait wait = new FluentWait(driver).withTimeout(totalWaitTimeInSecs, TimeUnit.SECONDS)
				.pollingEvery(poolWaitTimeInSecs, TimeUnit.SECONDS);

		WebElement webElement = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				try {
					Report.info("WebElement '" + objName + "'" + " is Dispalyed");
					return driver.findElement(map.getLocator(objName));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//					e.printStackTrace();
					Report.fail("Could not able to find the element in wait time: "+objName+ totalWaitTimeInSecs + " Seconds");
					Assert.fail("Could not able to find the element in wait time: "+objName+ totalWaitTimeInSecs + " Seconds");

				}
				return null;
			}
		});
		return webElement;
	}


	/**Describes the Method name at the stating of each Test.*/
	public void startLine(Method methodName)
	{
		Report.info("------------" +methodName.getName() +" Test Starting------------");
		softAssertCount = 0;
	}

	/**Describes the Method name at the  ending of each Test.*/
	public void endLine(Method methodName)
	{
		validateAllVerificationPoints(); 
		Report.info("------------" + methodName.getName() + " Test Ended------------");
	}

	public int getPoolWaitTime() {
		return Integer.parseInt(readConfigFile("Fluent_WaitTime"));
	}

	public int getTotalWaitTimeInSecs() {
		return Integer.parseInt(readConfigFile("ElementLoad_WaitTime"));
	}


	public long getPageLoadWaitTime() {
		// TODO Auto-generated method stub
		return Integer.parseInt(readConfigFile("PageLoad_WaitTime"));
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement} based on <code>identifier</code>
	 * </p>
	 * 
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 */
	public void explicitlyWaitForElement(String objName,
			long waitTimeInSeconds) {

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;
		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocator(objName)));
			Report.info("'"+ objName + "' Element is clickable");
		} catch (Exception e) {
			//			e.printStackTrace();
			element = handleStaleElementExpection(objName, waitTimeInSeconds,
					webDriverWaitForTask, element, e);
		}
	}
	
	//Explicitly Wait for element(which is dynamic xpath)(and not from objmap.properties)
	public void explicitlyWaitForDynamicElement(String objName, long waitTimeInSeconds) {

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver, waitTimeInSeconds);
		WebElement element = null;
		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.visibilityOf(driver.findElement(By.xpath(objName))));
			Report.info("'"+ objName + "' Element is visible");
		} catch (Exception e) {
			element = handleStaleElementExpection(objName, waitTimeInSeconds,
					webDriverWaitForTask, element, e);
		}
	}
	
	public void explicitlyWaitForElementToPresent(String objName,
			long waitTimeInSeconds) {

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;
		try {
			element = webDriverWaitForTask.until(ExpectedConditions.presenceOfElementLocated(map.getLocator(objName)));
			Report.info("'"+ objName + "' Element Presence Located");
		} catch (Exception e) {
			//			e.printStackTrace();
			element = handleStaleElementExpection(objName, waitTimeInSeconds,
					webDriverWaitForTask, element, e);
		}
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement}, 
	 * and gets it bases on <code>identifier</code> value
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @return {@link WebElement} located based on <code>identifier</code>
	 */
	public WebElement explicitlyWaitAndGetWebElement(String objName, long waitTimeInSeconds) {

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocator(objName)));			
			Report.info("'" + objName + "' Element is fetched");
		} catch (Exception e) {
			//			e.printStackTrace();

			element = handleStaleElementExpection(objName, waitTimeInSeconds,
					webDriverWaitForTask, element, e);

		}
		return element;
	}

	protected WebElement handleStaleElementExpection(String objName,
			long waitTimeInSeconds, WebDriverWait webDriverWaitForTask,
			WebElement element, Exception e) {
		if (e instanceof StaleElementReferenceException) {
			try {
				element = webDriverWaitForTask.until(ExpectedConditions
						.elementToBeClickable(map.getLocator(objName)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Report.fail("Could not able to find the element in wait time: '"+ objName + "' " + waitTimeInSeconds + " Seconds");
				Assert.fail("Could not able to find the element in wait time: '"+ objName + "' " +waitTimeInSeconds + " Seconds");

			}
		} else {
			Report.fail("Could not able to find the element '"+ objName + "' " + " in wait time: " + waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element '"+ objName + "' " + " in wait time: "+ waitTimeInSeconds + " Seconds");

		}
		return element;
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement}, 
	 * and gets it bases on <code>identifier</code> value
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @return {@link WebElement} located based on <code>identifier</code>
	 */
	public List<WebElement> explicitlyWaitAndGetWebElements(String objName, long waitTimeInSeconds) {

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		List<WebElement> elements = null;

		try {
			elements =  webDriverWaitForTask.until(ExpectedConditions.presenceOfAllElementsLocatedBy(map.getLocator(objName)));			
			Report.info(objName + "Element is fetched");
		} catch (TimeoutException e) {
			//			e.printStackTrace();
			Report.fail("Could not able to find the element in wait time:"+objName+ waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: "+objName+ waitTimeInSeconds + " Seconds");

		} catch (Exception e) {
			//			e.printStackTrace();
			Report.fail("Please check the locator info for element : "+ objName);
			Assert.fail("Please check the locator info for element : "+ objName);

		}
		return elements;
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement}, 
	 * and gets it bases on <code>identifier</code> value
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @return {@link WebElement} located based on <code>identifier</code>
	 * @throws Exception 
	 */
	public String explicitlyWaitAndGetWebElementText(String objName, long waitTimeInSeconds){

		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		String elementText = "";
		try {
			webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(driver.findElement(map.getLocator(objName))));
			return driver.findElement(map.getLocator(objName)).getText();
		} catch(StaleElementReferenceException e){
			elementText = explicitlyWaitAndGetWebElementText(objName, getTotalWaitTimeInSecs());
		} catch (Exception e) {
			Report.fail("Could not able to find the element '" + objName + "' " + "in wait time " + waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element '" + objName + "' " + "in wait time " + waitTimeInSeconds + " Seconds");

		}
		return elementText;
	}

	public String explicitlyWaitAndGetWebElementTagName(String objName,
			int totalWaitTimeInSecs) {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				totalWaitTimeInSecs);
		String elementTagName = "";
		try {
			elementTagName =	webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(driver.findElement(map.getLocator(objName)))).getTagName();
		} catch(StaleElementReferenceException e ){
			elementTagName = explicitlyWaitAndGetWebElementTagName(objName, totalWaitTimeInSecs);
		} catch (Exception e) {
			Report.fail("Could not able to find the element '" + objName + "' " + "in wait time " + totalWaitTimeInSecs + " Seconds");
			Assert.fail("Could not able to find the element '" + objName + "' " + "in wait time " + totalWaitTimeInSecs + " Seconds");
		}
		return elementTagName;
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement} based on <code>identifier</code> value, and
	 * clicks on the {@link WebElement}
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @throws Exception 
	 */
	public void explicityWaitForAnElementAndClick(String objName, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocator(objName)));
			element.click();
			Report.info("'" + objName + "' Element is clicked");
		} catch(StaleElementReferenceException e){
			explicityWaitForAnElementAndClick(objName, waitTimeInSeconds);
		} catch (Exception e) {
			//	e.printStackTrace();
			Report.fail("Could not able to find the element '" + objName + "' "
					+ "in wait time " + waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element '" + objName + "' "
					+ "in wait time " + waitTimeInSeconds + " Seconds");
		}
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement} based on <code>identifier</code> value, and
	 * clicks on the {@link WebElement}
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @throws Exception 
	 */
	public void explicityWaitForAnElementWithXpathAndClick(String objXpath, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(By.xpath(objXpath)));
			element.click();
			Report.info("'" + objXpath + "' Element is clicked");
		} catch (Exception e) {
			//			e.printStackTrace();
			Report.fail("Could not able to find the element in wait time:" + objXpath + " " + waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: " + objXpath + " " + waitTimeInSeconds + " Seconds");

		}
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement} based on <code>identifier</code> value, and
	 * clicks on the {@link WebElement}
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @throws Exception 
	 */
	public void explicityWaitForAnElementWithXpathAndDoubleClick(String objXpath, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(By.xpath(objXpath)));
			Actions action = new Actions(driver);
			action.doubleClick(element).perform();
			Report.info("'" + objXpath + "' Element is Double clicked");
		} catch (Exception e) {
			//			e.printStackTrace();
			Report.fail("Could not able to find the element in wait time:" + objXpath + " " + waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: " + objXpath + " " + waitTimeInSeconds + " Seconds");

		}
	}

	/**
	 * <p>
	 * The method explicitly waits for a {@link WebElement} based on <code>identifier</code> value, and
	 * clicks on the {@link WebElement}
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement.
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @throws Exception 
	 */
	public void explicityWaitForAnElementAndDoubleClick(String objName, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocator(objName)));
			Actions action = new Actions(driver);
			action.doubleClick(element).perform();
			Report.info(objName + "Element is Double clicked");
		} catch (Exception e) {
			//			e.printStackTrace();
			Report.fail("Could not able to find the element in wait time:"+objName+ waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: "+objName+ waitTimeInSeconds + " Seconds");

		}
	}

	/**
	 * <p>
	 * The method explicitly waits for an existence of WebElement(either button or Hyperlink), 
	 * and Click on it
	 * </p>
	 * @param driver The {@link WebDriver } Instance
	 * @param identifier The <code>identifier</code> to locate the WebElement. 
	 * <p>The <code>identifier</code> value can be an Xpath or CSS or ID or 
	 * name or linktext or partial link text or classname or tagname
	 * </p>
	 * @param waitTimeInSeconds No of Seconds to wait for that WebElement
	 * @param identifierType The value type of <code>identifier</code> is specified in this argument
	 * <p>
	 * See {@link IdentifierTypes }  to know about different Identifier type values
	 * </p>
	 * @throws Exception 
	 */
	public void explictlyWaitandClickonHyperlink(String objName, long waitTimeInSeconds) throws Exception {

		explicitlyWaitForElement(objName, waitTimeInSeconds);

		if (isElementPresent(objName)) {
			explicityWaitForAnElementAndClick(objName,waitTimeInSeconds);
		}

	}

	public void hoverAndClick(String[] hoverer, String element) throws Exception {
		//declare new Action

		Actions actions = new Actions(driver);
		for (String we : hoverer) {
			// hover each of them
			try {
				//				explicitlyWait(we, getPoolWaitTime());
				Action hovering = actions.moveToElement(driver.findElement(map.getLocator(we))).build();
				hovering.perform();
				Thread.sleep(5000L);
				Report.info("Clicked on Menu '" + we + "'");
			} catch (Exception e) {
				Report.info("Could not able click on menu '" + we + "'");
				Assert.fail("Could not able to click on menu '" + we + "'");
			}

		}

		try {
			//			explicityWaitForAnElementAndClick(element, getTotalWaitTimeInSecs());
			WebElement lastSubmenu = explicitlyWaitAndGetWebElement(element, getTotalWaitTimeInSecs());
			actions.click(lastSubmenu).perform();
			Report.info("Clicked on submenu item '" + element + "'");

		} catch (Exception e) {
			Report.info("Could not able click on submenu '" + element + "'");
			Assert.fail("Could not able click on submenu '" + element + "'");
		}

	}

	public void hoverAndClick(String mainMenu, String submenu1) throws Exception {

		Actions actions = new Actions(driver);
		Action hovering = actions.moveToElement(driver.findElement(map.getLocator(mainMenu))).click().build();
		Report.info("Mainmenu opened");
		hovering.perform();
		WebDriverWait wait = new WebDriverWait(driver, getPoolWaitTime()); 
		wait.until(ExpectedConditions.presenceOfElementLocated(map.getLocator(submenu1)));  // until this submenu is found

		//identify menu option from the resulting menu display and click
		actions.moveToElement(driver.findElement(map.getLocator(submenu1))).click().build();
		Report.info("Submenu opened");
		hovering.perform();	
	}

	public void hoverAndClick1(String mainMenu, String submenu1) throws Exception {

		WebElement menu1 = explicitlyWaitAndGetWebElement(mainMenu, getTotalWaitTimeInSecs());

		Actions actions = new Actions(driver);
		actions.moveToElement(menu1).perform();
		Thread.sleep(getPoolWaitTime());
		actions.moveToElement(explicitlyWaitAndGetWebElement(submenu1, getTotalWaitTimeInSecs())).click().perform();
		//		explicityWaitForAnElementAndClick(submenu1, getTotalWaitTimeInSecs());
	}

	public void hoverAndClick1(String mainMenu, String submenu1, String submenu2) throws Exception {

		WebElement menu1 = explicitlyWaitAndGetWebElement(mainMenu, getTotalWaitTimeInSecs());

		Actions actions = new Actions(driver);
		actions.moveToElement(menu1).perform();
		Thread.sleep(10000);
		WebElement menu2 = driver.findElement(map.getLocator(submenu1));
		actions.moveToElement(menu2).perform();
		Thread.sleep(10000);
		WebElement menu3 = driver.findElement(map.getLocator(submenu2));
		actions.click(menu3).perform();
		//		explicityWaitForAnElementAndClick(submenu1, getTotalWaitTimeInSecs());
	}


	/**
	 * Select the options that have a @value value matching with the given Value
	 * @param objname object identification information
	 * @param Value argument that have a matching value for options, these @value values are visible only when we see the html code
	 * <pre>
	 * {@code
	 * For example: for html code as below: Use attribute @value 
	 * <select id="create-2504-select" class="requiredField" name="values[2504]">
	 *		<option value=""/>
	 *		<option value="L" selected="selected">Low</option>
	 *		<option value="M">Medium</option>
	 *		<option value="H">High</option>
	 *	</select>
	 * }
	 * </pre>
	 */
	public void selectTextByValue (String objname, String Value){
		try{
			Select select = new Select(driver.findElement(map.getLocator(objname)));
			Report.info("dropdown '"+ objname +"' is selected");
			select.selectByValue(Value);
			Report.pass("text '"+Value+"' is selected from the dropdown '"+objname+"'");
		} catch(Exception e){
			Report.fail("'"+objname+"' is not present or text '"+Value+"' is not selected");
			Assert.fail("'"+objname+"' is not present or text '"+Value+"' is not selected");
		}

	}

	/**
	 * To Select an options that display text matching the given argument
	 * @param objname object identification information
	 * @param text Visble text that happen to see in the UI
	 * <pre>
	 * {@code
	 * For example: for html code as below: Use value of <option> element 
	 * <select id="create-2504-select" class="requiredField" name="values[2504]">
	 *		<option value=""/>
	 *		<option value="L" selected="selected">Low</option>
	 *		<option value="M">Medium</option>
	 *		<option value="H">High</option>
	 *	</select>
	 * }
	 * </pre>
	 */
	public void selectByVisibleText (String objName, String text){
		WebElement selectDropdown = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
		try{
			Select select = new Select(selectDropdown);
			Report.info("dropdown '"+ objName +"' is selected");
			select.selectByVisibleText(text);
			Report.pass("text '"+text+"' is selected from the dropdown '"+objName+"'");
		}catch(Exception e){
			Report.fail("'"+objName+"' is not present or text '"+text+"' is not selected");
			Assert.fail("'"+objName+"' is not present or text '"+text+"' is not selected");
		}

	}


	/** SelectTextByIndex method (objname = the object name from objectmap file)**/
	public void selectTextByIndex (String objname, int indexno){
		try{

			Select select = new Select(driver.findElement(map.getLocator(objname)));
			Report.info("dropdown '"+ objname +"' is selected");
			select.selectByIndex(indexno);
			Report.info("text '"+indexno+"' is selected from the dropdown '"+objname+"'");
		} catch(Exception e){
			Report.fail("'"+objname+"' is not present or text '"+indexno+"' is not selected");
			Assert.fail("'"+objname+"' is not present or text '"+indexno+"' is not selected");
		}

	}

	/** SelectTextByValue method (objname = the object name from objectmap file)*/
	public void verifyCurrentSelectByText (String objname, String Value){
		try
		{
			Select select = new Select(driver.findElement(map.getLocator(objname)));
			Report.info("dropdown " + objname +"' is selected");
			String currentSelectedOptionvalue = select.getFirstSelectedOption().getText();
			/*if(currentSelectedOptionvalue.equals("")){
				List<WebElement> options = select.getOptions();
				for(WebElement option: options){
					if(isAttribtuePresent(option, "selected")){
						currentSelectedOptionvalue = option.getAttribute("value");
						break;
					}
				}
			}*/
			if(currentSelectedOptionvalue.contains(Value)){
				Report.pass("dropdown '" + objname +"' with value '" + currentSelectedOptionvalue +"' is verified");
			}else{
				softAssertCount++;
				Report.warn("For object '"+ objname + " expected text '" + Value +"' is not same as actual text '" + currentSelectedOptionvalue + "'");
				softAssert.fail("For object '"+ objname + " expected text '" + Value +"' is not same as actual text '" + currentSelectedOptionvalue + "'");
			}
		}
		catch(Exception e)
		{
			Report.fail("'"+objname+"' is not present or text '"+Value+"' is not selected");
			Assert.fail("'"+objname+"' is not present or text '"+Value+"' is not selected");
		}

	}

	public String getCurrentSelectOption (String objname){
		String currentSelectedOptionvalue = "dummy";
		try{
			Select select = new Select(driver.findElement(map.getLocator(objname)));
			//			Report.info("dropdown " + objname +"' is selected");
			currentSelectedOptionvalue = select.getFirstSelectedOption().getText();

		}
		catch(Exception e){
			Report.fail("'"+objname+"' is not present ");
			Assert.fail("'"+objname+"' is not present ");
		}
		return currentSelectedOptionvalue;
	}

	/**
	 * Getting all the items in dropdown list and verify the element is List
	 */
	public void getAllListItemAndVerify(String value, String objname) {
		try {
			ArrayList<String> listItems = new ArrayList<String>();
			WebElement mySelectElm = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			Select mySelect = new Select(mySelectElm);
			List<WebElement> options = mySelect.getOptions();
			for (WebElement option : options) {
				String element = option.getText();
				listItems.add(element);
			}
			if (listItems.contains(value)) {
				Report.pass("dropdown '" + objname +"' with value '" + value +"' is verified");
			} else {
				softAssertCount++;
				Report.warn("The item '" + value +"' is not in Dropdown List ");
				softAssert.fail("The item '" + value +"' is not in Dropdown List ");
			}

		} catch (Exception e) {
			Report.fail("Unable to find on element " + e.getStackTrace());
		}
	}

	/*
	 * This method will verify if a specified text value does not
	 * appear in the values in a specified drop down list
	 */
	public void verifyDropDownNotContainText(String objname, String Value)
	{
		try
		{
			Select select = new Select(driver.findElement(map.getLocator(objname)));

			List<WebElement> options = select.getOptions();
			boolean match = false;
			for(WebElement we:options)  
			{  		  
				String temp = we.getText();
				if (temp.equals(Value)){

					match = true;  
				}
			}
			if(match == true){
				Assert.fail("Text is displaying in drop down when it should not");
			}else{
				Report.pass("Text id not displaying in drop down:"+Value);
			}


		}
		catch(Exception e)
		{
			Report.fail("'"+objname+"' is not present or text '"+Value+"' is present");
			Assert.fail("'"+objname+"' is not present or text '"+Value+"' is present");
		}
	}


	public void verifyAllDowndownValues(ArrayList<String> value, String objname) {
		try {
			ArrayList<String> listItems = new ArrayList<String>();
			WebElement mySelectElm = driver
					.findElement(map.getLocator(objname));
			Select mySelect = new Select(mySelectElm);
			List<WebElement> options = mySelect.getOptions();
			for (WebElement option : options) {
				String element = option.getText();
				listItems.add(element);
			}
			if (listItems.containsAll((value))) {
				Report.pass("Elements in dropdown " + objname + " matches with expected list of values");
			} else {
				softAssertCount++;
				Report.warn("Elements in dropdown " + objname + " does not match with expected list of values");
				softAssert.fail("Elements in dropdown " + objname + " does not match with expected list of values");
			}

		} catch (Exception e) {
			Report.fail("Unable to find on element " + e.getStackTrace());
		}
	}

	/**
	 * Returns true for everything but disabled input elements.
	 * @param objName object locator info
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isEnabled(String objName){
		try {
			if(driver.findElement(map.getLocator(objName)).isEnabled()){
				Report.pass("Element " + objName + " is Enabled");
				return true;
			}

			else{
				Report.info("Element " + objName + " is not Enabled");
				return false;

			}

		} catch (Exception e) {
			//e.printStackTrace();
			Report.fail("Locator type is not defined!!");
		}
		return false;

	}

	/*
	 * 
	 */
	public String readTextFile(String path) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		StringBuilder sb = new StringBuilder();
		String lineSeperator = System.getProperty("line.seperator");
		String data = null;

		while((line = br.readLine())!=null){
			sb.append(line);
			//sb.append(lineSeperator);

		}

		data = sb.toString();
		br.close();
		return data;

	}

	/**
	 * The method is used to get WebElement for a specific object information
	 * @param objName takes object identification information for a web element
	 * @return WebElement matching the object information passed to this method, otherwise return null 
	 */
	public WebElement getWebElement(String objName){
		WebElement element = null;
		try {
			element = driver.findElement(map.getLocator(objName));
			Report.info("Able to find the element: " + objName);
			return element;
		} catch (NoSuchElementException e) {
			Report.fail("Not able to find the element: " + objName);
			e.printStackTrace();
		} catch (Exception e) {
			Report.fail("Please check locator info for finding the element: " + objName);
			e.printStackTrace();
		}
		return null;

	}


	/**
	 * Writes some data to tinyMCE in page
	 * @param data Data to be passed to tinyMCE
	 * @param objName object identification information for tinmyMCE
	 * @throws Exception Returns NoSuchElementException if no WebElement is noticed with object identification information
	 */
	public void writeToTinyMCE(String data, String objName) throws Exception{
		WebElement element = driver.findElement(map.getLocator(objName));
		JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
		jsExecute.executeScript("arguments[0].innerHTML = ('<p>"+ data +"</p>')", element);

	}


	/**
	 * Inserts date and time into Date fields
	 * @param object object identification information for date field
	 * @param date date to be passed in DD/MM/YYYY format
	 * @param time time to be passed in HH:MM
	 * @throws Exception Returns NoSuchElementException if no WebElement is noticed with object identification information
	 */
	public void datePicker(String object, String date, String time) throws Exception{
		WebElement dateBox =  driver.findElement(map.getLocator(object));
		dateBox.clear();
		dateBox.sendKeys(date);
		dateBox.sendKeys(Keys.SPACE);
		dateBox.sendKeys(time);
		dateBox.sendKeys(Keys.TAB);

	}

	public String switchToFrame1(String object) throws Exception{
		String mainWindow = driver.getWindowHandle();
		WebElement myFrame = driver.findElement(map.getLocator(object));
		driver.switchTo().frame(myFrame);
		return mainWindow;

	}

	/**
	 * Strip document URN from the string passed
	 * @param object Success message which contains the Document urn details
	 * @return Document URN
	 */
	public String getDocumentURN(String object){
		String[] str=object.split(" ");
		return str[1];
	}

	/**
	 * This used is used instead of Menu hovering in IE.
	 * Navigates to specific page, by passing in the relative URL for a specific menu item
	 * @param url pass the relative url - not the absolute url
	 */
	public void navigateToPage(String url) {
		driver.get(baseUrl + url);
		//ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
		handleULeafInterruptionError();
		//appSpecificMethods.handleULeafInterruptionError();
		waitForPageLoad();
		handleULeafInterruptionError();
		//appSpecificMethods.handleULeafInterruptionError();
		validateNavigationtoPage(baseUrl + url);

	}

	public void handleULeafInterruptionError() {
		if(isAlertPresent()){
			acceptAlertBox(driver);
		}
	}

	/**
	 * Validate the application is in current url 
	 * @param url - current url of the application
	 */
	private void validateNavigationtoPage(String url) {
		String currentPageURL = getCurrentPageURL();
		Report.info("current Page URL: " + currentPageURL);
		Report.info("expected page URL:" + url);
		if(currentPageURL.equals(url)){
			Report.pass("Navigated to " + url + " Successfully");
		} else{
			driver.get(url);
		}

	}

	/**
	 * Get current URL of the application
	 * @return current URL of the application
	 */
	public String getCurrentPageURL(){
		return driver.getCurrentUrl();
	}

	/* 
	 * Synchronization
	 * */
	public void waitForPageLoad1() {

		Wait<WebDriver> wait = new WebDriverWait(driver, getPageLoadWaitTime());
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				Report.info("Current Window State       : "
						+ String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
				return String
						.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
						.equals("complete");
			}
		});
	}

	/**
	 * This methods halts the selenium execution until the page loading waggon whell is stopped/completed/disappeared
	 */
	public void waitForPageLoad() {
		Report.info("JS: Wating for ready state complete");
		(new WebDriverWait(driver, getPageLoadWaitTime()+getPageLoadWaitTime()+getPageLoadWaitTime())).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				JavascriptExecutor js = (JavascriptExecutor) d;
				/*ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
				appSpecificMethods.handleULeafInterruptionError();*/
				handleULeafInterruptionError();
				String readyState = js.executeScript("return document.readyState").toString();
				Report.info("JS: Ready State: " + readyState);
				return (Boolean) readyState.equals("complete");
			}
		});

		/*  WebDriverWait wait = new WebDriverWait(driver, getPageLoadWaitTime());

	    Predicate<WebDriver> pageLoaded = new Predicate<WebDriver>() {

	        @Override
	        public boolean apply(WebDriver d) {
	        	 JavascriptExecutor js = (JavascriptExecutor) d;
		            String readyState = js.executeScript("return document.readyState").toString();
		            Report.info("JS: Ready State: " + readyState);
	            return (Boolean) readyState.equals("complete");
	        }

	    };
	    wait.until(pageLoaded);*/

	}

	/**
	 * This methods halts the selenium execution until the specific Jquery loading waggon whell is stopped/completed/disappeared
	 * Usually noticed on click of buttons in applications
	 * @param driver
	 */
	public void waitForJQuery(WebDriver driver) {
		Report.info("JQuery: Wating for JQuery to be active");
		(new WebDriverWait(driver, getPageLoadWaitTime())).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				JavascriptExecutor js = (JavascriptExecutor) d;
				String readyState = js.executeScript("return !!window.jQuery && window.jQuery.active == 0").toString();
				Report.info("JQuery: Ready State: " + readyState);
				return (Boolean) readyState.equals("true");
			}
		});
	}

	public void WaitForAjax() throws InterruptedException {
		while (true) {
			Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
			if (ajaxIsComplete) {
				break;
			}
			Thread.sleep(getPageLoadWaitTime());
		}
	}

	/** Switching to New Window **/
	/**
	 * User only when accepting a dialog box in child window, closes clild windows and navigates to Parent window.
	 * @param title
	 * @return
	 */
	public WebDriver getHandleToWindow(String title) {

		//parentWindowHandle = WebDriverInitialize.getDriver().getWindowHandle(); // save the current window handle.

		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows :  " + handles.size());

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			Report.info("Window Title : " + popupWindow.getTitle());
			Report.info("Window Url : " + popupWindow.getCurrentUrl());

			if (popupWindow.getTitle().contains(title)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		waitForPageLoad();
		return popupWindow;
	}


	/**
	 * @param title
	 * @return
	 */
	public String getCurrentWindowHandle() {

		return driver.getWindowHandle();
	}

	public int getWindowCount() {
		return driver.getWindowHandles().size();
	}

	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @return handler to parent window
	 */
	public WebDriver closePopupWindowAndNavigateToParentWindow(String childWindowTitle, String parentWindowTitle){
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + handles.size());
		boolean isChildWindowClosed = false;

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(childWindowTitle) && isChildWindowClosed == false){
				driver.close();
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				isChildWindowClosed = true;
				break;
			}
		}
		handles = driver.getWindowHandles();
		Report.info("No of windows after closing child window :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(parentWindowTitle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());

				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		return popupWindow;

	}

	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @return handler to parent window
	 */
	public WebDriver closeChildWindowAndNavigateToParentWindowHandle(String childWindowHandle, String parentWindowHandle, int noOfWinBefore, int noOfWinAfter){
		WebDriver popupWindow = null;
		Set<String> winHandles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + winHandles.size() + "No of windows Expected : " + noOfWinBefore  );
		boolean isChildWindowClosed = false;
		// removing the parent window handle
		winHandles.remove(parentWindowHandle);

		for (String s : winHandles) {
			String windowHandle = s;
			String childWindowTitle = "";
			popupWindow = driver.switchTo().window(windowHandle);
			childWindowTitle = popupWindow.getTitle();
			if (isChildWindowClosed == false && 
					!popupWindow.getWindowHandle().equals(parentWindowHandle)){
				driver.close();
				isChildWindowClosed = true;
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				break;
			}
		}
		winHandles = driver.getWindowHandles();
		return driver.switchTo().window(parentWindowHandle);

	}

	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @return handler to parent window
	 */
	public void closeChildWindow(String childWindowHandle, Set<String> parentWindowHandles, int noOfWinBefore, int noOfWinAfter){
		WebDriver popupWindow = null;
		Set<String> winHandles = driver.getWindowHandles();
		Report.info("No of windows Noticed:  " + winHandles.size() + " No of windows Expected: " + noOfWinBefore  );
		boolean isChildWindowClosed = false;
		// removing the parent window handle
		winHandles.removeAll(parentWindowHandles);
		for (Iterator<String> iterator = winHandles.iterator(); iterator.hasNext();) {
			String windowHandle = (String) iterator.next();
			System.out.println("Window handle to remove:" + windowHandle);
		}
		for (String s : winHandles) {
			String windowHandle = s;
			String childWindowTitle = "";
			popupWindow = driver.switchTo().window(windowHandle);
			childWindowTitle = popupWindow.getTitle();
			if (isChildWindowClosed == false){
				driver.close();
				isChildWindowClosed = true;
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				break;
			}
		}
		winHandles = driver.getWindowHandles();

	}
	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @return handler to parent window
	 */
	public WebDriver closePopupWindowAndNavigateToParentWindowHandle(String childWindowTitle, String parentWindowHandle){
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + handles.size());
		boolean isChildWindowClosed = false;

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(childWindowTitle) && isChildWindowClosed == false && 
					!popupWindow.getWindowHandle().equals(parentWindowHandle)){
				driver.close();
				isChildWindowClosed = true;
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				break;
			}
		}
		handles = driver.getWindowHandles();
		Report.info("No of windows after closing child window :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getWindowHandle().equals(parentWindowHandle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				Report.info("Selected Window Handle : " + popupWindow.getWindowHandle());
				//				return popupWindow;
				break;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		return popupWindow;

	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @return handler to the new popup window
	 */

	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to parent window
	 */
	public WebDriver closePopupWindowAndNavigateToParentWindow(String childWindowTitle, String parentWindowTitle, int noOfWindowsBefore, int noOfWindowsAfter){
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + handles.size());
		boolean isChildWindowClosed = false;

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(childWindowTitle) && isChildWindowClosed == false){
				driver.close();
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				isChildWindowClosed = true;
				break;
			}
		}
		handles = driver.getWindowHandles();
		Report.info("No of windows after closing child window :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(parentWindowTitle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());

				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		return popupWindow;

	}

	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to parent window
	 */
	public WebDriver closePopupWindowAndNavigateToParentWindowHandle(String childWindowTitle, String parentWindowHandle, int noOfWindowsBefore, int noOfWindowsAfter){
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + handles.size());
		boolean isChildWindowClosed = false;

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getTitle().contains(childWindowTitle) && isChildWindowClosed == false & 
					!popupWindow.getWindowHandle().equals(parentWindowHandle)){
				driver.close();
				isChildWindowClosed = true;
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				break;
			}
		}
		handles = driver.getWindowHandles();
		Report.info("No of windows after closing child window :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);

			if (popupWindow.getWindowHandle().equals(parentWindowHandle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		return popupWindow;

	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * Usually this method is called when only one window (ie parent window) is present, 
	 * and invoking a link/button/icon triggers child window, and total window count would be 2
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @return handler to the new popup window
	 */

	public WebDriver clickAndWaitForBrowserPopup(String childWindowTitle, String objName) {
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows should be 1 :  " + handles.size());

		click(objName); 

		/*	   int timeCount = 1;

		do
		{
		   handles = driver.getWindowHandles();
		   try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
				Report.info("Could not wait for interuppted time");
				e.printStackTrace();
			}
		   timeCount++;
		   if ( timeCount > getTotalWaitTimeInSecs() ) 
		   {
		       break;
		   }
		}
		while ( handles.size() == 1 );*/

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		int currentWindowCount = getWindowCount();
		wait.until(ExpectedConditions.numberOfWindowsToBe(currentWindowCount+1));
		handles = driver.getWindowHandles();
		Report.info("No of windows should be 2 :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			Report.info("Window Title : " + popupWindow.getTitle());
			Report.info("Window Url : " + popupWindow.getCurrentUrl());

			if (popupWindow.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		//		waitForPageLoad();
		return popupWindow;
	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to the new popup window
	 * @throws Exception 
	 */
	public WebDriver clickAndWaitForBrowserPopup(String childWindowTitle, String objName, int noOfWindowsBefore, int noOfWindowsAfter) throws Exception {
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows should be " + noOfWindowsBefore + " : " + handles.size());

		click(objName); 

		/*	   int timeCount = 1;

		do
		{
		   handles = driver.getWindowHandles();
		   try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
				Report.info("Could not wait for interuppted time");
				e.printStackTrace();
			}
		   timeCount++;
		   if ( timeCount > getTotalWaitTimeInSecs() ) 
		   {
		       break;
		   }
		}
		while ( handles.size() == 1 );*/

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		handles = driver.getWindowHandles();
		Report.info("No of windows should be "+ noOfWindowsAfter + " :  " + handles.size());
		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			Report.info("Window Title : " + popupWindow.getTitle());
			Report.info("Window Url : " + popupWindow.getCurrentUrl());

			if (popupWindow.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		//		waitForPageLoad();
		return popupWindow;
	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * *** Advised to store the parentWindow handle before calling this method
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to the new popup window
	 */
	public WebDriver clickAndWaitForChildWindow(String childWindowTitle, String objName, int noOfWindowsBefore, int noOfWindowsAfter) {
		WebDriver newChildWindow = null;
		ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());
		Report.info("No of windows expected to be " + noOfWindowsBefore + " : and notice as " + windows.size());

		click(objName); 

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		windows = new ArrayList<String> (driver.getWindowHandles());
		Report.info("No of windows expected to be "+ noOfWindowsAfter + " : and noticed as " + windows.size());
		newChildWindow = driver.switchTo().window(windows.get(noOfWindowsBefore));

		if (newChildWindow.getTitle().contains(childWindowTitle)){
			Report.info("Selected Window Title : " + newChildWindow.getTitle());
		}
		Report.info("Current Window Title :" + newChildWindow.getTitle());
		waitForPageLoad();
		return newChildWindow;
	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * *** Advised to store the parentWindow handle before calling this method
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to the new popup window
	 */
	public WebDriver clickAndWaitForChildWindow(String childWindowTitle, Set<String> parentWindowHandles, String objName, int noOfWindowsBefore, int noOfWindowsAfter) {
		WebDriver newChildWindowDriver = null;
		Set<String> windows = driver.getWindowHandles();
		Report.info("No of windows expected to be " + noOfWindowsBefore + " : and notice as " + windows.size());

		click(objName); 

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		windows = driver.getWindowHandles();
		Report.info("No of windows expected to be "+ noOfWindowsAfter + " : and noticed as " + windows.size());
		//		newChildWindow = driver.switchTo().window(windows.get(noOfWindowsBefore));
		Report.info("Windows of parent windows should be " +  noOfWindowsBefore + " and identified as :" + parentWindowHandles.size() );
		windows.removeAll(parentWindowHandles);
		Report.info("Windows of child windows should be 1 and identified as :" + windows.size() );
		for (String s : windows) {
			String windowHandle = s; 
			newChildWindowDriver = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			waitForJQuery(driver);
			if (newChildWindowDriver.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + newChildWindowDriver.getTitle());
				break;
			}
		}
		Report.info("Current Window Title :" + newChildWindowDriver.getTitle());
		waitForPageLoad();
		return newChildWindowDriver;
	}

	public WebDriver clickAndNavigateToExistingChildWindow(String parentWindowHandle, Set<String> parentWindowHandles, String objName, int noOfWindowsBefore, int noOfWindowsAfter) {
		WebDriver newChildWindowDriver = null;
		Set<String> windows = driver.getWindowHandles();
		Report.info("No of windows expected to be " + noOfWindowsBefore + " : and notice as " + windows.size());

		click(objName); 

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		windows = driver.getWindowHandles();
		Report.info("No of windows expected to be "+ noOfWindowsAfter + " : and noticed as " + windows.size());
		//		newChildWindow = driver.switchTo().window(windows.get(noOfWindowsBefore));
		Report.info("Windows of parent windows should be " +  noOfWindowsBefore + " and identified as :" + parentWindowHandles.size() );
		windows.removeAll(parentWindowHandles);
		windows.add(parentWindowHandle);
		Report.info("Windows of child windows should be 1 and identified as :" + windows.size() );
		for (String s : windows) {
			String windowHandle = s; 
			newChildWindowDriver = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			/*	if (newChildWindowDriver.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + newChildWindowDriver.getTitle());
			break;
			}*/
		}
		Report.info("Current Window Title :" + newChildWindowDriver.getTitle());
		waitForPageLoad();
		return newChildWindowDriver;
	}

	/**
	 * Used when a new window popup comes up on clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * *** Advised to store the parentWindow handle before calling this method
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to the new popup window
	 */
	public WebDriver clickAndWaitForChildWindow(String childWindowTitle, ArrayList<String> parentWindowHandles, String objName, int noOfWindowsBefore, int noOfWindowsAfter) {
		WebDriver newChildWindowDriver = null;
		ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());
		Report.info("No of windows expected to be " + noOfWindowsBefore + " : and notice as " + windows.size());

		if(objName!=null)
		{
			click(objName); 
		}		

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		windows = new ArrayList<String>(driver.getWindowHandles());
		Report.info("No of windows expected to be "+ noOfWindowsAfter + " : and noticed as " + windows.size());
		//		newChildWindow = driver.switchTo().window(windows.get(noOfWindowsBefore));
		Report.info("Windows of parent windows should be " +  noOfWindowsBefore + " and identified as :" + parentWindowHandles.size() );
		windows.removeAll(parentWindowHandles);
		Report.info("Windows of child windows should be 1 and identified as :" + windows.size() );
		for (String s : windows) {
			String windowHandle = s; 
			newChildWindowDriver = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			if (newChildWindowDriver.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + newChildWindowDriver.getTitle());
				break;
			}
		}
		Report.info("Current Window Title :" + newChildWindowDriver.getTitle());
		waitForPageLoad();
		return newChildWindowDriver;
	}

	/**
	 * Used when a new window popup comes up on doubleclicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * *** Advised to store the parentWindow handle before calling this method
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @param noOfWindowsBefore total number of windows before a child window popup is doubleclicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to the new popup window
	 */
	public WebDriver doubleClickAndWaitForChildWindow(String childWindowTitle, ArrayList<String> parentWindowHandles, String objName, int noOfWindowsBefore, int noOfWindowsAfter) {
		WebDriver newChildWindowDriver = null;
		ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());
		Report.info("No of windows expected to be " + noOfWindowsBefore + " : and notice as " + windows.size());

		try{
			performDoubleClickUsingJavaScript(objName);
			sleep(5000);
			}catch(Exception e){}
			

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsAfter));
		windows = new ArrayList<String>(driver.getWindowHandles());
		Report.info("No of windows expected to be "+ noOfWindowsAfter + " : and noticed as " + windows.size());
		//		newChildWindow = driver.switchTo().window(windows.get(noOfWindowsBefore));
		Report.info("Windows of parent windows should be " +  noOfWindowsBefore + " and identified as :" + parentWindowHandles.size() );
		windows.removeAll(parentWindowHandles);
		Report.info("Windows of child windows should be 1 and identified as :" + windows.size() );
		for (String s : windows) {
			String windowHandle = s; 
			newChildWindowDriver = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			if (newChildWindowDriver.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + newChildWindowDriver.getTitle());
				break;
			}
		}
		Report.info("Current Window Title :" + newChildWindowDriver.getTitle());
		waitForPageLoad();
		return newChildWindowDriver;
	}


	/**
	 * Usually a successor method to {@link ReusableUtilities.SeleniumBaseClass#clickAndWaitForBrowserPopup(String childWindowTitle, String objName)}.
	 * Called when the operations on child windows are done and wanted to navigate back to parent window 
	 * @param childWindowTitle Name of the child window
	 * @param parentWindowTitle name of the parent window
	 * @param noOfWindowsBefore total number of windows before a child window popup is clicked
	 * @param noOfWindowsAfter total number of windows after a child window popup appears
	 * @return handler to parent window
	 */
	public WebDriver closechildWindowAndNavigateToItsParentWindowHandle(WebDriver toCloseChildWindowDriver, String parentWindowHandle, int noOfWindowsBefore, int noOfWindowsAfter){
		WebDriver childWindowDriver = null;
		WebDriver parentWindowDriver = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows Noticed :  " + handles.size() + " : Expected window count: " + noOfWindowsBefore);
		boolean isChildWindowClosed = false;

		String expectedChildWinHandleToClose = toCloseChildWindowDriver.getWindowHandle();
		String expectedChildWinTitleToClose = toCloseChildWindowDriver.getTitle();

		// Closing the child windows
		for (String s : handles) {
			String windowHandle = s; 
			childWindowDriver = driver.switchTo().window(windowHandle);

			if (childWindowDriver.getTitle().equals(expectedChildWinTitleToClose) && 
					(!childWindowDriver.getWindowHandle().equals(parentWindowHandle)) && 
					childWindowDriver.getWindowHandle().equals(expectedChildWinHandleToClose) && 
					isChildWindowClosed == false){
				String childWindowTitle = childWindowDriver.getTitle();
				String childWindowHandleDetails = childWindowDriver.getWindowHandle();
				driver.close();
				isChildWindowClosed = true;
				Report.info("Successully closed child window with Title : " + childWindowTitle);
				Report.info("Successully closed child window Handle : " + childWindowHandleDetails + " : Expected window to be closed :" + expectedChildWinHandleToClose);
				break;
			}
		}

		//		handles = driver.getWindowHandles();
		Report.info("No of windows after closing child window :  " + handles.size() + " : Expected window count: " + noOfWindowsAfter);
		parentWindowDriver = driver.switchTo().window(parentWindowHandle);

		/*		for (String s : handles) {
			String windowHandle = s; 
			parentWindowDriver = driver.switchTo().window(windowHandle);
			waitForPageLoad();
			Report.info("Window Title : " + parentWindow.getTitle());
			Report.info("Window Url : " + parentWindow.getCurrentUrl());

			if (parentWindowDriver.getWindowHandle().equals(parentWindowHandle)){
				Report.info("Selected Window Title : " + parentWindowDriver.getTitle());
				Report.info("Selected Window Handle : " + parentWindowDriver.getWindowHandle());
//				return parentWindow;
				break;
			}
		}*/
		Report.info("Current Window Title :" + parentWindowDriver.getTitle());
		Report.info("Current Window handle : " + parentWindowDriver.getWindowHandle() + " Expected window handle: " + parentWindowHandle);
		return parentWindowDriver;

	}


	/**
	 * Used when a new window popup comes up on double clicking a button/link/icon in IE
	 * An alternative for getHandleToWindow in IE
	 * @param childWindowTitle Name of the child window to which the windows focus is transferred to 
	 * @param objName object identification information for the link/button/icon which opens up a popup window
	 * @return handler to the new popup window
	 */
	public WebDriver doubleClickAndWaitForBrowserPopup(String childWindowTitle, String objName) {
		WebDriver popupWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Report.info("No of windows should be 1 :  " + handles.size());

		doubleClick(objName);
		/* int timeCount = 1;

		do
		{
		   handles = driver.getWindowHandles();
		   try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
				Report.info("Could not wait for interuppted time");
				e.printStackTrace();
			}
		   timeCount++;
		   if ( timeCount > getTotalWaitTimeInSecs() ) 
		   {
		       break;
		   }
		}
		while ( handles.size() == 1 );*/

		WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		handles = driver.getWindowHandles();
		Report.info("No of windows should be 2 :  " + handles.size());

		for (String s : handles) {
			String windowHandle = s; 
			popupWindow = driver.switchTo().window(windowHandle);
			Report.info("Window Title : " + popupWindow.getTitle());
			Report.info("Window Url : " + popupWindow.getCurrentUrl());

			if (popupWindow.getTitle().contains(childWindowTitle)){
				Report.info("Selected Window Title : " + popupWindow.getTitle());
				return popupWindow;
			}
		}
		Report.info("Window Title :" + popupWindow.getTitle());
		waitForPageLoad();
		return popupWindow;
	}

	/*
	 * Frames
	 */

	/*
	 * Deprecated method, not used in IE
	 */
	public void switchToFrame(final String frameXpath) {
		/*try {
			WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(frameXpath)));
			Report.info("Could able to swith to frame");
		} catch (Exception e) {
			e.printStackTrace();
			Report.fail("Could able to swith to frame " + frameXpath);
		}*/

		try {
			WebDriverWait wait = new WebDriverWait(driver, getTotalWaitTimeInSecs());
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(frameXpath)));
			Report.info("Navigated to frame with id " + frameXpath);
		} catch (NoSuchFrameException e) {
			Report.fail("Unable to locate frame with id " + frameXpath
					+ e.getStackTrace());
		} catch (Exception e) {
			Report.fail("Unable to navigate to frame with id " + frameXpath
					+ e.getStackTrace());
		}
		//		driver.switchTo().frame(driver.findElement(By.xpath(frameXpath)));
	}

	/*
	 * Deprecated method, not used in IE
	 */
	public void switchToDefaultContentFrame() {
		try {
			driver.switchTo().defaultContent();
			Report.info("Navigated back to webpage from frame");
		} catch (Exception e) {
			Report.fail("unable to navigate back to main webpage from frame"
					+ e.getStackTrace());
		}
	}

	/**
	 * 
	 */
	public void enterTextInTinyMCEInIE(){
		/*JavascriptExecutor js = (JavascriptExecutor) driver;
		String returnVal = js.executeScript("tinyMCE.activeEditor.setContent('<p>Native API text</p> TinyMCE')").toString();
		 Report.info("JS: Ready State: " + returnVal);*/
		((JavascriptExecutor)driver).executeScript("tinyMCE.activeEditor.setContent('<p>Native API text</p> TinyMCE')");
	}

	/**
	 * Insert text into all tinyMCE editors in a page which are writable
	 * @param text text to be inserted into tinyMCE editor
	 */
	public void insertIntoAllTinyMCEinPage(String text){

		List<WebElement> tinyMCE = getTinyMCEInPage();
		Report.info("No of tinyMCE in page is: " + tinyMCE.size());

		for (WebElement tiny: tinyMCE ) {
			String tinyMCEId = tiny.getAttribute("id");
			if(!tinyMCEId.contains("readOnly")){
				String tinyId = tinyMCEId.replaceAll("_ifr","");
				Report.info("TinyMCE id" + tinyId);
				enterTextInTinyMCE(tinyId, text);
			}
		}

	}

	/**
	 * Get all TinyMCEs in page
	 * @return all iframe WebElements in a webpage
	 */
	public List<WebElement> getTinyMCEInPage(){
		return explicitlyWaitAndGetWebElements("frame_Action_FastAction_QueueSubmittedSubmissionText", getTotalWaitTimeInSecs());
	}

	/**
	 * Enter text into TinyMCE editor, usually the id is the same as hidden corresponding textarea id of a iframe
	 * @param textAreaId textArea id
	 * @param textToInsert text to insert in tinyMCE editor
	 */
	public void enterTextInTinyMCE(String textAreaId, String textToInsert){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("tinymce.getInstanceById('" + textAreaId + "').setContent('" + textToInsert + "')");
	}

	/**
	 * Get text from tinyMCE editor and remove starting <p> tag and ending </p> tag
	 * For Dubgging - DON'T NOT SWITCH TO FIREFOX BROWSER
	 * @param textAreaId texArea id
	 * @return text contained in TinyMCE editor/ textarea field
	 * @throws Exception NoSuchElement exception
	 */
	public String getTinyMCEText(String textAreaId) throws Exception{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement element = driver.findElement(map.getLocator(textAreaId));
		String tinyMCEId = element.getAttribute("id");
		String textInserted = null;
		Report.info("Text id is - Textarea id:" + tinyMCEId);
		String script = "return tinymce.getInstanceById('" + tinyMCEId + "').getContent()";
		textInserted =  (String) js.executeScript(script);
		Report.info("Text idenfified from tinyMCE textfield:" + textInserted);
		// remove the starting <p> tag and ending </p> tag
		textInserted = textInserted.replaceAll("\\<[^>]*>","");
		Report.info("Text returned from getTinyMCEText()" + textInserted.trim());
		return textInserted;
	}

	/*
	 * Hovering over Menus
	 */
	/**
	 * 
	 * @param hoverElement
	 */

	public void mouseHoverJScript(WebElement hoverElement) {

		try {
			//if (isElementPresent(hoverElement)) {
			String mouseOverScript = "if(document.createEvent){"
					+ "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initEvent('mouseover',	true, false); "
					+ "arguments[0].dispatchEvent(evObj);"
					+ "} else if(document.createEventObject) { "
					+ "	arguments[0].fireEvent('onmouseover');"
					+ "}";
			((JavascriptExecutor) driver).executeScript(mouseOverScript, hoverElement);
			Report.pass("Successfully hovered on Element :" + hoverElement);
			//	} else {
			//	Report.fail("Element was not visible to hover " + "\n");
			//}
		} catch (StaleElementReferenceException e) {
			Report.fail("Element with " + hoverElement + "is not attached to the page document" + e.getStackTrace());
		} catch (NoSuchElementException e) {
			Report.fail("Element " + hoverElement + " was not found in DOM" + e.getStackTrace());
		} catch (Exception e) {
			e.printStackTrace();
			Report.fail("Error occurred while hovering" + e.getStackTrace());
		}
	}


	public void enterAgeSubmitbuttonWithJScript(String objName, String age) {
		WebElement submitButton = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
		String submitButtonId = submitButton.getAttribute("id");
		//		Report.info("submit button tag info:" + submitButton.toString());
		//		Report.info("submission ID attribute:" + submitButtonId);

		String javaScriptCode = "function getSubmitBtn(parentName, childObj) {"
				+ " var testObj = childObj.parentNode;"
				+ " var count = 1;"
				+ " while(true) {"
				+ "if (testObj.nodeName == parentName && testObj.className.indexOf('ui-dialog') > -1) {"
				+ " var btns = testObj.getElementsByClassName('btn');"
				+ " for(var i=0; i<btns.length; i++){"
				+ "if (btns[i].textContent && btns[i].textContent.indexOf('Submit') > -1) {"
				+ "return btns[i];"
				+ " }"
				+ " }"
				+ " }"
				+ " testObj = testObj.parentNode;"
				+ " count++;"
				+ "}"
				+ "}"
				+ "var elm = document.getElementById(\'" + submitButtonId +"\');"
				+ "var submitBtn = getSubmitBtn('DIV', elm);"
				+ "elm.focus();"
				+ "elm.value= " + age +";"
				+ "if ('createEvent' in document) {"
				+ " var evt = document.createEvent('HTMLEvents');"
				+ " evt.initEvent('change', false, true);"
				+ "elm.dispatchEvent(evt);"
				+ "}"
				+ "else{"
				+ " elm.fireEvent('onchange');}"
				+ "submitBtn.focus();"
				+ "submitBtn.click();";
		//		Report.info("javascript code::");
		//		Report.info(javaScriptCode);
		((JavascriptExecutor) driver).executeScript(javaScriptCode);        
	}     

	public void clickTypingPenIcon() throws InterruptedException {
		sleep(5000);
		explicitlyWaitForElement("icon_Documents_TinyMce", getTotalWaitTimeInSecs());		
		String javaScriptCode = "var span = document.getElementsByClassName('fa ui-icon-pencil')[0];"
				+ " span.focus();"
				+ "span.click(); ";
		Report.info("javascript code::");
		Report.info(javaScriptCode);
		((JavascriptExecutor) driver).executeScript(javaScriptCode); 
		waitForJQuery(driver);
	}


	WebElement Element;

	/**
	 * Click an element using Javascript
	 * 
	 * @param element
	 *            WebElement information
	 * 
	 * @throws Exception
	 */
	public void performClickElementViaJavaScript(String objName)
			throws Exception {
		try {
		    Report.pass("Clicking on element with using java script click");
			Element = driver.findElement(map.getLocator(objName));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", Element);
			 Report.pass("Element is clicked sucessfully through JS");

		} catch (StaleElementReferenceException e) {
			Report.fail("Element is not attached to the page document "+ e.getStackTrace());
		} 
		catch(Exception e) {
			Report.fail("Unable to click on element " + e.getStackTrace());
		}

	}
	
	
	/**
	 * @author - Vikas Rana
	 * 
	 * Double Click an element using Javascript
	 * 
	 * @param objName WebElement locater information
	 *      
	 *  Function can be used for double clicking on element in case web driver functins are unable or inconsistent for double clicking.      
	 */
	public void performDoubleClickUsingJavaScript(String objName)
			throws Exception {
		try {
		    Report.pass("Double clicking on element with using java script click");
			 WebElement element= driver.findElement(map.getLocator(objName));
			 ((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents');"+ 
					    "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"+ 
					    "arguments[0].dispatchEvent(evt);", element);
			Report.pass("Element is double clicked sucessfully through JS");

		} catch (StaleElementReferenceException e) {
			Report.fail("Element is not attached to the page document "+ e.getStackTrace());
		} 
		catch (Exception e) {
			Report.fail("Unable to double click on element " + e.getStackTrace());
		}

	}
	
	/**
	 * Sendkeys an element using Javascript
	 * 
	 * @param ObjName - WebElement locator 
	 * 
	 * @param inputText- String send to text box
	 *            
	 * @throws Exception
	 */

	public void performSendKeysViaJavaScript(String objName, String inputText)throws Exception {
		try {
		Element = driver.findElement(map.getLocator(objName));
		String js = "arguments[0].setAttribute('value','" + inputText + "')";
		((JavascriptExecutor) driver).executeScript(js, Element);
		 Report.pass("Sendkeys is performed through JS sucessfulling ");

		} catch (StaleElementReferenceException e) {
			Report.fail("Element is not attached to the page document "+ e.getStackTrace());
		} catch (Exception e) {
			Report.fail("Unable to perform sendkeys on element " + e.getStackTrace());
		}
	}

	/**
     * Click an element using Javascript
     * @param element WebElement information 
      * @throws Exception
     */
     public void clickElementUsingJS(WebElement element) throws Exception {
           try {
                 if (isElementPresent(element)) {
                       Report.pass("Clicking on element with using java script click");

                       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                 } else {
                       Report.fail("Unable to click on element either element not enabled or not displayed");
                 }
           } catch (StaleElementReferenceException e) {
                 Report.fail("Element is not attached to the page document "+ e.getStackTrace());
           } catch (NoSuchElementException e) {
                 Report.fail("Element was not found in DOM "+ e.getStackTrace());
           } catch (Exception e) {
                 Report.fail("Unable to click on element "+ e.getStackTrace());
           }
     }

	/**
	 * Click an element using Javascript
	 * @param element WebElement information 
	 * @throws Exception
	 */
	public void clickElementUsingJS(String elementIdenfier) throws Exception {
		WebElement element = explicitlyWaitAndGetWebElement(elementIdenfier, getTotalWaitTimeInSecs());

		try {
			if (isElementPresent(element) && element.isDisplayed() && element.isEnabled()) {
				Report.pass("Clicking on element with using java script click");

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else if(isElementPresent(element) && !(element.isDisplayed())){
				clickElementUsingJS(elementIdenfier);
			} else {
				Report.fail("Unable to click on element either element not enabled or not displayed");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			Report.fail("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			Report.fail("Unable to click on element "+ e.getStackTrace());
		}
	}

	public String getElementContentUsingJS(String elementId) throws Exception {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String elementContent = (String) js.executeScript("return document.getElementById(elementId).innerHTML;");
		return elementContent;
	}



	/*
	 * Expand or collapse Sections 
	 */
	/**
	 * Expand a section within a webpage
	 * @param obj object identification information of the section
	 */
	public void expandSection(String obj){
		WebElement element = explicitlyWaitAndGetWebElement(obj, getTotalWaitTimeInSecs());
		String collapseOrExpand = element.getAttribute("class");

		if(collapseOrExpand.contains("collapsed")||collapseOrExpand.contains("Collapsed")){
			element.click();
			Report.info("Section " + obj + " Successfully expanded");
		} else{
			Report.info("Section " + obj + " already in expanded state");
		}

	}

	/**
	 * Collapse a section in webpage
	 * @param obj object identification information of the section
	 */
	public void collapseSection(String obj){
		WebElement element = explicitlyWaitAndGetWebElement(obj, getTotalWaitTimeInSecs());
		String collapseOrExpand = element.getAttribute("class");

		if(collapseOrExpand.contains("expanded")){
			element.click();
			Report.info("Section " + obj + " Successfully collapsed");
		} else {
			Report.info("Section " + obj + " already in collapsed state");
		}
	}

	/**
	 * verify section on a webpage is in collapsed state 
	 * @param obj object identification information of the section, usually takes <div> element id object information
	 */
	public void verifySectionCollapsed(String obj){
		WebElement element = explicitlyWaitAndGetWebElement(obj, getTotalWaitTimeInSecs());
		String collapseOrExpand = element.getAttribute("class");

		if(collapseOrExpand.contains("collapsed")||collapseOrExpand.contains("Collapsed")){
			Report.pass("Section '" + obj + "' is in collapsed state");
		} else {
			softAssertCount++;
			Report.warn("Section '" + obj + "' is in expanded state");
			softAssert.fail("Section '" + obj + "' is in expanded state");
		}
	}

	/**
	 * verify section on a webpage is in expanded state 
	 * @param obj object identification information of the section, usually takes <div> element id object information
	 */
	public void verifySectionExpanded(String obj){
		WebElement element = explicitlyWaitAndGetWebElement(obj, getTotalWaitTimeInSecs());
		String collapseOrExpand = element.getAttribute("class");

		if(collapseOrExpand.contains("expanded")||collapseOrExpand.contains("Expanded")){
			Report.pass("Section '" + obj + "' is in expanded state");
		} else {
			softAssertCount++;
			Report.warn("Section '" + obj + "' is in collapsed state");
			softAssert.fail("Section '" + obj + "' is in collapsed state");
		}
	}

	/**
	 * Verifying cursor focus on a specific element is present or not
	 * @param objName object identification information of an element
	 */
	public void verifyCurrentCursorFocus(String objName) {

		try {

			WebElement obj = driver.findElement(map.getLocator(objName));
			WebElement element = driver.switchTo().activeElement();
			softAssert.assertEquals(obj.getText(), element.getText());
			if (obj.equals(element)) {
				Report.info("Current cursor focus is  in '" + objName
						+ "' element");
			} else {
				softAssertCount++;
				Report.warn("Current cursor focus is Not in '" + objName
						+ "' element");
			}

		} catch (Exception e) {
			Report.fail("Unable to find on element '" + objName + "' " + e.getStackTrace());
		}
	}

	/**
	 * Open new browser instance and get focus of that window
	 * Used for navigating to menu items which opens popup window; eg - View Action in popup
	 */
	public String openNewWindowAndGetHandle() {
		String childWindowHandle = null;

		try {
			baseWindow = driver.getWindowHandle();
			System.out.println("Base window handle:" + baseWindow);
			Report.info("Opening New Window and Swich To window");
			((JavascriptExecutor) driver).executeScript("window.open();");
			Set<String> allWindow = driver.getWindowHandles();
			for (Iterator<String> iterator = allWindow.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				System.out.println("Window Handle:" + string);
			}
			String windowHandle = ((String) allWindow.toArray()[1]);
			System.out.println("child Window handle:" + windowHandle);
			System.out.println("current Window handle:" + getCurrentWindowHandle());
			swithToChildWindow(windowHandle);
			System.out.println("current Window handle for swith:" + getCurrentWindowHandle());
			while (!windowHandle.equals(getCurrentWindowHandle())) {
				swithToChildWindow(windowHandle);
			}
			childWindowHandle = windowHandle;
			System.out.println("current Window handle:" + childWindowHandle);
			return windowHandle;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return childWindowHandle;
	}

	public String openNewWindowAndGetHandle(String parentWindowHandle) {
		String childWindowHandle = null;

		try {
			//			baseWindow = driver.getWindowHandle();
			Set<String> allHandlesBeforepopup = driver.getWindowHandles();
			System.out.println("Base window handle:" + baseWindow);
			Report.info("Opening New Window and Swich To window");
			((JavascriptExecutor) driver).executeScript("window.open();");
			/*Set<String> allWindow = driver.getWindowHandles();
			for (Iterator<String> iterator = allWindow.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				System.out.println("Window Handle:" + string);
			}*/
			String windowHandle = null;
			//			String windowHandle = ((String) allWindow.toArray()[1]);
			Set<String> allHandlesafterpopup = driver.getWindowHandles();
			for (Iterator<String> iterator = allHandlesafterpopup.iterator(); iterator
					.hasNext();) {
				windowHandle = (String) iterator.next();
				System.out.println("Window handle:" + windowHandle);
			}
			allHandlesafterpopup.removeAll(allHandlesBeforepopup);
			System.out.println("child Window handle:" + windowHandle);
			System.out.println("current Window handle:" + getCurrentWindowHandle());
			swithToChildWindow(windowHandle);
			System.out.println("current Window handle for swith:" + getCurrentWindowHandle());
			while (!windowHandle.equals(getCurrentWindowHandle())) {
				swithToChildWindow(windowHandle);
			}
			childWindowHandle = windowHandle;
			System.out.println("current Window handle:" + childWindowHandle);
			return windowHandle;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return childWindowHandle;
	}


	public void swithToChildWindow(String windowHandle) {
		/*if(isAlertPresent()){
			acceptAlertBox(driver);
		}*/
		/*while (!windowHandle.equals(getCurrentWindowHandle())) {
			driver.switchTo().window(windowHandle);
		}*/
		driver.switchTo().window(windowHandle);


	}

	public void closeWindowAndGetHandle(){
		driver.close();
		driver.switchTo().window(baseWindow);
		Report.info("Closing window and and Swich To window");
	}

	public WebDriver openNewWindowAndGetHandleWithChildWebDriver(WebDriver driver) {

		try {

			((JavascriptExecutor) driver).executeScript("window.open();");
			sleep(5000);
			Set<String> allWindow = driver.getWindowHandles();
			String windowHandle = ((String) allWindow.toArray()[1]);
			WebDriver winDriver = driver.switchTo().window(windowHandle);
			winDriver.manage().window().maximize();
			Report.info("Opening New Window and Swich To window");
			return winDriver;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return driver;
	}

	public void pressReturnKey(String objName)
	{
		try
		{
			WebElement e = fluentWaitAndGetWebElement(getTotalWaitTimeInSecs(), getPoolWaitTime(), objName);
			e.sendKeys(Keys.RETURN);
			//			driver.findElement(map.getLocator(objname)).clear();
			//			driver.findElement(map.getLocator(objname)).sendKeys(value);
			Report.pass("return key is hit in '"+objName+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objName+"' is not displayed");
			Assert.fail("element '"+objName+"' is not displayed");
		}
	}


	/**
	 * Tabs
	 */

	/**
	 * Verifies if a tab is active in application, by passing the object identification information 
	 * @param objname pass object identification information
	 */
	@SuppressWarnings("unchecked")
	public boolean verifyTabActive(String objname) {
		try {

			/*			@SuppressWarnings("rawtypes")
			Wait wait = new FluentWait(driver).withTimeout(getTotalWaitTimeInSecs(), TimeUnit.SECONDS)
					.pollingEvery(getPoolWaitTime(), TimeUnit.SECONDS);
				wait.until(ExpectedConditions.visibilityOfElementLocated(map.getLocator(objname)));
				WebElement element = fluentWaitAndGetWebElement(getTotalWaitTimeInSecs(), getPoolWaitTime(), objname);*/
			WebElement element = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			WebElement parentLiElement = getToLiElement(element);
			if(element.getAttribute("aria-selected").contains("true") || isElementPresent(objname) && ( parentLiElement.getAttribute("class").contains("ui-state-active") | parentLiElement.getAttribute("class").contains("active"))){
				Report.pass("Tab " + objname + " Enabled");
				return true;
			} else{
				Report.fail("Tab " + objname + " NOT Enabled");
				Assert.fail("Tab " + objname + " NOT Enabled");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public WebElement getToLiElement(WebElement element){
		if(element.getTagName().equals("span")){
			WebElement a =  getParentElement(element);
			Report.info("Current Element is " + element.getTagName());
			return getParentElement(a);
		} else{
			if (element.getTagName().equals("a")){
				Report.info("Current Element is " + element.getTagName());
				return getParentElement(element);
			} else {
				Report.info("Current Element is " + element.getTagName());
				return element;
			}
		}
	}

	public WebElement getParentElement(WebElement element) {
		WebElement parentElement = element.findElement(By.xpath("./.."));
		Report.info("Navigated to parent elemement '" + parentElement.getTagName() + "'");
		return parentElement;
	}



	public boolean isChkBoxChked(String objName) throws Exception {
		WebElement element = driver.findElement(map.getLocator(objName));
		if (isElementPresent(objName) && 
				element.getAttribute("type").contains("checkbox") && 
				isAttribtuePresent(element, "checked")) {
			Report.info("Checkbox '" + objName + "' is checked now");
			return true;
		}
		Report.info("Checkbox '" + objName + "' is not checked");
		return false;
	}

	public boolean isChkBoxUnChked(String objName) throws Exception{
		WebElement element = driver.findElement(map.getLocator(objName));
		String elementTypeAttVal = null;
		try {
			elementTypeAttVal = element.getAttribute("type");
		} catch (StaleElementReferenceException e) {
			element = driver.findElement(map.getLocator(objName));
			elementTypeAttVal = element.getAttribute("type");
		}
		catch (Exception e) {
			Report.warn("The attribute value for element could not be found.");
		}
		if (isElementPresent(objName) && 
				elementTypeAttVal.contains("checkbox") && 
				!(isAttribtuePresent(element, "checked"))) {
			Report.info("Checkbox '" + objName + "' is not checked now");
			return true;
		}
		Report.info("Checkbox '" + objName + "' is checked");
		return false;
	}


	/**
	 * checkbox specific methods
	 * @throws Exception 
	 */
	public boolean isCheckBoxChecked(String objName) throws Exception {
		WebElement element = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
		if (isElementPresent(objName) && 
				element.getAttribute("type").contains("checkbox")||element.getAttribute("type").contains("radio") && 
				isAttribtuePresent(objName, "checked")) {
			Report.info("Checkbox '" + objName + "' is checked now");
			return true;
		}
		Report.info("Checkbox '" + objName + "' is not checked");
		return false;
	}

	public boolean isCheckBoxUnChecked(String objName) throws Exception{
		WebElement element = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
		String elementTypeAttVal = null;
		try {
			elementTypeAttVal = element.getAttribute("type");
		} catch (StaleElementReferenceException e) {
			element = explicitlyWaitAndGetWebElement(objName, getTotalWaitTimeInSecs());
			elementTypeAttVal = element.getAttribute("type");
		}
		catch (Exception e) {
			Report.warn("The attribute value for element could not be found.");
		}
		if (isElementPresent(objName) && elementTypeAttVal.contains("checkbox")||element.getAttribute("type").contains("radio")&& !(isAttribtuePresent(objName, "checked"))) {
			Report.info("Checkbox '" + objName + "' is not checked now");
			return true;
		}
		Report.info("Checkbox '" + objName + "' is checked");
		return false;
	}

	public void checkCheckBox(String objName) throws Exception{
		if(isCheckBoxUnChecked(objName)){
			click(objName);
			Report.info("Checkbox '" + objName + "' is marked as checked");
		} else {
			Report.info("Checkbox '" + objName + "' is already checked");
		}
	}

	public void uncheckCheBox(String objName) throws Exception{
		if(isCheckBoxChecked(objName)){
			click(objName);
			Report.info("Checkbox '" + objName + "' is marked as un-checked");
		} else {
			Report.info("Checkbox '" + objName + "' is already un-checked");
		}
	}

	protected void verifyCheckBoxReadOnly(String objName) {

		if(isAttribtuePresent(objName, "disabled")){
			Report.pass("CheckBox '" + objName + "' is in disabled state");
		} else {
			softAssertCount++;
			Report.warn("CheckBox '" + objName + "' is NOT in disabled state");
			softAssert.fail("CheckBox '" + objName + "' is NOT in disabled state");
		}
	}

	public String getUserName(){
		return readConfigFile("username").toUpperCase();
	}

	public String getPassword(){
		return readConfigFile("password");
	}

	// verifying element are sorted in table column
	// passing objname should be column xpath
	public void verifySortTableColumn(String objName) throws Exception {
		try {
			ArrayList<String> obtainedList = new ArrayList<>();

			List<WebElement> elementList = driver.findElements(map
					.getLocator(objName));
			for (WebElement we : elementList) {
				System.out.println(we.getText());
				obtainedList.add(we.getText().toUpperCase());
			}
			

			ArrayList<String> sortedList = new ArrayList<>();
			for (String s : obtainedList) {
				sortedList.add(s);
			}
			Collections.sort(sortedList);
			// Assert.assertTrue(sortedList.equals(obtainedList));
			if (sortedList.equals(obtainedList)) {
				Report.pass("Table Colummn  is sorted in ascending order");
			} else {
				softAssertCount++;
				Report.fail("Table Colummn  is Not sorted ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// verifying element are sorted in table column
		// passing objname should be column xpath
		public void verifySortTableColumnDecendingOrder(String objName) throws Exception {
			try {
				ArrayList<String> obtainedList = new ArrayList<>();

				List<WebElement> elementList = driver.findElements(map
						.getLocator(objName));
				for (WebElement we : elementList) {
					System.out.println(we.getText());
					obtainedList.add(we.getText().toUpperCase());
				}
				

				ArrayList<String> sortedList = new ArrayList<>();
				for (String s : obtainedList) {
					sortedList.add(s);
				}
				Collections.sort(sortedList, Collections.reverseOrder());
				//Collections.sort(sortedList);
				// Assert.assertTrue(sortedList.equals(obtainedList));
				if (sortedList.equals(obtainedList)) {
					Report.pass("Table Colummn  is sorted in ascending order");
				} else {
					softAssertCount++;
					Report.fail("Table Colummn  is Not sorted ");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	public void injectingJQuery() throws IOException{
		jQueryLoader = readJSFile(JQUERY_LOAD_SCRIPT);
		jQuery = (JavascriptExecutor) driver;
		jQuery.executeAsyncScript(jQueryLoader /*, http://localhost:8080/jquery-1.7.2.js */);
		Report.info("JQuery injection is done");
	}

	/**
	 * read JqueryLoad snippet file
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readJSFile(String file) throws IOException {
		Charset cs = Charset.forName("UTF-8");
		FileInputStream stream = new FileInputStream(file);
		try {
			Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		}
		finally {
			stream.close();
		}        
	}



	/**
	 * 
	 * @param filePath- docxcontent file path 
	 */
	public String readDocFile(String filePath)throws Exception{

		File file = new File(filePath);
		String docDataRead = null;
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		XWPFDocument document = new XWPFDocument(fis);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (XWPFParagraph para : paragraphs) {
			docDataRead =  para.getText();
		}

		fis.close();
		return docDataRead;

	}

	/**
	 * 
	 * @param filePath- Location to write to file 
	 * @param fileName
	 * @throws Exception
	 */
	public void writeToFile(String filePath,  String fileName, String data)throws Exception{

		FileWriter fWriter = null;
		BufferedWriter writer = null;
		fWriter = new FileWriter(filePath+fileName);
		writer = new BufferedWriter(fWriter);
		writer.append(data);
		writer.newLine(); 
		writer.close(); 

	}

	public boolean verifyTabNotActive(String objname) {
		try {

			WebElement element = explicitlyWaitAndGetWebElement(objname, getTotalWaitTimeInSecs());
			WebElement parentLiElement = getToLiElement(element);
			if(isElementPresent(objname) && ( parentLiElement.getAttribute("class").contains("ui-state-disabled"))){
				Report.pass("Tab " + objname + " Disabled");
				return true;
			} else{
				Report.fail("Tab " + objname + " Enabled");
				Assert.fail("Tab " + objname + " Enabled");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Verifies if a field is read only in application, by passing the object identification information 
	 * @param objname pass object identification information
	 */
	public boolean verifyFieldIsReadOnly(String objname) {
		try {
			WebElement element = driver.findElement(map.getLocator(objname));
			if(isElementPresent(objname) && (element.getAttribute("readonly").equals("true"))){
				Report.pass("Textfield " + objname + " is readonly");
				return true;
			} else{
				Report.fail("Textfield " + objname + " is not readonly");
				Assert.fail("Textfield " + objname + " is not readonly");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Performs Right Click and selects the Objects from the contect Menu
	 * @param objname - Right Click Object 
	 * @param selectObjname - Selection Object after Right Click
	 */

	public void rightClickSelection(String objname , String selectObjname){
		try{
			explicityWaitForAnElementAndRightClickAndSelect(objname, getTotalWaitTimeInSecs());
			Report.pass("Right Clicked on '"+ objname +"'");
			waitForPageLoad();
			selectAfterRightClick(selectObjname);
		}catch(Exception e){
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	/**
	 * After reight Click Selects the object from the contect Menu
	 * @param objname- Selection Object after Right Click
	 */

	public void selectAfterRightClick(String objname){
		try{
			explicityWaitForAnElementAndSelectAfterRightClick(objname, getTotalWaitTimeInSecs());
			Report.pass("Selected after Right Click '"+ objname +"'");
			waitForPageLoad();
		}catch(Exception e){
			Report.fail("element '"+objname+"' is not displayed");
			Assert.fail("element '"+objname+"' is not displayed");
		}
	}

	/**
	 * 
	 * @param objName- Right Click Object
	 * @param waitTimeInSeconds
	 * @throws Exception
	 */

	public void explicityWaitForAnElementAndRightClickAndSelect(String objName, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocator(objName)));
			Actions rightClick = new Actions(driver);		
			rightClick.moveToElement(element);
			rightClick.contextClick(element).build().perform();
			Report.info(objName + "Element is Right clicked ");
		}catch (Exception e) {
			Report.fail("Could not able to find the element in wait time:"+objName+ waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: "+objName+ waitTimeInSeconds + " Seconds");

		}
	}
	/**
	 * 
	 * @param objName- Selection Object after Right Click
	 * @param waitTimeInSeconds
	 * @throws Exception
	 */

	public void explicityWaitForAnElementAndSelectAfterRightClick(String objName, long waitTimeInSeconds) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				waitTimeInSeconds);
		WebElement elementSelect = null;
		try {
			elementSelect = webDriverWaitForTask.until(ExpectedConditions.elementToBeClickable(map.getLocator(objName)));
			elementSelect.click();
			Report.info(objName + "Element is Selected after Right Click");
		} catch (Exception e) {
			Report.fail("Could not able to find the element in wait time:"+objName+ waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: "+objName+ waitTimeInSeconds + " Seconds");

		}
	}
	/**
	 * 
	 * @param expectedElementFont
	 * @param Objname
	 * @throws Exception
	 */
	//400 for font-weight : normal, and 700 for font-weight : bold
	public void verifyElementFontWeight(String expectedElementFont, String Objname)throws Exception{
		WebElement elementFont = explicitlyWaitAndGetWebElement(Objname, getTotalWaitTimeInSecs());
		String font = elementFont.getCssValue("font-weight");
		System.out.println("font wait is ="+font);
		if(expectedElementFont.equals(font)){
			Report.pass("The font Weight is '"+font+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("Expected Font '" + expectedElementFont +"' is not same as actual font '" + font + "'");
			softAssert.fail("Expected Font '" + expectedElementFont +"' is not same as actual colour '" + font + "'");
		}
	}
	/**
	 * Verifies Elements which are present in DOM but GUI not  displayed
	 * @param obj
	 * @return 
	 * @throws Exception
	 */
	public boolean verifyHiddenDivisionClosed(String obj) throws Exception{
		WebElement element = driver.findElement(map.getLocator(obj));
		String elementPresentButNotDisplayed = element.getAttribute("style");

		if(elementPresentButNotDisplayed.contains("display: none")){

			Report.pass("Element/Pop Up Division  " + obj + " Present  in DOM and GUI pop up Closed ");
			return true;
		} else {
			Report.info("Element/Pop Up Division " + obj + " Present in DOM  but GUI pop up not closed ");
			return false;
		}
	}

	/**
	 * 
	 * @param maxLengthValue - Field Defined Max Character length
	 * @param obj - 
	 * @throws Exception
	 */
	public void verifyFieldMaxLength(String maxLengthValue , String obj,String attribute) throws Exception{
		WebElement element = driver.findElement(map.getLocator(obj));
		String elementMaxLength = element.getAttribute(attribute);
		Report.info("elementMaxLength:"+ elementMaxLength);

		if(elementMaxLength.equals(maxLengthValue)){

			Report.pass("maxLengthValue  is same as elementMaxLength:"+elementMaxLength+ "for" +obj);
		} else {
			Report.fail("maxLengthValue  is  not same as elementMaxLength:"+elementMaxLength+ "for" +obj);
		}
	}

	/**
	 * 
	 * @param objname
	 * @param expValue
	 */
	public void verifyTableRowCountForEqualAndGreater(String objname, int expValue , String replacement){
		int actualVaule=0;
		actualVaule = getTableRowCountForEqualAndGreater(objname,replacement);
		Report.info("Table contains "+actualVaule+ " rows");

		if (actualVaule >= expValue){

			Report.pass("Verified table rows and the Table contains:"+actualVaule+" row");
		} else {

			Report.fail("Actual table row count:"+actualVaule+" and expected table row count:"+expValue+" did not match");
			Assert.fail("Table '"+objname+"' is not displayed");
		}

	}

	public void verifyTableRowCountForEqualAndGreater(String objname, int expValue) throws Exception{
		int actualVaule=0;
		actualVaule = driver.findElements(map.getLocator(objname)).size();
		Report.info("Table contains "+actualVaule+ " rows");

		if (actualVaule >= expValue){

			Report.pass("Verified table rows and the Table contains:"+actualVaule+" row");
		} else {

			Report.fail("Actual table row count:"+actualVaule+" and expected table row count:"+expValue+" did not match");
			Assert.fail("Table '"+objname+"' is not displayed");
		}

	}
	public int getTableRowCountForEqualAndGreater(String objname,String replacement){

		try {
			return driver.findElements(map.getLocatorWithDynamicXpath(objname, replacement)).size();
		} catch (Exception e) {
			Report.fail("Table '"+objname+"' is not displayed");
			Assert.fail("Table '"+objname+"' is not displayed");
			return 0;
		}

	}


	/**
	 * 
	 * @param actValue
	 * @param expValue
	 */

	public void compareTwoIntValues(int actValue ,int expValue ){
		if (actValue == expValue){
			Report.info("Actual value :"+actValue+ "Expected value :" +expValue+ " Found Match");
		}
		else{
			Report.info("Actual value :"+actValue+ "Expected value :" +expValue+ " Not Found Match");
			Assert.fail("Actual value :"+actValue+ "Expected value :" +expValue+ " Not Found Match");

		}
	}

	//Method to get Machine Name Details
	public String getHostName() throws Exception{
		InetAddress ip;
		String hostname;
		ip = InetAddress.getLocalHost();
		hostname = ip.getHostName();
		Report.info("current IP address : " + ip);
		Report.info("current Hostname : " + hostname);
		return hostname;


	}

	/**
	 * 
	 * @param objname- Object on Which Mouse Hover is performed
	 */
	public void mouseHover(String objname){
		try{

			Actions mouseHover = new Actions(driver);
			WebElement toolTip = (driver.findElement(map.getLocator(objname)));
			mouseHover.moveToElement(toolTip).build().perform();		
			Report.pass("Mouse Hover on '"+objname+"' is successfull");

		}catch(Exception e){

			Report.fail("element '"+objname+"' is not displayed or Mouse Hover unsuccesfull");
			Assert.fail("element '"+objname+"' is not displayed or Mouse Hover unsuccesful");

		}
	}

	/**
	 * This function return the time format for the date
	 * @param strDate 
	 * @throws Exception
	 */
	public static String getDateFormat(String date) {
		String[] dateFormat1 = new String[6];
		dateFormat1[0] = "dd MMM yyyy";
		dateFormat1[1] = "dd-MMM-yy";
		dateFormat1[2] = "dd/MM/yyyy";
		dateFormat1[3] = "MM/dd/yyyy";
		dateFormat1[4] = "yyyy-MM-dd";
		dateFormat1[5] = "mm-dd-yyyy";
		int dateWise = 0;

		while (dateWise <= (dateFormat1.length - 1)) {

			SimpleDateFormat sdfrmt = new SimpleDateFormat(
					dateFormat1[dateWise]);
			sdfrmt.setLenient(false);
			try {
				Date javaDate = sdfrmt.parse(date);
				return dateFormat1[dateWise];
			}

			catch (ParseException e) {
			}

			dateWise++;
		}
		return "Wrong Date Format";

	}


	/**
	 * This function return the time format for the date
	 * @param strDate 
	 * @throws Exception
	 */
	public static String getTimeFormat(String time) {
		String[] timeFormat = new String[4];
		timeFormat[0] = "HH:mm:ss a";
		timeFormat[1] = "HH:mm:ss";
		timeFormat[2] = "HH:mm a";
		timeFormat[3] = "HH:mm";

		int timeWise = 0;
		while (timeWise <= (timeFormat.length - 1)) {

			SimpleDateFormat sdfrmt = new SimpleDateFormat(timeFormat[timeWise]);
			sdfrmt.setLenient(false);
			try {
				Date javaDate = sdfrmt.parse(time);
				return timeFormat[timeWise];
			}

			catch (ParseException e) {
				System.out.println("Throw exception "+timeFormat[timeWise]);
			}

			timeWise++;
		}
		return "Wrong Time Format";

	}	

	/**
	 * 
	 * @param obj- verify Element is Disabled 
	 * @throws Exception 
	 */
	public boolean isDisabled(String objName) throws Exception {

		String disabled = driver.findElement(map.getLocator(objName)).getAttribute("disabled");
		if(isElementPresent(objName) && (disabled.contains("true"))){
			Report.pass("Element " + objName + " is disabled");
			return true;
		} else{
			Report.fail("Element " + objName + " is enabled");
			return false;
		}

	}

	public void explicityWaitForAnElementAndRightClickAndSelectForDynamicElement(String objName, long waitTimeInSeconds,String replacement) throws Exception {
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,waitTimeInSeconds);
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocatorWithDynamicXpath(objName,replacement)));
			Actions rightClick = new Actions(driver);		
			rightClick.moveToElement(element);
			rightClick.contextClick(element).build().perform();
			Report.info(objName + "Element is Right clicked ");
		}catch (Exception e) {
			Report.fail("Could not able to find the element in wait time:"+objName+ waitTimeInSeconds + " Seconds");
			Assert.fail("Could not able to find the element in wait time: "+objName+ waitTimeInSeconds + " Seconds");

		}
	}
	
	
	/**
	 * Used to find a element in the element list. EX. tag list, table row or column etc
	 * @param 'objName' 
	 * @param "expectedValue"
	 * Function take the exepcted value and match with the values of element list
	 */
	@SuppressWarnings("unused")
	public void verifyElementExistsInTheList(String objName , String expectedValue) throws Exception{
		sleep(5000);
		String actulValue = null;
		boolean elementMatch=false;
		List<WebElement> elementList=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementList){
			Report.info("Iterative Element Is="+element.getText());
			if((element.getText().contains(expectedValue))){
				elementMatch=true;
				actulValue=element.getText();
				break;
			}
		} 
		if(elementMatch==true){
			Report.pass("The Expected Value: '"+expectedValue+"' Is Found In The Element List");
		}
		if(elementMatch==false){
			softAssertCount++;
			Report.warn("The Expected Value: '"+expectedValue+"' Is Not Found In The Element List");
			softAssert.fail("The Expected Value '"+expectedValue+"' IS Not Same As Actual Value '" + actulValue + "'");
			Report.fail("The Expected Value Is Not Found In The Element List");

		}
	} 
	
	/**Used to find a Starting Text of element in the element list. EX. tag list, table row or column etc
	 * Usually used for wild Card search Text match
	 * @param objName - Table Name 
	 * @param expectedValue - expected partial text
	 * @throws Exception
	 */
	public void verifyElementsInListStartsWith(String objName ,String expectedValue) throws Exception{
		String actulValue = null;
		boolean elementStartsWith=false;
		List<WebElement> elementListStartsWith=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementListStartsWith){
			if((element.getText().startsWith(expectedValue))){
				elementStartsWith=true;
				actulValue=element.getText();
			}
			else{
				elementStartsWith=false;
			}
		} 
		if(elementStartsWith==true){
			Report.pass("The element starts from : '"+expectedValue+"' ");
		}
		if(elementStartsWith==false){
			softAssertCount++;
			Report.warn("The element does not start from : '"+expectedValue+"' ");
			softAssert.fail("The Actual Value "+ actulValue + "does not start with '"+expectedValue+"' ");
			Report.fail("The element does not start from : '"+expectedValue+"' ");

		}
	} 
	
	/**Used to find a Ending Text of element in the element list. EX. tag list, table row or column etc
	 * Usually used for wild Card search Text match
	 * @param objName - Table Name 
	 * @param expectedValue - expected partial text
	 * @throws Exception
	 */
	public void verifyElementsInListEndsWith(String objName ,String expectedValue) throws Exception{
		String actulValue = null;
		boolean elementEndsWith=false;
		List<WebElement> elementListEndsWith=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementListEndsWith){
			if((element.getText().endsWith(expectedValue))){
				elementEndsWith=true;
				actulValue=element.getText();
			}
			else{
				elementEndsWith=false;
			}
		} 
		if(elementEndsWith==true){
			Report.pass("The element ends with : '"+expectedValue+"' ");
		}
		if(elementEndsWith==false){
			softAssertCount++;
			Report.warn("The element does not end with : '"+expectedValue+"' ");
			softAssert.fail("The Actual Value "+ actulValue + "does not end with '"+expectedValue+"' ");
			Report.fail("The element does not end with : '"+expectedValue+"' ");

		}
	} 

	
	/**Used to find a Starting Text of element in the element list. EX. tag list, table row or column etc
	 * Usually used for wild Card search Text match
	 * @param objName - Table Name 
	 * @param expectedValue - expected partial text
	 * @throws Exception
	 */
	public void verifyElementsInListNotStartsAndNotEndWithAndContainText(String objName ,String expectedValue) throws Exception{
		String actulValue = null;
		boolean elementStartsWith=false;
		List<WebElement> elementListStartsWith=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementListStartsWith){
			if(!(element.getText().startsWith(expectedValue))&& !(element.getText().endsWith(expectedValue))&& (element.getText().contains(expectedValue))){
				System.out.println("check the elemet text is="+element.getText());
				elementStartsWith=true;
				actulValue=element.getText();
			}
		} 
		if(elementStartsWith==true){
			Report.pass("The element contains'"+expectedValue+"' ");
		}
		if(elementStartsWith==false){
			softAssertCount++;
			Report.warn("The element does not contains : '"+expectedValue+"' ");
			softAssert.fail("The Actual Value "+ actulValue + "does not start with '"+expectedValue+"' ");
			Report.fail("The element does not contains : '"+expectedValue+"' ");

		}
	} 

	
	/**
	 * 
	 * @param expectedCursorType: cursor: pointer( for hand type Cursor) 
	 *                            cursor: default(for default cursor)
	 *                            cursor: none(for no cursor display)
	 * @param Objname
	 * @throws Exception
	 */
	
	public void verifyElementCursorType(String expectedCursorType, String Objname)throws Exception{
		WebElement elementCursor = explicitlyWaitAndGetWebElement(Objname, getTotalWaitTimeInSecs());
		String actCursorType = elementCursor.getCssValue("cursor");
		Report.info("actCursorType is :"+actCursorType);
		if(expectedCursorType.equals(actCursorType)){
			Report.pass("The cursor type is '"+actCursorType+"' is verified");
		}else{
			softAssertCount++;
			Report.warn("Expected Cursor '" + expectedCursorType +"' is not same as actual font '" + actCursorType + "'");
			softAssert.fail("Expected Cursor '" + expectedCursorType +"' is not same as actual colour '" + actCursorType + "'");
		}
	}
	
	/**
	 * Used to check that element not exists in the element list. EX. tag list, table row or column etc
	 * @param 'objName' 
	 * @param "expectedValue"
	 * Function take the exepcted value and match with the values of element list
	 */
	public void verifyElementNotExistsInTheList(String objName , String expectedValue) throws Exception{
		String actulValue = null;
		boolean elementMatch=true;
		List<WebElement> elementList=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementList){
			if((element.getText().contains(expectedValue))){
				elementMatch=false;
				actulValue=element.getText();
				break;
			}
		} 
		if(elementMatch==true){
			Report.pass("The Expected Value: '"+expectedValue+"' Is Not Found In The Element List");
		}
		if(elementMatch==false){
			softAssertCount++;
			Report.warn("The Expected Value: '"+expectedValue+"' Is  Found In The Element List");
			softAssert.fail("The Expected Value '"+expectedValue+"' IS  Same As Actual Value '" + actulValue + "'");
			Report.fail("The Expected Value Is  Found In The Element List");

		}
	} 

	/**
	 * Used to find a element in each iteration of element list. EX. tag list, table row or column etc
	 * @param 'objName' 
	 * @param "expectedValue"
	 * Function take the exepcted value and match with the values of each iteration of element list
	 */
	@SuppressWarnings("unused")
	public void verifyElementExistsInEachIterationOfList(String objName , String expectedValue) throws Exception{
		sleep(5000);
		String actulValue = null;
		boolean elementMatch=false;
		List<WebElement> elementList=driver.findElements(map.getLocator(objName));
		for (WebElement element:elementList){
			if((element.getText().contains(expectedValue))){
				elementMatch=true;
				actulValue=element.getText();
				
			}
			else{
				elementMatch=false;
				break;
			}
		} 
		if(elementMatch==true){
			Report.pass("The Expected Value: '"+expectedValue+"' Is Found In Each Iteration Of Element List");
		}
		if(elementMatch==false){
			softAssertCount++;
			Report.warn("The Expected Value: '"+expectedValue+"' Is Not Found In Each Iteration Of Element List");
			softAssert.fail("The Expected Value '"+expectedValue+"' IS Not Same As Actual Value '" + actulValue + "'");
			Report.fail("The Expected Value Is Not In Each Iteration Of Element List");

		}
	} 
	
	/**Used to find a dynanic element in the list or collection. EX. label list, table rows  etc
	 * Perform click on dynamic element
	 * @param objName 
	 * @Replacement - replace dynamic value with replacement parameter
	 * @throws Exception
	 */
	public void clickOnDynamicElement(String objName, String replacement){
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,
				getTotalWaitTimeInSecs());
		WebElement element = null;

		try {
			element = webDriverWaitForTask.until(ExpectedConditions
					.elementToBeClickable(map.getLocatorWithDynamicXpath(objName, replacement)));
			element.click();
			Report.info("'" + objName + "' Element is clicked");
		} catch(StaleElementReferenceException e){
			Report.info("StaleElementReferenceException Caught");
		} catch (Exception e) {
			//	e.printStackTrace();
			Report.fail("Could not able to find the element '" + objName + "' "
					+ "in wait time " + getTotalWaitTimeInSecs() + " Seconds");
			Assert.fail("Could not able to find the element '" + objName + "' "
					+ "in wait time " + getTotalWaitTimeInSecs() + " Seconds");
		}
	}
	
	
	/**Used to find a dynanic element in the list or collection. EX. label list, table rows  etc
	 * Perform sendkeys on dynamic element
	 * @param objName 
	 * @Replacement - replace dynamic value with replacement parameter at run time
	 * @param value - string text is entered in textfield or textbox
	 * @throws Exception
	 */
	public void sendKeysForDynamicElement(String objName,String replacement, String value)	{
		WebDriverWait webDriverWaitForTask = new WebDriverWait(driver,getTotalWaitTimeInSecs());
		WebElement element = null;
		try {
			element = webDriverWaitForTask.until(ExpectedConditions.elementToBeClickable(map.getLocatorWithDynamicXpath(objName, replacement)));			
			Report.info("'" + objName + "' Element is fetched");
		} catch (Exception e) {
			Report.fail("element '"+objName+"' is not displayed");
			Assert.fail("element '"+objName+"' is not displayed");
		}
		try
		{
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + value + "';", element);
			Report.pass("'"+value+"' is entered in '"+objName+"'");
		}
		catch(Exception e)
		{
			Report.fail("element '"+objName+"' is not displayed");
			Assert.fail("element '"+objName+"' is not displayed");
		}
	}

	/** Wait until the element does not disappear from page EX. table pagination spinner etc.
	 * @param objName 
	 * @throws Exception
	 */
	
	public void waitForSpinnerElementToDisappear(String objName) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(map.getLocator(objName)));

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/****************************************************************************************/
	
	public void launchAppInIEOnPerfromacneDJIApp(String URL)
	{
		display_Execution_DateTime();
		openIEBrowserLaunchPerfromacneDJIApp(URL);
	}
	
	public void openIEBrowserLaunchPerfromacneDJIApp(String URL) 
	{
		try{
			baseUrl=readConfigFile("TestRunAppURL");

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// to clear cache in IE
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("unexpectedAlertBehaviour", "accept");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("disable-popup-blocking", true);
			capabilities.setCapability("enablePersistentHover", true);

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\AutoItfiles\\IEDriverServer1.exe");
			driver = new InternetExplorerDriver(capabilities);
			ApplicationUtilities appSpecificMethods = new ApplicationUtilities();
			appSpecificMethods.handleSecurityWindow1();
			sleep(3000);
			driver.get(URL);


			sleep(2000);			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			//			driver.manage().timeouts().implicitlyWait(getTotalWaitTimeInSecs(),TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(getPageLoadWaitTime(), TimeUnit.SECONDS);
			Report.pass("Opened IEBrowser and Application is launched");

		}
		catch(Exception e)
		{			
			Report.fail("Application is not launched " + e);			
		}
	}	
	
	
	
	
}
