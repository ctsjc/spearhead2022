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

    MultipleDictionaryTrainer dictionaryTrainer;

    @Autowired
    public ChunkTrainerManager(@Value("${model.outpath}") String outpath,
                               ChunkTrainer chunkTrainer,
                               MultipleDictionaryTrainer dictionaryTrainer) {
        this.chunkTrainer = chunkTrainer;
        this.outputPath = outpath;
        this.dictionaryTrainer=dictionaryTrainer;
        log.info("Output Path={}", this.outputPath);
        build();
    }

    public void build() {
        /*IDea is we will keep the model files in module-builder's Resource folder,
        * where are as the bin folder will be shared accross other modules,
        * so output path is model folder..*/
        buildChunk("/train/chunkTrain.txt", "chunk.bin");

        buildChunk("/train/chunkBasic.txt", "chunkBasic.bin");

        dictionaryTrainer.setupModel("/train/dictionaries/aws.txt",
                this.outputPath + "/" + "dictionary-aws.bin");
    }

    private void buildChunk(String modelPath, String outputFileName) {
        log.info("Building {}|{}",modelPath, outputFileName);
        chunkTrainer.setupModel(modelPath, this.outputPath + "/" + outputFileName);
    }
}
