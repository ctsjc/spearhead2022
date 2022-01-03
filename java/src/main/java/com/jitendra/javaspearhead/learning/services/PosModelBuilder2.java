package com.jitendra.javaspearhead.learning.services;

import opennlp.tools.postag.*;
import opennlp.tools.util.*;

import java.io.*;
import java.util.Dictionary;


public class PosModelBuilder2 {
    public void manager() {

    }


    public static class MyPOSTaggerFactory extends POSTaggerFactory {

        public MyPOSTaggerFactory() {
            super();
        }

        public MyPOSTaggerFactory(Dictionary dic, TagDictionary tagDic) {
           // super(dic, tagDic);
        }

        @Override
        public POSContextGenerator getPOSContextGenerator() {
            return new MyPOSContextGenerator();
        }

    }

    public static class MyPOSContextGenerator implements POSContextGenerator {

        public String[] getContext(int pos, String[] tokens, String[] prevTags,
                                   Object[] ac) {
            // TODO implement the context generator!
            // Use opennlp.tools.postag.DefaultPOSContextGenerator as example:
            // http://svn.apache.org/viewvc/opennlp/trunk/opennlp-tools/src/main/java/opennlp/tools/postag/DefaultPOSContextGenerator.java?view=markup
            return null;
        }

    }

    public static void createModel() {
        POSModel model = null;

        // Train the model

        InputStream dataIn = null;
        try {
            dataIn = new FileInputStream("en-pos.train");

            InputStreamFactory isf = new MarkableFileInputStreamFactory(
                    new File("en-pos.train"));
            ObjectStream<String> lineStream = new PlainTextByLineStream(isf,
                    "UTF-8");
            ObjectStream<POSSample> sampleStream = new WordTagSampleStream(
                    lineStream);

            POSTaggerFactory factory = new POSTaggerFactory();

            model = POSTaggerME.train("en", sampleStream,
                    TrainingParameters.defaultParams(), factory);
        } catch (IOException e) {
            // Failed to read or parse training data, training failed
            e.printStackTrace();
        } finally {
            if (dataIn != null) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    // Not an issue, training already finished.
                    // The exception should be logged and investigated
                    // if part of a production system.
                    e.printStackTrace();
                }
            }
        }

        // serialize the model
        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(
                    "en-pos.bin"));
            model.serialize(modelOut);
        } catch (IOException e) {
            // Failed to save model
            e.printStackTrace();
        } finally {
            if (modelOut != null) {
                try {
                    modelOut.close();
                } catch (IOException e) {
                    // Failed to correctly save model.
                    // Written model might be invalid.
                    e.printStackTrace();
                }
            }

        }
    }
}
