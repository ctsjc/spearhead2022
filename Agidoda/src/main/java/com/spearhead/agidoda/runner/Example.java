package com.spearhead.agidoda.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spearhead.agidoda.aggregator.QuestionPhraseAggregator;
import com.spearhead.agidoda.beans.CommonBag;
import com.spearhead.agidoda.beans.QuestionBean;
import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.beans.dictionary.DictionaryTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import com.spearhead.agidoda.parser.ChunkSPFinder;
import com.spearhead.agidoda.parser.DictionaryWordFinder;
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

    @Autowired
    DictionaryWordFinder dictionaryWordFinder;

    //     inputParagraph = "The hub and spoke system allows us to transport passengers between a large number of destinations with substantially" +
    //                " more frequent service than if each route were served directly.";
    public void testPhraseFinder() {
        String[] strs= ("NP-and-spoke-" +
                "NP-allows-NP-to transport-NP-between-NP-of-NP-with-NP-" +
                "than-if-NP-were served directly").split("-");
        String[] tags="NP-CC-VP-NP-VP-NP-VP-NP-PP-NP-PP-NP-PP-NP-SBAR-PP-NP-VP".split("-");
        /*Ideal should be */
        List<ChunkGroup> outcome = spFinder.toChunkGroups(strs, tags);
        //System.out.println( JsonPrinter.print(outcome));
    }

    public  List<CommonBag> testQuestionFinder(String paragraph) {

        String[] sentences = openNLPService.getSdetector().sentDetect(paragraph);
        int i=0;
        List<CommonBag> commonBags=new ArrayList<>();
        while(i<sentences.length){
            System.out.println(sentences[i]);
            // Find Dictionary Words

            String sentence=sentences[i];
            List<DictionaryTuple> dictionary = dictionaryWordFinder.find(sentence);
            Map<String, String> dictionaryWord2SentenceMapping= new HashMap<>();

            dictionary.forEach(dictionaryTuple -> {
                log.info("Dictionary ::: {}\t{}",dictionaryTuple.getWord(),dictionaryTuple.getPhrase());
            });
            for (int j = 0; j < dictionary.size(); j++) {
                dictionaryWord2SentenceMapping.put("EN"+j, dictionary.get(j).getWord());
                sentence=sentence.replace(dictionary.get(j).getWord(), "EN"+j);
            }
            log.info("After Dictionary Sentence :: {}",sentence);



            /**Its the sequence of Noun by Verbs as it is
             *  sequence | NP0-provides-NP2-across-NP4-of-NP6
             * */
            String sequence = phraseFinder.convertToNounPhrasedSequence(sentence);
            log.info("2. sequence: {} ", sequence);
            List<QuestionBean> names = questionFinder.questions(sequence.split("-"));
            log.info("3. names: {}", names);
            CommonBag commonBag = new CommonBag();
            commonBag.setString(sentence);
            commonBag.addToObjects(phraseFinder.getPhraseForm());
            commonBag.addToObjects(names);
            commonBag.setDictionaryWord2SentenceMapping(dictionaryWord2SentenceMapping);
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

        commonBags.forEach(commonBag -> log.info(JsonPrinter.print(commonBag.getMap())));

        return commonBags;
    }


}
