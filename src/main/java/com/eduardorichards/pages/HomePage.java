package com.eduardorichards.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eduardorichards.core.config.ConfigReader;

public class HomePage extends AbstractPage {

    private static final String URL_FRAGMENT = "/en";
    private static final String COOKIE_ACCEPT_BUTTON_CSS = "#onetrust-accept-btn-handler";
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    @FindBy(css = "[data-cy='find-program-btn'] a")
    private WebElement findAProgramButton;

    @FindBy(linkText = "Pass the navigation test")
    private WebElement passNavigationTestButton;

    public HomePage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    @Override
    public boolean isLoaded() {
        waitForVisibility(findAProgramButton);
        return true;
    }

    public void navigateTo() {
        driver.get(ConfigReader.getBaseUrl());
        dismissCookieBannerIfPresent();
    }

    private void dismissCookieBannerIfPresent() {
        try {
            WebElement acceptButton = new WebDriverWait(driver,
                    Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(COOKIE_ACCEPT_BUTTON_CSS)));

            try {
                acceptButton.click();
            } catch (ElementClickInterceptedException interceptedByAnimation) {
                log.warn("click intercepted on cookie banner, falling back to JS click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
            }

            new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                    .until(ExpectedConditions.invisibilityOf(acceptButton));
        } catch (TimeoutException bannerNotPresent) {
            log.debug("Cookie banner not present, skipping dismissal");
            // intended empty
        }
    }

    public void clickFindAProgram() {
        clickElement(findAProgramButton);
    }

    public void clickPassNavigationTest() {
        clickElement(passNavigationTestButton);
    }
}