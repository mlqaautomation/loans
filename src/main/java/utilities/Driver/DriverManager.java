package utilities.Driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utilities.Logger.LoggingUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

public class DriverManager {
    //thread local for web driver
    public static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<> ();
    //private static final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");
    public static synchronized void createDriver(final DriverType browser){
        switch (browser){
            case CHROME:
                setupChrome();
                break;
            case FIREFOX:
                setupFirefox();
                break;
            default:
                LoggingUtils.error("Invalid Browser...");
                break;
        }
        setupBrowserTimeouts();
    }
    public static synchronized void setupChrome(){
        LoggingUtils.info("Setting up chrome driver...");
        HashMap<String, Object> chromePreferences = new HashMap<>();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless=new");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
//        options.addArguments("--incognito");
        options.addArguments("use-fake-ui-for-media-stream");

        String extensionPath = "C:\\EG2003\\MLSYSTEM\\ml-chrome-extension";

        options.addArguments("--load-extension=" + extensionPath);

        setDriver(new ChromeDriver(options));
        LoggingUtils.info("Chrome Driver created successfully");

        String sessionId = ((ChromeDriver) getDriver ()).getSessionId().toString();
        LoggingUtils.info("Session ID: " + sessionId);
    }

    public static synchronized void setupFirefox() {
        LoggingUtils.info("Setting up Firefox driver...");
        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.addArguments("--remote_allow-origin");


        setDriver(new FirefoxDriver (options));
        LoggingUtils.info("Firefox Driver created successfully");

        String sessionId = ((FirefoxDriver) getDriver()).getSessionId().toString();
        LoggingUtils.info("Session ID: " + sessionId);
    }


    //method for quitting driver
public static synchronized  void closeWebBrowser() {
    WebDriver driver = getDriver();
    if (driver != null) {
        Set<String> windowHandles = driver.getWindowHandles();
        String mainWindowHandle = driver.getWindowHandle(); // Store the main window handle

        // Close all windows except the main window
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                LoggingUtils.info("Switch to window " + windowHandle);
                driver.close();
                LoggingUtils.info("Window closed");
            }
        }

        // Switch back to the main window handle
        driver.switchTo().window(mainWindowHandle);

        driver.quit();
        LoggingUtils.info("Driver quit");
        setDriver(null); // Set WebDriver instance to null after quitting
    }
}

    //method for setting up driver
    private static synchronized void setDriver (WebDriver driver){
        DriverManager.DRIVER.set(driver);
    }

    //method for getting driver
    public static synchronized WebDriver getDriver () {
        return DriverManager.DRIVER.get ();
    }

    //setup for timeouts
    private static synchronized void setupBrowserTimeouts(){
        LoggingUtils.info("Setting browser timeouts...");
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        getDriver().manage().timeouts().pageLoadTimeout (Duration.ofSeconds (20));
        getDriver().manage().timeouts().scriptTimeout (Duration.ofSeconds (20));
    }

    private DriverManager(){}
}
