package com.eduardorichards.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareerJourneyPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/career-journey";
    private static final String HEADING_TEXT = "Journey to career in tech";

    @FindBy(xpath = "//*[normalize-space(text())='" + HEADING_TEXT + "']")
    private List<WebElement> journeyHeadingCandidates;

    @FindBy(xpath = "//button[contains(., 'Start the test')]")
    private List<WebElement> startTestButtonCandidates;

    public CareerJourneyPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    @Override
    public String getHeadingText() {
        isLoaded();
        WebElement visibleHeading = staletolerantWait().until(driver ->
            journeyHeadingCandidates.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
        return visibleHeading.getText();
    }

    public void clickStartTest() {
        WebElement visibleButton = staletolerantWait().until(driver ->
            startTestButtonCandidates.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
        clickElement(visibleButton);
    }
}