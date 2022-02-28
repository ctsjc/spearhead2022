package com.spearhead.agidoda.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spearhead.agidoda.aggregator.QuestionPhraseAggregator;
import com.spearhead.agidoda.beans.CommonBag;
import com.spearhead.agidoda.beans.QuestionBean;
import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import com.spearhead.agidoda.parser.ChunkSPFinder;
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

    @Autowired
    ChunkSPFinder spFinder;
    public void testPhraseFinder() {
        String[] strs= "NP-and-spoke-NP-allows-NP-to transport-NP-between-NP-of-NP-with-NP-than-if-NP-were served directly".split("-");
        String[] tags="NP-CC-VP-NP-VP-NP-VP-NP-PP-NP-PP-NP-PP-NP-SBAR-PP-NP-VP".split("-");

        List<ChunkGroup> outcome = spFinder.toChunkGroups(strs, tags);
        //System.out.println( JsonPrinter.print(outcome));
    }

    public  List<CommonBag> testQuestionFinder(String paragraph) {

        String[] sentences = openNLPService.getSdetector().sentDetect(paragraph);
        int i=0;
        List<CommonBag> commonBags=new ArrayList<>();
        while(i<sentences.length){
            System.out.println(sentences[i]);

            String sentence=sentences[i];
            String sequence = phraseFinder.convertToNounPhrasedSequence(sentence);
            log.info("2. sequence: {} ", sequence);
            List<QuestionBean> names = questionFinder.questions(sequence.split("-"));
            log.info("3. names: {}", names);
            CommonBag commonBag = new CommonBag();
            commonBag.setString(sentence);
            commonBag.addToObjects(phraseFinder.getPhraseForm());
            commonBag.addToObjects(names);
            commonBag.addToListMap("namePhraseSequence", sequence);

            Map<String, Object> map = new HashMap<>();
            map.put("names", names);
            map.put("commonBag", commonBag);
            commonBag = questionPhraseAggregator.qq(map);
            log.info("4. questionPhraseAggregator: {} ",commonBag);
            commonBags.add(commonBag);

            i++;
        }

      /*  List<CommonBag> cs = commonBags.stream().map(commonBag -> {
            commonBag.setObjects(null);
            commonBag.setMapList(null);
            return commonBag;
        }).collect(Collectors.toList());*/
        log.info("\n===========================\n");
        log.info(JsonPrinter.print(commonBags));
        return commonBags;
    }


}
