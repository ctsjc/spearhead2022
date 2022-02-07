package com.spearhead.agidoda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@SpringBootTest
public class FebTest {

    String inputParagraph="The Company transports people and cargo through its mainline and regional operations. " +
            "With key global aviation rights in North America, Asia-Pacific, Europe, Middle East and Latin America, UAL has the world’s most comprehensive global route network. " +
            "UAL, through United and its regional carriers, operates more than 4,500 flights a day to 339 airports across five continents from the Company’s hubs at Newark Liberty International Airport (“Newark”), " +
            "Chicago O’Hare International Airport (“Chicago O’Hare”), " +
            "Denver International Airport (“Denver”), " +
            "George Bush Intercontinental Airport (“Houston Bush”), " +
            "Los Angeles International Airport (“LAX”), A.B. Won Pat International Airport (“Guam”), " +
            "San Francisco International Airport (“SFO”) and Washington " +
            "Dulles International Airport (“Washington Dulles”). " +
            "All of the Company’s domestic hubs are located in large business and population centers, contributing to a large amount of “origin and destination” traffic. " +
            "The hub and spoke system allows us to transport passengers between a large number of destinations with substantially more frequent service than if each route were served directly." +
            "The hub system also allows us to add service to a new destination from a large number of cities using only one or a limited number of aircraft." +
            "As discussed under Alliances below, United is a member of Star Alliance, the world’s largest alliance network." +
            "Financial information on the Company’s operating revenues by geographic regions, as reported to the U.S. Department of Transportation (the “DOT”), can be found in Note 17 to the financial statements included in Part II, Item 8 of this report.";

    String expectedTable;

    @Test
    void name() {
        System.out.println(inputParagraph);

    }

    @BeforeEach
    void setUp() {
        String fileName = "expectedTable.json";
        URL resource = getClass().getClassLoader().getResource(fileName);
        System.out.println(resource.toString());
        File file = new File(getClass().getResource(fileName).getFile());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode tree = objectMapper.readTree(file);
            System.out.println(tree.asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
