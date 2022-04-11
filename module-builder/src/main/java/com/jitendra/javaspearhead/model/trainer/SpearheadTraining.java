package com.jitendra.javaspearhead.model.trainer;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;

@Slf4j
@Service
public class SpearheadTraining {

    String modelPath = "/train/unitedModel.txt";

    String outpath;
    NameFinderME nameFinder;
    @Autowired
    public SpearheadTraining(@Value("${model.outpath}")String outpath) {
        this.outpath = outpath;
        nameFinder = setupModel();
    }


    //NameFinderME nameFinder;

    private NameFinderME setupModel() {
        try {
            ObjectStream<NameSample> sampleStream = readTrainingFile(modelPath);

            TrainingParameters params = setTrainingParam();

            TokenNameFinderModel nameFinderModel = NameFinderME.train("eng", null,
                    sampleStream,
                    params,
                    TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));


            // Write the file
            BufferedOutputStream modelOut = null;
            try {
                File modelFile = new File(outpath+"/unitedModel.bin");

                if (!modelFile.exists()) {
                    modelFile.createNewFile();
                }
                modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
                nameFinderModel.serialize(modelOut);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (modelOut != null)
                    modelOut.close();
            }

            nameFinder = new NameFinderME(nameFinderModel);
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

    private ObjectStream<NameSample> readTrainingFile(String modelPath) throws IOException {
        return new NameSampleDataStream(
                new PlainTextByLineStream(new InputStreamFactory() {
                    @Override
                    public InputStream createInputStream() throws IOException {
                        return getClass()
                                .getResourceAsStream(modelPath);
                    }
                }, "UTF-8"));
    }

    void execute() {
    }
}
