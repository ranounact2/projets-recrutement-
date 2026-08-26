package com.centoria.jobmaroc.test.playwright.tests;

import com.centoria.jobmaroc.test.playwright.factory.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

/**
 * Enterprise JUnit 5 Base Test Class for Playwright Lifecycle.
 * Initializes clean browser session before each test and releases resources after execution.
 */
public abstract class BasePlaywrightTest {

    protected Page page;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        page = PlaywrightFactory.initPage();
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().map(m -> m.getName()).orElse("test");
        PlaywrightFactory.stopTracing(testName);
        PlaywrightFactory.cleanUp();
    }
}
