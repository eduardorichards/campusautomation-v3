package com.eduardorichards.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.eduardorichards.core.config.ConfigReader;
import com.eduardorichards.model.TrainingProgram;
import com.eduardorichards.pages.HomePage;
import com.eduardorichards.pages.ProgramDetailPage;
import com.eduardorichards.pages.TrainingProgramsPage;

@Test(groups = "regression")
public class LocationFilterTest extends BaseTest {

    @DataProvider(name = "countries")
    public Object[][] countries() {
        return ConfigReader.getFilterCountries().stream()
                .map(country -> new Object[] { country })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "countries")
    public void shouldFilterProgramsByLocation(String country) {
        HomePage homePage = new HomePage();
        homePage.navigateTo();

        homePage.clickFindAProgram();
        TrainingProgramsPage trainingProgramsPage = new TrainingProgramsPage();
        assertEquals(trainingProgramsPage.getHeadingText(), "Training programs");

        trainingProgramsPage.openLocationFilter();
        assertTrue(trainingProgramsPage.isLocationDropdownOpen());

        trainingProgramsPage.selectCountry(country);
        assertTrue(trainingProgramsPage.isFilterApplied());

        TrainingProgram expectedProgram = trainingProgramsPage.openFirstResultCard();

        ProgramDetailPage programDetailPage = new ProgramDetailPage();
        assertEquals(programDetailPage.getHeadingText(), expectedProgram.getTitle());
    }
}