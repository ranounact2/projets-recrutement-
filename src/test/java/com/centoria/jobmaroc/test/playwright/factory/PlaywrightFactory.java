package com.centoria.jobmaroc.test.playwright.factory;

import com.centoria.jobmaroc.test.playwright.config.TestConfig;
import com.microsoft.playwright.*;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Thread-safe Factory for managing Playwright instances, Browsers, and Contexts.
 * Ensures clean lifecycle management and isolated browser sessions.
 */
public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static Page initPage() {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        String browserName = TestConfig.getBrowserType();
        boolean headless = TestConfig.isHeadless();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setArgs(Arrays.asList("--no-sandbox", "--disable-setuid-sandbox"));

        Browser browser;
        switch (browserName) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chromium":
            default:
                browser = playwright.chromium().launch(launchOptions);
                break;
        }
        browserThreadLocal.set(browser);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1280, 800)
                .setIgnoreHTTPSErrors(true);

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(TestConfig.getDefaultTimeout());
        contextThreadLocal.set(context);

        // Start tracing for debugging
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        Page page = context.newPage();
        pageThreadLocal.set(page);
        return page;
    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    public static BrowserContext getContext() {
        return contextThreadLocal.get();
    }

    public static void stopTracing(String traceName) {
        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            try {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("target/playwright-traces/" + traceName + ".zip")));
            } catch (Exception ignored) {}
        }
    }

    public static void cleanUp() {
        Page page = pageThreadLocal.get();
        if (page != null) {
            page.close();
            pageThreadLocal.remove();
        }

        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            context.close();
            contextThreadLocal.remove();
        }

        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            browser.close();
            browserThreadLocal.remove();
        }

        Playwright playwright = playwrightThreadLocal.get();
        if (playwright != null) {
            playwright.close();
            playwrightThreadLocal.remove();
        }
    }
}
