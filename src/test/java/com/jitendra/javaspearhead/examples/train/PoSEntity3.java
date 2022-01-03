package com.jitendra.javaspearhead.examples.train;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@Slf4j
public class PoSEntity3 {
    POSTaggerME posTagger = null;

    public PoSEntity3() {
        this.posTagger = setupModel();
    }

    @Test
    public void name() throws IOException {
        // train the name finder
        // now test if it can detect the sample sentences
        String[] sentence = "NATO United States Barack Obama was Trump".split("\\s+");
        test(sentence);
    }

    private void test(String[] sentence) {
        //String[] sentence = "NATO United States Barack Obama was Trump".split("\\s+");

        String[] names = posTagger.tag(sentence);

        Stream.of(names)
                .forEach(span -> {
                    log.info("find type: " + span);
                });
    }

    private POSTaggerME setupModel() {
        try {
            ObjectStream<POSSample> sampleStream = readTrainingFile();

            TrainingParameters params = setTrainingParam();
            POSModel model = POSTaggerME.train("en",
                    sampleStream,
                    params,
                   new POSTaggerFactory());

            POSTaggerME posTaggerME = new POSTaggerME(model);
            return posTaggerME;
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

    private ObjectStream<POSSample> readTrainingFile() throws IOException {
        ObjectStream<POSSample> sampleStream = new WordTagSampleStream(new PlainTextByLineStream(new InputStreamFactory() {
            @Override
            public InputStream createInputStream() throws IOException {
                return getClass()
                        .getResourceAsStream("/train/pos/"+"en-pos.train");
            }
        }, "UTF-8"));
        return sampleStream;
    }
}
