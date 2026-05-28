package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader – loads config/config.properties from the classpath.
 * Usage: ConfigReader.get("deviceName")
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (is == null) {
                throw new RuntimeException("config/config.properties not found on classpath");
            }
            props.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    /** Returns the value for the given key, or throws if missing. */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value.trim();
    }

    /** Returns the value for the given key, or the defaultValue if missing. */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue).trim();
    }
}
