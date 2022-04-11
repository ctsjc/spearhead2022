package com.jitendra.javaspearhead.learning;

import com.jitendra.javaspearhead.learning.beans.ChunkGroup;
import com.jitendra.javaspearhead.learning.beans.PartOfSpeech;
import com.jitendra.javaspearhead.learning.beans.Tuple;
import com.jitendra.javaspearhead.learning.services.NamedEntity3;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonUtilExample {
    
    @Autowired
    CommonExample commonExample;

    NamedEntity3 namedEntity3;

    public void runner() throws Exception {
        String sentence = "American reconnaissance satellites " +
                "have reportedly spotted " +
                "Chinese ships " +
                "suspected of " +
                "selling " +
                "oil to North Korean vessels about 30 times since October.";
        chunk(sentence);
        Map<String, List<Tuple>> map = groupByPhrase(sentence);

        log.info("Phrase Map is :: "+map);
        log.info("\n\nVerbs are \n");
        commonExample.findVerbs(sentence).forEach(System.out::println);


        namedEntity3.name((String[]) commonExample.toSimpleTokens(sentence).toArray());

        // now as you have common verbs, find its questions.
        // its main root
        /*log.info("\n\n\n");
        sentence="The US Treasury published surveillance photographs reportedly taken on October 19 of the North Korean vessel Rye Song Gang 1 lashed to a large Chinese vessel in deep waters, apparently showing hoses transferring oil.";
        chunk(sentence);
        log.info("\n\n\n");
        sentence="South Korean officials told the Chosun Ilbo that the ships were allegedly trading in the West Sea between China and South Korea in a bid to bypass strict United Nations sanctions on oil exports to the pariah regime over its ongoing nuclear and weapons programme.";
        chunk(sentence);*/


        /*paagraph();*/
    }

    private Map<String, List<Tuple>> groupByPhrase(String sentence) throws Exception {
        log.info("==========================");
        Map<String, List<Tuple>> map = commonExample.groupsOfVerbNouns(sentence);
        //printMap(map);
        return map;

    }

    private void printMap(Map<String, List<Tuple>> map) {
        log.info ( String.valueOf(map));
        log.info("-------------------------");
        map.keySet().forEach(key ->{
            log.info("\n"+key+"\n");
            map.get(key).forEach(value -> log.info(String.valueOf(value.getT())));
        });
    }

    private void paagraph() throws IOException {
        String paragraph = "US. spy satellites have captured images of Chinese ships illegally selling oil to North Korea vessels on the West Sea 30 times in the past three months, according to a South Korean newspaper. " +
                "Ship-to-ship trades with North Korea on the high seas are forbidden under United Nations sanctions adopted in September. The U.S. Treasury Department also placed six North Korean shipping companies on a sanctions list on November 21.";

        List<List<ChunkGroup>> chukGroups = (commonExample.toSentences(paragraph)).stream().
                map(strings -> {
                    try {
                        return commonExample.toChunkGroups(strings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

        chukGroups.forEach(chunkGroups -> {
            //log.info("----"+chunkGroups);
            log.info("\n");

            chunkGroups.forEach(ch -> log.info( String.valueOf(ch.phrase())));
        });
    }

    void chunk(String sentence) throws Exception {
        //String sentence = "American reconnaissance satellites have reportedly spotted Chinese ships suspected of selling oil to North Korean vessels about 30 times since October.";

        List<String> chunks = commonExample.toChunks(sentence);
        List<String> token2 = commonExample.toSimpleTokens(sentence);
        List<String> poss = commonExample.toPOS(sentence);
        List<String> lemma = commonExample.toLemma(sentence);
        List<Span> spans = commonExample.toSpan(sentence);

        printTable(chunks, token2, poss, lemma, spans);
        List<ChunkGroup> chunkGroups = commonExample.toChunkGroups(sentence);
        List<Tuple> tuples = chunkGroups.stream().map(ChunkGroup::phrase).collect(Collectors.toList());
        printTable(tuples);
    }

    private void printTable(List<String> chunks, List<String> token2, List<String> poss, List<String> lemmas, List<Span> spans) {
        String[] parts = {"Token", "Lemma", "Chunk", "POS"};

        String[][] table = new String[token2.size() + 1][];
        table[0] = parts;
        for (int i = 0; i < token2.size(); i++) {
            String token = token2.get(i);
            String pos = poss.get(i);
            String chunk = chunks.get(i);
            String lemma = lemmas.get(i);


            for (int j = 0; j < parts.length; j++) {
                table[i + 1] = new String[]{token, lemma, chunk, PartOfSpeech.get(pos).name()};
            }
        }
        for (String[] row : table) {
            System.out.format("%15s\t%15s\t%20s\t%20s %n", row);
        }
    }


    private void printTable(List<Tuple> tuples) {
        log.info("\n\n\tPhrase Table\n");

        String[][] table = new String[tuples.size()][];

        for (int i = 0; i < tuples.size(); i++) {
            String token = (String) tuples.get(i).getT();
            String pos = (String) tuples.get(i).getJ();
            String prominant =(String) tuples.get(i).getK();


            for (int j = 0; j < 3; j++) {
                table[i] = new String[]{ prominant,pos,token};
            }
        }
        for (String[] row : table) {
            System.out.format("%25s\t%25s\t%25s %n", row);
        }
    }
}
