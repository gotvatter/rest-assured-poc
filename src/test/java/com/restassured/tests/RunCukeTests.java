package com.restassured.tests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        tags = {"@Run"},
        glue = {"com.restassured.implementation.requests",
                "com.restassured.implementation.actions"})
public class RunCukeTests {

}

