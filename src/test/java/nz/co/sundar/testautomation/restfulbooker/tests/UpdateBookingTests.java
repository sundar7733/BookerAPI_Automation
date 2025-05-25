package nz.co.sundar.testautomation.restfulbooker.tests;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.base.TestBase;
import nz.co.sundar.testautomation.restfulbooker.pojo.Booking;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingData;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingResponse;
import nz.co.sundar.testautomation.restfulbooker.utils.AssertionsUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.BookingUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.PojoUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
/**
 * Test class to verify the update functionality of bookings in the Restful Booker API.
 * This class extends {@link TestBase} to inherit shared setup and reporting utilities.
 *
 * <p>The test case uses parameterized tests to read booking data from CSV files.
 * The test case logs the request details, creates a booking, updates it with new data,
 * and validates the response using assertions.
 * </p>
 *
 * @author Sundarram Krishnakumar
 */
public class UpdateBookingTests extends TestBase {
    String method = "PUT";
    // Load and pair rows from both CSVs
    static Stream<Arguments> bookingDataProvider() throws IOException {
        Reader createReader = Files.newBufferedReader(Paths.get("src/test/resources/csv/create_booking.csv"));
        Reader updateReader = Files.newBufferedReader(Paths.get("src/test/resources/csv/update_booking.csv"));

        List<CSVRecord> createRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(createReader).getRecords();
        List<CSVRecord> updateRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(updateReader).getRecords();

        List<Arguments> arguments = new ArrayList<>();
        for (int i = 0; i < createRecords.size(); i++) {
            BookingData createData = parseCSVRecord(createRecords.get(i));
            BookingData updateData = parseCSVRecord(updateRecords.get(i));
            arguments.add(Arguments.of(createData, updateData));

        }

        return arguments.stream();
    }
/**
     * Parses a CSV record into a BookingData object.
     *
     * @param record The CSVRecord to parse
     * @return A BookingData object containing the parsed values
 **/
    public static BookingData parseCSVRecord(CSVRecord record) {
        return new BookingData(
                record.get("testcase"),
                record.get("firstname"),
                record.get("lastname"),
                Integer.parseInt(record.get("totalprice")),
                Boolean.parseBoolean(record.get("depositpaid")),
                record.get("checkin"),
                record.get("checkout"),
                record.get("additionalneeds")
        );
    }
/**
     * Test case to update an existing booking with new details and validate the response.
     * <p>
     * Steps performed:
     * <ul>
     *     <li>Logs the PUT request operation</li>
     *     <li>Creates a booking first using data from CSV</li>
     *     <li>Updates the booking with new data from another CSV</li>
     *     <li>Validates the updated response content</li>
     *     <li>Logs success messages for reporting and debugging</li>
     * </ul>
     * </p>
     */
    @ParameterizedTest(name = "UpdateBooking Test #{index} - {1}")
    @MethodSource("bookingDataProvider")
    public void updateBookingTest(BookingData createData, BookingData updateData) {
        // Step 1: Create a booking first
        Response response = BookingUtils.createTestBooking(createData.firstname, createData.lastname, createData.totalprice,
                createData.depositpaid, createData.checkin, createData.checkout, createData.additionalneeds);

        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());
        int bookingId = bookingResponse.getBookingid();

        // Step 2: Update the booking using the ID

        response = BookingUtils.updateSpecificBooking(bookingId, updateData.firstname, updateData.lastname, updateData.totalprice,
                updateData.depositpaid, updateData.checkin, updateData.checkout, updateData.additionalneeds);
        int httpStatusCode = response.getStatusCode();

        logRequestForBookingId(method, bookingId);

        // Step 3: Convert updated response directly to Booking POJO
        Booking updatedBooking = PojoUtils.convertJsonToBooking(response.asString());
        reportManager.logInfo("Updated BookingId: " + bookingId);

        // Step 4: Log update ID and assert updated values
        AssertionsUtils.assertBookingResponse(updatedBooking, httpStatusCode,
                updateData.firstname, updateData.lastname, updateData.totalprice,
                updateData.depositpaid, updateData.checkin, updateData.checkout, updateData.additionalneeds);

        reportManager.getTest().info("Booking updated successfully for ID: " + bookingId);
        reportManager.logInfo("Response field validation for Booking ID " + bookingId + " passed successfully.");
    }
/**
     * Test case to update a booking with an empty booking ID.
     * <p>
     * This test verifies that the API correctly handles an attempt to update a booking with an empty ID,
     * expecting a 404 Not Found response.
     * </p>
     */
    @Test
    public void updateEmptyBookingIdTest() {
        String bookingId = ""; // Empty booking ID
        Response response = BookingUtils.updateInvalidBookingId(bookingId);

        logRequestForBookingId(method, bookingId);

        try {
            AssertionsUtils.assertErrorResponse(response, 404, "Not Found");
        } catch (AssertionError e) {
            reportManager.logFail("Expected 404 Not Found but got: " + response.getStatusCode() + " - " + response.asString());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Booking not Updated with expected response: " + response.asString());
    }
/**
     * Test case to update a booking with an invalid booking ID.
     * <p>
     * This test verifies that the API correctly handles an attempt to update a booking with an invalid ID,
     * expecting a 405 Method Not Allowed response.
     * </p>
     */
    @Test
    public void updateInvalidBookingIdTest() {
        String bookingId = "abc"; // Invalid booking ID
        Response response = BookingUtils.updateInvalidBookingId(bookingId);

        logRequestForBookingId(method, bookingId);

        try {
            AssertionsUtils.assertErrorResponse(response, 405, "Method Not Allowed");
        } catch (AssertionError e) {
            reportManager.logFail("Expected error response not received for invalid booking ID update: " + e.getMessage());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Invalid Booking not updated with expected response: " + response.asString());

    }
/**
     * Test case to update a booking without providing an authentication token.
     * <p>
     * This test verifies that the API correctly handles an attempt to update a booking without authentication,
     * expecting a 403 Forbidden response.
     * </p>
    */
    @Test
    public void UpdateBookingWithNoAuthTest() {
              // Step 1: Create a valid booking first
        Response response = BookingUtils.createTestBooking();
        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());

        int bookingId = bookingResponse.getBookingid();

        // Step 2: Update the booking using the ID
        response = BookingUtils.UpdateBookingWithNoAuthTest(bookingId, " ");
        logRequestForBookingId(method, bookingId);

        try {
            AssertionsUtils.assertErrorResponse(response, 403, "Forbidden");
        } catch (AssertionError e) {
            reportManager.logFail("Expected error response not received for update without auth token: " + e.getMessage());
            throw e; // Re-throw to fail the test
        }
        reportManager.logInfo("Booking not updated with expected response: " + response.asString());
    }
}

