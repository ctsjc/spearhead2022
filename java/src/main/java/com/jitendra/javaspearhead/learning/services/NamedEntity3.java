package com.jitendra.javaspearhead.learning.services;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.*;
import opennlp.tools.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class NamedEntity3 {
    NameFinderME nameFinder = setupModel();

    public NamedEntity3() {
        this.nameFinder = setupModel();
    }

    public void name(String[] sentence) throws IOException {
        // train the name finder
        // now test if it can detect the sample sentences
       // test(sentence);
    }

    private void test(String[] sentence) {
        //String[] sentence = "NATO United States Barack Obama was Trump".split("\\s+");

        Span[] names = nameFinder.find(sentence);

        Stream.of(names)
                .forEach(span -> {
                    String named = IntStream.range(span.getStart(), span.getEnd())
                            .mapToObj(i -> sentence[i])
                            .collect(Collectors.joining(" "));
                    log.info("find type: " + span.getType() + ",name: " + named + "\t" + (span.getProb() * 100));
                });
    }

    private NameFinderME setupModel() {
        try {
            ObjectStream<NameSample> sampleStream = readTrainingFile();

            TrainingParameters params = setTrainingParam();

            TokenNameFinderModel nameFinderModel = NameFinderME.train("eng", null, sampleStream,
                    params, TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));

            NameFinderME nameFinder = new NameFinderME(nameFinderModel);
            return nameFinder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TrainingParameters setTrainingParam() {
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ALGORITHM_PARAM, "MAXENT");
        params.put(TrainingParameters.ITERATIONS_PARAM, 70);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);
        return params;
    }

    private ObjectStream<NameSample> readTrainingFile() throws IOException {
        ObjectStream<NameSample> sampleStream = new NameSampleDataStream(
                new PlainTextByLineStream(new InputStreamFactory() {
                    @Override
                    public InputStream createInputStream() throws IOException {
                        return getClass()
                                .getResourceAsStream("/train/trainingNameEntity2.txt");
                    }
                }, "UTF-8"));
        return sampleStream;
    }
}
