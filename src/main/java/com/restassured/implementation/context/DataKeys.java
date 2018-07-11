package com.restassured.implementation.context;

public enum DataKeys {
    VALIDATION_CONFIGURATION("Validation Configuration"),
    HTTP_RESPONSE("Http request response"),
    HTTP_RESPONSE_STRING("Http request string response"),
    HTTP_RESPONSE_INPUT_STREAM("Http request input stream response");

    private String description;


    DataKeys(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
