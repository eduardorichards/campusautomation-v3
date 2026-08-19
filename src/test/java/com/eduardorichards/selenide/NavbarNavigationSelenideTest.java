package com.eduardorichards.selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$$;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;

public class NavbarNavigationSelenideTest {

    private static final String BASE_URL = "https://campus.epam.com/en";
    private static final String COOKIE_ACCEPT_BUTTON_CSS = "#onetrust-accept-btn-handler";

    @BeforeMethod
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 30000;
        Configuration.browserSize = "1920x1080";
    }

    @Test
    public void shouldNavigateThroughNavbarLinks() {
        open(BASE_URL);
        dismissCookieBannerIfPresent();

        $("[data-name='CareerJourney']").click();
        $$(By.xpath("//*[normalize-space(text())='Journey to career in tech']"))
                .filterBy(Condition.visible)
                .first()
                .shouldBe(Condition.visible);
                
        $("[data-name='Skills']").click();
        $("h1").shouldHave(Condition.text("Training skills"));

        $("[data-name='Blog']").click();
        $("h1").shouldHave(Condition.text("Blog"));
        assertTrue($("input[placeholder='Search by keywords']").isDisplayed());

        $("[data-name='About']").click();
        $("h1").shouldHave(Condition.text("Elevate your career through education"));
    }

    private void dismissCookieBannerIfPresent() {
        if ($(COOKIE_ACCEPT_BUTTON_CSS).exists()) {
            $(COOKIE_ACCEPT_BUTTON_CSS).click();
        }
    }

    @AfterMethod
    public void tearDown() {
        closeWebDriver();
    }
}
