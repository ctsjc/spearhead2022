package com.jitendra.javaspearhead.learning.services;

import com.jitendra.javaspearhead.learning.services.chunk.ChunkService;
import lombok.Data;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Data
public class OpenNLPService {

    SentenceDetectorME sdetector;
    private WhitespaceTokenizer tokenizer;
    private NameFinderME nameFinderME;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;
    private ChunkerME chunker;
    private ChunkService chunkService;
    public OpenNLPService() {
        try {
            this.sdetector = setSentenceDetector();
            this.nameFinderME = setNameFinder();
            posTagger = setPosTagger();
            lemmatizer = setDictionaryLemmatizer();
            chunker = setChunkerME();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokenizer = WhitespaceTokenizer.INSTANCE;
    }

    private SentenceDetectorME setSentenceDetector() throws IOException {
        InputStream is = getClass().getResourceAsStream("/models/en-sent.bin");
        SentenceModel model = new SentenceModel(is);

        SentenceDetectorME sdetector = new SentenceDetectorME(model);
        return sdetector;
    }

    private NameFinderME setNameFinder() throws IOException {
        return createNameFinder("/models/en-ner-person.bin");
    }

    private ChunkerME setChunkerME() throws IOException {
        InputStream inputStreamChunker = getClass()
                .getResourceAsStream("/models/en-chunker.bin");
        ChunkerModel chunkerModel
                = new ChunkerModel(inputStreamChunker);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        return chunker;
    }

    private DictionaryLemmatizer setDictionaryLemmatizer() throws IOException {
        InputStream dictLemmatizer = getClass()
                .getResourceAsStream("/models/en-lemmatizer.dict");
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(
                dictLemmatizer);
        return lemmatizer;
    }

    private POSTaggerME setPosTagger() throws IOException {
        InputStream inputStreamPOSTagger = getClass()
                .getResourceAsStream("/models/en-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStreamPOSTagger);
        return new POSTaggerME(posModel);
    }

    public NameFinderME createNameFinder(String modelFile) throws IOException {
        InputStream inputStreamNameFinder = getClass()
                .getResourceAsStream(modelFile);
        TokenNameFinderModel model = new TokenNameFinderModel(
                inputStreamNameFinder);
        return new NameFinderME(model);
    }

}
