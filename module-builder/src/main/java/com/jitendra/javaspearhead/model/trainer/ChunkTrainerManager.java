package com.jitendra.javaspearhead.model.trainer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChunkTrainerManager {
    ChunkTrainer chunkTrainer;
    String outputPath;

    @Autowired
    public ChunkTrainerManager(ChunkTrainer chunkTrainer, @Value("${model.outpath}") String outpath) {
        this.chunkTrainer = chunkTrainer;
        this.outputPath = outpath;
        log.info("Output Path={}", this.outputPath);
        build();
    }

    public void build() {
        String modelPath = "";
        String outputPath = this.outputPath + System.lineSeparator();
        modelPath = "/train/chunkTrain.txt";
        outputPath = "chunk.bin";
        chunkTrainer.setupModel(modelPath, this.outputPath + "/" + outputPath);

        modelPath = "/train/chunkBasic.txt";
        outputPath = "chunkBasic.bin";
        chunkTrainer.setupModel(modelPath, this.outputPath + "/" + outputPath);
    }
}
