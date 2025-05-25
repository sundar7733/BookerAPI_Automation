package nz.co.sundar.testautomation.restfulbooker.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Utility class responsible for managing authentication tokens for accessing the
 * Restful Booker API. It retrieves credentials from a properties file and provides
 * a mechanism to retrieve and cache the token so that authentication does not need
 * to be performed repeatedly during test execution.
 */

public class TokenManager {
    /**
     * Stores the authentication token after it has been generated.
     */
    private static String token;

    /**
     * Endpoint URL for generating the authentication token.
     */
    private static final String AUTH_URL = ConfigReader.getProperty("authURL");
    /**
     * Returns the current authentication token. If the token has not yet been generated,
     * it invokes the {@link #generateToken()} method to get a new one.
     *
     * @return A valid authentication token as a {@code String}.
     */
    public static String getToken() {
        if (token == null) {
            token = generateToken();
        }
        return token;
    }
    /**
     * Generates a new authentication token by sending a POST request to the authentication
     * endpoint. The username and password are retrieved from a {@code config.properties} file
     * using the {@link ConfigReader} utility. If the request is successful (HTTP 200),
     * the token is extracted from the response and returned. If not, a {@link RuntimeException}
     * is thrown with the response status code.
     *
     * @return A valid authentication token as a {@code String}.
     * @throws RuntimeException if the HTTP response code is not 200.
     */
    private static String generateToken() {
        Response response = generateTokenResponse(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password")
        );

        if (response.statusCode() == 200) {
            return response.jsonPath().getString("token");
        } else {
            throw new RuntimeException("Failed to generate token. Status code: " + response.statusCode());
        }
    }

    public static Response generateTokenResponse(String username, String password) {
        String authPayload = String.format("""
        {
            "username" : "%s",
            "password" : "%s"
        }
        """, username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .post(AUTH_URL);
    }

    public static void invalidateToken() {
        token = null;
    }

    public static Response sendUpdateRequest(String token, String bookingIdResourcePath, String payload) {
        return given()
                .header("Cookie", "token=" + token)
                .contentType(ContentType.JSON)
                .log().body()
                .body(payload)
                .put(bookingIdResourcePath);
    }

    public static Response sendDeleteRequest(String token, String bookingIdResourcePath) {
        return given()
                .header("Cookie", "token=" + token)
                .contentType(ContentType.JSON)
                .delete(bookingIdResourcePath);
    }
}

