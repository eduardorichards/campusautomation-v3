package com.eduardorichards.pages;

import java.time.Duration;
import java.util.function.Supplier;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eduardorichards.core.config.ConfigReader;
import com.eduardorichards.core.driver.DriverManager;

public abstract class AbstractPage {
    
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private static final int MAX_STALE_RETRY_ATTEMPTS = 3;
    private static final Logger log = LoggerFactory.getLogger(AbstractPage.class);

    @FindBy(tagName = "h1")
    private WebElement pageHeading;

    @FindBy(css = "[data-name='CareerJourney']")
    private WebElement careerJourneyLink;

    @FindBy(css = "[data-name='Skills']")
    private WebElement skillsLink;

    @FindBy(css = "[data-name='Blog']")
    private WebElement blogLink;

    @FindBy(css = "[data-name='About']")
    private WebElement aboutUsLink;

    protected AbstractPage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        PageFactory.initElements(driver, this);
    }

    protected WebDriverWait staletolerantWait() {
        WebDriverWait tolerant = new WebDriverWait(driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        tolerant.ignoring(StaleElementReferenceException.class);
        return tolerant;
    }

    protected abstract String getUrlFragment();

    public boolean isLoaded() {
        return waitForUrlToContain(getUrlFragment());
    }

    public String getHeadingText() {
        isLoaded();
        return waitForStableText(pageHeading);
    }

    protected WebElement getPageHeadingElement() {
        return pageHeading;
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean waitForUrlToContain(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void waitForTextToChange(WebElement element, String previousText) {
        wait.until(driver -> !element.getText().equals(previousText));
    }

    protected String getElementText(WebElement element) {
        waitForVisibility(element);
        return element.getText();
    }

    protected void clickElement(WebElement element) {
        waitForVisibility(element);
        element.click();
    }


    /**
     * Re-runs {@code action} when the DOM re-renders between locating an
     * element and using it (React result lists in particular), retrying up
     * to {@link #MAX_STALE_RETRY_ATTEMPTS} times before giving up.
     */
    protected <T> T withStaleRetry(Supplier<T> action) {
        StaleElementReferenceException lastException = null;

        for (int attempt = 1; attempt <= MAX_STALE_RETRY_ATTEMPTS; attempt++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException stale) {
                lastException = stale;
                log.warn("Stale element on attemp {}/{}, retrying", attempt, MAX_STALE_RETRY_ATTEMPTS);
            }
        }
        log.error("Exhausted {} retry attemps due to stale element", MAX_STALE_RETRY_ATTEMPTS);
        throw lastException;
    }

    private static final Duration STABILITY_CHECK_INTERVAL = Duration.ofMillis(150);

    protected String waitForStableText(WebElement element) {
        return wait.until(driver -> {
            String firstRead = element.getText();
            sleepQuietly(STABILITY_CHECK_INTERVAL);
            String secondRead = element.getText();
            return (!firstRead.isEmpty() && firstRead.equals(secondRead)) ? firstRead : null;
        });
    }

    private void sleepQuietly(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickCareerJourney() {
        clickElement(careerJourneyLink);
    }

    public void clickSkills() {
        clickElement(skillsLink);
    }

    public void clickBlog() {
        clickElement(blogLink);
    }

    public void clickAboutUs() {
        clickElement(aboutUsLink);
    }
}
