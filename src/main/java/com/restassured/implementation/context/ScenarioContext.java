package com.restassured.implementation.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {
    private static final Map<DataKeys, Object> data = new HashMap<>();


    public void save(DataKeys key, Object value) {
        data.put(key, value);
    }

    public Object getData(DataKeys key) {
        return data.get(key);
    }

    public void removeData(DataKeys key) {
        if (data.containsKey(key)) {
            data.remove(key);
        }
    }
}
