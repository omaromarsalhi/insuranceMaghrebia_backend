package com.maghrebia.hr.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> parseJsonString(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
    }
}