package com.centoria.jobmaroc.test.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page Object class for Selenium tests.
 * Provides common methods for all page objects.
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final int TIMEOUT_SECONDS = 10;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Clear and type text into an input field
     */
    protected void clearAndType(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Click on an element using JavaScript (bypasses overlays)
     */
    protected void click(WebElement element) {
        dismissCookieBannerIfPresent();
        try {
            waitForClickable(element).click();
        } catch (Exception e) {
            // Fallback to JavaScript click if normal click fails
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
    
    /**
     * Dismiss cookie banner if present
     */
    protected void dismissCookieBannerIfPresent() {
        try {
            // Try to find and click accept/dismiss button on cookie banner
            List<WebElement> cookieBanners = driver.findElements(
                By.cssSelector(".cookies-banner, .cookie-banner, #cookie-banner, [class*='cookie']")
            );
            
            if (!cookieBanners.isEmpty()) {
                // Try to click accept button
                List<WebElement> acceptButtons = driver.findElements(
                    By.cssSelector(".cookies-banner button, .cookie-accept, [class*='cookie'] button:first-child, .cookies-banner__accept")
                );
                
                if (!acceptButtons.isEmpty()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButtons.get(0));
                    // Wait a bit for banner to disappear
                    Thread.sleep(500);
                } else {
                    // Hide the banner using JavaScript
                    for (WebElement banner : cookieBanners) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].style.display='none';", banner
                        );
                    }
                }
            }
        } catch (Exception e) {
            // Ignore - banner might not exist
        }
    }
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element
        );
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
