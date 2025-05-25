package nz.co.sundar.testautomation.restfulbooker.tests;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.base.TestBase;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingResponse;
import nz.co.sundar.testautomation.restfulbooker.utils.AssertionsUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.BookingUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.PojoUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.ReportManager;
import org.junit.jupiter.api.Test;

/**
 * Test class responsible for verifying the deletion of a booking using the Restful Booker API.
 *
 * <p>This class extends {@link TestBase}, allowing reuse of request configuration, test reporting, and shared utilities.</p>
 *
 * <p><strong>Purpose:</strong> Ensure that a previously created booking can be deleted using a DELETE request,
 * and that the API returns the correct status and response for the operation.</p>
 *
 * <p><strong>Assumption:</strong> A booking is created dynamically within the same test before attempting deletion,
 * ensuring test independence and repeatability.</p>
 *
 * <p><strong>Highlights:</strong></p>
 * <ul>
 *   <li>Creates a test booking to get a valid booking ID</li>
 *   <li>Sends a DELETE request to the API with that ID</li>
 *   <li>Validates response code and logs result using ExtentReports</li>
 * </ul>
 *
 * @author Sundarram Krishnakumar
 */
public class DeleteBookingTests extends TestBase {
    String method = "DELETE";
    /**
     * Test case to verify successful deletion of a booking from the API.
     *
     * <p><strong>Test Workflow:</strong></p>
     * <ol>
     *   <li>Logs the HTTP DELETE operation</li>
     *   <li>Creates a new booking and extracts the booking ID</li>
     *   <li>Sends a DELETE request for the booking ID</li>
     *   <li>Validates the response using a custom soft assertion utility</li>
     *   <li>Logs the outcome in both the console and test report</li>
     * </ol>
     *
     * <p>This test validates both the API's DELETE functionality and the integration with the reporting and assertion framework.</p>
     */
    @Test
    public void deleteBookingTest() {

        Response response = BookingUtils.createTestBooking();

        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());
        int bookingId = bookingResponse.getBookingid();

        logRequestForBookingId(method, bookingId);

        response = BookingUtils.deleteTestBookingId(bookingId);

        AssertionsUtils.assertDeleteBookingResponse(response, ReportManager.getInstance());

        reportManager.getTest().info("Booking deleted successfully for ID: " + bookingId);
        reportManager.logInfo("Booking deleted successfully for ID: " + bookingId);
    }

    /**
     * Test case attempting to delete a booking id that does not exist.
     * Since this is a Public API, we cannot guarantee that an arbitrary id does not exist.
     * To guarantee that the test is repeatable, we create a booking and delete it.
     * Then we attempt to delete the same booking id again, which is our test case.
     */
    @Test
    public void deleteBookingTwiceTest() {

        Response response = BookingUtils.createTestBooking();

        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());
        int bookingId = bookingResponse.getBookingid();

        response = BookingUtils.deleteTestBookingId(bookingId);
        logRequestForBookingId(method, bookingId);
        AssertionsUtils.assertDeleteBookingResponse(response, ReportManager.getInstance());

        reportManager.getTest().info("Booking deleted successfully for ID: " + bookingId);
        reportManager.logInfo("Booking deleted successfully for ID: " + bookingId);

        response = BookingUtils.deleteTestBookingId(bookingId);
        logRequestForBookingId(method, bookingId);
       try {
            AssertionsUtils.assertErrorResponse(response, 405, "Method Not Allowed");
        } catch (AssertionError e) {
            reportManager.logFail("Expected 405 Method Not Allowed but got: " + response.getStatusCode() + " - " + response.asString());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Booking not deleted with expected response: " + response.asString());

    }
/**
     * Test case to verify that deleting a booking with an empty booking ID returns a 404 Not Found error.
     *
     * <p>This test ensures that the API correctly handles requests with invalid or missing booking IDs.</p>
     */
    @Test
    public void deleteEmptyBookingIdTest() {
        String bookingId = ""; // Empty booking ID
        Response response = BookingUtils.deleteTestBookingId(bookingId);

        logRequestForBookingId(method, bookingId);

        try {
            AssertionsUtils.assertErrorResponse(response, 404, "Not Found");
        } catch (AssertionError e) {
            reportManager.logFail("Expected 404 Not Found but got: " + response.getStatusCode() + " - " + response.asString());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Booking not deleted with expected response: " + response.asString());
    }
/**
     * Test case to verify that attempting to delete a booking with an invalid booking ID returns a 405 Method Not Allowed error.
     *
     * <p>This test ensures that the API correctly handles requests with invalid booking IDs.</p>
     */
    @Test
    public void deleteInvalidBookingIdTest() {
        String bookingId = "abc"; // Invalid booking ID
        Response response = BookingUtils.deleteTestBookingId(bookingId);
        logRequestForBookingId(method, bookingId);

        try {
            AssertionsUtils.assertErrorResponse(response, 405, "Method Not Allowed");
        } catch (AssertionError e) {
            reportManager.logFail("Expected 405 Method Not Allowed but got: " + response.getStatusCode() + " - " + response.asString());

            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Invalid Booking not deleted with expected response: " + response.asString());
    }
/**
     * Test case to verify that deleting a booking without authentication returns a 403 Forbidden error.
     *
     * <p>This test ensures that the API correctly restricts access to delete operations without proper authentication.</p>
     */
    @Test
    public void deleteWithNoAuthTest() {
        String bookingId = "1"; // Assuming booking ID 1 exists for testing purposes
        Response response = BookingUtils.deleteTestBookingId(bookingId, "");
        logRequestForBookingId(method, bookingId);
        try {
            AssertionsUtils.assertErrorResponse(response, 403, "Forbidden");
        } catch (AssertionError e) {
            reportManager.logFail("Expected 403 Forbidden but got: " + response.getStatusCode() + " - " + response.asString());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Booking not deleted with expected response: " + response.asString());
    }
}

