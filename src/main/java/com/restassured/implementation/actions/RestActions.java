package com.restassured.implementation.actions;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.springframework.stereotype.Component;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static org.hamcrest.core.Is.is;

@Component
public class RestActions {
    public JsonSchemaFactory getValidationConfiguration() {

        // This allows fine-grained configuration for the validation with schema version
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
                .freeze();
        return jsonSchemaFactory;
    }

    public ResponseSpecification getResponseSpecBuilder() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        builder.expectBody("options.size()", is(4));
       return builder.build();
    }

    public RequestSpecification getRequestSpecBuilder() {

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.setSessionId("123456");
        return builder.build();

    }
}
