package com.spearhead.agidoda.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPrinter<T> {
    static ObjectMapper objectMapper=new ObjectMapper();
    public static <T> String print(T t){
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Cannot Print";
    }
}
