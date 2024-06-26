package baseTest;


import loans.testSteps.PracticeSteps;
import org.openqa.selenium.*;
import loans.pageObjects.LoginPages;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utilities.Driver.DriverType;
import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;
import utilities.PropertyReader.propertyReader;
import utilities.ReusableComponents.TOTPGenerator;

import java.time.Duration;
import java.util.Set;

import static utilities.Driver.DriverManager.*;
import static utilities.ReusableComponents.GeneralMethod.*;


public class BaseTest {

    public static propertyReader prop;

    protected PracticeSteps QCLSteps;

    @Parameters({"browser", "system"})
    @BeforeClass(alwaysRun = true)
    public void setUp(final String browser, String system) throws Exception {
        initializeDriver(DriverType.valueOf(browser.toUpperCase()));
        getDriver().manage().deleteAllCookies();
        getDriver().get(System.getProperty("targetUrl"));
    }

    private synchronized void initializeDriver(DriverType driverType) {
        createDriver(driverType);
    }


//    @BeforeMethod(alwaysRun = true)
//    public void setUpTests() throws Exception {
//        getDriver().get(System.getProperty("homeUrl"));
//    }

    @AfterMethod(alwaysRun = true)
    public  void clean(){
        LoggingUtils.info("Test Ended");

    }

    @AfterClass(alwaysRun = true)
    public  void tearDown () {
        closeWebBrowser();

    }

    public void propertyFileReader() {
        prop = new propertyReader("src\\main\\resources\\testDatas\\testData.properties");
    }


    public final void signInGoogle(String google)throws NoSuchElementException {
        try{
            click ( LoginPages.googleSign, " Google  sign-on " );
            waitSleep ( 1200 );
            switchNextTab();
            typeEnter (LoginPages.email_google, " Google email input field ",  reader.getEmailByRole(google) );
            typeEnter(LoginPages.password_google, " Google password input field ", reader.getPasswordByRole(google) );
            typeEnter(LoginPages.authenticator, "Auth Key Field", TOTPGenerator.getTwoFactorCode());
            switchToPreviousTab();
        }catch (Exception e){
            LoggingUtils.error ( "Unsuccessful sign in with google with an error message  "  + e.getMessage ());
            ExtentReporter.logFail ( "Unsuccessful sign in with google " , " Unable to sign in with google with with an error of " + e.getMessage ());
        }
    }


    public  void loginKpx(String role)throws Exception{
        try{
            waitSleep ( 1000 );
            type(LoginPages.kpxusername,  reader.getKpxUsername(role), "kpx username field");
            type(LoginPages.kpxpassword, reader.getKpxPassword(role),"kpx password field" );
            onClick(LoginPages.kpxlogind, "Login button");
        }catch (Exception e){
            ExtentReporter.logFail("Logn-in KPX " , "error"+ e);
            ExtentReporter.logFail ( "Unsuccessful log in KPX" , " Unable to sign in KPX with with an error of " + e.getMessage ());
        }
    }

    public static void switchToNextTabAndClose() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                LoggingUtils.info("Switched to " + windowHandle);
                ExtentReporter.logInfo("Switch to next tab and close","Switched to " + windowHandle);
                getDriver().close();
                break;
            }
        }

        getDriver().switchTo().window(currentWindowHandle);
        LoggingUtils.info("Switched back to " + currentWindowHandle);
        ExtentReporter.logInfo("Switch to next tab and close","Switched back to " + currentWindowHandle);
    }

    public  void onClick(By locator, String elementName){
        try{
            if(isVisible(locator, elementName)) {
                getDriver ().findElement ( locator ).click ();
                LoggingUtils.info ( "Clicked on element " + locator + " locator of  " + elementName );
            }
        }catch (Exception e){
            LoggingUtils.error ( e.getMessage () );
            LoggingUtils.error ( "Unable to click on element " + locator + " locator of  " + elementName   );
        }
    }

    public void type(By locator, String value,  String elementName){
        try{
            if(isVisible(locator, elementName)) {
                getDriver ().findElement ( locator ).sendKeys ( value );
                LoggingUtils.info ( "Type into field: " + locator  );
                ExtentReporter.logInfo ( " Type in the " + elementName , " Type in the " + elementName + "Successfully" );
            }
        }catch (Exception e){
            LoggingUtils.error ( "Failed to type into field: " + locator + " with an error of " + e.getMessage ()   );
            ExtentReporter.logFail ( "Unsuccessful to type in the " + elementName , "Unsuccessful to type in the element with an error of " + e.getMessage () );
        }
    }


    public void typeEnter(By locator, String elementName, String text){
        try {
            if(isVisible(locator, elementName)) {
                getDriver ().findElement ( locator ).sendKeys ( text );
                WebElement element = getDriver ().findElement ( locator );
                element.sendKeys(Keys.ENTER);
                LoggingUtils.info("Typed into field: " + elementName );
                ExtentReporter.logInfo ( "Type then enter ", " Typed in the " + elementName  );
            }else{
                LoggingUtils.error("Failed to type into field: "+ elementName );
                ExtentReporter.logFail ( "Unable  to type then enter ", " Typed in the " + elementName  );
            }
        } catch (Exception e) {
            LoggingUtils.error (">>Failed to type into field: "+ elementName + ", Value: "+ text);
            ExtentReporter.logFail ( "Unable  to type then enter ", " Typed in the " + elementName  + " with an error " + e.getMessage ());
        }
    }



    public static void switchNextTab() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                LoggingUtils.info("Switch to " + currentWindowHandle);
                break;
            }
        }
    }


    public void switchToPreviousTab() {
        String currentWindowHandle = getWebDriver().getWindowHandle();
        Set<String> windowHandles = getWebDriver().getWindowHandles();
        String previousWindowHandle = null;

        for (String windowHandle : windowHandles) {
            if (windowHandle.equals(currentWindowHandle)) {
                break;
            }
            previousWindowHandle = windowHandle;
        }

        if (previousWindowHandle != null) {
            getWebDriver().switchTo().window(previousWindowHandle);
            LoggingUtils.info("Switch to " + previousWindowHandle);
        } else {
            throw new IllegalStateException("No previous tab found");
        }
    }


    public boolean isVisible(By locator, String elementName) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait (driver, Duration.ofSeconds ( 10 ));

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            LoggingUtils.info("Element: " + elementName + " is visible");
            return true;
        } catch (NoSuchElementException e) {
            LoggingUtils.error("Element: " + elementName + " is not visible");
            throw new AssertionError("Element: " + elementName + " is not visible", e);
        } catch (StaleElementReferenceException e) {
            // Retry the visibility check
            for (int i = 0; i < 3; i++) {
                try {
                    wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(locator)));
                    LoggingUtils.info("Element: " + elementName + " is visible after retry");
                    return true;
                } catch (StaleElementReferenceException ignored) {
                    // Wait for a short duration before retrying
                    sleepForMilliseconds(500);
                }
            }
            LoggingUtils.error("Element: " + elementName + " is not visible after retries");
            throw new AssertionError("Element: " + elementName + " is not visible after retries", e);
        } catch (TimeoutException e) {
            LoggingUtils.error("Timeout occurred while waiting for element: " + elementName + " to become visible");
            throw new AssertionError("Timeout occurred while waiting for element: " + elementName + " to become visible", e);
        } catch (Exception e) {
            LoggingUtils.error("An exception occurred while checking element visibility: " + e.getMessage());
            throw e;
        }
    }

    void sleepForMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

}
