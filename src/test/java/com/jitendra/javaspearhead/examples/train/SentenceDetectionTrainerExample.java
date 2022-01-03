package com.jitendra.javaspearhead.examples.train;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
@Slf4j
public class SentenceDetectionTrainerExample {

    public static final String MODEL_FILE ="en-sentence.bin";
    public static final String TRAIN_TRAINING_DATA_SENTENCES_TXT = "/train/trainingDataSentences.txt";

    void generateModel() {
        // read file
        ObjectStream sampleStream = readStream();

        // read training parameter
        TrainingParameters trainingParameters = defaultTrainingParameter();

        try {
            // Train model
            SentenceModel model = SentenceDetectorME.train("en",
                    sampleStream,
                    true, null,
                    trainingParameters);
            // save model to local file
            //src/test/resources/models/
            File file= new File(MODEL_FILE);
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream modelOut=new BufferedOutputStream(new FileOutputStream(MODEL_FILE));
            model.serialize(modelOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TrainingParameters defaultTrainingParameter() {
        TrainingParameters  trainingParameters= new TrainingParameters();
        trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, 1000+"");
        trainingParameters.put(TrainingParameters.CUTOFF_PARAM, 0+"");
        trainingParameters.put(TrainingParameters.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        return trainingParameters;
    }

    private ObjectStream readStream() {

        ObjectStream lineStream = null;
        ObjectStream sampleStream = null;
        try {
            InputStreamFactory in = readFileAsStream();
            lineStream = new PlainTextByLineStream(in, "UTF-8");
            sampleStream = new SentenceSampleStream(lineStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleStream;
    }

    private InputStreamFactory readFileAsStream() {
        InputStreamFactory in=null;
        try {
            in = new InputStreamFactory() {
                public InputStream createInputStream() throws IOException {
                    return getClass()
                            .getResourceAsStream(TRAIN_TRAINING_DATA_SENTENCES_TXT);
                }
            };
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return in;
    }

    @Test
    void name() throws IOException {
        log.info("Jay Shree Ram");
        // train
        generateModel();
        // test
      //  test();

    }

    private void test() throws IOException {
        SentenceModel sentdetectModel=new SentenceModel(new File(MODEL_FILE));
        SentenceDetectorME sentDetector = new SentenceDetectorME(sentdetectModel);
        String paragraph = "This is a statement. This is another statement." +
                "Now is an abstract word for time, that is always flying.";

        String sentences[] = sentDetector.sentDetect(paragraph);
        Arrays.stream(sentences).forEach(System.out::println);
    }
}
