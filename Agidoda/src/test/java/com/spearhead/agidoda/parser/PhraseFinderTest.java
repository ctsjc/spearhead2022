package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import opennlp.tools.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class PhraseFinderTest {
    @InjectMocks
    PhraseFinder phraseFinder;
    @Spy
    OpenNLPService openNLPService;

    @Test
    void name() {
        String sentence = "With key global aviation rights in North America, Asia-Pacific, Europe, Middle East and Latin America, UAL has the world’s most comprehensive global route network. ";
        // phraseFinder.convertToPhrasedSequence(sentence);
        String actual=phraseFinder.convertToNounPhrasedSequence(sentence);
        System.out.println(actual);

    }



}