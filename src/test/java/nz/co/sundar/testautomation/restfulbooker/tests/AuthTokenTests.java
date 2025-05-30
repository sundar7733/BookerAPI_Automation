package nz.co.sundar.testautomation.restfulbooker.tests;

import io.restassured.response.Response;
import nz.co.sundar.testautomation.restfulbooker.base.TestBase;
import nz.co.sundar.testautomation.restfulbooker.utils.AssertionsUtils;
import nz.co.sundar.testautomation.restfulbooker.utils.ConfigReader;
import nz.co.sundar.testautomation.restfulbooker.utils.TokenManager;
import org.junit.jupiter.api.Test;

/**
 * Test class responsible for validating authentication token generation
 * */

public class AuthTokenTests extends TestBase {
    /**
     * Test case to validate the generation of an invalid authentication token
     * <p>
     *     This test verifies that an authentication token cannot be generated with incorrect credentials.
     */
    @Test
    public void InvalidAuthToken() {

        String username = "testuser"; // Intentionally incorrect username
        String password = "testpassword"; // Intentionally incorrect password

        Response response = TokenManager.generateTokenResponse(username, password);

        try {
            AssertionsUtils.assertAuthErrorResponse(response, 200, "Bad credentials");
        } catch (AssertionError e) {
            reportManager.logFail("Token validation failed:" + e.getMessage());
            throw e; // Re-throw to fail the test
      }

    }
    /**
     * Test case to validate the generation of a valid authentication token
     * <p>
     * This test verifies that a valid authentication token can be generated using the correct credentials.
     * </p>
     */
    @Test
    public void ValidAuthToken() {

        String username = ConfigReader.getProperty("username"); // Correct username from a properties file
        String password = ConfigReader.getProperty("password"); // Correct password from a properties file

        Response response = TokenManager.generateTokenResponse(username, password);

        try {
            AssertionsUtils.assertAuthValidResponse(response, 200);
        } catch (AssertionError e) {
            reportManager.logFail("Token validation failed:" + e.getMessage());
            throw e; // Re-throw to fail the test
        }

    }

}
