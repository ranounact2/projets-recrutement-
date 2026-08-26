package com.centoria.jobmaroc.test.bdd;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Cucumber Test Runner for BDD tests.
 * 
 * This runner executes all .feature files in src/test/resources/features
 * and maps them to step definitions in the bdd package.
 * 
 * To run specific scenarios, use tags:
 * - @smoke: Quick sanity tests
 * - @creation: Job creation tests
 * - @update: Job update tests
 * - @validation: Form validation tests
 * - @search: Search functionality tests
 * - @filter: Filter functionality tests
 * 
 * Run with: mvn test -Dtest=CucumberTestRunner
 * Run with tags: mvn test -Dtest=CucumberTestRunner -Dcucumber.filter.tags="@smoke"
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.centoria.jobmaroc.test.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @wip")
public class CucumberTestRunner {
    // This class is intentionally empty.
    // It's used only as a holder for the above annotations.
}
