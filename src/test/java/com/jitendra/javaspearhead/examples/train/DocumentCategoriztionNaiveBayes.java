package com.jitendra.javaspearhead.examples.train;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.junit.jupiter.api.Test;

import java.io.*;
@Slf4j
public class DocumentCategoriztionNaiveBayes {

    public static final String EN_MOVIE_CLASSIFIER_NAIVE_BAYES_BIN = "en-movie-classifier-naive-bayes.bin";
    public static final String MODEL_FILE =EN_MOVIE_CLASSIFIER_NAIVE_BAYES_BIN;

    void generateModel() {
        // read file
        ObjectStream sampleStream = readStream();

        // read training parameter
        TrainingParameters trainingParameters = defaultTrainingParameter();

        try {
            // Train model
            DoccatModel model = DocumentCategorizerME.train("en",sampleStream, trainingParameters, new DoccatFactory());
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
        trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, 10+"");
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
            sampleStream = new DocumentSampleStream(lineStream);
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
                            .getResourceAsStream("/train/en-movie-category.train");
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
        test();

    }

    private void test() throws IOException {
        InputStream is = getClass().getResourceAsStream(EN_MOVIE_CLASSIFIER_NAIVE_BAYES_BIN);
        DoccatModel model = new DoccatModel(new File(EN_MOVIE_CLASSIFIER_NAIVE_BAYES_BIN));
        DocumentCategorizer doccat = new DocumentCategorizerME(model);
        double[] aProbs = doccat.categorize(("Afterwards Stuart and Charlie notice Kate in the shotgun Stuart took " +
               // "at Leopolds ball and realise that her destiny must be to go back and be with Leopold That night while " +
                "Kate is accepting her promotion at a company banquet he and Charlie race to meet her and show her the " +
              //  "pictures Kate initially rejects their overtures and goes on to give her acceptance speech but it is there " +
                "that she sees Stuarts picture").replaceAll("[^A-Za-z]", " ")
                .split(" "));

        // print the probabilities of the categories
        log.info("\n---------------------------------\nCategory : Probability\n---------------------------------");
        for(int i=0;i<doccat.getNumberOfCategories();i++){
            log.info(doccat.getCategory(i)+" : "+aProbs[i]);
        }
        log.info("---------------------------------");

        log.info("\n"+doccat.getBestCategory(aProbs)+" : is the predicted category for the given sentence.");



    }
}
