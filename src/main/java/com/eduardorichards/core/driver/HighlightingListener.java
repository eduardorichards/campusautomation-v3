package com.eduardorichards.core.driver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eduardorichards.core.config.ConfigReader;

public class HighlightingListener implements WebDriverListener {

    private static final Logger log = LoggerFactory.getLogger(HighlightingListener.class);
    private static final int HIGHLIGHT_PAUSE_MILLIS = 300;

    private static final String HIGHLIGHT_SCRIPT = "arguments[0].animate(["
            + "  { boxShadow: '0 0 0 0 rgba(255, 193, 7, 0.9)' },"
            + "  { boxShadow: '0 0 0 8px rgba(255, 193, 7, 0)' }"
            + "], { duration: 650, easing: 'ease-out' });";

    private static final String RIPPLE_SCRIPT = "var rect = arguments[0].getBoundingClientRect();"
            + "var x = rect.left + rect.width / 2;"
            + "var y = rect.top + rect.height / 2;"
            + "var dot = document.createElement('div');"
            + "dot.style.cssText = 'position:fixed; left:' + x + 'px; top:' + y + 'px; '"
            + "  + 'width:12px; height:12px; margin:-6px; border-radius:50%; '"
            + "  + 'background:rgba(255,193,7,0.8); pointer-events:none; z-index:999999;';"
            + "document.body.appendChild(dot);"
            + "dot.animate(["
            + "  { transform: 'scale(1)', opacity: 1 },"
            + "  { transform: 'scale(3)', opacity: 0 }"
            + "], { duration: 500, easing: 'ease-out' }).onfinish = function() { dot.remove(); };";

    private final WebDriver driver;

    public HighlightingListener(WebDriver driver) {
        this.driver = driver;
    }

    private void highlight(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(HIGHLIGHT_SCRIPT, element);
            pauseIfHeaded();
        } catch (Exception e) {
            log.debug("Could not highlight element: {}", e.getMessage());
        }
    }

    private void ripple(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(RIPPLE_SCRIPT, element);
        } catch (Exception e) {
            log.debug("Could not render ripple: {}", e.getMessage());
        }
    }

    private void pauseIfHeaded() {
        if (ConfigReader.isHeadless()) {
            return;
        }
        try {
            Thread.sleep(HIGHLIGHT_PAUSE_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void beforeClick(WebElement element) {
        highlight(element);
        ripple(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlight(element);
        ripple(element);
    }

    @Override
    public void beforeClear(WebElement element) {
        highlight(element);
        ripple(element);
    }
}
