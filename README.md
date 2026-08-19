# campusautomation-v2

Test automation framework for **[EPAM Campus](https://campus.epam.com)**, built with Selenium WebDriver, TestNG, and Java.

Only public, unauthenticated pages are covered, no login covered.

## What it does

Three linear regression scenarios, each backed by a dedicated test class and Page Object chain:

| Test class | Scenario |
|---|---|
| `NavbarNavigationTest` | Navigates through the main navbar links (Career journey → Skills → Blog → About us) and verifies each destination page loads. |
| `LocationFilterTest` | Filters the training programs catalog by country (data-driven, one run per configured country), then verifies the clicked result card matches its detail page. |
| `CareerJourneyQuizTest` | Walks a fixed path through the career journey quiz and verifies redirection to the job-fit guidance page. |

`NavbarNavigationTest` also belongs to the **smoke** group; all three belong to **regression**.

### Bonus features

- **Element highlighting** — `LoggingDriverDecorator` outlines each element in red via JavaScript right before it's acted on, making test runs easier to follow visually.
- **Selenide PoC** — `NavbarNavigationSelenideTest` reimplements the navbar scenario using Selenide, isolated from the main regression suites in its own package and TestNG suite file, as a comparison point against the framework's manual wait/retry utilities.

## Requirements

- Java 17 (JDK)
- Maven 3.x
- Chrome and/or Firefox installed locally (Selenium resolves the matching driver automatically)

## Configuration

Environment-specific settings live in `src/test/resources/`:

- `config-local.properties` — headed Chrome, used by default
- `config-ci.properties` — headless Chrome, used in CI

Select the environment with `-Denv=local` or `-Denv=ci` (default: `local`). Override the browser with `-Dbrowser=chrome|firefox` regardless of environment. Each properties file also defines the list of countries `LocationFilterTest` runs against (`filter.countries`).

## How to run

```bash
# Full regression suite, local environment (default)
mvn clean test

# Smoke suite, CI environment, Firefox
mvn clean test -Denv=ci -Dsuite.xml.file=testng-smoke.xml -Dbrowser=firefox

# Regression suite, CI environment, headless Chrome
mvn clean test -Denv=ci -Dsuite.xml.file=testng-regression.xml

# Selenide bonus PoC only
mvn clean test -Dsuite.xml.file=testng-selenide.xml
```

Available suite files (`-Dsuite.xml.file=`):

| Suite | Contents |
|---|---|
| `testng.xml` | All three tests, run in parallel by class (default) |
| `testng-smoke.xml` | `NavbarNavigationTest` only (`smoke` group) |
| `testng-regression.xml` | All three tests (`regression` group) |
| `testng-selenide.xml` | Selenide bonus PoC only |

## Reports & artifacts

- JUnit XML reports: `target/surefire-reports/`
- Screenshots on failure: `target/screenshots/` (captured by `ScreenshotListener`)
- Logs: console + daily rolling file at `logs/campusautomation.log` (log4j2)

## CI

Runs on GitLab CI (`.gitlab-ci.yml`) using a Kubernetes-executor shared runner with the `maven:3.9-eclipse-temurin-17` image. Chrome is installed from Google's official apt repo before each run. The pipeline defaults to the regression suite and publishes JUnit reports plus failure screenshots as job artifacts.

## Project structure

```
campusautomation-v2/
├── src/
│   ├── main/java/com/eduardorichards/
│   │   ├── core/
│   │   │   ├── config/ConfigReader.java        # Loads config-<env>.properties, system property overrides
│   │   │   └── driver/
│   │   │       ├── DriverManager.java           # ThreadLocal WebDriver, browser factory registry
│   │   │       └── LoggingDriverDecorator.java  # Logs actions + highlights elements before interaction
│   │   ├── model/TrainingProgram.java           # Domain entity (title, detail URL)
│   │   └── pages/
│   │       ├── AbstractPage.java                # Shared waits, stale-retry, navbar links
│   │       ├── HomePage.java
│   │       ├── TrainingProgramsPage.java
│   │       ├── ProgramDetailPage.java
│   │       ├── CareerJourneyPage.java
│   │       ├── CareerJourneyQuizPage.java
│   │       ├── GuidancePage.java
│   │       ├── SkillsPage.java
│   │       ├── BlogPage.java
│   │       └── AboutUsPage.java
│   └── test/
│       ├── java/com/eduardorichards/
│       │   ├── tests/
│       │   │   ├── BaseTest.java                # Driver lifecycle + screenshot listener
│       │   │   ├── NavbarNavigationTest.java
│       │   │   ├── LocationFilterTest.java
│       │   │   └── CareerJourneyQuizTest.java
│       │   ├── core/listeners/ScreenshotListener.java
│       │   └── selenide/NavbarNavigationSelenideTest.java   # Bonus PoC
│       └── resources/
│           ├── config-local.properties
│           ├── config-ci.properties
│           └── log4j2.xml
├── testng.xml
├── testng-smoke.xml
├── testng-regression.xml
├── testng-selenide.xml
├── .gitlab-ci.yml
└── pom.xml
```

*By Eduardo Richards*
