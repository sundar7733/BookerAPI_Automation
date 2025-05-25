package nz.co.sundar.testautomation.restfulbooker.utils;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.pojo.Booking;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingId;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for performing assertions on API responses
 * and logging results to the ExtentTest report.
 */

public class AssertionsUtils {
    private static final ReportManager reportManager = ReportManager.getInstance();

    /**
     * Performs assertions for equality and logs the result.
     */

    private static <T> void assertEquals(T expected, T actual, String message, List<String> errors) {
        try {
            Assertions.assertEquals(expected, actual, message);
            String passMessage = "PASS: " + message + " | Expected: " + expected + ", Actual: " + actual;
            reportManager.logPass(passMessage);
        } catch (AssertionError e) {
            String errorMessage = "FAIL: " + message + " | expected: " + expected + " but was: " + actual;
            reportManager.logFail(errorMessage);
            errors.add(errorMessage);
        }
    }

    /**
     * Performs assertions that condition is true and logs the result.
     */
    private static void assertTrue(boolean condition, String message, List<String> errors) {
        try {
            Assertions.assertTrue(condition, message);
            String passMessage = "PASS: " + message + " | Expected: true, Actual: " + condition;
            reportManager.logPass(passMessage);
        } catch (AssertionError e) {
            String errorMessage = message + " ==> expected: true but was: " + condition;
            reportManager.logFail(errorMessage);
            errors.add(errorMessage);
        }
    }

    /**
     * Asserts the details of a booking object and logs each results.
     *
     * @param booking           The booking object to validate
     * @param httpStatusCode    Expected HTTP status code
     * @param expectedFirstName Expected first name
     * @param expectedLastName  Expected last name
     * @param expectedTotalPrice Expected total price
     * @param expectedDepositPaid Expected deposit paid status
     * @param expectedCheckin    Expected check-in date
     * @param expectedCheckout   Expected check-out date
     * @param expectedAdditionalNeeds Expected additional needs
     *
     **/
    public static void assertBookingResponse(Booking booking, int httpStatusCode, String expectedFirstName, String expectedLastName, int expectedTotalPrice, boolean expectedDepositPaid,
                                             String expectedCheckin, String expectedCheckout,
                                             String expectedAdditionalNeeds) {


        List<String> errors = new ArrayList<>();

        try {
            assertEquals(200, httpStatusCode, "Validating http status code", errors);
            assertEquals(expectedFirstName, booking.getFirstname(), "Validating First Name", errors);
            assertEquals(expectedLastName, booking.getLastname(), "Validating Last Name", errors);
            assertEquals(expectedTotalPrice, booking.getTotalprice(), "Validating Total Price", errors);
            assertEquals(expectedDepositPaid, booking.isDepositpaid(), "Validating Deposit Paid", errors);
            assertEquals(expectedCheckin, booking.getBookingdates().getCheckin(), "Validating Check-in date", errors);
            assertEquals(expectedCheckout, booking.getBookingdates().getCheckout(), "Validating Check-out date", errors);
            assertEquals(expectedAdditionalNeeds, booking.getAdditionalneeds(), "Validating Additionalneeds", errors);
        } catch (Exception e) {
            reportManager.logFail("EXCEPTION: Failed to assert booking response. Error: " + e.getMessage());
            errors.add("Exception occurred: " + e.getMessage());
        }
        assertAllErrors(errors, reportManager);

    }

    /**
     * Asserts a list of booking IDs and validates expected HTTP status and list content.
     *
     * @param bookingIdList The list of booking IDs returned from the API
     * @param statusCode    HTTP status code to validate
     * @param reportManager The report manager to log test results
     */
    public static void assertBookingIdListResponse(List<BookingId> bookingIdList, int statusCode, ReportManager reportManager) {
        List<String> errors = new ArrayList<>();

        try {
            // Validate HTTP status code
            assertEquals(200, statusCode, "Validating http status code", errors);

            // Validate bookingIdList is not null
            boolean isNotNull = bookingIdList != null;
            assertTrue(isNotNull, "Validating bookingId list not null", errors);

            // Validate bookingIdList is not empty
            boolean isNotEmpty = bookingIdList != null && !bookingIdList.isEmpty();
            assertTrue(isNotEmpty, "Validating bookingId list not empty", errors);

            if (isNotEmpty) {
                reportManager.logPass("PASS: Booking ID list is not empty. Found " + bookingIdList.size() + " booking IDs.");
            }

        } catch (Exception e) {
            reportManager.logFail("EXCEPTION: Failed to assert booking ID list response. Error: " + e.getMessage());
            errors.add("Exception occurred: " + e.getMessage());
        }

        assertAllErrors(errors, reportManager);
    }

    /**
     * Asserts the status code of a booking delete response.
     *
     * @param response      The response from the delete operation
     * @param reportManager The report manager to log test results
     */
    public static void assertDeleteBookingResponse(Response response, ReportManager reportManager) {
        List<String> errors = null;
        try {
            errors = new ArrayList<>();
            assertEquals(201, response.getStatusCode(), "Validating http status code", errors);

        } catch (Exception e) {
            reportManager.logFail("EXCEPTION: Failed to assert delete booking response. Error: " + e.getMessage());
            assert errors != null;
            errors.add("Exception occurred: " + e.getMessage());
        }
        assertAllErrors(errors, reportManager);
    }

    /**
     * Aggregates and reports all soft assertion failures.
     *
     * @param errors        List of assertion error messages
     * @param reportManager The ExtentTest object used for logging
     */
    public static void assertAllErrors(List<String> errors, ReportManager reportManager) {
        if (errors != null && !errors.isEmpty()) {
            for (String error : errors) {
                reportManager.logInfo("ASSERTION FAILURE: " + error);
            }
            throw new AssertionError("Test failed due to assertion errors: " + errors);
        }
    }
    /**
     * Asserts the error response from an API call.
     *
     * @param response            The API response to validate
     * @param expectedStatusCode  The expected HTTP status code
     * @param expectedErrorMessage The expected error message in the response body
     */
    public static void assertErrorResponse(Response response, int expectedStatusCode, String expectedErrorMessage) {
        try {
            Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), "Validating http status code");
            Assertions.assertEquals(expectedErrorMessage, response.getBody().asString(), "Validating error message");

            reportManager.logPass("PASS: Error response validated successfully. Status Code: " + expectedStatusCode + ", Message: " + expectedErrorMessage);
        } catch (Exception e) {
            reportManager.logFail("FAIL: Error response validation failed. " + e.getMessage());
            throw new RuntimeException(e);

        }
    }
    /**
     * Asserts the authentication error response from an API call.
     *
     * @param response            The API response to validate
     * @param expectedStatusCode  The expected HTTP status code
     * @param expectedErrorMessage The expected error message in the response body
     */
    public static void assertAuthErrorResponse(Response response, int expectedStatusCode, String expectedErrorMessage) {
        try {
            Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), "Validating http status code");
            Assertions.assertEquals(expectedErrorMessage, response.jsonPath().getString("reason"), "Validating error message");

            reportManager.logPass("PASS: Error response validated successfully. Status Code: " + expectedStatusCode + ", Message: " + expectedErrorMessage);
        } catch (Exception e) {
            reportManager.logFail("FAIL: Error response validation failed. " + e.getMessage());
            throw new RuntimeException(e);

        }
    }
    /**
     * Asserts the authentication token response from an API call.
     *
     * @param response            The API response to validate
     * @param expectedStatusCode  The expected HTTP status code
     */
    public static void assertAuthValidResponse(Response response, int expectedStatusCode) {
        try {
            Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), "Validating http status code");

            String actualtoken = response.jsonPath().getString("token");
            //For security reasons, we will not log the full token for reporting
            String maskedToken = actualtoken.substring(0, 4) + "****" + actualtoken.substring(actualtoken.length() - 4);

            Assertions.assertEquals(actualtoken, response.jsonPath().getString("token"), "Validating token message");
            Assertions.assertNotNull(actualtoken, "Token should not be null");
            Assertions.assertFalse(actualtoken.trim().isEmpty(), "Token should not be empty");

            reportManager.logPass("PASS: Token response validated successfully. Status Code: " + expectedStatusCode + ", Actual Token : " + maskedToken);
        } catch (Exception e) {
            reportManager.logFail("FAIL: Token response validation failed. " + e.getMessage());
            throw new RuntimeException(e);

        }
    }
}
