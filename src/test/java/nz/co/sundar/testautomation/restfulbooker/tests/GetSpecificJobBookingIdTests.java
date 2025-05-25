package nz.co.sundar.testautomation.restfulbooker.tests;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.base.TestBase;
import nz.co.sundar.testautomation.restfulbooker.pojo.Booking;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingResponse;
import nz.co.sundar.testautomation.restfulbooker.utils.AssertionsUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.BookingUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.PojoUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.ReportManager;
import org.junit.jupiter.api.Test;
/**
 * Test class to validate the retrieval of a specific booking from the Restful Booker API.
 *
 * <p>This test covers the flow of:</p>
 * <ul>
 *   <li>Creating a new booking via the API</li>
 *   <li>Fetching that booking by its ID</li>
 *   <li>Validating key fields in the response such as first name and last name</li>
 * </ul>
 *
 * <p>Extends {@link TestBase} to leverage shared setup and ExtentReport logging functionality.</p>
 *
 * <p><strong>Note:</strong> This test creates a fresh booking each time to ensure data isolation.</p>
 *
 * @see BookingUtils
 * @see BookingResponse
 * @see Booking
 * @see PojoUtils
 * @see ReportManager
 * @see TestBase
 *
 * @author  Sundarram Krishnakumar
 */
public class GetSpecificJobBookingIdTests extends TestBase {
    String method = "GET";
    /**
     * Test case to retrieve and validate a booking using its unique booking ID.
     *
     * <p><strong>Test Workflow:</strong></p>
     * <ol>
     *   <li>Logs the GET request intent</li>
     *   <li>Creates a test booking via the API and extracts the booking ID</li>
     *   <li>Performs a GET request using the booking ID to fetch details</li>
     *   <li>Parses the response into a {@code Booking} object</li>
     *   <li>Asserts the booking content matches expected values (first name, last name)</li>
     *   <li>Logs successful validation into the report</li>
     * </ol>
     */
    @Test
    public void getSpecificBookingIdTest() {

        // Step 1: Create a booking first
        Response response = BookingUtils.createTestBooking();
        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());

        int bookingId = bookingResponse.getBookingid();

        logRequestForBookingId(method, bookingId);
        // Step 2: Retrieve the booking by ID
        response = BookingUtils.getSpecificBookingId(bookingId);

        int httpStatusCode = response.getStatusCode();
        // Step 3: Convert updated response directly to Booking POJO
        Booking booking = PojoUtils.convertJsonToBooking(response.asString());
        // Step 4: Assert response content
        AssertionsUtils.assertBookingResponse(booking, httpStatusCode,"Jim", "Brown", 111, true,"2023-01-01", "2023-01-02", "Breakfast");

        reportManager.logInfo("Validating BookingId: " + bookingId);
        reportManager.getTest().info("Response field validation for Specific Booking ID " +bookingId+ " passed successfully.");
        reportManager.logInfo("Response field validation for Specific Booking ID " +bookingId+ " completed");
    }
    /**
     * Test case to validate the retrieval of a booking using an invalid booking ID.
     *
     * <p><strong>Test Workflow:</strong></p>
     * <ol>
     *   <li>Logs the GET request intent</li>
     *   <li>Attempts to retrieve a booking using an invalid ID</li>
     *   <li>Asserts that the response contains an error status code and message</li>
     *   <li>Logs the outcome in the report</li>
     * </ol>
     */
    @Test
    public void getInvalidBookingIdTest() {
        String bookingId = "abc"; // Invalid booking ID
        Response response = BookingUtils.getSpecificBookingId(bookingId);
        logRequestForBookingId(method, bookingId);
        try {
            AssertionsUtils.assertErrorResponse(response, 404, "Not Found");
        } catch (AssertionError e) {
            reportManager.logFail("Expected error response not received for invalid booking ID Retrieval: " + e.getMessage());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Invalid Booking not updated with expected response: " + response.asString());
    }
}
