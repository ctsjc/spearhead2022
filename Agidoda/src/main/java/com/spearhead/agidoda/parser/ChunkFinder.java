package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.beans.chunks.TokenTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ChunkFinder {

    @Autowired
    OpenNLPService openNLPService;

    public List<ChunkGroup> toChunkGroups(String sentence) {
        List<ChunkGroup> lists = new ArrayList<>();

        String[] tokenize = openNLPService.getTokenizer().tokenize(sentence);
        List<String> tokens = Arrays.asList(tokenize);

        String[] tagArray = openNLPService.getPosTagger().tag(tokenize);
        List<String> tags = Arrays.asList(tagArray);
        String[] chunk = openNLPService.getChunker().chunk(tokenize, tagArray);

        List<String> chunks = Arrays.asList(chunk);

        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.size(); i++) {
            log.info("\nchunk :{}\nTOken :{}\ntags :{}", chunks.get(i), tokens.get(i), tags.get(i));
            char firstChar = chunks.get(i).charAt(0);

            switch (firstChar) {
                case 'O': {
                    chunkGroup = new ChunkGroup();
                    lists.add(chunkGroup);
                    chunkGroup.getChunks().add(tags.get(i));
                    chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
                    break;
                }
                case 'B': {
                    chunkGroup = new ChunkGroup();
                    lists.add(chunkGroup);

                    chunkGroup.getChunks().add(chunks.get(i));

                    chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
                }
                break;
                case 'I': {
                    chunkGroup.getChunks().add(chunks.get(i));
                    chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
                }
                break;
                default: {
                    chunkGroup.getChunks().add(chunks.get(i));
                    chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
                }
            }
        }
        log.info("\n");
        return lists;
    }

    public List<ChunkGroup> toChunkGroupsV2(String sentence) {
        List<ChunkGroup> lists = new ArrayList<>();

        String[] tokenize = openNLPService.getTokenizer().tokenize(sentence);
        List<String> tokens = Arrays.asList(tokenize);

        String[] tagArray = openNLPService.getPosTagger().tag(tokenize);
        List<String> tags = Arrays.asList(tagArray);
        Span[] chunks = openNLPService.getChunker().chunkAsSpans(tokenize, tagArray);
        log.info("chunkSpan :{}", openNLPService.getChunker().chunkAsSpans(tokenize, tagArray));

        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.length; i++) {
            String type = chunks[i].getType();
            chunkGroup = new ChunkGroup();
            lists.add(chunkGroup);
            chunkGroup.getChunks().add(type);
            StringBuilder phrase = new StringBuilder();
            StringBuilder tag = new StringBuilder();
            for (int j = chunks[i].getStart(); j < chunks[i].getEnd(); j++) {
                phrase.append(tokens.get(j)).append(" ");
                tag.append(tags.get(j)).append(" ");
            }
            chunkGroup.getTokens().add(new TokenTuple(phrase.toString().trim(), tag.toString().trim()));
        }
        return lists;
    }

}
