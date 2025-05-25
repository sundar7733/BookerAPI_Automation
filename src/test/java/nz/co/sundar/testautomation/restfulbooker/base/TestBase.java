package nz.co.sundar.testautomation.restfulbooker.base;

import io.restassured.RestAssured;
import nz.co.sundar.testautomation.restfulbooker.utils.ConfigReader;
import nz.co.sundar.testautomation.restfulbooker.utils.ReportManager;
import org.junit.jupiter.api.*;

/**
 * Base class for REST API nz.co.sundar.testautomation.restfulbooker.tests against the Restful Booker API.
 * Sets up nz.co.sundar.testautomation.restfulbooker.base.base URI, generates reports using ExtentReports, and creates
 * a test booking before running test classes.
 * <p>
 * This class uses JUnit 5's {@code @TestInstance(PER_CLASS)} lifecycle to
 * ensure setup and teardown occurs only once per test class.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {
    /**
     * ExtentReports object used to configure and manage the reporting system.
     */
    protected final ReportManager reportManager = ReportManager.getInstance();

    @BeforeAll
    public void setup() {

        RestAssured.baseURI = ConfigReader.getProperty("baseURI");

    }

    @BeforeEach
    public void startTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName().replace("()", "");
        ReportManager.getInstance().createTest(testName);
    }
    /**
     * Logs the request details including the method and resource path.
     * This is used to log the request URL and method for better traceability in reports.
     *
     * @param method        The HTTP method (e.g., GET, POST, DELETE).
     * @param resourcePath  The resource path for the API endpoint.
     */
    public void logRequestDetails(String method, String resourcePath) {
        String fullRequestUrl = RestAssured.baseURI + resourcePath;
        ReportManager.getInstance().logInfo("Request URL: " + fullRequestUrl);
        ReportManager.getInstance().logInfo("Request Method: " + method);
    }

    // For /booking only
    public void logRequestDetails(String method) {
        logRequestDetails(method, "/booking");
    }

    // For /booking/{id} with int
    public void logRequestForBookingId(String method, int bookingId) {
        logRequestDetails(method, "/booking/" + bookingId);
    }

    // For /booking/{id} with String (e.g., invalid id)
    public void logRequestForBookingId(String method, String bookingId) {
        logRequestDetails(method, "/booking/" + bookingId);
    }

    /**
     * Flushes the ExtentReports object after all nz.co.sundar.testautomation.restfulbooker.tests in the class have completed.
     * This generates the final report file.
     */

    @AfterAll
    public void tearDown() {
        ReportManager.getInstance().flushReport();
    }
}