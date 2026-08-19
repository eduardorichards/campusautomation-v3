package com.eduardorichards.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.eduardorichards.pages.AboutUsPage;
import com.eduardorichards.pages.BlogPage;
import com.eduardorichards.pages.CareerJourneyPage;
import com.eduardorichards.pages.HomePage;
import com.eduardorichards.pages.SkillsPage;

@Test(groups = {"smoke", "regression"})
public class NavbarNavigationTest extends BaseTest {

    @Test
    public void shouldNavigateThroughMainNavbarLinks() {
        HomePage homePage = new HomePage();
        homePage.navigateTo();

        homePage.clickCareerJourney();
        CareerJourneyPage careerJourneyPage = new CareerJourneyPage();
        assertEquals(careerJourneyPage.getHeadingText(), "Journey to career in tech");

        careerJourneyPage.clickSkills();
        SkillsPage skillsPage = new SkillsPage();
        assertEquals(skillsPage.getHeadingText(), "Training skills");

        skillsPage.clickBlog();
        BlogPage blogPage = new BlogPage();
        assertEquals(blogPage.getHeadingText(), "Blog");
        assertTrue(blogPage.isSearchInputDisplayed());

        blogPage.clickAboutUs();
        AboutUsPage aboutUsPage = new AboutUsPage();
        assertEquals(aboutUsPage.getHeadingText(), "Elevate your career through education");
    }
}