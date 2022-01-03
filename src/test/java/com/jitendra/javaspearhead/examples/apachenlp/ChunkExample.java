package com.jitendra.javaspearhead.examples.apachenlp;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ChunkExample {

    @Test
    public void
    givenChunkerModel_whenChunk_thenChunksAreDetected()
            throws Exception {

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize("He reckons the current account  " +
                "deficit will narrow to only 8 billion.");

        InputStream inputStreamPOSTagger = getClass()
                .getResourceAsStream("/models/en-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStreamPOSTagger);
        POSTaggerME posTagger = new POSTaggerME(posModel);
        String tags[] = posTagger.tag(tokens);

        InputStream inputStreamChunker = getClass()
                .getResourceAsStream("/models/en-chunker.bin");
        ChunkerModel chunkerModel
                = new ChunkerModel(inputStreamChunker);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        String[] chunks = chunker.chunk(tokens, tags);
        assertThat(chunks).contains(
                "B-NP", "B-VP", "B-NP", "I-NP",
                "I-NP", "I-NP", "B-VP", "I-VP",
                "B-PP", "B-NP", "I-NP", "I-NP", "O");
    }
}
