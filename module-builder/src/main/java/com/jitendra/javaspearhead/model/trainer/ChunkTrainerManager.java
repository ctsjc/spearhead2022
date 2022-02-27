package com.jitendra.javaspearhead.model.trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChunkTrainerManager {
    ChunkTrainer chunkTrainer;

    @Autowired
    public ChunkTrainerManager(ChunkTrainer chunkTrainer) {
        this.chunkTrainer = chunkTrainer;
        build();
    }

    public void build(){
        String modelPath="";
        String outputPath="";
       modelPath="/train/chunkTrain.txt";
        outputPath="chunk.bin";
        chunkTrainer.setupModel(modelPath, outputPath);

        modelPath="/train/chunkBasic.txt";
        outputPath="chunkBasic.bin";
        chunkTrainer.setupModel(modelPath, outputPath);
    }
}
