package nz.co.sundar.testautomation.restfulbooker.tests;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.base.TestBase;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingId;
import nz.co.sundar.testautomation.restfulbooker.utils.AssertionsUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.BookingUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.PojoUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.ReportManager;
import org.junit.jupiter.api.Test;

import java.util.List;
/**
 * Test class to verify retrieval of all booking IDs from the Restful Booker API.
 *
 * <p>This test ensures the GET endpoint that returns all booking IDs is functioning correctly
 * by asserting both the response status and content.</p>
 *
 * <p>It extends {@link TestBase}, which provides shared setup, teardown, and ExtentReports integration.</p>
 *
 * <p><strong>Purpose:</strong> Validate the system can list all existing bookings.</p>
 *
 * <p><strong>Dependencies:</strong> Assumes the API contains one or more existing bookings.</p>
 *
 * <p><strong>Output:</strong> Response is parsed and validated, and results are logged to ExtentReports.</p>
 *
 * @see BookingUtils#getAllBookingIds(boolean)
 * @see PojoUtils#convertJsonToBookingIdList(String)
 * @see ReportManager
 * @see TestBase
 * @see BookingId
 *
 * @author Sundarram Krishnakumar
 */
public class GetAllBookingIdsTests extends TestBase {
 String method = "GET";
    /**
     * Test case to validate the retrieval of all booking IDs from the API.
     *
     * <p><strong>Test Workflow:</strong></p>
     * <ol>
     *   <li>Logs the GET request operation using {@code logRequestDetails()}</li>
     *   <li>Calls the API endpoint to fetch all booking IDs</li>
     *   <li>Deserializes the JSON response to a {@code List<BookingId>}</li>
     *   <li>Asserts the response status code and non-empty list of IDs</li>
     *   <li>Logs the total number of IDs fetched using {@link ReportManager}</li>
     * </ol>
     *
     * <p>This test ensures the integrity and availability of the booking ID listing feature.</p>
     */
    @Test
    public void getAllBookingIdsTest() {

        Response response = BookingUtils.getAllBookingIds(true);
        logRequestDetails(method);

        // Deserialize JSON response into List of BookingId POJOs
        List<BookingId> bookingIdList = PojoUtils.convertJsonToBookingIdList(response.asString());
        int httpStatusCode = response.getStatusCode();

        AssertionsUtils.assertBookingIdListResponse(bookingIdList, httpStatusCode, ReportManager.getInstance());

        reportManager.getTest().info("Fetched All " + bookingIdList.size() + " booking IDs");

    }
    /**
     * Test case to validate the retrieval of all booking IDs from the API without any headers.
     *
     * <p>This test is similar to {@link #getAllBookingIdsTest()} but does not include any custom headers.</p>
     *
     * <p><strong>Purpose:</strong> Ensure that the API can still return booking IDs without additional headers.</p>
     */
    @Test
    public void getAllBookingIdsTestWithNoHeaders() {

        Response response = BookingUtils.getAllBookingIds(false);
        logRequestDetails(method);

        // Deserialize JSON response into List of BookingId POJOs
        List<BookingId> bookingIdList = PojoUtils.convertJsonToBookingIdList(response.asString());
        int httpStatusCode = response.getStatusCode();

        AssertionsUtils.assertBookingIdListResponse(bookingIdList, httpStatusCode, ReportManager.getInstance());

        reportManager.getTest().info("Fetched All " + bookingIdList.size() + " booking IDs");

    }

}

