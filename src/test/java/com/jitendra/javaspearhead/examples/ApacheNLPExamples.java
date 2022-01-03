package com.jitendra.javaspearhead.examples;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class ApacheNLPExamples {

        @Test
        void readSentence() throws IOException {
            log.info("Jay Shree Ram");
            String paragraph = "This is a statement. This is another statement."
                    + "Now is an abstract word for time, "
                    + "that is always flying. And my email address is google@gmail.com.";

            InputStream is = getClass().getResourceAsStream("/models/en-sent.bin");
            SentenceModel model = new SentenceModel(is);

            SentenceDetectorME sdetector = new SentenceDetectorME(model);

            String sentences[] = sdetector.sentDetect(paragraph);
            assertThat(sentences).contains(
                    "This is a statement.",
                    "This is another statement.",
                    "Now is an abstract word for time, that is always flying.",
                    "And my email address is google@gmail.com.");
        }
        @Test
        public void givenWhitespaceTokenizer_whenTokenize_thenTokensAreDetected()
                throws Exception {

            WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
            String[] tokens = tokenizer.tokenize("Baeldung is a Spring Resource.");

            assertThat(tokens)
                    .contains("Baeldung", "is", "a", "Spring", "Resource.");
        }
        @Test
        public void givenSimpleTokenizer_whenTokenize_thenTokensAreDetected()
                throws Exception {

            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String[] tokens = tokenizer
                    .tokenize("Baeldung is a Spring Resource.");

            assertThat(tokens)
                    .contains("Baeldung", "is", "a", "Spring", "Resource", ".");
        }
        @Test
        public void
        givenEnglishPersonModel_whenNER_thenPersonsAreDetected()
                throws Exception {

            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String[] tokens = tokenizer
                    .tokenize("John is 26 years old. His best friend's "
                            + "name is Leonard. He has a sister named Penny.");

            InputStream inputStreamNameFinder = getClass()
                    .getResourceAsStream("/models/en-ner-person.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(
                    inputStreamNameFinder);
            NameFinderME nameFinderME = new NameFinderME(model);
            List<Span> spans = Arrays.asList(nameFinderME.find(tokens));

            assertThat(spans.toString())
                    .isEqualTo("[[0..1) person, [13..14) person, [20..21) person]");
        }

        @Test
        public void givenPOSModel_whenPOSTagging_thenPOSAreDetected()
                throws Exception {

            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String[] tokens = tokenizer.tokenize("John has a sister named Penny.");

            InputStream inputStreamPOSTagger = getClass()
                    .getResourceAsStream("/models/en-pos-maxent.bin");
            POSModel posModel = new POSModel(inputStreamPOSTagger);
            POSTaggerME posTagger = new POSTaggerME(posModel);
            String tags[] = posTagger.tag(tokens);

            assertThat(tags).contains("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".");
        }

        @Test
        public void givenEnglishDictionary_whenLemmatize_thenLemmasAreDetected()
                throws Exception {

            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String[] tokens = tokenizer.tokenize("John has a sister named Penny.");

            InputStream inputStreamPOSTagger = getClass()
                    .getResourceAsStream("/models/en-pos-maxent.bin");
            POSModel posModel = new POSModel(inputStreamPOSTagger);
            POSTaggerME posTagger = new POSTaggerME(posModel);
            String tags[] = posTagger.tag(tokens);
            InputStream dictLemmatizer = getClass()
                    .getResourceAsStream("/models/en-lemmatizer.dict");
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(
                    dictLemmatizer);
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);

            assertThat(lemmas)
                    .contains("O", "have", "a", "sister", "name", "O", "O");
        }

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
