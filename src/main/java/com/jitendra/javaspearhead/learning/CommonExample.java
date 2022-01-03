package com.jitendra.javaspearhead.learning;

import com.jitendra.javaspearhead.learning.beans.ChunkGroup;
import com.jitendra.javaspearhead.learning.beans.PartOfSpeech;
import com.jitendra.javaspearhead.learning.beans.Tuple;
import com.jitendra.javaspearhead.learning.services.chunk.ChunkService;
import com.jitendra.javaspearhead.learning.services.OpenNLPService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Service
@Slf4j
public class CommonExample {
    OpenNLPService openNLPService;
    ChunkService chunkService;
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    public CommonExample(OpenNLPService openNLPService) {
        this.openNLPService = openNLPService;
    }

    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    public List<String> toSentences(String paragraph) throws IOException {
        String sentences[] = openNLPService.getSdetector().sentDetect(paragraph);
        return Arrays.asList(sentences);
    }

    public List<String> toWhitespaceTokens(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer().tokenize(sentence);
        return Arrays.asList(tokens);
    }

    public List<String> toSimpleTokens(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer()
                .tokenize(sentence);
        return Arrays.asList(tokens);
    }

    public List<Span>
    toSpan(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer()
                .tokenize(sentence);


        List<Span> spans = Arrays.asList(openNLPService.getNameFinderME().find(tokens));
        return spans;
    }

    public List<String> toPOS(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer().tokenize(sentence);
        String tags[] = openNLPService.getPosTagger().tag(tokens);

        return Arrays.asList(tags);
    }

    public List<String> toLemma(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer().tokenize(sentence);
        String tags[] = openNLPService.getPosTagger().tag(tokens);
        String[] lemmas = openNLPService.getLemmatizer().lemmatize(tokens, tags);
        return Arrays.asList(lemmas);
    }

    public List<String>
    toChunks(String sentence)
            throws Exception {
        return chunkService.toChunks(sentence);
    }

    public List<ChunkGroup> toChunkGroups(String sentence) throws Exception {
        return chunkService.toChunkGroups(sentence);
    }

    public LinkedHashMap<String, List<Tuple>> groupsOfVerbNouns(String sentence) throws Exception {

        List<Tuple> phrases = toPhrases(sentence);
        log.info("phrases :: " + phrases.stream().map(Tuple::getT).collect(Collectors.toList()));
        LinkedHashMap<String, List<Tuple>> map = phrases.stream().
                collect(Collectors.groupingBy(tuple -> String.valueOf(tuple.getK()).split("-")[0], LinkedHashMap::new, Collectors.toList()));
        return map;
    }

    public LinkedHashMap<String, List<Tuple>> groupsOfVerbNouns(List<Tuple> phrases) throws Exception {
        log.info("phrases :: " + phrases.stream().map(Tuple::getT).collect(Collectors.toList()));
        LinkedHashMap<String, List<Tuple>> map = phrases.stream().
                collect(Collectors.groupingBy(tuple -> String.valueOf(tuple.getK()).split("-")[0], LinkedHashMap::new, Collectors.toList()));
        return map;
    }


    public List<Tuple> toPhrases(String sentence) throws Exception {
        List<ChunkGroup> chunkGroups = toChunkGroups(sentence);
        // find phrases
        List<Tuple> phrases = chunkGroups.stream().map(ChunkGroup::phrase).collect(Collectors.toList());
        return phrases;
    }

    public List<String> findVerbs(String sentence) throws Exception {
        List<String> verbLists = new ArrayList<>();
        Map<String, List<Tuple>> map = groupsOfVerbNouns(sentence);
        map.get("VP").forEach(tuple -> {
            try {
                //log.info(tuple);
                List<String> lemmas = this.toLemma(String.valueOf(tuple.getT()));
                List<String> poss = this.toPOS(String.valueOf(tuple.getT()));
                List<String> tuples = this.toSimpleTokens(String.valueOf(tuple.getT()));
                for (int i = 0; i < tuples.size(); i++) {
                    log.info(tuples.get(i) + "\t" + lemmas.get(i) + "\t" + PartOfSpeech.get(poss.get(i)).name());
                    if (PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB ||
                            PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PAST ||
                            PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PRESENT) {
                        verbLists.add(String.valueOf(lemmas.get(i)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return verbLists;
    }

    public List<String> findVerb(String verbPhrase) throws Exception {
        String verb = "";
        List<String> lemmas = this.toLemma(String.valueOf(verbPhrase));
        List<String> poss = this.toPOS(String.valueOf(verbPhrase));
        List<String> tuples = this.toSimpleTokens(String.valueOf(verbPhrase));
        List<String> verbLists = new ArrayList<>(tuples.size());
        for (int i = 0; i < tuples.size(); i++) {
            // log.info(tuples.get(i)+"\t"+lemmas.get(i)+"\t"+ PartOfSpeech.get(poss.get(i)).name());
            if (PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PAST_TENSE ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PRESENT ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PAST ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_SINGULAR_PRESENT_NONTHIRD_PERSON ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_SINGULAR_PRESENT_THIRD_PERSON ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_MODAL ||
                    PartOfSpeech.get(poss.get(i)) == PartOfSpeech.VERB_PARTICIPLE_PRESENT) {
                verbLists.add(String.valueOf(lemmas.get(i)));
            }
        }

        return verbLists;
    }

    public List<String> findNounPhrases(List<Tuple> sentence) throws Exception {
        List<String> nounPhraseLists = new ArrayList<>();
        Map<String, List<Tuple>> map = groupsOfVerbNouns(sentence);
        nounPhraseLists = map.get("NP").stream().map(tuple -> String.valueOf(tuple.getT())).collect(Collectors.toList());
        return nounPhraseLists;
    }

    public List<String> findNounPhrases(String sentence) throws Exception {
        List<String> nounPhraseLists = new ArrayList<>();
        Map<String, List<Tuple>> map = groupsOfVerbNouns(sentence);
        nounPhraseLists = map.get("NP").stream().map(tuple -> String.valueOf(tuple.getT())).collect(Collectors.toList());
        return nounPhraseLists;
    }


    public String retrieveVerbQuestionaries(String verb, String sentence) {
        try {

            //return createNameFinder("/models/en-ner-person.bin");
            String modelFile = "/models/entity/" + "en-name-" + verb + ".bin";
            Resource resource = resourceLoader.getResource("classpath:" + modelFile);
            log.info("model file={} resource-exists={}", modelFile, resource.exists());
            if (resource.exists()) {
                openNLPService.setNameFinderME(openNLPService.createNameFinder(modelFile));
                String[] tokenize = openNLPService.getTokenizer().tokenize(sentence);
                Span[] fields = openNLPService.getNameFinderME().find(tokenize);
                for (int i = 0; i < fields.length; i++) {
                    String named = IntStream.range(fields[i].getStart(), fields[i].getEnd())
                            .mapToObj(j -> tokenize[j])
                            .collect(Collectors.joining(" "));
                    log.info("Verbs ::: {}\t{}\t{}", fields[i].getType(), fields[i].getProb(), named);
                }
            } else {
                log.info("Model file is not found. {}", modelFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
