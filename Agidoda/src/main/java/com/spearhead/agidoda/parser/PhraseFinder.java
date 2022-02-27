package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.beans.PhraseForm;
import com.spearhead.agidoda.beans.chunks.TokenTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import com.spearhead.agidoda.utility.JsonPrinter;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhraseFinder {


    PhraseForm phraseForm;

    @Autowired
    ChunkFinder chunkFinder;


    @Autowired
    ChunkSPFinder spFinder;

    /**
     * input :: "With key global aviation rights in North America, Asia-Pacific, Europe, Middle East and Latin America, UAL has the world’s most comprehensive global route network. "
     * output :: PP-NP-PP-NP-O-NP-VP-NP
     */
    public String convertToPhrasedSequence(String sentence) {
        phraseForm = getPhraseForm(sentence);
        return phraseForm.getPhraseSequence();
    }

    public PhraseForm getPhraseForm() {
        return phraseForm;
    }


    public void some(){

    }
    public String convertToNounPhrasedSequence(String sentence) {
        phraseForm = getPhraseForm(sentence);
        log.info("2 phraseForm {}", JsonPrinter.print(phraseForm));
        List<Map<String, String>> phrases = phraseForm.getPhrases();
        String[] seq = phraseForm.getPhraseSequence().split("-");
        List<String> updatedSequence = replaceNonNounPhrase(phrases, seq);
        log.info("2 Intermittent\nupdatedSequence:{}\nphrases{}\nseq:{}", updatedSequence, phrases, seq);
        log.debug(phraseForm.getPhraseSequence());
        String upSequence = String.join("-", updatedSequence);
        log.debug(upSequence);
        return upSequence;
    }

    /**
     * this method will convert returns the corresponding verb instead of verb phrase identifier
     */
    private List<String> replaceNonNounPhrase(List<Map<String, String>> phrases, String[] seq) {
        List<String> updatedSequence = new ArrayList<>();
        int i = 0;
        while (i < phrases.size()) {
            Map<String, String> phraseMap = phrases.get(i);
            String rkey = (String) phraseMap.keySet().toArray()[0];
            if (!rkey.equals("B-NP")) {
                String value = phraseMap.get(rkey);
                updatedSequence.add(value);
            } else {
                updatedSequence.add(seq[i]);
            }
            i++;
        }
        return updatedSequence;
    }

    /**
     * this method will convert returns the corresponding verb instead of verb phrase identifier
     */
    private List<String> groupNounPhrase(List<Map<String, String>> phrases, String[] seq) {
        List<String> updatedSequence = new ArrayList<>();
        int i = 0;
        while (i < phrases.size()) {
            Map<String, String> phraseMap = phrases.get(i);
            String rkey = (String) phraseMap.keySet().toArray()[0];
            if (!rkey.equals("NP")) {
                String value = phraseMap.get(rkey);
                updatedSequence.add(value);
            } else {
                updatedSequence.add(seq[i]);
            }
            i++;
        }
        return updatedSequence;
    }


    private PhraseForm getPhraseForm(String sentence) {
        List<ChunkGroup> chunks = chunkFinder.toChunkGroups(sentence);
        log.info("chunks : {}", JsonPrinter.print(chunks));
        PhraseForm phraseForm = new PhraseForm();
        phraseForm.setSentence(sentence);
        List<String> sequence = new ArrayList<>();

        int i = 0;
        List<Map<String, String>> phrases = new ArrayList<>();
        while (i < chunks.size()) {
            Map<String, String> map = new HashMap<>();
            ChunkGroup chunkGroup = chunks.get(i);
            Optional<String> key = Optional.of(chunkGroup.getChunks().get(0));
            if (key.isPresent()) {
                String chunkType = key.get();// B-NP, I-NP, O, NP
                log.info("chunkGroup {}", chunkGroup);
                if (chunkType.contains("B-")) {
                    sequence.add(chunkType.split("-")[1] + "" + i);
                } else if (chunkType.contains("O")) {
                    sequence.add(chunkType + "" + i);
                } else {
                    sequence.add(chunkType + "" + i);
                }
            }

            String phrase = chunkGroup.getTokens().stream().map(TokenTuple::getToken).collect(Collectors.joining(" "));
            map.put(key.get(), phrase);
            phrases.add(map);
            log.info("ChunkGroup : {}" +
                            "\nsequence : {}" +
                            "\n Phrase : {}" +
                            "\nMap : {}" +
                            "\nPhrases : {}",
                    JsonPrinter.print(chunkGroup), JsonPrinter.print(sequence), JsonPrinter.print(phrase)
                    , JsonPrinter.print(map)
                    , JsonPrinter.print(phrases));
            i++;
        }
        phraseForm.setPhraseSequence(String.join("-", sequence));
        phraseForm.setPhrases(phrases);
        phraseForm.join();
        return phraseForm;
    }




    public String find(String sequencePart) {
        List<Map<String, String>> phrases = phraseForm.getPhrases();
        String[] seq = phraseForm.getPhraseSequence().split("-");
        int i = 0;
        while (i < phrases.size()) {
            if (sequencePart.equals(seq[i])) {
                Map<String, String> phraseMap = phrases.get(i);
                String rkey = (String) phraseMap.keySet().toArray()[0];
                return phraseMap.get(rkey);
            }
            i++;
        }
        return "";
    }

    //"NP0-spoke-NP2-allows-NP4-to transport-NP6-between-NP8-of-NP10-with-NP12-than-if-NP15-were served"
    public String groupByVerb(String sequence) {

        return null;
    }
}
