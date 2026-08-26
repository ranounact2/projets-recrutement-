package com.centoria.jobmaroc.test.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise Page Object for Search Result Pages.
 * Handles job list cards, result counts, breadcrumbs, empty state messaging, result verification.
 */
public class SearchResultPage extends BasePage {

    // Selectors
    public static final String OFFRE_CARDS = "section.offres .offre, div.offre";
    public static final String TABLE_OFFRE_CONTAINER = ".table-offre";
    public static final String BREADCRUMBS = ".breadcrumbs";
    public static final String RESULT_HEADING = "section.offres h2, .bread h2";
    public static final String NO_RESULTS_MESSAGE = ".no-results, .alert-warning, p:has-text('aucune'), p:has-text('aucun')";
    public static final String JOB_TITLES = "section.offres .offre h3 a";
    public static final String JOB_CITIES = "section.offres .offre span:has(img[alt*='Lieu'])";
    public static final String JOB_SECTORS = "section.offres .offre span:has(img[alt*='Secteur'])";

    public SearchResultPage(Page page) {
        super(page);
    }

    public int getJobCardsCount() {
        return page.locator(OFFRE_CARDS).count();
    }

    public boolean hasJobResults() {
        return getJobCardsCount() > 0;
    }

    public List<String> getAllJobTitles() {
        List<String> titles = new ArrayList<>();
        Locator locators = page.locator(JOB_TITLES);
        int count = locators.count();
        for (int i = 0; i < count; i++) {
            titles.add(locators.nth(i).innerText().trim());
        }
        return titles;
    }

    public List<String> getAllJobCardTexts() {
        List<String> texts = new ArrayList<>();
        Locator locators = page.locator(OFFRE_CARDS);
        int count = locators.count();
        for (int i = 0; i < count; i++) {
            texts.add(locators.nth(i).innerText().trim());
        }
        return texts;
    }

    public String getBreadcrumbText() {
        if (isElementVisible(BREADCRUMBS)) {
            return getElementText(BREADCRUMBS);
        }
        return "";
    }

    public boolean isNoResultsDisplayed() {
        return isElementVisible(NO_RESULTS_MESSAGE) || (getJobCardsCount() == 0);
    }

    public String getResultPageTitle() {
        return getPageTitle();
    }

    public String getFirstJobTitle() {
        if (getJobCardsCount() > 0) {
            return page.locator(JOB_TITLES).first().innerText().trim();
        }
        return null;
    }
}
