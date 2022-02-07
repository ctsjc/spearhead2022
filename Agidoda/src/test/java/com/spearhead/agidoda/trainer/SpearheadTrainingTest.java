package com.spearhead.agidoda.trainer;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
class SpearheadTrainingTest {
    @InjectMocks
    SpearheadTraining spearheadTraining;

    @Test
    void name() throws FileNotFoundException {
        spearheadTraining.execute();


        InputStream modelIn = new FileInputStream("unitedModel.bin");
        try {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

            NameFinderME nameFinder = new NameFinderME(model);

            String[] sentence = "NATO United States Barack Obama was Trump".split("\\s+");
            Span[] names = nameFinder.find(sentence);
            System.out.println("Jay Shree Ram " + names.length);
            Stream.of(names)
                    .forEach(span -> {
                        String named = IntStream.range(span.getStart(), span.getEnd())
                                .mapToObj(i -> sentence[i])
                                .collect(Collectors.joining(" "));
                        log.info("find type: " + span.getType() + ",name: " + named + "\t" + (span.getProb() * 100));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }


    }
}