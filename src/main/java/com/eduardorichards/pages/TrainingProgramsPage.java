package com.eduardorichards.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;

import com.eduardorichards.model.TrainingProgram;

public class TrainingProgramsPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/training";
    private static final String FILTER_URL_FRAGMENT = "filter=";
    private static final String RESULT_CARD_XPATH = "//a[contains(@href, '/training/')]";
    private static final String CARD_TITLE_CSS = "[class*='training-card_card-name__']";

    @FindBy(xpath = "//div[@role='button' and normalize-space()='Location']")
    private List<WebElement> locationFiltertriggerCandidates;

    @FindBy(css = "input[placeholder='Search']")
    private WebElement locationSearchInput;

    @FindBy(xpath = RESULT_CARD_XPATH)
    private List<WebElement> resultCards;

    public TrainingProgramsPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    public void openLocationFilter() {
        WebElement visibleTrigger = wait.until(driver -> locationFiltertriggerCandidates.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
        clickElement(visibleTrigger);
    }

    public boolean isLocationDropdownOpen() {
        waitForVisibility(locationSearchInput);
        return locationSearchInput.isDisplayed();
    }

    private void clearSearchInput() {
        Keys selectAllKey = System.getProperty("os.name").toLowerCase().contains("mac")
                ? Keys.COMMAND
                : Keys.CONTROL;
        locationSearchInput.sendKeys(Keys.chord(selectAllKey, "a"), Keys.BACK_SPACE);
    }

    private static final int MAX_SELECT_ATTEMPTS = 3;

    public void selectCountry(String countryName) {
        By checkboxLocator = By.xpath(
                "//div[@class='text' and normalize-space(text())='" + countryName + "']");

        for (int attempt = 1; attempt <= MAX_SELECT_ATTEMPTS; attempt++) {
            clearSearchInput();
            locationSearchInput.sendKeys(countryName);

            WebElement countryCheckBox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
            countryCheckBox.click();

            try {
                wait.until(ExpectedConditions.urlContains(FILTER_URL_FRAGMENT));
                return;
            } catch (TimeoutException retryable) {
                if (attempt == MAX_SELECT_ATTEMPTS) {
                    throw retryable;
                }
            }
        }
    }

    public boolean isFilterApplied() {
        return waitForUrlToContain(FILTER_URL_FRAGMENT);
    }

    public TrainingProgram openFirstResultCard() {
        return withStaleRetry(() -> {
            WebElement firstCard = resultCards.get(0);
            String title = firstCard.findElement(By.cssSelector(CARD_TITLE_CSS)).getText();
            String detailUrl = firstCard.getDomProperty("href");
            firstCard.click();
            return new TrainingProgram(title, detailUrl);
        });
    }
}
