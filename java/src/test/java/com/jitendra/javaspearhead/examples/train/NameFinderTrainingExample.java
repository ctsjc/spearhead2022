package com.jitendra.javaspearhead.examples.train;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
@Slf4j
public class NameFinderTrainingExample {

    @Test
    public void test() {
        log.info("Jay Shree Ram");
        ObjectStream sampleStream = createObjectStream();

        TrainingParameters params = setTrainingParameters();

        // training the model using TokenNameFinderModel class
        TokenNameFinderModel nameFinderModel = createTokenNameFinderModel(sampleStream, params);

        writeModel(nameFinderModel);
        log.info("Done");

        // testing the model and printing the types it found in the input sentence
        test(nameFinderModel);
    }

    private void test(TokenNameFinderModel nameFinderModel) {
        log.info("========== Testing Initiated ===========");
        TokenNameFinder nameFinder = new NameFinderME(nameFinderModel);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] testSentence ={"youtube","elasticsearch","Fernandes","is","a","tourist","from","Spain"};
        log.info("Finding types in the test sentence..");
        Span[] names = nameFinder.find(testSentence);
        for(Span name:names){
            String personName="";
            for(int i=name.getStart();i<name.getEnd();i++){
                personName+=testSentence[i]+" ";
            }
            log.info(name.getType()+" : "+personName+"\t [probability="+name.getProb()+"]");
        }
        log.info("========== Testing Done ===========");
    }

    private void writeModel(TokenNameFinderModel nameFinderModel) {
        // saving the model to "ner-custom-model.bin" file
        try {
            File output = new File("ner-custom-model.bin");
            FileOutputStream outputStream = new FileOutputStream(output);
            nameFinderModel.serialize(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TokenNameFinderModel createTokenNameFinderModel(ObjectStream sampleStream, TrainingParameters params) {
        TokenNameFinderModel nameFinderModel = null;
        try {
            nameFinderModel = NameFinderME.train("en", null, sampleStream,
                    params, TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameFinderModel;
    }

    private TrainingParameters setTrainingParameters() {
        // setting the parameters for training
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 1);
        params.put(TrainingParameters.CUTOFF_PARAM, 0);
        return params;
    }

    private ObjectStream createObjectStream() {
        InputStreamFactory in = null;
        try {
            in = new InputStreamFactory() {
                public InputStream createInputStream() throws IOException {
                    return getClass()
                            .getResourceAsStream("/models/AnnotatedSentences.txt");
                }
            };
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        ObjectStream sampleStream = null;
        try {
            sampleStream = new NameSampleDataStream(
                    new PlainTextByLineStream(in, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return sampleStream;
    }
}
