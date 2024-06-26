package utilities.ReusableComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static utilities.Driver.DriverManager.getDriver;

public class Reports extends ExtentReporter {

    public static int getElementCount(By locator) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            int count = elements.size();  // Subtracting 2 from the element size
            LoggingUtils.info("Element count: " + count);
            ExtentReporter.logInfo("Element Count", "Counted " + count + " elements with locator: " + locator);
            return count;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get element count: " + e.getMessage());
            ExtentReporter.logFail("Unable to Get Element Count", "Failed to get element count with locator: " + locator);
            throw e;
        }
    }
    public static int getPtnCount(By locator) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            int count = elements.size() - 2;  // Subtracting 2 from the element size
            LoggingUtils.info("Element count: " + count);
            ExtentReporter.logInfo("Element Count", "Counted " + count + " elements with locator: " + locator);
            return count;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get element count: " + e.getMessage());
            ExtentReporter.logFail("Unable to Get Element Count", "Failed to get element count with locator: " + locator);
            throw e;
        }
    }

    public static String formatLoanValue(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        return decimalFormat.format(value);
    }


    public static double parseLoanValue(String loanValueText) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance( Locale.US);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat.parse(loanValueText).doubleValue();
    }

    public static String getTotalValue(By locator, String valueName) {
        try {
            List<WebElement> elements = getDriver ().findElements ( locator );
            double total = 0.0;
            int size = elements.size ();

            for (int i = 0; i < size - 1; i++) {
                String loanValueText = elements.get ( i ).getText ().replace ( ",", "" ); // Remove commas
                double loanValue = parseLoanValue ( loanValueText );
                total += loanValue;
            }

            String formattedTotal = formatLoanValue ( total );
            LoggingUtils.info ( "Total " + valueName + " value: " + formattedTotal );
            ExtentReporter.logInfo ( "Total " + valueName + " Value", "Calculated total " + valueName + " value: " + formattedTotal );

            return formattedTotal;
        } catch (Exception e) {
            LoggingUtils.error ( "Failed to get total " + valueName + " value: " + e.getMessage () );
            ExtentReporter.logFail ( "Unable to Get Total " + valueName + " Value", "Failed to get total " + valueName + " value with locator: " + locator );
            throw new Error ( " Errror : " + e.getMessage () );
        }

    }
}
