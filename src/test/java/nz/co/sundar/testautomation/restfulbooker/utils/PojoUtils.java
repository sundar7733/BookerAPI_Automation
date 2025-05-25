package nz.co.sundar.testautomation.restfulbooker.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.sundar.testautomation.restfulbooker.pojo.Booking;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingId;
import nz.co.sundar.testautomation.restfulbooker.pojo.BookingResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PojoUtils {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "    \"firstname\": \"Jim\",\n" +
                "    \"lastname\": \"Brown\",\n" +
                "    \"totalprice\": 111,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2018-01-01\",\n" +
                "        \"checkout\": \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Booking booking = objectMapper.readValue(jsonString, Booking.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Booking convertJsonToBooking(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Booking booking = null;
        try {
            booking = objectMapper.readValue(jsonString, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public static BookingResponse convertJsonToBookingResponse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        BookingResponse bookingResponse = null;
        try {
            bookingResponse = objectMapper.readValue(jsonString, BookingResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingResponse;
    }

    public static List<BookingId> convertJsonToBookingIdList(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<BookingId> bookingIds = new ArrayList<>();
        try {
            bookingIds = objectMapper.readValue(jsonString,
                    new TypeReference<List<BookingId>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingIds;
    }

}
