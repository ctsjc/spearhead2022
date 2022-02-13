package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.QuestionBean;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/***
 * This will accept the noun phase sequence and finds the questions
 * */
@Slf4j
@Service
public class QuestionFinder {
    String modelFile = "unitedModel.bin";
    NameFinderME nameFinder=null;
    public QuestionFinder() {

    }

    /**
     * input :: "With NP1 in NP2, NP3 has NP4".split("\\s+");
     *
     * */
    public List<QuestionBean> questions(String[] sequence){
        List<QuestionBean> questionBeans= new ArrayList<>();
        InputStream modelIn=null;
        try {
            modelIn = new FileInputStream(modelFile);
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            nameFinder = new NameFinderME(model);
            Span[] names = nameFinder.find(sequence);
            System.out.println("Jay Shree Ram " + names.length);
            Stream.of(names)
                    .forEach(span -> {
                        String named = IntStream.range(span.getStart(), span.getEnd())
                                .mapToObj(i -> sequence[i])
                                .collect(Collectors.joining(" "));
                        log.info("find type: " + span.getType() + ",name: " + named + "\t" + (span.getProb() * 100));
                        questionBeans.add(new QuestionBean(named, span.getType(), String.valueOf(span.getProb() * 100)));
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



        return questionBeans;
    }
}
