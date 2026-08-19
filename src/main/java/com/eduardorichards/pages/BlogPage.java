package com.eduardorichards.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BlogPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/blog";

    @FindBy(css = "input[placeholder='Search by keywords']")
    private WebElement searchByKeywordsInput;

    public BlogPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    public boolean isSearchInputDisplayed() {
        waitForVisibility(searchByKeywordsInput);
        return searchByKeywordsInput.isDisplayed();
    }

}
