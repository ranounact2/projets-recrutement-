package com.centoria.jobmaroc.test.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Enterprise Page Object for Home Page (`/`).
 * Encapsulates the search form elements: keyword, city, domain, button.
 */
public class HomePage extends BasePage {

    // Selectors
    public static final String KEYWORD_INPUT = "#keyword";
    public static final String CITY_SELECT = "#city";
    public static final String DOMAIN_SELECT = "#domain";
    public static final String SEARCH_BUTTON = "#linkButton";
    public static final String MAIN_HEADING = "section.intro h1";
    public static final String SUB_HEADING = "section.intro p";

    public HomePage(Page page) {
        super(page);
    }

    public HomePage open() {
        navigateTo("/");
        return this;
    }

    public HomePage fillKeyword(String keyword) {
        fillInput(KEYWORD_INPUT, keyword);
        return this;
    }

    public HomePage selectCityBySlug(String citySlug) {
        selectOptionByValue(CITY_SELECT, citySlug);
        return this;
    }

    public HomePage selectDomainBySlug(String domainSlug) {
        selectOptionByValue(DOMAIN_SELECT, domainSlug);
        return this;
    }

    public SearchResultPage clickSearchButton() {
        clickElement(SEARCH_BUTTON);
        return new SearchResultPage(page);
    }

    public void clickSearchButtonExpectingAlert() {
        clickElement(SEARCH_BUTTON);
    }

    public String getKeywordValue() {
        return getInputValue(KEYWORD_INPUT);
    }

    public String getSelectedCitySlug() {
        return page.locator(CITY_SELECT).inputValue();
    }

    public String getSelectedDomainSlug() {
        return page.locator(DOMAIN_SELECT).inputValue();
    }

    public String getKeywordPlaceholder() {
        return page.locator(KEYWORD_INPUT).getAttribute("placeholder");
    }

    public String getMainHeadingText() {
        return getElementText(MAIN_HEADING);
    }

    public boolean isKeywordInputVisible() {
        return isElementVisible(KEYWORD_INPUT);
    }

    public boolean isCitySelectVisible() {
        return isElementVisible(CITY_SELECT);
    }

    public boolean isDomainSelectVisible() {
        return isElementVisible(DOMAIN_SELECT);
    }

    public boolean isSearchButtonVisible() {
        return isElementVisible(SEARCH_BUTTON);
    }

    public void pressEnterOnKeywordInput() {
        page.locator(KEYWORD_INPUT).press("Enter");
    }

    public int getCityOptionCount() {
        return page.locator(CITY_SELECT + " option").count();
    }

    public int getDomainOptionCount() {
        return page.locator(DOMAIN_SELECT + " option").count();
    }
}
