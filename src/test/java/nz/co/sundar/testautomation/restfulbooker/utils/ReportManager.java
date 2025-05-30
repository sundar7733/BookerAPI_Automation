package nz.co.sundar.testautomation.restfulbooker.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReportManager is a utility class responsible for managing the ExtentReports lifecycle,
 * including initializing the report, logging information, and generating the final report output.
 * It follows a Singleton pattern to ensure only one instance is used throughout the test run.
 */

public class ReportManager {

    private static ReportManager instance;
    private final ExtentReports extentReports;
    private ExtentTest test;
    private final Instant startTime;
    private final ZoneId pacificAucklandZone = ZoneId.of("Pacific/Auckland");
    private final ZonedDateTime testStartTime;
    /**
     * Private constructor initializes ExtentReports and sets up the reporter configuration.
     */
    private ReportManager() {
        extentReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/test-results/BookerAPITestResults.html");
        sparkReporter.config().setDocumentTitle("BookerAPI Test Report");
        sparkReporter.config().setReportName("BookerAPI Test Automation Report");
        startTime = Instant.now();
        testStartTime = ZonedDateTime.now(pacificAucklandZone);
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Returns the singleton instance of ReportManager.
     *
     * @return the singleton instance
     */
    public static ReportManager getInstance() {
        if (instance == null) {
            instance = new ReportManager();
        }
        return instance;
    }

    /**
     * Creates a new test entry in the report.
     *
     * @param testName the name of the test to create
     */
    public void createTest(String testName) {
        test = extentReports.createTest(testName);
    }
    /**
     * Finalizes and flushes the report output.
     * Also records the total execution time and test duration.
     */
    public void flushReport() {
        Instant endTime = Instant.now();
        ZonedDateTime endTimeAuckland = ZonedDateTime.ofInstant(endTime, pacificAucklandZone);

        Duration totalTime = Duration.between(startTime, endTime);
        long hours = totalTime.toHours();
        long minutes = totalTime.toMinutes() % 60;
        long seconds = totalTime.getSeconds() % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");


        extentReports.setSystemInfo("Test Start Time", testStartTime.format(dateFormatter));
        extentReports.setSystemInfo("Test End Time", endTimeAuckland.format(dateFormatter));
        extentReports.setSystemInfo("Total Execution Time", formattedTime);

        extentReports.flush();
    }

    /**
     * Logs an informational message to the current test.
     *
     * @param toLog the message to log
     */
    public void logInfo(String toLog) {
        test.log(Status.INFO, toLog);
    }
    /**
     * Logs a PASS message with green color.
     *
     * @param message the message to log
     */
    public void logPass(String message) {
        /* Commented out the MarkupHelper line as it is not used in the current version of ExtentReports. Can be used if needed for
        color text to appear on the report.*/
       // test.pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
        test.pass(message);
    }

    /**
     * Logs a FAIL message with red color.
     *
     * @param message the message to log
     */
    public void logFail(String message) {
       // test.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
        test.fail(message);
    }
    /**
     * Returns the current test instance.
     *
     * @return the current ExtentTest instance
     */
    public ExtentTest getTest() {
        return test;
    }
}
