package com.jitendra.javaspearhead.learning.services;

import com.jitendra.javaspearhead.learning.CommonExample;
import com.jitendra.javaspearhead.learning.beans.PhraseType;
import com.jitendra.javaspearhead.learning.beans.SinglePhrase;
import com.jitendra.javaspearhead.learning.beans.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KaveriWinter {

    /***
     * Read the sentence.
     * replace NP
     * find the verb
     * get the question bank
     * We want all the questions
     *
     *
     * */


    @Autowired
    CommonExample commonExample;
    public void execute() throws Exception {
        String paragraph ="The 25-year-old ran a season best time of 43.85 to finish ahead of Colombia's Anthony Jose Zambrano and London 2012 winner Kirani James.\n" +
                "With his upright running style, Gardiner raced clear on the final straight and even looked to ease up near the finish line to become Olympic champion.\n" +
                "Handed his country’s flag after winning the race, Gardiner’s celebrations were subdued as he looked shattered and took in his achievement. " +
                "His victory never seriously looked in doubt, as the rest of the field around him tired.";

        class ParseParagraph {
            String sentence;
            String phraseWithMarker;
            Set<String> verbs;
            List<String> nouns;
            List<SinglePhrase> singlePhrases;
        }
        List<ParseParagraph> parseParagraphs = parseParagraph(paragraph).stream().map(sentence -> {
            try {
                List<Tuple> phrases = groupByPhrase(sentence);
                List<String> nounPhrase = commonExample.findNounPhrases(phrases);
                // Add the logic... Replace all those noun phrase which are bounded between two VP or ...
                String phraseWithMarker = markWithPhraseMarker(phrases);
                /*Whats the purpose of single phrase*/
                /* multiword American reconnaissance satellite is converted to `American reconnaissance satellite `*/
                List<SinglePhrase> singlePhrases = consolidateToSinglePhrase(phrases);
                // Now get table for all available verbs
                // find verb
                Map<String, SinglePhrase> verbMap = findMainVerbOfSentence(singlePhrases);

                ParseParagraph parseParagraph = new ParseParagraph();
                parseParagraph.sentence = sentence;
                parseParagraph.phraseWithMarker = phraseWithMarker;
                parseParagraph.verbs = verbMap.keySet();
                parseParagraph.nouns = nounPhrase;
                parseParagraph.singlePhrases = singlePhrases;
                return parseParagraph;
            } catch (Exception e) {
                log.error("");
            }
            return null;
        }).collect(Collectors.toList());

        parseParagraphs.forEach(parseParagraph -> {
            log.info("\n----------------\nSentence={}\nWith Marker={}\nMain Verb={}\nNouns={}\n---------------------\n",
                    parseParagraph.sentence, parseParagraph.phraseWithMarker, parseParagraph.verbs, parseParagraph.nouns);
        });
    }

    private void verbQuestionaries(String sentence, Map<String, SinglePhrase> verbMap) {
        for (Map.Entry<String, SinglePhrase> e : verbMap.entrySet()) {
            System.out.println(
                    e.getKey() + "\t" +
                            e.getValue().getPhrase() + "\t" +
                            commonExample.retrieveVerbQuestionaries(e.getKey(), sentence));
        }
    }

    private List<SinglePhrase> consolidateToSinglePhrase(List<Tuple> phrases) throws Exception {
        List<SinglePhrase> singlePhrases = new ArrayList<>();
        for (Tuple entry : phrases) {
            log.info("{}\t{}\t{}", entry.getT(), entry.getJ(), entry.getK());
            if (String.valueOf(entry.getK()).equalsIgnoreCase("NP")) {
                singlePhrases.add(SinglePhrase.of(entry).withPhrase("NP"));
            } else {
                singlePhrases.add(SinglePhrase.of(entry));
            }
        }
        return singlePhrases;
    }

    private String markWithPhraseMarker(List<Tuple> phrases) {
        log.info("======== phrasesMap =====");
        List<String> phraseList = phrases.stream()
                .map(singlePhrase ->
                                "<" + singlePhrase.getK() + ">" +
                                singlePhrase.getT() +
                                "</" + singlePhrase.getK() + ">")
                .collect(Collectors.toList());
        return String.join(" ", phraseList);
    }

    private Map<String, SinglePhrase> findMainVerbOfSentence(List<SinglePhrase> singlePhrases) throws Exception {
        List<SinglePhrase> verbs = singlePhrases.stream().filter(singlePhrase -> singlePhrase.getType() == PhraseType.VP).collect(Collectors.toList());
        Map<String, SinglePhrase> verbMap = new HashMap<>();
        for (SinglePhrase phrase : verbs) {
            List<String> verb = commonExample.findVerb(phrase.getPhrase());
            verbMap.put(String.join(" ", verb), phrase);
        }
        return verbMap;
    }

    private List<String> parseParagraph( String paragraph) throws IOException {
        return commonExample.toSentences(paragraph);
    }

    private List<Tuple> groupByPhrase(String sentence) throws Exception {
        List<Tuple> phrases = commonExample.toPhrases(sentence);
        return phrases;
    }
}
