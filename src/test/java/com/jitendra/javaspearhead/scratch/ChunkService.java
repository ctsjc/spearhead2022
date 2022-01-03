package com.jitendra.javaspearhead.scratch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ChunkService {
    OpenNLPService openNLPService;
    CommonExample commonExample;


    public ChunkService() {
        this.openNLPService = new OpenNLPService();
        this.commonExample = new CommonExample();
    }
    public ChunkService(CommonExample  commonExample) {
        this.openNLPService = new OpenNLPService();
        this.commonExample =commonExample;
    }
    public List<String>
    toChunks(String sentence)
            throws Exception {
        String[] tokens = openNLPService.getTokenizer().tokenize(sentence);
        String tags[] = openNLPService.getPosTagger().tag(tokens);
        String[] chunks = openNLPService.getChunker().chunk(tokens, tags);
        log.info("chunks :: {}",chunks);
        return Arrays.asList(chunks);
    }

    public List<ChunkGroup> toChunkGroups(String sentence) throws Exception {
        List<ChunkGroup> lists = new ArrayList<>();
        List<String> chunks = toChunks(sentence);
        List<String> tokens = commonExample.toSimpleTokens(sentence);
        List<String> poss = commonExample.toPOS(sentence);
        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.size(); i++) {
            char firstChar = chunks.get(i).charAt(0);
            if (firstChar == 'B') {
                chunkGroup = new ChunkGroup();
                lists.add(chunkGroup);
                chunkGroup.getChunks().add(chunks.get(i));
                chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), poss.get(i)));
            } else {
                chunkGroup.getChunks().add(chunks.get(i));
                chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), poss.get(i)));
            }
        }
        return lists;
    }


}
