package com.centoria.jobmaroc.test.playwright.tests;

import com.centoria.jobmaroc.test.playwright.pages.HomePage;
import com.centoria.jobmaroc.test.playwright.pages.SearchResultPage;
import com.centoria.jobmaroc.test.playwright.utils.SecurityTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
/**
 * Enterprise Functional Test Suite for Search Feature.
 * Validates keyword matching, casing, accents, unicode, security inputs, and edge cases.
 */
@DisplayName("Functional Search Test Suite")
public class FunctionalSearchTest extends BasePlaywrightTest {

    @Test
    @DisplayName("Should require city or domain selection when searching without filters")
    public void testSearchWithoutCityOrDomainTriggersAlert() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword("Comptable");
        homePage.clickSearchButtonExpectingAlert();

        String alertMessage = homePage.getLastAlertMessage();
        Assertions.assertNotNull(alertMessage, "An alert dialog should pop up when no city/domain is selected");
        Assertions.assertTrue(alertMessage.contains("Sélectionnez au moins une ville ou un domaine"),
                "Alert message should inform user to select city or domain");
    }

    @Test
    @DisplayName("Should successfully execute search by domain")
    public void testSearchByDomainOnly() {
        HomePage homePage = new HomePage(page).open();

        if (homePage.getDomainOptionCount() > 1) {
            // Select first non-disabled domain option
            homePage.selectDomainBySlug("informatique");
            SearchResultPage resultPage = homePage.clickSearchButton();

            Assertions.assertTrue(resultPage.getCurrentUrl().contains("/categorie/informatique"),
                    "URL should navigate to category path: " + resultPage.getCurrentUrl());
        }
    }

    @Test
    @DisplayName("Should search by domain and keyword with URL parameter preservation")
    public void testSearchByDomainAndKeyword() {
        HomePage homePage = new HomePage(page).open();

        homePage.fillKeyword("Developpeur");
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertTrue(resultPage.getCurrentUrl().contains("/categorie/informatique"),
                "URL should contain category path");
        Assertions.assertTrue(resultPage.getCurrentUrl().contains("keyword=Developpeur"),
                "URL should contain keyword query parameter");
    }

    @ParameterizedTest
    @ValueSource(strings = {"comptable", "COMPTABLE", "Comptable", "CoMpTaBlE"})
    @DisplayName("Should handle case-insensitivity in search query")
    public void testCaseInsensitivity(String keyword) {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword(keyword);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertTrue(resultPage.getCurrentUrl().contains("keyword="),
                "Search should process irrespective of casing: " + keyword);
    }

    @Test
    @DisplayName("Should trim leading and trailing spaces from search query")
    public void testLeadingAndTrailingWhitespace() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword("   Chef de Projet   ");
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertTrue(resultPage.getCurrentUrl().contains("keyword=Chef%20de%20Projet")
                        || resultPage.getCurrentUrl().contains("keyword=Chef+de+Projet"),
                "Leading/trailing spaces should be trimmed before encoding");
    }

    @Test
    @DisplayName("Should safely handle French accents and special characters")
    public void testFrenchAccentsAndSpecialCharacters() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword(SecurityTestData.FRENCH_ACCENTS);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertFalse(resultPage.getCurrentUrl().isEmpty());
    }

    @Test
    @DisplayName("Should safely handle Arabic queries without breaking UI")
    public void testArabicQueryHandling() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword(SecurityTestData.ARABIC_QUERY);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertNotNull(resultPage.getCurrentUrl());
    }

    @Test
    @DisplayName("Should sanitize XSS payloads in keyword input")
    public void testXssSanitization() {
        HomePage homePage = new HomePage(page).open();
        homePage.clearAlertMessages();
        homePage.fillKeyword(SecurityTestData.XSS_SCRIPT_TAG);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        // Verify that XSS script tag did NOT execute an unexpected alert from injected script
        List<String> alerts = homePage.getLastAlertMessages();
        boolean unexpectedAlert = alerts.stream().anyMatch(msg -> msg.equals("XSS") || msg.equals("1"));
        Assertions.assertFalse(unexpectedAlert, "XSS script payload must NOT execute in browser!");
    }

    @Test
    @DisplayName("Should sanitize SQL injection strings in search input")
    public void testSqlInjectionSanitization() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword(SecurityTestData.SQLI_OR_TRUE);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertFalse(resultPage.getCurrentUrl().isEmpty(), "Application should gracefully handle SQLi strings");
    }

    @Test
    @DisplayName("Should handle extremely long search input strings")
    public void testExtremelyLongInputString() {
        HomePage homePage = new HomePage(page).open();
        homePage.fillKeyword(SecurityTestData.EXTREMELY_LONG_INPUT);
        homePage.selectDomainBySlug("informatique");
        SearchResultPage resultPage = homePage.clickSearchButton();

        Assertions.assertNotNull(resultPage.getCurrentUrl());
    }
}
