package nz.co.sundar.testautomation.restfulbooker.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.payloads.BookingPayloads;
import nz.co.sundar.testautomation.restfulbooker.pojo.Booking;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingDates;

import static io.restassured.RestAssured.given;

public class BookingUtils {
    public static String bookingResourcePath = "/booking";

    public static Response createTestBooking() {

        Response response = BookingUtils.createTestBooking("Jim", "Brown");
        return response;
    }

    public static Response createTestBooking(String firstName, String lastName) {

        String payload = BookingPayloads.createBookingPayload(
                firstName,
                lastName,
                111,  // totalPrice
                true,          // depositPaid
                "2023-01-01",  // checkin
                "2023-01-02",  // checkout
                "Breakfast"    // additionalNeeds
        );

        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(bookingResourcePath);
    }

    public static Response createTestBooking(String firstName, String lastName, int totalPrice,
                                             boolean depositPaid, String checkin, String checkout,
                                             String additionalNeeds) {

        BookingDates dates = new BookingDates(checkin, checkout);
        Booking booking = new Booking(firstName, lastName, totalPrice, depositPaid, dates, additionalNeeds);

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().body()
                .body(booking)
                .when()
                .post(bookingResourcePath);
    }
    public static Response createInvalidBooking() {

        String payload = BookingPayloads.createInvalidBookingPayload(
                "John",
                "Doe",
                "totalPrice",   // invalid totalPrice
                "depositPaid",          // invalid depositPaid
                "invalid check-in",    // invalid checkin
                "invalid check-out",  // invalid checkout
                "Breakfast"    // additionalNeeds
        );

        return given()
                .contentType(ContentType.JSON)
                .log().body()
                .body(payload)
                .post(bookingResourcePath);
    }

    public static Response updateSpecificBooking(int bookingId,
                                                 String firstName,
                                                 String lastName,
                                                 int totalPrice,
                                                 boolean depositPaid,
                                                 String checkin,
                                                 String checkout,
                                                 String additionalNeeds) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;

        String payload = BookingPayloads.createBookingPayload(
                firstName,
                lastName,
                totalPrice,
                depositPaid,
                checkin,
                checkout,
                additionalNeeds
        );

        String token = TokenManager.getToken();

        Response response = TokenManager.sendUpdateRequest(token, bookingIdResourcePath, payload);
        // Retry logic for 403 Forbidden
        if (response.statusCode() == 403) {
            // Token expired or invalid. Refreshing token and retrying...
            TokenManager.invalidateToken(); // Reset token
            token = TokenManager.getToken(); // Get a new one
            response = TokenManager.sendUpdateRequest(token, bookingIdResourcePath, payload);
        }

        return response;
    }

    public static Response getSpecificBookingId(int bookingId) {
        return getSpecificBookingId(String.valueOf(bookingId));
    }
    public static Response getSpecificBookingId(String bookingId) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;

        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(bookingIdResourcePath)
                .then()
                .extract()
                .response();
    }
    public static Response getAllBookingIds(boolean includeHeaders) {

        return (includeHeaders ? given().contentType(ContentType.JSON) : given())
                .when()
                .get(bookingResourcePath)
                .then()
                .extract()
                .response();
    }
    public static Response deleteTestBookingId(int bookingId) {
        return deleteTestBookingId(String.valueOf(bookingId));
    }

    public static Response deleteTestBookingId(String bookingId) {
        return deleteTestBookingId(String.valueOf(bookingId), TokenManager.getToken());
    }

    public static Response deleteTestBookingId(String bookingId, String token) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;

        Response response = TokenManager.sendDeleteRequest(token, bookingIdResourcePath);
        // Retry logic for 403 Forbidden
        if (response.statusCode() == 403) {
            // Token expired or invalid. Refreshing token and retrying...
            TokenManager.invalidateToken(); // Reset token
            token = TokenManager.getToken(); // Get a new one
            response = TokenManager.sendDeleteRequest(token, bookingIdResourcePath);
        }

        return response;
    }

    /** As token is being refreshed in the middle of tests, this method is used to test the No Auth scenario when calling
     * UpdateTestBookingId with NoAuth 403 validation specifically.
     *
     */
    public static Response UpdateBookingWithNoAuthTest(int bookingId, String token) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;

        return given()
                .header("Cookie", "token=" + token)
                .contentType(ContentType.JSON)
                .when()
                .put(bookingIdResourcePath)
                .then()
                .extract()
                .response();
    }
    /** As token is being refreshed in the middle of tests, this method is used to test the No Auth scenario when calling
     * DeleteTestBookingId with NoAuth.
     *
     */
    public static Response deleteBookingWithNoAuthTest(int bookingId, String token) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;

        return given()
                .header("Cookie", "token=" + token)
                .contentType(ContentType.JSON)
                .when()
                .delete(bookingIdResourcePath)
                .then()
                .extract()
                .response();
    }

    public static Response updateInvalidBookingId(String bookingId) {
        return updateEmptyBookingId(String.valueOf(bookingId), TokenManager.getToken());
    }
    public static Response updateEmptyBookingId(String bookingId) {
        return updateEmptyBookingId(String.valueOf(bookingId), TokenManager.getToken());
    }
    public static Response updateEmptyBookingId(String bookingId, String token) {
        String bookingIdResourcePath = bookingResourcePath + "/" + bookingId;
        String payload = BookingPayloads.createInvalidBookingPayload(
                "John",
                "Doe",
                "totalPrice",   // invalid totalPrice
                "depositPaid",          // invalid depositPaid
                "invalid check-in",    // invalid checkin
                "invalid check-out",  // invalid checkout
                "Breakfast"    // additionalNeeds
        );
        Response response = TokenManager.sendUpdateRequest(token, bookingIdResourcePath,payload);
        // Retry logic for 403 Forbidden
        if (response.statusCode() == 403) {
            // Token expired or invalid. Refreshing token and retrying...
            TokenManager.invalidateToken(); // Reset token
            token = TokenManager.getToken(); // Get a new one
            response = TokenManager.sendUpdateRequest(token, bookingIdResourcePath,payload);
        }

        return response;
    }
}