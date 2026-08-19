package com.eduardorichards.core.driver;

import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eduardorichards.core.config.ConfigReader;

public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static final Map<String, Supplier<WebDriver>> BROWSER_FACTORIES = Map.of(
            "chrome", DriverManager::createChromeDriver,
            "firefox", DriverManager::createFirefoxDriver);

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments(
                "--headless=new",
                "--window-size=1920,1080",
                "--no-sandbox",
                "disable-dev-shm-usage");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();

        Supplier<WebDriver> factory = BROWSER_FACTORIES.get(browser);
        if (factory == null) {
            throw new RuntimeException("Not supported browser" + browser);
        }

        WebDriver rawDriver = factory.get();
        WebDriver driver = new LoggingDriverDecorator().decorate(rawDriver);

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));
        
        log.info("Created {} driver on thread {}",browser, Thread.currentThread().getId());

        return driver;
    }

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver());
        }
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
