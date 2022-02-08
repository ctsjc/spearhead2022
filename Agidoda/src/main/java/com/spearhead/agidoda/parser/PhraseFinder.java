package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class PhraseFinder {

    @Autowired
    OpenNLPService openNLPService;
    public String convertToPhrasedSequece(String sentence){
        String sequnece="";
        String[] tokens = openNLPService.getTokenizer().tokenize(sentence);
        Arrays.stream(tokens).forEach(System.out::println);
        return sequnece;
    }
}
