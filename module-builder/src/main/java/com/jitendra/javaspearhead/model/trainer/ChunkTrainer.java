package com.jitendra.javaspearhead.model.trainer;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Purpose of this class to integrate chunker Training Example.
 * but the goal is to understand how chunker can accomodate token and tags
 * The problem Right now we have is, Our sentence is made up of phrases. Combination of Phrases can also be the questionaries.
 * I wanted to club the multiple phreases into single Block.
 * then my algorithm can work on Block level.
 * <p>
 * i.e.
 * Sentence :: The hub and spoke system allows us to transport passengers between a large number of destinations with substantially more frequent service than if each route were served directly."
 * <p>
 * The hub and spoke system
 * allows
 * us
 * to transport
 * passengers
 * between
 * a large number of destinations with substantially more frequent service
 * than
 * if each route were served directly.
 * <p>
 * Convert Sentence to simple format
 * level1 :: System allows us to transport passengers between destinations than if each route were served directly.
 * <p>
 * Blocks: Block1 allows NP to transport NP between Block2
 * <p>
 * Block2 : a large number of destinations with substantially more frequent service than if each route were served directly.
 */
@Slf4j
@Service
public class ChunkTrainer {

    // ChunkerME nameFinder;

    public ChunkerME setupModel(String modelPath, String outputPath) {
        try {
            log.info("modelPath {}", modelPath);
            ObjectStream<ChunkSample> sampleStream = readTrainingFile(modelPath);

            TrainingParameters params = setTrainingParam();

            ChunkerModel nameFinderModel = ChunkerME.train("eng",
                    sampleStream,
                    params, new ChunkerFactory());


            // Write the file
            BufferedOutputStream modelOut = null;
            try {
                log.info("Writing the file {}", outputPath);
                modelOut = new BufferedOutputStream(new FileOutputStream(outputPath));
                nameFinderModel.serialize(modelOut);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (modelOut != null)
                    modelOut.close();
            }
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

    private ObjectStream<ChunkSample> readTrainingFile(String modelPath) throws IOException {
        return new ChunkSampleStream(
                new PlainTextByLineStream(new InputStreamFactory() {
                    @Override
                    public InputStream createInputStream() throws IOException {
                        return getClass()
                                .getResourceAsStream(modelPath);
                    }
                }, "UTF-8"));
    }
}
