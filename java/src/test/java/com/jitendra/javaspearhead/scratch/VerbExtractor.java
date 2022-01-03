package com.jitendra.javaspearhead.scratch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VerbExtractor {

    CommonExample commonExample;

    @Autowired
    public VerbExtractor(CommonExample commonExample) {
        this.commonExample = commonExample;
    }

    public List<String> findVerbs(String sentence) throws Exception {
        List<String> verbLists = new ArrayList<>();
        Map<String, List<Tuple>> map = commonExample.groupsOfVerbNouns(sentence);
        map.get("Verb").forEach(tuple -> {
            try {
                //log.info(tuple);
                List<String> lemmas = commonExample.toLemma(String.valueOf(tuple.t));
                List<String> poss = commonExample.toPOS(String.valueOf(tuple.t));
                List<String> tuples = commonExample.toSimpleTokens(String.valueOf(tuple.t));
                for (int i = 0; i < tuples.size(); i++) {
                    //log.info(tuples.get(i)+"\t"+lemmas.get(i)+"\t"+ PartOfSpeech.get(poss.get(i)).name());
                    if (PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB ||
                            PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PAST ||
                            PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PRESENT) {
                        verbLists.add(String.valueOf(lemmas.get(i)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return verbLists;
    }
}
