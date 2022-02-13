package com.spearhead.agidoda.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This class is intentded to print the instance in json format.
 * in future, We should print the table.
 * */
public class JsonPrinter<T> {
    static ObjectMapper objectMapper=new ObjectMapper();

    /**
     * @link PhraseFinder
     * @param t instance which we want to print.
     * @return Well formed String
     * */
    public static <T> String print(T t){
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Cannot Print";
    }
}
