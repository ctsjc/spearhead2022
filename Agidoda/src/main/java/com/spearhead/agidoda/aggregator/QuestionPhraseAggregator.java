package com.spearhead.agidoda.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spearhead.agidoda.beans.CommonBag;
import com.spearhead.agidoda.beans.QuestionBean;
import com.spearhead.agidoda.parser.PhraseFinder;
import com.spearhead.agidoda.parser.QuestionFinder;
import com.spearhead.agidoda.utility.JsonPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class QuestionPhraseAggregator {
    @Autowired
    QuestionFinder questionFinder;
    @Autowired
    PhraseFinder phraseFinder;

    ObjectMapper objectMapper=new ObjectMapper();
    public CommonBag qq(Map<String, Object> bag){
        List<QuestionBean> names = (List<QuestionBean>) bag.get("names");
        log.info("QuestionBeans : {} ", JsonPrinter.print(names));
        CommonBag commonBag= (CommonBag) bag.get("commonBag");
        Stream.of(names)
                .forEach(questionBeans -> {
                    int i = 0;
                    while (i < questionBeans.size()) {
                        commonBag.addToMap(questionBeans.get(i).getQuestion(),
                                questionBeans.get(i).getPhrase()+" | "+phraseFinder.find(questionBeans.get(i).getPhrase()));
                        i++;
                    }
                });
        try {
           // commonBag.setObjects(null);
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commonBag));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return commonBag;
    }
}
