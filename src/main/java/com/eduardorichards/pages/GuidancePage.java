package com.eduardorichards.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GuidancePage extends AbstractPage {

    private static final String URL_FRAGMENT = "/career-test/guidance";

    @FindBy(linkText = "Pass the test")
    private WebElement passTestButton;

    public GuidancePage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    public boolean isPassTestButtonDisplayed() {
        waitForVisibility(passTestButton);
        return passTestButton.isDisplayed();
    }

}
