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
import org.junit.jupiter.api.Assertions;
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
 * Test class responsible for verifying the creation of a new booking using the Restful Booker API.
 *
 * <p>This test class extends {@link TestBase}, inheriting setup, teardown, and reporting capabilities.
 * It focuses on validating successful booking creation by sending a POST request and verifying the
 * response using soft assertions and ExtentReports for detailed logging.</p>
 *
 * <p><strong>Features covered:</strong></p>
 * <ul>
 *   <li>Dynamic payload generation for booking creation</li>
 *   <li>POJO-based deserialization of API responses</li>
 *   <li>Soft assertions with custom error handling and structured logging</li>
 * </ul>
 *
 * <p><strong>Note:</strong> The booking ID generated during the test execution is extracted and logged
 * for potential reuse or reference in further test cases.</p>
 *
 * @author Sundarram Krishnakumar
 */
public class CreateBookingTests extends TestBase {
    String method = "POST";
    // Load and pair rows from both CSVs
    static Stream<Arguments> bookingDataProvider() throws IOException {
        Reader createReader = Files.newBufferedReader(Paths.get("src/test/resources/csv/create_booking.csv"));

        List<CSVRecord> createRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(createReader).getRecords();

        List<Arguments> arguments = new ArrayList<>();
        for (CSVRecord createRecord : createRecords) {
            BookingData createData = parseCSVRecord(createRecord);
            arguments.add(Arguments.of(createData));
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
     * Test case to create a new booking with provided data and validate the response.
     *
     * <p>This test uses parameterized inputs from a CSV file to create bookings with various data sets.</p>
     *
     * <p><strong>Steps performed:</strong></p>
     * <ol>
     *   <li>Logs the HTTP method and endpoint being tested</li>
     *   <li>Creates a booking using the provided data</li>
     *   <li>Deserializes the response into POJOs</li>
     *   <li>Validates the booking ID and response contents using assertions</li>
     *   <li>Logs test outcome to the Extent report</li>
     * </ol>
     *
     * <p>This test ensures that the booking API correctly returns valid response data and status
     * upon successful creation of a new booking record.</p>
     */
    @ParameterizedTest(name = "CreateBooking Test #{index} - {0}")
    @MethodSource("bookingDataProvider")
    public void createBookingTest(BookingData createData) {

        Response response = BookingUtils.createTestBooking(createData.firstname, createData.lastname, createData.totalprice,
                createData.depositpaid, createData.checkin, createData.checkout, createData.additionalneeds);

        BookingResponse bookingResponse = PojoUtils.convertJsonToBookingResponse(response.asString());

        Booking booking = bookingResponse.getBooking();

        int bookingId = bookingResponse.getBookingid();
        int httpStatusCode = response.getStatusCode();

        logRequestDetails(method);

        try{
        Assertions.assertTrue(bookingId > 0, "Booking ID should be a positive integer greater than 0. Actual: " + bookingId);
        reportManager.logPass("Booking ID is valid: " + bookingId);
        } catch (NullPointerException e) {
            reportManager.logFail("Booking ID is null: " + e.getMessage());
            Assertions.fail("Booking ID is null: " + e.getMessage());
        } catch (AssertionError e) {
            reportManager.logFail("Booking ID is not valid: " + e.getMessage());
            Assertions.fail("Booking ID is not valid: " + e.getMessage());
        }

        AssertionsUtils.assertBookingResponse(booking, httpStatusCode,
                createData.firstname, createData.lastname, createData.totalprice,
                createData.depositpaid, createData.checkin, createData.checkout, createData.additionalneeds);

        reportManager.logInfo("Create booking response: " + response.asString());
        reportManager.getTest().info("Booking created with ID: " + bookingId);
        reportManager.logInfo("Response field validation for Booking ID " + bookingId + " completed successfully.");



    }
    /**
     * Test case to create a booking with null name fields.
     * <p>
     * This test verifies that the API correctly handles a request with null values for the first and last name,
     * expecting an internal server error response.
     * </p>
     */
    @Test
    public void createNullNameBookingTest() {

        Response response = BookingUtils.createTestBooking(null, null);

        logRequestDetails(method);

        AssertionsUtils.assertErrorResponse(response, 500, "Internal Server Error");

        reportManager.logInfo("Booking not created with expected response: " + response.asString());
    }

    /**
     * Test case to create a booking with invalid deposit value.
     */
    @Test
    public void createInvalidDepositTest() {

        Response response = BookingUtils.createInvalidBooking();

        logRequestDetails(method);

        boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");

        try {
        Assertions.assertTrue(depositPaid, "Expected depositpaid as true. Actual: " + depositPaid);

        reportManager.logPass("Deposit paid is true as expected. Actual: " + depositPaid);
        } catch (AssertionError e) {
            reportManager.logFail("Deposit paid is not true: " + e.getMessage());
            Assertions.fail("Deposit paid is not true: " + e.getMessage());
        }

        reportManager.logInfo("Invalid Deposit Paid Booking Test completed");
    }

    /**
     * Test case to create a booking with invalid total price.
     * <p>
     * This test verifies that the API correctly handles a request with an invalid total price,
     * expecting the total price to be null in the response.
     * </p>
     */
    @Test
    public void createInvalidPriceTest() {

        Response response = BookingUtils.createInvalidBooking();

        logRequestDetails(method);

        Object totalPrice = response.jsonPath().get("booking.totalprice");

        try {
            Assertions.assertNull(totalPrice, "Expected totalprice as null. Actual: " + totalPrice);

            reportManager.logPass("Total price is null as expected: Actual " + totalPrice);
        } catch (AssertionError e) {
            reportManager.logFail("Total price is not null: " + e.getMessage());
            Assertions.fail("Total price is not null: " + e.getMessage());
        }

        reportManager.logInfo("Invalid Total price Test completed");
    }
/**
     * Test case to create a booking with invalid check-in and check-out dates.
     * <p>
     * This test verifies that the API correctly handles a request with invalid date formats,
     * expecting the check-in and check-out dates to be in an invalid format.
     * </p>
     */
    @Test
    public void createInvalidCheckInCheckOutDateTest() {

        Response response = BookingUtils.createInvalidBooking();

        logRequestDetails(method);

        String invalidcheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        String invalidcheckout = response.jsonPath().getString("booking.bookingdates.checkout");

        try {
            Assertions.assertEquals("0NaN-aN-aN", invalidcheckin, "Expected invalid checkin value");
            Assertions.assertEquals("0NaN-aN-aN", invalidcheckout, "Expected invalid checkout value");

            reportManager.logPass("Invalid check-in response as  expected: Actual " + invalidcheckin);
            reportManager.logPass("Invalid check-out response as  expected: Actual " + invalidcheckout);
        } catch (AssertionError e) {
            reportManager.logFail("Invalid check-in/check-out response not as expected" + e.getMessage());
            Assertions.fail("Invalid check-in/check-out response not as expected :" + e.getMessage());
        }

        reportManager.logInfo("Invalid check-in/check-out Test completed");
    }
}
