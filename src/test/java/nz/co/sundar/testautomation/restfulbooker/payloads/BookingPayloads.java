package nz.co.sundar.testautomation.restfulbooker.payloads;

public class BookingPayloads {
    public static String createBookingPayload(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin, String checkout, String additionalNeeds) {
        return """
                {
                    "firstname": %s,
                    "lastname": %s,
                    "totalprice": %d,
                    "depositpaid": %b,
                    "bookingdates": {
                        "checkin": %s,
                        "checkout": %s
                    },
                    "additionalneeds": %s
                }
                """.formatted(normaliseString(firstname), normaliseString(lastname), totalPrice, depositPaid, normaliseString(checkin), normaliseString(checkout), normaliseString(additionalNeeds));
    }
    public static String createInvalidBookingPayload(String firstname, String lastname, String totalPrice, String depositPaid, String checkin, String checkout, String additionalNeeds) {
        return """
                {
                    "firstname": %s,
                    "lastname": %s,
                    "totalprice": "%s",
                    "depositpaid": "%s",
                    "bookingdates": {
                        "checkin": %s,
                        "checkout": %s
                    },
                    "additionalneeds": %s
                }
                """.formatted(normaliseString(firstname), normaliseString(lastname), totalPrice, depositPaid, normaliseString(checkin), normaliseString(checkout), normaliseString(additionalNeeds));
    }

    private static String normaliseString(String str) {
        if (str == null) {
            return null;
        }
        else {
            return "\"" + str + "\"";
        }

    }
}

