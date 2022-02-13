package com.spearhead.agidoda.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spearhead.agidoda.aggregator.QuestionPhraseAggregator;
import com.spearhead.agidoda.beans.CommonBag;
import com.spearhead.agidoda.beans.QuestionBean;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import com.spearhead.agidoda.parser.PhraseFinder;
import com.spearhead.agidoda.parser.QuestionFinder;
import com.spearhead.agidoda.utility.JsonPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Example {
    @Autowired
    QuestionFinder questionFinder;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    PhraseFinder phraseFinder;

    @Autowired
    OpenNLPService openNLPService;
    @Autowired
    QuestionPhraseAggregator questionPhraseAggregator;

    public void testPhraseFinder() {

    }

    public  List<CommonBag> testQuestionFinder(String paragraph) {

        String[] sentences = openNLPService.getSdetector().sentDetect(paragraph);
        int i=0;
        List<CommonBag> commonBags=new ArrayList<>();
        while(i<sentences.length){
            System.out.println(sentences[i]);

            String sentence=sentences[i];
            String sequence = phraseFinder.convertToNounPhrasedSequence(sentence);
            List<QuestionBean> names = questionFinder.questions(sequence.split("-"));
            CommonBag commonBag = new CommonBag();
            commonBag.setString(sentence);
            commonBag.addToObjects(phraseFinder.getPhraseForm());
            commonBag.addToObjects(names);
            commonBag.addToListMap("namePhraseSequence", sequence);

            Map<String, Object> map = new HashMap<>();
            map.put("names", names);
            map.put("commonBag", commonBag);
            commonBag = questionPhraseAggregator.qq(map);
            commonBags.add(commonBag);

            i++;
        }

        List<CommonBag> cs = commonBags.stream().map(commonBag -> {
            commonBag.setObjects(null);
            commonBag.setMapList(null);
            return commonBag;
        }).collect(Collectors.toList());
        log.info("\n===========================\n");
        log.info(JsonPrinter.print(cs));
        return commonBags;
    }


}
