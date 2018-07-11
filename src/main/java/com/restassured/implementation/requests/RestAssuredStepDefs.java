package com.restassured.implementation.requests;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.restassured.implementation.actions.RestActions;
import com.restassured.implementation.context.DataKeys;
import com.restassured.implementation.context.ScenarioContext;
import com.restassured.implementation.message.Message;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.restassured.RestAssured.*;
import static io.restassured.config.XmlConfig.xmlConfig;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsd;


import static io.restassured.path.json.JsonPath.from;
import static io.restassured.path.xml.XmlPath.from;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.core.Is.is;

import io.restassured.matcher.RestAssuredMatchers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RestAssuredStepDefs {
    @Autowired
    RestActions restActions;

    @Autowired
    ScenarioContext scenarioContext;

    @Value("${baseUrl}")
    private String baseUrl;

    JsonSchemaFactory jsonSchemaFactory;


    @Given("^schema validation is customized$")
    public void customizeValidation() {
        jsonSchemaFactory = restActions.getValidationConfiguration();
        scenarioContext.save(DataKeys.VALIDATION_CONFIGURATION, jsonSchemaFactory);
    }

    @Given("^user sends '(.*)' request for id number '(.*)'$")
    public void sendHTTPRequest(String httpRequest, String pollId) {

        //Set string message to object to later usage in request
        Message message = new Message();
        message.setMessage("My message");

        RequestSpecification requestSpec = restActions.getRequestSpecBuilder();

        //1. Store response as object
        scenarioContext.save(DataKeys.HTTP_RESPONSE,
                 given()
                .spec(requestSpec)
                .contentType("application/json")
                .cookie("username", "John")
                .pathParam("pollId", pollId)
//                REST Assured will serialize the object to JSON since the request content-type is application/json
                .body(message, ObjectMapperType.JACKSON_2)
                .auth()
                .basic("username", "password")
                .when()
                // Log all request specification details
                .log()
                .all()
                .request(httpRequest, baseUrl + "polls/{pollId}"));
        //2. Store as string
//        scenarioContext.save(DataKeys.HTTP_RESPONSE_STRING, get(baseUrl + "polls/" + pollId).asString());
        //3. Store as InputStream
//        scenarioContext.save(DataKeys.HTTP_RESPONSE_INPUT_STREAM, get(baseUrl + "polls/1").asInputStream());
    }

    @When("^response is correctly validated against schema$")
    public void responseIsCorrectlyValidated() {

        Response response = (Response) scenarioContext.getData(DataKeys.HTTP_RESPONSE);

        //1. Customized  Schema Validation of the response for JSON format

        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema.json").using(jsonSchemaFactory));

        //2. Standard  Schema Validation of the response for XML format

//        response.then().assertThat().body(matchesXsd(xsd));
    }

    @Then("^response is retrieved and validated$")
    public void responseIsRetrievedAndValidated() throws Throwable {
        Response response = (Response) scenarioContext.getData(DataKeys.HTTP_RESPONSE);
        String responseString = (String) scenarioContext.getData(DataKeys.HTTP_RESPONSE_STRING);

        ResponseSpecification responseSpec = restActions.getResponseSpecBuilder();

        //1. Validating XML responses
//        response.then().body("options.id.1", equalTo("55"));

        //2. Validating XML responses using x-path
//        response.then().body(hasXPath("/options/value/1", containsString("55")));

        //3.Validating using XmlPath and with groovy syntax
//        List<String> ids = from(responseString).getList("options.id.find { it.@type == 'id' }.item");

        //4.Extracting using XmlPath
//        String firstName = from(responseString).get("question");

        //1. verify JSON responses  with groovy syntax
        response.then()
                .log()
                .all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                // validation from specification object
                .spec(responseSpec)
                // additional validation
                .body("options.value.collect{it.length()}.sum()", greaterThan(16));

        //2. verify/pars JSON responses  with JsonPath and with groovy syntax
//        int sumOfAllValueLengths = from(responseString).getInt("options.value*.length().sum()");
//        assertThat(sumOfAllValueLengths, is(26));

        // Get all headers
        Headers allHeaders = response.getHeaders();
        // Get a single header value:
        System.out.println(response.getHeader("headerName"));
        // Get all cookies as simple name-value pairs
        Map<String, String> allCookies = response.getCookies();
        // Get a single cookie value:
        System.out.println(response.getCookie("cookieName"));
        // Get status line
        System.out.println(response.getStatusLine());
        // Get status code
        System.out.println(response.getStatusCode());
        // Get request - response time
        System.out.println("Request duration was " + response.timeIn(TimeUnit.MILLISECONDS) + " Miliseconds");
    }

    @Then("^returned response code is '(\\d+)'$")
    public void returnedResponseCodeIs(int responseCode) throws Throwable {
        Response response = (Response) scenarioContext.getData(DataKeys.HTTP_RESPONSE);
        assertThat(response.getStatusCode(), is(200));
    }
}