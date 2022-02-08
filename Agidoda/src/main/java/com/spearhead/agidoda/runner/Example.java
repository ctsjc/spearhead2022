package com.spearhead.agidoda.runner;

import com.spearhead.agidoda.beans.QuestionBean;
import com.spearhead.agidoda.parser.PhraseFinder;
import com.spearhead.agidoda.parser.QuestionFinder;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class Example {
    @Autowired
    QuestionFinder questionFinder;

    @Autowired
    PhraseFinder phraseFinder;
    public void testPhraseFinder(){

    }
    public void testQuestionFinder(){
        String[] sequence = "With NP1 in NP2, NP3 has NP4".split("\\s+");

        List<QuestionBean> names = questionFinder.questions(sequence);
        Stream.of(names)
                .forEach(System.out::println);
    }
}
