package com.jitendra.javaspearhead.scratch;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
public class CommonExample {


    OpenNLPService openNLPService;


    ChunkService chunkService;
    public CommonExample() {
        openNLPService=new OpenNLPService();
        chunkService = new ChunkService(this);
    }



    List<String> toSentences(String paragraph) throws IOException {
        log.info("Jay Shree Ram");
        String sentences[] = openNLPService.sdetector.sentDetect(paragraph);
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

    public Map<String, List<Tuple>> groupsOfVerbNouns(String sentence) throws Exception {

        List<ChunkGroup> chunkGroups = toChunkGroups(sentence);
        // find phrases
        List<Tuple> phrases = chunkGroups.stream().map(ChunkGroup::phrase).collect(Collectors.toList());

        Map<String, List<Tuple>> map = phrases.stream().collect(Collectors.groupingBy(tuple -> String.valueOf(tuple.k).split("-")[0]));
        return map;
    }

    public List<String> findVerbs(String sentence) throws Exception {
        List<String> verbLists = new ArrayList<>();
        Map<String, List<Tuple>> map = groupsOfVerbNouns(sentence);
        map.get("VP").forEach(tuple -> {
            try {
                //log.info(tuple);
                List<String> lemmas = this.toLemma(String.valueOf(tuple.t));
                List<String> poss = this.toPOS(String.valueOf(tuple.t));
                List<String> tuples = this.toSimpleTokens(String.valueOf(tuple.t));
                for (int i = 0; i < tuples.size(); i++) {
                    //log.info(tuples.get(i)+"\t"+lemmas.get(i)+"\t"+ PartOfSpeech.get(poss.get(i)).name());
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
}
