package com.test.web.crawler.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    private static final ObjectMapper INSTANCE = new ObjectMapper();

    private JsonConverter() {
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
