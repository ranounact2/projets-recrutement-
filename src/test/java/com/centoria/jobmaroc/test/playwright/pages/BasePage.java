package com.centoria.jobmaroc.test.playwright.pages;

import com.centoria.jobmaroc.test.playwright.config.TestConfig;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise Base Page Object Model.
 * Encapsulates core Playwright Page interactions, wait strategies, alerts, and assertions.
 */
public abstract class BasePage {

    protected final Page page;
    private final List<String> lastAlertMessages = new ArrayList<>();

    public BasePage(Page page) {
        this.page = page;
        setupDialogListener();
    }

    private void setupDialogListener() {
        this.page.onDialog(dialog -> {
            lastAlertMessages.add(dialog.message());
            dialog.accept();
        });
    }

    public void clearAlertMessages() {
        lastAlertMessages.clear();
    }

    public List<String> getLastAlertMessages() {
        return new ArrayList<>(lastAlertMessages);
    }

    public String getLastAlertMessage() {
        if (lastAlertMessages.isEmpty()) {
            return null;
        }
        return lastAlertMessages.get(lastAlertMessages.size() - 1);
    }

    public void navigateTo(String path) {
        String fullUrl = TestConfig.getBaseUrl() + (path.startsWith("/") ? path : "/" + path);
        page.navigate(fullUrl);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public String getPageTitle() {
        return page.title();
    }

    protected void fillInput(String selector, String text) {
        Locator locator = page.locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.clear();
        if (text != null) {
            locator.fill(text);
        }
    }

    protected void clickElement(String selector) {
        Locator locator = page.locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.click();
    }

    protected void selectOptionByValue(String selector, String value) {
        Locator locator = page.locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.selectOption(value);
    }

    protected boolean isElementVisible(String selector) {
        try {
            Locator locator = page.locator(selector);
            return locator.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getElementText(String selector) {
        Locator locator = page.locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator.innerText();
    }

    protected String getInputValue(String selector) {
        return page.locator(selector).inputValue();
    }

    public void captureScreenshot(String filename) {
        try {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("target/screenshots/" + filename + ".png"))
                    .setFullPage(true));
        } catch (Exception ignored) {}
    }

    public void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}
