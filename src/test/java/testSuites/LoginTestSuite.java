package testSuites;

import baseTest.BaseTest;
import loans.pageObjects.LoginPages;
import loans.testSteps.LoginSteps;
import org.testng.annotations.Test;
import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;

public class LoginTestSuite extends BaseTest {
    @Test(priority = 0)
    public void loginGoogle() throws Exception {
        try{
            LoginSteps.signInGoogle("teller");
            LoginSteps.loginKpx("teller");
            LoginSteps.onClick ( LoginPages.objQCLL, " Navigate to Loans" );
            LoginSteps.onClick ( LoginPages.objcontinue, "continue " );
        }catch (Exception e){
            LoggingUtils.error ( "Error encounter when trying to log in with  google with an errro of " + e.getMessage () );
            ExtentReporter.logFail ( "Unable to log in with Google ", "Error encounter when trying to log in with  google with an errro of " + e.getMessage () );
        }
    }
    @Test(priority = 1, dependsOnMethods = "loginGoogle", groups = "Test")
    public void toVerifyHoldModule() throws Exception{
        System.out.println ("Testingggggg");
    }
}
