package com.eduardorichards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CareerJourneyQuizPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/career-journey";
    private static final String ANSWER_OPTION_XPATH_TEMPLATE = "//*[normalize-space(text())='%s' and not(*)]";

    public CareerJourneyQuizPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

    @Override
    public boolean isLoaded() {
        waitForVisibility(getPageHeadingElement());
        return true;
    }

    public String getQuestionHeadingText() {
        return getHeadingText();
    }

    public void selectAnswer(String answerText) {
        String previousQuestion = getHeadingText();

        WebElement answerOption = driver.findElement(
                By.xpath(String.format(ANSWER_OPTION_XPATH_TEMPLATE, answerText)));
        clickElement(answerOption);

        waitForTextToChange(getPageHeadingElement(), previousQuestion);
    }
}