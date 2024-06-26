package utilities.ReusableComponents;

import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class MonthPicker extends ExtentReporter {

    public static Month getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();

        String formattedMonth = formatMonth(currentMonth);
        LoggingUtils.info("Current Month: " + formattedMonth);
        ExtentReporter.logInfo("Get Current Month", "The current month is " + formattedMonth);
        return currentMonth;
    }

    public static Month getFirstMonthBackwards() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        Month firstMonthBackwards;
        int currentMonthValue = currentMonth.getValue();
        if (currentMonthValue > 1) {
            firstMonthBackwards = Month.of(currentMonthValue - 1);
        } else {
            int previousYear = currentYear - 1;
            firstMonthBackwards = Month.DECEMBER;
        }

        String formattedMonth = formatMonth(firstMonthBackwards);
        LoggingUtils.info("First Month Backwards: " + formattedMonth);
        ExtentReporter.logInfo("Get First Month Backwards", "The first month backwards is " + formattedMonth);

        return firstMonthBackwards;
    }
    public static Month getSecondMonthBackwards() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        Month secondMonthBackwards;
        int currentMonthValue = currentMonth.getValue();
        if (currentMonthValue >= 2) {
            secondMonthBackwards = Month.of(currentMonthValue - 1);
        } else {
            int previousYear = currentYear - 1;
            int previousMonthValue = 12 - (1 - currentMonthValue);
            secondMonthBackwards = Month.of(previousMonthValue);
        }

        String formattedMonth = formatMonth(secondMonthBackwards);
        LoggingUtils.info("Second Month Backwards: " + formattedMonth);
        ExtentReporter.logInfo("Get Second Month Backwards", "The second month backwards is " + formattedMonth);

        return secondMonthBackwards;
    }

    public static Month getThirdMonthBackwards() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        Month thirdMonthBackwards;
        int currentMonthValue = currentMonth.getValue();
        if (currentMonthValue >= 3) {
            thirdMonthBackwards = Month.of(currentMonthValue - 2);
        } else {
            int previousYear = currentYear - 1;
            int previousMonthValue = 12 - (2 - currentMonthValue);
            thirdMonthBackwards = Month.of(previousMonthValue);
        }

        String formattedMonth = formatMonth(thirdMonthBackwards);
        LoggingUtils.info("Third Month Backwards: " + formattedMonth);
        ExtentReporter.logInfo("Get Third Month Backwards", "The third month backwards is " + formattedMonth);

        return thirdMonthBackwards;
    }

    public static Month getFourthMonthBackwards() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        Month fourthMonthBackwards;
        int currentMonthValue = currentMonth.getValue();
        if (currentMonthValue >= 4) {
            fourthMonthBackwards = Month.of(currentMonthValue - 3);
        } else {
            int previousYear = currentYear - 1;
            int previousMonthValue = 12 - (3 - currentMonthValue);
            fourthMonthBackwards = Month.of(previousMonthValue);
        }

        String formattedMonth = formatMonth(fourthMonthBackwards);
        LoggingUtils.info("Fourth Month Backwards: " + formattedMonth);
        ExtentReporter.logInfo("Get Fourth Month Backwards", "The fourth month backwards is " + formattedMonth);

        return fourthMonthBackwards;
    }

    public static Month getFifthMonthBackwards() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        Month fifthMonthBackwards;
        int currentMonthValue = currentMonth.getValue();
        if (currentMonthValue >= 5) {
            fifthMonthBackwards = Month.of(currentMonthValue - 4);
        } else {
            int previousYear = currentYear - 1;
            int previousMonthValue = 12 - (4 - currentMonthValue);
            fifthMonthBackwards = Month.of(previousMonthValue);
        }

        String formattedMonth = formatMonth(fifthMonthBackwards);
        LoggingUtils.info("Fifth Month Backwards: " + formattedMonth);
        ExtentReporter.logInfo("Get Fifth Month Backwards", "The fifth month backwards is " + formattedMonth);

        return fifthMonthBackwards;
    }

    public static String getThe1Monthand15thDayOnwarfs() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusMonths(1).plusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String formattedDate = futureDate.format(formatter);

        return formattedDate;
    }


    public static String formatMonth(Month month) {
        String monthName = month.name();
        return monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
    }
}
