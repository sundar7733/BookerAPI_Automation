package nz.co.sundar.testautomation.restfulbooker.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read properties from a configuration file.
 */
public class ConfigReader {
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

