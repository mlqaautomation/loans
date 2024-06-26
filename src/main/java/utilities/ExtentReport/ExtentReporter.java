package utilities.ExtentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.Driver.DriverManager;
import utilities.Logger.LoggingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static utilities.Driver.DriverManager.getDriver;
public class ExtentReporter implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    private static final String SCREENSHOTS_DIRECTORY = "Screenshots";
    private static File src;

    public void onStart(ITestContext context) {
        extent = new ExtentReports();
        String projectDirectory = System.getProperty("user.dir");
        String reportsDirectory = projectDirectory + "/Reports";
        String suiteName = context.getSuite().getName();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String group = context.getCurrentXmlTest().getIncludedGroups().toArray(new String[0])[0];
        String reportFileName = currentDate + "/" + suiteName + "_" + group + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportsDirectory + "/" + reportFileName);
        extent.attachReporter(spark);
    }

    public static synchronized WebDriver getWebDriver() {
        return getDriver();
    }

    public static synchronized ExtentReports getExtentReports() {
        return extent;
    }

    public static synchronized void logPass(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "pass");
    }

    public static synchronized void logFail(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "fail");

    }
    public static synchronized void logInfo(String step, String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(), step, message, "info");
    }
    private static synchronized void logStatusWithScreenshot(ExtentTest extentTest, String step, String message, String logType) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            String screenshotPath = captureScreenshot(driver, extentTest.getModel().getName());
            if (screenshotPath != null) {
                switch (logType.toLowerCase()) {
                    case "pass":
                        extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).pass(message);
                        break;
                    case "fail":
                        extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).fail(message);
                        break;
                    case "info":
                        extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).info(message);
                        break;
                    default:
                        break;
                }
            } else {
                LoggingUtils.error("Failed to capture screenshot.");
            }
        } else {
            LoggingUtils.error("WebDriver instance is null. Skipping capturing screenshot.");
        }
    }
    private static synchronized String captureScreenshot(WebDriver driver, String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                File src = screenshotDriver.getScreenshotAs(OutputType.FILE);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String screenshotPath = getScreenshotDirectoryPath() + File.separator + timestamp + ".png";
                File screenshotFile = new File(screenshotPath);
                FileHandler.copy(src, screenshotFile);
                return screenshotFile.getAbsolutePath();
            } else {
                LoggingUtils.error("WebDriver does not support taking screenshots.");
            }
        } catch (IOException e) {
            LoggingUtils.error(e.getMessage());
        }
        return null;
    }

    private static synchronized String getScreenshotDirectoryPath() {
        String projectDirectory = System.getProperty("user.dir");
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String screenshotsDirectory = projectDirectory + File.separator + SCREENSHOTS_DIRECTORY + File.separator + currentDate;
        createDirectory(screenshotsDirectory);
        return screenshotsDirectory;
    }
    private static synchronized void createDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                LoggingUtils.error(e.getMessage());
            }
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result){
        try{
            ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
            extentTestThreadLocal.set(extentTest);
            LoggingUtils.info("------->>>Test: "+ result.getName() + " Started<<<--------");
        }catch (Exception e){
            LoggingUtils.error(e.getMessage());
        }
    }
    @Override
    public synchronized void onFinish(ITestContext context){
        extent.flush();
    }

    @Override
    public synchronized void onTestFailure(ITestResult result){
        if(getDriver() != null){
            // logFail(result.getName(),result.getThrowable().getMessage());
            extentTestThreadLocal.get().log(Status.FAIL, result.getName()+ " is Failed " + result.getThrowable().getLocalizedMessage());
            LoggingUtils.error("------->>>Test: "+ result.getName() + " Failed<<<--------");
        }
    }
    @Override
    public synchronized void onTestSkipped(ITestResult result){
        extentTestThreadLocal.get().log(Status.SKIP, result.getName()+ " has been Skipped");
        LoggingUtils.info("------->>>Test: "+ result.getName() + " Skipped<<<--------");
    }
    @Override
    public synchronized void onTestSuccess(ITestResult result){
        extentTestThreadLocal.get().log(Status.PASS, result.getName()+ " Passed");
        LoggingUtils.info("------->>>Test: "+ result.getName() + " Passed<<<--------");
    }

}