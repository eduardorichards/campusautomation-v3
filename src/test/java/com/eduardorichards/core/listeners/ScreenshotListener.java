package com.eduardorichards.core.listeners;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.eduardorichards.core.driver.DriverManager;

public class ScreenshotListener implements ITestListener {
    
    private static final Logger log = LoggerFactory.getLogger(ScreenshotListener.class);

    private static final String SCREENSHOT_DIR = "target/screenshots";

    private static DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String fileName = testName + "_" + timestamp + ".png";

        try {
            Path targetPath = Path.of(SCREENSHOT_DIR, fileName);
            Files.createDirectories(targetPath.getParent());

            File screenshot = ((TakesScreenshot) DriverManager.getDriver())
                .getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), targetPath);

            log.error("Test '{}' failed", testName);
            log.info("Screenshot saved to: {}", targetPath.toAbsolutePath());
            
        } catch (Exception e) {
            log.error("Test {} failed, and screenshot capture also failed: {}", testName, e.getMessage());
        }
    }
}
