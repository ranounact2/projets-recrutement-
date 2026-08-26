package com.centoria.jobmaroc.test.playwright.config;

/**
 * Enterprise Configuration Manager for Playwright Automation.
 * Reads environment variables with fallback default values.
 */
public class TestConfig {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_BROWSER = "chromium";
    private static final double DEFAULT_TIMEOUT = 10000.0;
    private static final boolean DEFAULT_HEADLESS = true;

    public static String getBaseUrl() {
        String envUrl = System.getenv("TEST_BASE_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            return envUrl.trim();
        }
        return System.getProperty("test.base.url", DEFAULT_BASE_URL);
    }

    public static String getBrowserType() {
        String envBrowser = System.getenv("TEST_BROWSER");
        if (envBrowser != null && !envBrowser.trim().isEmpty()) {
            return envBrowser.trim().toLowerCase();
        }
        return System.getProperty("test.browser", DEFAULT_BROWSER).toLowerCase();
    }

    public static double getDefaultTimeout() {
        String envTimeout = System.getenv("TEST_TIMEOUT");
        if (envTimeout != null && !envTimeout.trim().isEmpty()) {
            try {
                return Double.parseDouble(envTimeout.trim());
            } catch (NumberFormatException ignored) {}
        }
        return DEFAULT_TIMEOUT;
    }

    public static boolean isHeadless() {
        String envHeadless = System.getenv("TEST_HEADLESS");
        if (envHeadless != null && !envHeadless.trim().isEmpty()) {
            return Boolean.parseBoolean(envHeadless.trim());
        }
        return Boolean.parseBoolean(System.getProperty("test.headless", String.valueOf(DEFAULT_HEADLESS)));
    }
}
