package com.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

/**
 * @author akif
 * @since 1/14/22
 */

/**
 * Wrapper subclass for LinkedHashMap<String, Object> to shorten code
 */
public class JSONObject extends LinkedHashMap<String, Object> {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static <T> T getObjectFromJson(String json, Class<T> tClass) throws JsonProcessingException {
        return objectMapper.readValue(json, tClass);
    }
}
