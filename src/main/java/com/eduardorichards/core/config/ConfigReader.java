package com.eduardorichards.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_ENV = "local";

    static {
        String env = System.getProperty("env", DEFAULT_ENV);
        String fileName = "config-" + env + ".properties";

        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream(fileName)){
            if (input == null) {
                throw new RuntimeException(fileName + " not found on classpath");
            }

            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    private static String get(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null) {
            throw new RuntimeException("Missing key in config file: " + key);
        }
        return value;
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getBrowser() {
        return System.getProperty("browser", get("browser"));
        }

    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(get("implicit.wait.seconds"));
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds"));
    }

    public static List<String> getFilterCountries() {
        String countries = get("filter.countries");
        return Arrays.stream(countries.split(","))
            .map(String::trim)
            .toList();
    }
}
