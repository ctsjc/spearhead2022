package com.jitendra.javaspearhead.examples.apachenlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PersonExample {

    @Test
    public void
    givenEnglishPersonModel_whenNER_thenPersonsAreDetected()
            throws Exception {

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize(  "American reconnaissance satellites " +
                        "have reportedly spotted " +
                        "Chinese ships " +
                        "suspected of " +
                        "selling " +
                        "oil to North Korean vessels about 30 times since October.");

        InputStream inputStreamNameFinder = getClass()
                .getResourceAsStream("/models/en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(
                inputStreamNameFinder);
        NameFinderME nameFinderME = new NameFinderME(model);
        List<Span> spans = Arrays.asList(nameFinderME.find(tokens));

        spans.forEach(span -> {
            System.out.println(span.getType()+"\t"+span.getProb());
        });

    }

}
