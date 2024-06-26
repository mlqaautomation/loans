package utilities.ReusableComponents;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;
import utilities.yamlReader.yamlReader;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utilities.Driver.DriverManager.getDriver;

public abstract class GeneralMethod extends ExtentReporter {
    public static final WebDriver driver = getDriver();
    public static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    public static final yamlReader reader = new yamlReader();
    private static JavascriptExecutor js;

    public static void clickOnLastElement(By locator) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            int count = elements.size();

            if (count > 0) {
                WebElement lastElement = elements.get(count - 1);
                lastElement.click();
                LoggingUtils.info("Clicked on the last element");
                ExtentReporter.logInfo("Click on Last Element", "Clicked on the last element with locator: " + locator);
            } else {
                LoggingUtils.info("No elements found with locator: " + locator);
                ExtentReporter.logInfo("No Elements Found", "No elements found with locator: " + locator);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to click on the last element: " + e.getMessage());
            ExtentReporter.logFail("Unable to Click on Last Element", "Failed to click on the last element with locator: " + locator);
            throw e;
        }
    }

    public static String getTextToTheLastElement(By locator) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            int count = elements.size();

            if (count > 0) {
                WebElement lastElement = elements.get(count - 1);
                LoggingUtils.info("Get Text on the last element");
                ExtentReporter.logInfo("Get Text  on Last Element", "Get Text on the last element with locator: " + locator);
                return  lastElement.getText ();
            } else {
                LoggingUtils.info("No elements found with locator: " + locator);
                ExtentReporter.logInfo("No Elements Found", "No elements found with locator: " + locator);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to Get Text on the last element: " + e.getMessage());
            ExtentReporter.logFail("Unable to Get Text on Last Element", "Failed to Get Text on the last element with locator: " + locator);
            throw e;
        }
        return null;
    }

    public static void toggleButton(By locator, String elementName) {
        try {
            WebElement toggleButton = getDriver().findElement(locator);
            String ariaCheckedValue = toggleButton.getAttribute("aria-checked");

            if (ariaCheckedValue.equals("false")) {
                LoggingUtils.info("Toggle of "+ elementName +" is currently OFF");
                ExtentReporter.logInfo("Toggle State on " + elementName, "Toggle is currently OFF with locator: " + elementName);

                // Toggle the button ON
                toggleButton.click();
                LoggingUtils.info("Toggled of "+ elementName +" the button ON");
                ExtentReporter.logInfo("Toggle Action on " + elementName, "Toggled the button ON with locator: " + elementName);
                toggleOff ( locator, elementName );
            } else if (ariaCheckedValue.equals("true")) {
                LoggingUtils.info("Toggled of "+ elementName +" the button ON");
                ExtentReporter.logInfo("Toggle State on " + elementName, "Toggle is currently ON with locator: " + elementName);

                toggleButton.click();
                LoggingUtils.info("Toggle of "+ elementName +" is currently OFF");
                ExtentReporter.logInfo("Toggle Action on " + elementName, "Toggled the button OFF with locator: " + elementName);
                toggleOn ( locator, elementName );
            } else {
                LoggingUtils.info("Unable to determine toggle state with locator: " + elementName);
                ExtentReporter.logInfo("Toggle State on " + elementName, "Unable to determine toggle state with locator: " + elementName);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to toggle the button: " + e.getMessage());
            ExtentReporter.logFail("Toggle Button Error on " + elementName, "Failed to toggle the button with locator: " + elementName);
            throw e;
        }
    }
    public static void click(By locator, String elementName) {
        try {
            if (isVisible(locator, elementName)) {
                getDriver().findElement(locator).click();
                LoggingUtils.info(">>Clicked on element: " + elementName);
                ExtentReporter.logInfo("Click on element", " Click the element name : " + elementName + " with the  locator of " + locator);
            }
        } catch (Exception e) {
            LoggingUtils.error(">>Failed to click element: " + elementName + e.getMessage());
            ExtentReporter.logFail("Unable to Click ", " Unable to click the element " + elementName + " with an error : " + e.getMessage());
            throw e;
        }
    }

    public static void type(By locator, String elementName, String text) {
        try {
            if (isVisible(locator, elementName)) {
                WebElement element = getDriver().findElement(locator);
                element.sendKeys(text);
                LoggingUtils.info("Typed into field: " + elementName + ", Value: " + text);
                ExtentReporter.logInfo("Typed into field: " + elementName, "Typed Value: " + text);
            } else {
                ExtentReporter.logFail("Failed to type into field: " + elementName, " Typed Value: : " + text);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to type into field: " + elementName + ", Value: " + text + " " + e.getMessage());
            ExtentReporter.logFail("Failed to type into field: " + elementName, " Typed Value: : " + text + e.getMessage());
            throw e;
        }
    }

    public static void uploadFile(By locator, String fileName) {
        try {
            WebElement chooseFileButton = getDriver().findElement(locator);
            String projectDirectory = System.getProperty("user.dir");
            String filePath = projectDirectory + "/src/main/resources/testing.png";
            chooseFileButton.sendKeys(filePath);
            LoggingUtils.info(">>Uploaded file: " + fileName);
            ExtentReporter.logInfo("Upload a photo", "Uploading file photo");
        } catch (Exception e) {
            LoggingUtils.error(">>Failed to upload file: " + e.getMessage());
            ExtentReporter.logFail("Uploading a photo", "Uploading photo failed: " + e.getMessage());
            throw e;
        }
    }





    public static boolean isElementPresent(By locator) {
        try {
            getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static double calculateTotalAppraisedAmount(By appraisedAmountLocator, By crmAddOn) {
        try {
            double currentAppraisedAmount = getCurrentAppraisedAmount(appraisedAmountLocator);
            double computedAppraisedAmount = computeAppraisedAmount(appraisedAmountLocator, crmAddOn);
            double totalAppraisedAmount = currentAppraisedAmount + computedAppraisedAmount;

            LoggingUtils.info("Total Appraised Amount: " + totalAppraisedAmount);
            ExtentReporter.logInfo("Calculate Total Appraised Amount", "Getting the appraised ammount  with the value of " + totalAppraisedAmount);
            return totalAppraisedAmount;
        } catch (Exception e) {
            LoggingUtils.error("Failed to calculate Total Appraised Amount: " + e.getMessage());
            ExtentReporter.logFail("Calculate Total Appraised Amount", "Getting the appraised ammount  with the error of  " + e.getMessage());
            throw e;
        }

    }

    public static double getCurrentAppraisedAmount(By appraisedAmountLocator) {
        try {
            WebDriver driver = getDriver();
            WebElement element = driver.findElement(appraisedAmountLocator);
            String valueStr = element.getAttribute("value");

            // Remove commas from the value string
            valueStr = valueStr.replace(",", "");

            // Parse the value and return the current appraised amount
            double currentAppraisedAmount = Double.parseDouble(valueStr);

            LoggingUtils.info("Current Appraised Amount: " + currentAppraisedAmount);
            ExtentReporter.logInfo("Current Appraised Amount: ", "Getting the current appraised ammount" + currentAppraisedAmount);
            return currentAppraisedAmount;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get Current Appraised Amount: " + e.getMessage());
            ExtentReporter.logInfo(" Unable Get Appraised Amount: ", "Getting the current appraised ammount" + e.getMessage());
            throw e;
        }
    }

    public static double computeTransactionAmount(By oldAmount, By newAmount) {
        WebElement oldAmountElement = getDriver().findElement(oldAmount);
        WebElement newAmountElement = getDriver().findElement(newAmount);

        String oldAmountText = oldAmountElement.getText().trim().replace(",", "");
        String newAmountText = newAmountElement.getText().trim().replace(",", "");

        double oldAmountValue = 0.0;
        double newAmountValue = 0.0;

        try {
            oldAmountValue = Double.parseDouble(oldAmountText);
            newAmountValue = Double.parseDouble(newAmountText);
            double transactionAmount = newAmountValue - oldAmountValue;
            LoggingUtils.info(" Transaction Amount value is " + transactionAmount);
            ExtentReporter.logInfo("Compute transaction amount", "Computed value of transaction amount : " + transactionAmount);
        } catch (NumberFormatException e) {
            LoggingUtils.error("Failed to compute transaction amount: " + e.getMessage());
            ExtentReporter.logInfo("Unable to compute transaction amount", "Computed value of transaction amount but encounter error: " + e.getMessage());
            throw e;
        }

        return newAmountValue - oldAmountValue;
    }

    public static double calculateProceedAmount(By locator, String elementName) {
        try {
            WebElement element = getDriver().findElement(locator);
            String amountString = element.getText();
            amountString = amountString.replace(",", "");
            LoggingUtils.info("Principal amount value : " + amountString);
            double amount = Double.parseDouble(amountString);
            double interest = amount * 0.04;

            double charges = interest + 5;
            double proceeds = amount - charges;
            LoggingUtils.info("Proceed amount for " + elementName + ": " + proceeds);
            ExtentReporter.logInfo("Calculate proceed amount", "Getting the first value of the  " + elementName);
            return proceeds;
        } catch (NumberFormatException e) {
            LoggingUtils.error("Error parsing the amount for " + elementName + ": " + e.getMessage());
            ExtentReporter.logFail("Unable to Calculate the proceed amount", " Calculating the proceed amount failed with an error of " + e.getMessage());
            throw e;
        }
    }

    public static void typeBelowTenderAmount(By locator, By tenderLocator) {
        try {
            WebElement element = getDriver().findElement(locator);
            String transactionAmount = element.getAttribute("value");
            double tenderAmount = Double.parseDouble(transactionAmount) / 2;
            double aboveTransactionAmount = Double.parseDouble(transactionAmount) * 2;
            WebElement tenderElement = getDriver().findElement(tenderLocator);
            tenderElement.clear();
            tenderElement.sendKeys(String.format("%.2f", tenderAmount));
            LoggingUtils.info("Type below value of tender value of: " + tenderElement);
            ExtentReporter.logInfo("Type below value of tender value of:", "Type vlue of amount with the value of " + tenderElement);
        } catch (Exception e) {
            LoggingUtils.error("Unable to type below tender amount : " + e.getMessage());
            ExtentReporter.logFail("Unable to type below tender amount", " Unable to type below value of tender amount" + e.getMessage());
            throw e;
        }
    }

    public static void typeAboveTenderAmount(By locator, By tenderLocator) {
        try {
            WebElement element = getDriver().findElement(locator);
            String transactionAmount = element.getAttribute("value");
            double tenderAmount = Double.parseDouble(transactionAmount) / 2;
            double aboveTransactionAmount = Double.parseDouble(transactionAmount) * 2;
            WebElement tenderElement = getDriver().findElement(tenderLocator);
            tenderElement.clear();
            tenderElement.sendKeys(String.format("%.2f", aboveTransactionAmount));
            LoggingUtils.info("Type above value of tender value of: " + tenderElement);
            ExtentReporter.logInfo("Type above value of tender value of:", "Type vlue of amount with the value of " + tenderElement);
        } catch (Exception e) {
            LoggingUtils.error("Unable to type above tender amount : " + e.getMessage());
            ExtentReporter.logFail("Unable to type above tender amount", " Unable to type above value of tender amount" + e.getMessage());
            throw e;
        }
    }


    public static double computeAppraisedAmount(By appraisedAmountLocator, By crmAddOn) {
        try {
            WebDriver driver = getDriver();
            WebElement element = driver.findElement(appraisedAmountLocator);
            String valueStr = element.getAttribute("value");

            valueStr = valueStr.replace(",", "");
            int percentage = extractPercentage(crmAddOn, "Appraised Amount");
            double value = Double.parseDouble(valueStr);
            double appraisedAmount = value * (percentage / 100.0);

            LoggingUtils.info("Computed CRM add on  amount: " + appraisedAmount);
            ExtentReporter.logInfo("Compute Appraised Amount", "Calculating the appraised amount with the value of " + appraisedAmount);
            return appraisedAmount;
        } catch (Exception e) {
            LoggingUtils.error("Failed to compute appraised amount: " + e.getMessage());
            ExtentReporter.logFail("Unable to Compute Appraised Amount", "Calculating the appraised amount with but encounter error  " + e.getMessage());
            throw e;
        }

    }

    public static int extractPercentage(By locator, String elementName) {
        try {
            WebDriver driver = getDriver();
            WebElement labelElement = driver.findElement(locator);
            String label = labelElement.getText();

            Pattern pattern = Pattern.compile("\\((\\d+(?:\\.\\d+)?)%\\)");
            Matcher matcher = pattern.matcher(label);

            if (matcher.find()) {
                String percentageStr = matcher.group(1);
                double percentage = Double.parseDouble(percentageStr);
                int percentageValue = (int) percentage;
                LoggingUtils.info("Extracted percentage for " + elementName + ": " + percentageValue + "%");
                ExtentReporter.logInfo("Getting CRM percentage", "Extracted percentage for " + elementName + ": " + percentageValue + "%");
                return percentageValue;
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to extract percentage for " + elementName + ": " + e.getMessage());
            ExtentReporter.logFail("Unable to get  CRM percentage", "Extracted percentage for " + elementName + ": " + e.getMessage());
            throw e;
        }

        return 0; // Default value if no percentage is found or an exception occurs
    }


    public static void toggleOn(By locator, String elementName) {
        try {
            WebElement toggleButton = getDriver().findElement(locator);
            boolean isChecked = toggleButton.getAttribute("aria-checked").equals("true");

            if (isChecked) {
                LoggingUtils.info(elementName + " is already checked.");
                ExtentReporter.logInfo("Toggle On CRN ADD ON", "Toggle on the crm button and it is already toggle on");
            } else {
                toggleButton.click();
                LoggingUtils.info(elementName + " is now checked.");
                ExtentReporter.logInfo("Toggle On CRN ADD ON", "Toggle on the crm button and now it is toggle on");
            }
        } catch (Exception e) {
            LoggingUtils.info("Failed to toggle " + elementName + " on: " + e.getMessage());
            ExtentReporter.logFail("Unable Toggle On CRN ADD ON", "Toggle on the crm button but encounter this error : " + e.getMessage());
            throw e;
        }
    }

    public static void toggleOff(By locator, String elementName) {
        try {
            WebElement toggleButton = getDriver().findElement(locator);
            boolean isChecked = toggleButton.getAttribute("aria-checked").equals("true");

            if (!isChecked) {
                LoggingUtils.info(elementName + " is already unchecked.");
                ExtentReporter.logInfo("Toggle On CRN ADD OFF", "Toggle on the crm button and it is already toggle OFF");
            } else {
                toggleButton.click();
                LoggingUtils.info(elementName + " has been turned off.");
                ExtentReporter.logInfo("Toggle OFF CRN ADD ON", "Toggle on the crm button and now it is toggle off");
            }
        } catch (Exception e) {
            LoggingUtils.info("Failed to toggle " + elementName + " off: " + e.getMessage());
            ExtentReporter.logFail("Unable Toggle OFF CRN ADD ON", "Toggle on the crm button but encounter this error : " + e.getMessage());
            throw e;
        }
    }

    public static void scrollHorizontally(By byLocator, int scrollValue) {
        try {
            WebElement element = getDriver().findElement(byLocator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
            jsExecutor.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", element, scrollValue);
            LoggingUtils.info("Scrolled horizontally ");
            ExtentReporter.logInfo("Scroll Horizontally ", "Scrolling horizontally with the  value of :" + scrollValue);
        } catch (Exception e) {
            LoggingUtils.error(">>Failed to scroll horizontally using locator: " + byLocator + " - " + e.getMessage());
            ExtentReporter.logFail("Unable Scroll Horizontally ", "Scrolling horizontally with the  error of :" + e.getMessage());
            throw e;
        }
    }

    public static void scrollVertically(By byLocator, int scrollValue) {
        try {
            WebElement divElement = getWebDriver().findElement(byLocator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
            jsExecutor.executeScript("arguments[0].scrollLeft = arguments[1]", divElement, scrollValue);
            LoggingUtils.info(">>Scrolled vertically to using locator: " + byLocator);
            ExtentReporter.logInfo("Scroll Vertically ", "Scrolling vertically with the  value of :" + scrollValue);
        } catch (Exception e) {
            LoggingUtils.error(">>Failed to scroll vertically using locator: " + byLocator + " - " + e.getMessage());
            ExtentReporter.logFail("Unable Scroll vertically ", "Scrolling vertically with the  error of :" + e.getMessage());
            throw e;
        }
    }


    public static void typeEnter(By locator, String elementName, String text) {
        try {
            if (isVisible(locator, elementName)) {
                getDriver().findElement(locator).sendKeys(text);
                WebElement element = getDriver().findElement(locator);
                element.sendKeys(Keys.ENTER);
                LoggingUtils.info("Typed into field: " + elementName + ", Value: " + text);
                ExtentReporter.logInfo("Typed into field: " + elementName, "Typed Value: " + text);
            } else {
                ExtentReporter.logFail("Failed to type into field: " + elementName, "Typed Value: " + text);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to type into field: " + elementName + ", Value: " + text);
            ExtentReporter.logFail("Failed to type into field: " + elementName, "Typed Value: " + text);
            throw e;
        }
    }


    public static boolean isVisible(By locator, String elementName) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

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

        } catch (Exception e) {
            LoggingUtils.error("An exception occurred while checking element visibility: " + e.getMessage());
            ExtentReporter.logFail("Unable to  check if dislayed ", " Trying to check the " + elementName + " but failed with error " + e.getMessage());
            throw e;

        }
        return false;
    }

    public static void sleepForMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
//
        }
    }

    public static void mouseHover(By byLocator, String elementName) {
        try {
            if (isVisible(byLocator, elementName)) {
                WebElement element = getDriver().findElement(byLocator);
                Actions action = new Actions(getDriver());
                action.moveToElement(element).perform();
                LoggingUtils.info("Mouse hovered on element: " + elementName);
                ExtentReporter.logInfo("Mouse hover ", " Trying to hover the element : " + byLocator + " xpath value of " + elementName);
            }
        } catch (Exception e) {
            LoggingUtils.error(">>Failed to mouse hover on element: " + elementName + e.getMessage());
            ExtentReporter.logFail("Unable to Mouse hover ", " Trying to hover the element : " + byLocator + " xpath value of " + elementName + " faile caused : " + e.getMessage());
            throw e;
        }
    }


    /**
     * boolean return type for conditions
     *
     * @param locator
     * @return
     * @throws NoSuchElementException
     */

    public static boolean isDisplayed(By locator, String elementName) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(1));

        try {
            WebElement element = getDriver().findElement(locator);
            if (!element.isDisplayed()) {
                LoggingUtils.info("Element: " + elementName + " is not visible");
                return false;
            }
            LoggingUtils.info("Element: " + elementName + " is visible");
            return true;
        } catch (NoSuchElementException e) {
            LoggingUtils.info("Element: " + elementName + " is not found");
            throw e;
        }
    }

    public static boolean checkElement(By locator, String elementName) {
        return isDisplayed(locator, elementName);
    }

    public static void waitToDisappear(By locator, String elementName) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean isVisible = true;

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            isVisible = false;
        } catch (TimeoutException e) {
            LoggingUtils.info("Element: " + elementName + " is still visible");
            ExtentReporter.logInfo("Wait for the element to disappear",
                    "Waiting for the element: " + elementName + " to disappear");
            throw e;
        }

        for (; isVisible; ) {
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
                isVisible = false; // Element is no longer visible, exit the loop
            } catch (TimeoutException e) {
                LoggingUtils.info("Element: " + elementName + " is still visible");
                ExtentReporter.logInfo("Wait for the element to disappear",
                        "Waiting for the element: " + elementName + " to disappear");
                throw e;
            }
        }
    }

    public static void typeAboveLoanAmount(By locator, By loanLocator) {
        try {
            WebElement element = getDriver().findElement(locator);
            String transactionAmount = element.getText();
            double loanAmount = Double.parseDouble(transactionAmount);
            double aboveLoanAmount = loanAmount * 2;
            WebElement loanElement = getDriver().findElement(loanLocator);
            loanElement.clear();
            loanElement.sendKeys(String.format("%.2f", aboveLoanAmount));
            LoggingUtils.info("Type above value of loan amount: " + loanElement);
            ExtentReporter.logInfo("Type above value of loan amount:", "Type value of amount with the value of " + loanElement);
        } catch (Exception e) {
            LoggingUtils.error("Unable to type above loan amount: " + e.getMessage());
            ExtentReporter.logFail("Unable to type above loan amount", "Unable to type above value of loan amount" + e.getMessage());
            throw e;
        }
    }

    public static void inputShouldDisable(By locator, String inputName) {
        try {
            WebElement input = getDriver().findElement(locator);
            if (!input.isEnabled()) {
                LoggingUtils.info("Input " + inputName + " is disabled as expected.");
                ExtentReporter.logInfo("Input Disable Check", "Input " + inputName + " is disabled as expected.");
            } else {
                LoggingUtils.error("Input " + inputName + " is expected to be disabled but it is enabled.");
                ExtentReporter.logFail("Input Disable Check", "Input " + inputName + " is expected to be disabled but it is enabled.");
                throw new AssertionError("Input " + inputName + " is expected to be disabled but it is enabled.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot check input disable status" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot check input disable status" + e.getMessage());
            throw e;
        }
    }

    public static void inputShouldEnable(By locator, String inputName) {
        try {
            WebElement input = getDriver().findElement(locator);
            if (input.isEnabled()) {
                LoggingUtils.info("Input " + inputName + " is enabled as expected.");
                ExtentReporter.logInfo("Input Enable Check", "Input " + inputName + " is enabled as expected.");
            } else {
                LoggingUtils.error("Input " + inputName + " is expected to be enabled but it is disabled.");
                ExtentReporter.logFail("Input Enable Check", "Input " + inputName + " is expected to be enabled but it is disabled.");
                throw new AssertionError("Input " + inputName + " is expected to be enabled but it is disabled.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot check input enable status" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot check input enable status" + e.getMessage());
            throw e;
        }
    }

    public static void buttonShouldEnable(By locator, String buttonName) {
        try {
            WebElement button = getDriver().findElement(locator);
            if (button.isEnabled()) {
                LoggingUtils.info("Button " + buttonName + " is enabled as expected.");
                ExtentReporter.logInfo("Button Enable Check", "Button " + buttonName + " is enabled as expected.");
            } else {
                LoggingUtils.error("Button " + buttonName + " is expected to be enabled but it is disabled.");
                ExtentReporter.logFail("Button Enable Check", "Button " + buttonName + " is expected to be enabled but it is disabled.");
                throw new AssertionError("Button " + buttonName + " is expected to be enabled but it is disabled.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot check button enable status" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot check button enable status" + e.getMessage());
            throw e;
        }
    }

    public static void buttonShouldDisable(By locator, String buttonName) {
        try {
            WebElement button = getDriver().findElement(locator);
            if (button.isEnabled()) {
                LoggingUtils.error("Button " + buttonName + " is expected to be disabled but it is enabled.");
                ExtentReporter.logFail("Button Disable Check", "Button " + buttonName + " is expected to be disabled but it is enabled.");
                throw new AssertionError("Button " + buttonName + " is expected to be disabled but it is enabled.");
            } else {
                LoggingUtils.info("Button " + buttonName + " is disabled as expected.");
                ExtentReporter.logInfo("Button Disable Check", "Button " + buttonName + " is disabled as expected.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot check button disable status" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot check button disable status" + e.getMessage());
            throw e;
        }
    }

    public static boolean isButtonEnabled(By locator, String buttonName) {
        try {
            WebElement button = getDriver().findElement(locator);
            boolean isEnabled = button.isEnabled();
            LoggingUtils.info("Check if button " + buttonName + " is enabled: " + isEnabled);
            ExtentReporter.logInfo("Button Enabled Check", "Checking if button " + buttonName + " is enabled: " + isEnabled);
            return isEnabled;
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot check button enabled status" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot check button enabled status" + e.getMessage());
            throw e;
        }
    }
    public static void shouldNotBeEmpty(By locator, String elementName) {
        try {
            String text = getText(locator, elementName);
            if (text.isEmpty()) {
                LoggingUtils.info("Element '" + elementName + "' is empty.");
                ExtentReporter.logInfo("Element is empty", "The element '" + elementName + "' is empty.");
                throw new Error ("The " + elementName + " does not have any value");
            } else {
                LoggingUtils.info("Element '" + elementName + "' is not empty. With the value of " + text);
                ExtentReporter.logInfo("Element is not empty", "The element '" + elementName + "' is not empty.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Error occurred while checking if element is empty: " + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Error occurred while checking if element is empty: " + e.getMessage());
            throw new Error (" An error occur upon checking the Element with  an error of " + e);
        }
    }

    public static void inputShouldNotBeEmpty(By locator, String elementName) {
        try {
            String text = getValue(locator, elementName);
            if (text.isEmpty()) {
                LoggingUtils.info("Element '" + elementName + "' is empty.");
                ExtentReporter.logInfo("Element is empty", "The element '" + elementName + "' is empty.");
                throw new Error ("The " + elementName + " does not have any value");
            } else {
                LoggingUtils.info("Element '" + elementName + "' is not empty. With the value of " + text);
                ExtentReporter.logInfo("Element is not empty", "The element '" + elementName + "' is not empty.");
            }
        } catch (Exception e) {
            ExtentReporter.logFail("Error occurred while checking if element is empty: " + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Error occurred while checking if element is empty: " + e.getMessage());
            throw new Error (" An error occur upon checking the Element with  an error of " + e);
        }
    }

    public static String getValue(By locator, String elementName) {
        String val = null;
        try {
            val = getDriver().findElement(locator).getAttribute("value");
            LoggingUtils.info("Get text of the  element " + elementName + " value of " + val);
            ExtentReporter.logInfo("Get value for element", "Getting the value of the element the value is : " + val);
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot get value for element" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot get text for element" + e.getMessage());
            throw e;
        }
        return val;
    }

    public static String getText(By locator, String elementName) {
        String val = null;
        try {
            val = getDriver().findElement(locator).getText();
            LoggingUtils.info("Get text with the value of " + val);
        } catch (Exception e) {
            ExtentReporter.logFail("Cannot get text for element" + e.getMessage(), "Caused: " + e);
            LoggingUtils.error("Cannot get text for element" + e.getMessage());
            throw e;
        }
        return val;

    }

    public static boolean assertEqual(String actual, String expected) {
        try {
            Assert.assertEquals(actual, expected);
            LoggingUtils.info(actual + " and " + expected + " are matched");
            ExtentReporter.logInfo("Assertion: " + actual + " and " + expected + " are matched", "asserted values " + actual + " and " + expected);
        } catch (Exception e) {
            LoggingUtils.error("Assertion error: " + e.getMessage());
            ExtentReporter.logFail("Assertion error: " + e.getMessage(), "Caused: " + e);
            throw e;
        }
        return false;
    }

    public static void waitImplicitly(int seconds) {
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
            LoggingUtils.info(">>Waiting implicitly for: " + seconds + " seconds");
        } catch (Exception e) {
            LoggingUtils.error(">>waitImplicitly error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitSleep(int seconds) throws InterruptedException {
        try {
            Thread.sleep(seconds);
            LoggingUtils.info(">>Waiting for: " + seconds + " miliseconds");
        } catch (Exception e) {
            LoggingUtils.error(">>wait error: " + e.getMessage());
            throw e;
        }
    }

    public static void switchToNextTab() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                LoggingUtils.info("Switch to " + currentWindowHandle);
                ExtentReporter.logInfo("Switch Next Tab", "Window ID: " + currentWindowHandle);
                break;
            }
        }
    }

    public static void switchToNextTabAndClose() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        // Wait for the new window to be available
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(windowHandles.size()));

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                try {
                    getDriver().switchTo().window(windowHandle);
                    LoggingUtils.info("Switched to " + windowHandle);
                    ExtentReporter.logInfo("Switch to next tab and close", "Switched to " + windowHandle);
                    getDriver().close();
                    break;
                } catch (WebDriverException e) {
                    LoggingUtils.error("Failed to switch to the next tab and close: " + e.getMessage());
                    ExtentReporter.logFail("Switch to next tab and close", "Failed to switch to the next tab and close: " + e.getMessage());
                    throw e;
                }
            }
        }

        getDriver().switchTo().window(currentWindowHandle);
        LoggingUtils.info("Switched back to " + currentWindowHandle);
        ExtentReporter.logInfo("Switch to next tab and close", "Switched back to " + currentWindowHandle);
    }

    public static void switchToPopUpClose() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        // Check if there are multiple windows/tabs open
        if (windowHandles.size() <= 1) {
            LoggingUtils.info("No next tab/window to switch to or close");
            ExtentReporter.logInfo("Switch to pop up and close", "No next tab/window to switch to or close");
            return;
        }

        Iterator<String> iterator = windowHandles.iterator();
        String firstWindowHandle = iterator.next(); // Get the first window handle
        String secondWindowHandle = iterator.next(); // Get the second window handle

        try {
            getDriver().switchTo().window(secondWindowHandle);
            TimeUnit.SECONDS.sleep(1);
            getDriver().close();
            LoggingUtils.info("Closed window: " + secondWindowHandle);
            ExtentReporter.logInfo("Switch to pop up and close", "Closed window: " + secondWindowHandle);

            // Switch back to the first window
            getDriver().switchTo().window(firstWindowHandle);
        } catch (Exception e) {
            LoggingUtils.error("Failed to switch to/close window: " + secondWindowHandle);
            ExtentReporter.logFail("Switch to pop up and close", "Failed to switch to/close window: " + secondWindowHandle);
            // Handle the exception here, e.g., log an error message or perform alternative actions
        }
    }


    public static void verifyElementIfDisplayed(By locator, String elementName) {
        try {
            if (isVisible(locator, elementName)) {
                LoggingUtils.info("The element " + elementName + " is displayed");
                ExtentReporter.logInfo("Verify element if displayed", "The element " + elementName + " is displayed");
            } else {
                LoggingUtils.error("Failed to display the element :  " + elementName);
                ExtentReporter.logFail("Verify element if displayed", "Failed to display the element :  " + elementName);
                throw new Error ("Failed to display the element " + elementName);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to display the element :  " + elementName);
            ExtentReporter.logFail("Verify element if displayed", "Failed to display the element :  " + elementName);
            throw e;
        }
    }

    public static void switchToNextTabClose() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        // Check if there are multiple windows/tabs open
        if (windowHandles.size() <= 1) {
            LoggingUtils.info("No next tab/window to switch to or close");
            ExtentReporter.logInfo("Switch to next tab and close", "No next tab/window to switch to or close");
            return;
        }

        boolean foundNextWindow = false;

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                try {
                    getDriver().switchTo().window(windowHandle);
                    getDriver().close();
                    LoggingUtils.info("Closed window: " + windowHandle);
                    ExtentReporter.logInfo("Switch to next tab and close", "Closed window: " + windowHandle);
                    foundNextWindow = true;
                } catch (NoSuchWindowException e) {
                    LoggingUtils.error("Failed to switch to/close window: " + windowHandle);
                    ExtentReporter.logFail("Switch to next tab and close", "Failed to switch to/close window: " + windowHandle);
                    throw e;
                }
                break;
            }
        }

        if (!foundNextWindow) {
            LoggingUtils.error("No next tab/window found to switch to or close");
            ExtentReporter.logFail("Switch to next tab and close", "No next tab/window found to switch to or close");
        }

        // Switch driver focus back to the original window/tab
        getDriver().switchTo().window(currentWindowHandle);
    }

    public static void switchToPreviousTab() {
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
            ExtentReporter.logInfo("Switch to previous tab", "Switch to " + currentWindowHandle);
        } else {
            throw new IllegalStateException("No previous tab found");
        }
    }

    public static void    scrollToBottomOfPageWEB() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            logInfo("Scrolled to bottom", "Scrolling to the page to buttom");
        } catch (Exception e) {
            logFail("Scrolled to bottom", "Unable to scroll to bottom: " + e.getMessage());
            ExtentReporter.logFail("Scoll to the buttom", " Scrolling to  the buttom failed with an error of " + e.getMessage());
            throw e;
        }
    }

    public void scrollToTopOfPageWEB() {
        try {
            js = (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scrollBy(0,-250)", "");
            LoggingUtils.info("Scroll from top to the page");
            ExtentReporter.logInfo("Scroll from top to the page", "Scrollign from the bottom to top ");
        } catch (Exception e) {
            ExtentReporter.logFail("Scroll from top to the page", "Scrollign from the bottom to top " + e.getMessage());
        }
    }

    public List<WebElement> staleException_Click(WebElement locator) {
        List<WebElement> outcome = null;
        int repeat = 0;
        while (repeat <= 6) {
            try {
                List<WebElement> ele = getDriver().findElements((By) locator);
                break;
            } catch (StaleElementReferenceException e) {
                e.printStackTrace();
            }
            repeat++;
        }
        return outcome;
    }


//    public static void waitToDisappear(By locator, String elementName) {
//        long startTime = System.currentTimeMillis();
//        long maxWaitTime = 10000; // Maximum wait time in milliseconds
//        long interval = 3; // Interval between checks in milliseconds
//
//        while (System.currentTimeMillis() - startTime < maxWaitTime) {
//            if (!getDriver ().findElement(locator).isDisplayed()) {
//                LoggingUtils.info("Element: " + elementName + " has disappeared");
//                return;
//            }
//
//            try {
//                Thread.sleep(interval);
//            } catch (InterruptedException ignored) {
//                // Handle interrupted exception if needed
//            }
//        }
//
//        LoggingUtils.info("Element: " + elementName + " did not disappear within the specified time");
//    }



    public static void clearTextField(By byLocator, String FieldName) throws Exception {
        try {
            WebElement element = getDriver().findElement(byLocator);
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            LoggingUtils.info(" Clearing  input field of the element " + FieldName);
            ExtentReporter.logInfo("Clearing text field", "Typed the value into " + FieldName);
        } catch (Exception e) {
            LoggingUtils.error(" Unable to clear text field with an error of " + e.getMessage());
            ExtentReporter.logFail("Clearing text field", "Unable to Clear  tect field");
            throw e;
        }
    }

//    public static String getText(By locator, String elementName){
//        String val = null;
//        try{
//            val =getDriver ().findElement ( locator ).getText();
//            LoggingUtils.info ( " The value of " + elementName + " is " + val );
//            ExtentReporter.logInfo ( " Get Text Value of locator", " the value is : " + val );
//        }catch(Exception e){
//            ExtentReporter.logFail("Cannot get text for element" + e.getMessage(), "Caused: "+ e);
//            LoggingUtils.error("Cannot get text for element" + e.getMessage());
//        }
//        return val;
//    }

    public static void getTransactionAmountAsTender(By transactionAmount, By tenderLocator) {
        try {
            String tender = getValue(transactionAmount, "Transaction Amount");
            getDriver().findElement(tenderLocator).sendKeys(tender);
            LoggingUtils.info("Getting the amount of the Transaction Amount");
            ExtentReporter.logInfo("Get Transaction amount to use as tender", "Get Transaction amount to use as tender with the value of " + tender);
        } catch (Exception e) {
            LoggingUtils.error("Get transaction amount then put as tender amoun");
            ExtentReporter.logFail("Unable to get transaction amount", "Get Transaction amount to use as tender but encountered error " + e.getMessage());
            throw e;
        }
    }

    public static void waitTime(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {
            //  logger.error(e);
        }
    }

    public static void scrollDownWEB() {
//		js = (JavascriptExecutor) DriverManager.getDriver();
//		js.executeScript("window.scrollBy(0,250)", "");
        Actions actions = new Actions(getWebDriver());
        actions.sendKeys(Keys.PAGE_DOWN).perform();
    }

    //    static LoggingUtils logger = new LoggingUtils();
    public static void isDisable(By locator, String elementName) {
        try {
            WebDriver driver = getDriver();
            WebElement element = driver.findElement(locator);
            if (isVisible(locator, elementName) && element.isEnabled()) {
                element.click();
                LoggingUtils.info("Clicked on element: " + elementName);
            } else {
                LoggingUtils.info("The element is disabled: " + elementName);
            }
        } catch (Exception e) {
            LoggingUtils.error("Failed to validate : " + elementName + " if disable " + e.getMessage());
            throw e;
        }
    }

    public static boolean verifyElementIsDisabled(By byLocator, String str) throws Exception {

        try {
            WebElement element = getDriver().findElement(byLocator);
            boolean status = element.isEnabled();
            if (!status) {
                ExtentReporter.logInfo("checkElementPresent", " " + str + " is Disabled");
                LoggingUtils.info(" " + str + " is Disabled");
                return true;
            }
        } catch (Exception e) {
            ExtentReporter.logInfo("checkElementPresent", " " + str + " is Enabled");
            LoggingUtils.info(" " + str + " is Enabled");
            return false;
        }
        return false;

    }
}