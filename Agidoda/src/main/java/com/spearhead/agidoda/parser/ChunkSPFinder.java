package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.beans.chunks.TokenTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChunkSPFinder {
    @Autowired
    OpenNLPService openNLPService;

    private String modelDirectory;

    private ChunkerME chunker;

    @Autowired
    public ChunkSPFinder(@Value("${model.custom.path}") String modelDirectory) {
        try {
            this.modelDirectory = modelDirectory;
            this.chunker = setChunkerME();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ChunkerME setChunkerME() throws IOException {

        log.info("File is read from {}", modelDirectory +
                FileSystems.getDefault().getSeparator() + "chunkBasic.bin");


        InputStream inputStreamChunker = new FileInputStream(modelDirectory +
                        FileSystems.getDefault().getSeparator() + "chunkBasic.bin");
        ChunkerModel chunkerModel
                = new ChunkerModel(inputStreamChunker);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        return chunker;
    }


    public List<ChunkGroup> toChunkGroups(String[] sentence, String[] tagArray) {
        List<ChunkGroup> lists = new ArrayList<>();

        //String[] tokenize = StringUtils.split(sentence,"-");
        List<String> tokens = Arrays.asList(sentence);

        //String[] tagArray = openNLPService.getPosTagger().tag(tokenize);
        List<String> tags = Arrays.asList(tagArray);
        String[] chunk = chunker.chunk(sentence, tagArray);

        List<String> chunks = Arrays.asList(chunk);

        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.size(); i++) {
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
        System.out.println("=========================================");

        lists.stream().forEach(sg -> {
            System.out.println(sg.getChunks().get(0).substring(2) + "\t-\t" +
                    sg.getTokens().stream().map(tokenTuple -> tokenTuple.getToken()).collect(Collectors.joining(" ")));

        });
        System.out.println("=========================================");
        log.info("\n");
        return lists;
    }
}
