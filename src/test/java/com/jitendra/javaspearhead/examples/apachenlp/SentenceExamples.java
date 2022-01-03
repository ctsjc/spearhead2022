package com.jitendra.javaspearhead.examples.apachenlp;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class SentenceExamples {

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
        log.info( String.valueOf(Arrays.asList(sentences)));
        assertThat(sentences).contains(
                "This is a statement.",
                "This is another statement.",
                "Now is an abstract word for time, that is always flying.",
                "And my email address is google@gmail.com.");
    }

}
