package com.eduardorichards.core.driver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighlightingListener implements WebDriverListener {
    
    private static final Logger log = LoggerFactory.getLogger(HighlightingListener.class);
    private static final String HIGHLIGHT_SCRIPT = 
        "arguments[0].style.setProperty('outline', '3px solid red', 'important');";

    private final WebDriver driver;

    public HighlightingListener(WebDriver driver) {
        this.driver = driver;
    }

    private void highlight(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(HIGHLIGHT_SCRIPT, element);
        } catch (Exception e) {
            log.debug("Could not highlight element: {}", e.getMessage());
        }
    }

    @Override
    public void beforeClick(WebElement element) {
        highlight(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlight(element);
    }

    @Override
    public void beforeClear(WebElement element) {
        highlight(element);
    }
}
