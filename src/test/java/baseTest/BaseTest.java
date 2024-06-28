package baseTest;


import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utilities.Driver.DriverType;
import utilities.Logger.LoggingUtils;
import utilities.PropertyReader.propertyReader;

import static utilities.Driver.DriverManager.*;


public class BaseTest {

    public static propertyReader prop;


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




}
