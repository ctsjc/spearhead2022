package com.spearhead.agidoda.beans;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class PhraseForm {
    String sentence;
    List<Map<String, String>> phrases;
    String phraseSequence;

    @Override
    public String toString() {
        return "PhraseForm{" +
                "\nsentence:'" + sentence + '\'' +
                "\nphrases=" + phrases.stream().map(entrySet -> {
            String rkey = (String) entrySet.keySet().toArray()[0];
            String key = null;
            if (rkey.contains("B")) {
                key = rkey.split("-")[1];
            } else if (rkey.contains("O")) {
                key = rkey;
            }
            return "\n" + key + "-" + entrySet.get(rkey);
        }).collect(Collectors.toList()) +
                "\nphraseSequence='" + phraseSequence + '\'' +
                '}';
    }

    public void join() {
        /*if consecute B-NP then combine them*/
        List<Map<String, String>> updatedPhrases = new ArrayList<>(this.phrases.size());
        String[] phraseSequences = this.phraseSequence.split("-");
        List<String> updatedSequence = new ArrayList<>(phraseSequences.length);
        List<List<String>> joinSequence=Arrays.asList(Arrays.asList("NP","NP"),Arrays.asList("NP","O","NP"));   // Make them in rule for Chunk
        int i = 0;

        while (i < this.phrases.size() - 1) {
            Map<String, String> entry1 = this.phrases.get(i);
            Map<String, String> entry2 = this.phrases.get(i + 1);
            log.info("\nentry1 {}\nentry2 {}",entry1,entry2);
            if (entry1.keySet().equals(entry2.keySet())) {
                String rkey = (String) this.phrases.get(i).keySet().toArray()[0];
                String value1 = this.phrases.get(i).get(rkey);
                String value2 = this.phrases.get(i + 1).get(rkey);
                Map<String, String> map = new HashMap<>();
                map.put(rkey, value1 + " " + value2);
                updatedPhrases.add(map);
                updatedSequence.add(phraseSequences[i]);
                i += 2;
            } else {
                updatedPhrases.add(this.phrases.get(i));
                updatedSequence.add(phraseSequences[i]);
                i++;
            }
        }
        if(i < this.phrases.size()){
            updatedPhrases.add(this.phrases.get(i));
            updatedSequence.add(phraseSequences[i]);
        }
/*
        i=0;
        while (i < this.phrases.size() - 2) {
            Map<String, String> entry1 = this.phrases.get(i);
            Map<String, String> entry2 = this.phrases.get(i + 1);
            Map<String, String> entry3 = this.phrases.get(i + 2);

            if (Arrays.asList("NP","O","NP").equals(Arrays.asList(entry1, entry2,entry3))) {
                String rkey = (String) this.phrases.get(i).keySet().toArray()[0];
                String value1 = this.phrases.get(i).get(rkey);
                String value2 = this.phrases.get(i + 1).get(rkey);
                String value3 = this.phrases.get(i + 2).get(rkey);
                Map<String, String> map = new HashMap<>();
                map.put(rkey, value1 + " " + value2+ " " + value3);
                updatedPhrases.add(map);
                updatedSequence.add(phraseSequences[i]);
                i += 3;
            } else {
                updatedPhrases.add(this.phrases.get(i));
                updatedSequence.add(phraseSequences[i]);
                i++;
            }
        }*/


        this.phraseSequence = String.join("-", updatedSequence);
        this.phrases = updatedPhrases;
    }



}
