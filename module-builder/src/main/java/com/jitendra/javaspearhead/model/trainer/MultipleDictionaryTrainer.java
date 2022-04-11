package com.jitendra.javaspearhead.model.trainer;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
@Service
@Slf4j
public class MultipleDictionaryTrainer {

    public void setupModel(String modelPath, String outputPath) {
        log.info("Setting up dictionary");
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        Path directory = Paths.get(modelPath);
        if (!Files.exists(directory)) {
            log.error("Dictionaries are not present.");
        }
        InputStream inputStream = getClass()
                .getResourceAsStream(modelPath);
        Dictionary dictionary = new Dictionary();

        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.put(new StringList(tokenizer.tokenize(line)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedOutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(outputPath));
            dictionary.serialize(modelOut);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (modelOut != null) {
                try {
                    modelOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("File is created.");
    }


    public MultipleDictionaryTrainer() {

    }

    public List<Annotation> find() {

        String text = "The Matrix is a movie where Keanu Reeves acted as Neo " +
                "and he had co-actors Laurence Fishburne and Carrie-Anne Moss " +
                "as Morpheus and Trinity.";
        text = "AWS Identity and Access Management (IAM) provides fine-grained access control across all of AWS.";

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        List<Annotation> annotations = new ArrayList<>();
        List<Span> foundSpans = new ArrayList<>();

        String currentDir = "/home/g10c/repo/spearhead2022/module-builder/src/main/resources/train/";
        Path directory = Paths.get(currentDir, "dictionary.bin");
        File file = new File(directory.toString());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Dictionary dictionary = new Dictionary(fileInputStream);
            DictionaryNameFinder dictionaryNameFinder = new DictionaryNameFinder(dictionary, "aws");
            Span[] spans = dictionaryNameFinder.find(tokens);
            for (Span span : spans) {
                foundSpans.add(span);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(foundSpans, new Comparator<Span>() {
            @Override
            public int compare(Span o1, Span o2) {
                return o1.compareTo(o2);
            }
        });

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


    public static void main(String[] args) throws FileNotFoundException {
        MultipleDictionaryTrainer ner = new MultipleDictionaryTrainer();
        ner.find();
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