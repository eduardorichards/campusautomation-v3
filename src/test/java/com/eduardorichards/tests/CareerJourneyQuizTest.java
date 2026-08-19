package com.eduardorichards.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.eduardorichards.pages.CareerJourneyPage;
import com.eduardorichards.pages.CareerJourneyQuizPage;
import com.eduardorichards.pages.GuidancePage;
import com.eduardorichards.pages.HomePage;

@Test(groups = "regression")
public class CareerJourneyQuizTest extends BaseTest {

    @Test
    public void shouldRedirectToGuidanceAfterQuiz() {
        HomePage homePage = new HomePage();
        homePage.navigateTo();

        homePage.clickPassNavigationTest();
        CareerJourneyPage careerJourneyPage = new CareerJourneyPage();
        assertEquals(careerJourneyPage.getHeadingText(), "Journey to career in tech");

        careerJourneyPage.clickStartTest();
        CareerJourneyQuizPage quizPage = new CareerJourneyQuizPage();
        assertEquals(quizPage.getQuestionHeadingText(),
                "How would you evaluate your current level of knowledge in IT?");

        quizPage.selectAnswer("I have some knowledge of tech");
        assertEquals(quizPage.getQuestionHeadingText(), "Have you already chosen your skill?");

        quizPage.selectAnswer("No");
        GuidancePage guidancePage = new GuidancePage();
        assertTrue(guidancePage.isLoaded());
        assertEquals(guidancePage.getHeadingText(), "Test: What tech job is right for me?");
        assertTrue(guidancePage.isPassTestButtonDisplayed());
    }
}