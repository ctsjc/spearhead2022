package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.dictionary.DictionaryTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DictionaryWordFinder {
    private OpenNLPService openNLPService;

    DictionaryNameFinder dictionaryNameFinder;
    @Autowired
    public DictionaryWordFinder(@Value("${model.custom.path}") String modelDirectory,
    OpenNLPService openNLPService) {
        this.openNLPService = openNLPService;
        this.dictionaryNameFinder = openNLPService.getDictionaryNameFinder(modelDirectory);

    }

    public List<Annotation> find(String[] tokens) {
        List<Annotation> annotations = new ArrayList<>();
        List<Span> foundSpans = new ArrayList<>();

        try {
            Span[] spans = this.dictionaryNameFinder.find(tokens);
            foundSpans.addAll(Arrays.asList(spans));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        foundSpans.sort(Span::compareTo);

        for (Span span : foundSpans) {
            int start = span.getStart();
            int end = span.getEnd();
            String type = span.getType();
            String[] foundTokens = Arrays.copyOfRange(tokens, start, end);
            annotations.add(new Annotation(foundTokens, span));
        }

        for (Annotation annotation : annotations) {
            for (String token : annotation.getTokens()) {
                System.out.printf("%s ", token);
            }
            Span span = annotation.getSpan();
            System.out.printf("[%d..%d) %s\n", span.getStart(), span.getEnd(), span.getType());
        }
        return  annotations;
    }

    public List<DictionaryTuple> find(String sentence) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(sentence);
        List<Annotation> ann = find(tokens);
        List<DictionaryTuple> dictionaryTuples=new ArrayList<>();
        ann.forEach(annotation -> {
            DictionaryTuple tuple =new DictionaryTuple();
            String word = String.join(" ", annotation.getTokens());
            tuple.setWord(word);
            tuple.setPhrase(new ArrayList<>(annotation.getTokens().length));

            Arrays.stream(annotation.getTokens()).forEach(w ->{
                tuple.getPhrase().add(w);
            });
            dictionaryTuples.add(tuple);
        });
        log.info("dictionaryTuples:{}",dictionaryTuples);
        return dictionaryTuples;
    }


    private class Annotation {
        private String[] tokens;
        private Span span;

        public Annotation(String[] tokens, Span span) {
            this.tokens = tokens;
            this.span = span;
        }

        public String[] getTokens() {
            return tokens;
        }

        public Span getSpan() {
            return span;
        }
    }
}
