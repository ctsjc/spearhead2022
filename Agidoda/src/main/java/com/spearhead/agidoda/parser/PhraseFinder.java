package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.beans.PhraseForm;
import com.spearhead.agidoda.beans.chunks.TokenTuple;
import com.spearhead.agidoda.engine.opennlp.OpenNLPService;
import com.spearhead.agidoda.utility.JsonPrinter;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhraseFinder {

    @Autowired
    OpenNLPService openNLPService;
    PhraseForm phraseForm;
    /**
     * input :: "With key global aviation rights in North America, Asia-Pacific, Europe, Middle East and Latin America, UAL has the world’s most comprehensive global route network. "
     * output :: PP-NP-PP-NP-O-NP-VP-NP
    * */
    public String convertToPhrasedSequence(String sentence) {
        phraseForm = getPhraseForm(sentence);
        return phraseForm.getPhraseSequence();
    }

    public PhraseForm getPhraseForm() {
        return phraseForm;
    }

    public String convertToNounPhrasedSequence(String sentence) {
        phraseForm = getPhraseForm(sentence);
        log.info("JJJ phraseForm {}",JsonPrinter.print(phraseForm));
        List<Map<String, String>> phrases = phraseForm.getPhrases();
        int i=0;
        String[] seq = phraseForm.getPhraseSequence().split("-");
        List<String> updatedSequence= new ArrayList<>();

        while(i < phrases.size()){
            Map<String, String> phraseMap = phrases.get(i);
            String rkey = (String) phraseMap.keySet().toArray()[0];
            if( !rkey.equals("NP")){
                 String value = phraseMap.get(rkey);
                 updatedSequence.add(value);
            }else{
                updatedSequence.add(seq[i]);
            }
            i++;
        }
        log.debug(phraseForm.getPhraseSequence());
        String upSequence=String.join("-", updatedSequence);
        log.debug(upSequence);
        return upSequence;
    }


    private PhraseForm getPhraseForm(String sentence) {
        List<ChunkGroup> chunks = toChunkGroupsV2(sentence);
        log.info("chunks : {}", JsonPrinter.print(chunks));
        PhraseForm phraseForm = new PhraseForm();
        phraseForm.setSentence(sentence);
        List<String> sequence = new ArrayList<>();

        int i=0;
        List<Map<String, String>> phrases=new ArrayList<>();
        while(i < chunks.size()){
            Map<String, String> map = new HashMap<>();
            ChunkGroup chunkGroup = chunks.get(i);
            Optional<String> key = Optional.of(chunkGroup.getChunks().get(0));
            if( key.isPresent()){
                String chunkType= key.get();// B-NP, I-NP, O, NP
                log.info("chunkGroup {}",chunkGroup);
                if (chunkType.contains("B-")) {
                    sequence.add(chunkType.split("-")[1]+""+i);
                } else if (chunkType.contains("O")) {
                    sequence.add(chunkType+""+i);
                }else{
                    sequence.add(chunkType+""+i);
                }
            }

            String phrase = chunkGroup.getTokens().stream().map(TokenTuple::getToken).collect(Collectors.joining(" "));
            map.put(key.get(), phrase);
            phrases.add(map);
            log.info("ChunkGroup : {}" +
                    "\nsequence : {}" +
                    "\n Phrase : {}" +
                            "\nMap : {}"+
                    "\nPhrases : {}",
                    JsonPrinter.print(chunkGroup), JsonPrinter.print(sequence), JsonPrinter.print(phrase)
                    , JsonPrinter.print(map)
                    , JsonPrinter.print(phrases));
            i++;
        }
        phraseForm.setPhraseSequence(String.join("-", sequence));
        phraseForm.setPhrases(phrases);
        phraseForm.join();
        return phraseForm;
    }


    public List<ChunkGroup> toChunkGroups(String sentence) {
        List<ChunkGroup> lists = new ArrayList<>();

        String[] tokenize = openNLPService.getTokenizer().tokenize(sentence);
        List<String> tokens = Arrays.asList(tokenize);

        String[] tagArray = openNLPService.getPosTagger().tag(tokenize);
        List<String> tags = Arrays.asList(tagArray);
        String[] chunk = openNLPService.getChunker().chunk(tokenize, tagArray);
        log.info("chunk :{}", chunk);
        log.info("chunkSpan :{}", openNLPService.getChunker().chunkAsSpans(tokenize, tagArray));
        List<String> chunks = Arrays.asList(chunk);

        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.size(); i++) {
            char firstChar = chunks.get(i).charAt(0);
            if (firstChar == 'B' || firstChar == 'O') {
                chunkGroup = new ChunkGroup();
                lists.add(chunkGroup);
                chunkGroup.getChunks().add(chunks.get(i));

                chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
            } else if (firstChar == 'I') {
                chunkGroup.getChunks().add(chunks.get(i));
                chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
            } else {
                chunkGroup.getChunks().add(chunks.get(i));
                chunkGroup.getTokens().add(new TokenTuple(tokens.get(i), tags.get(i)));
            }
        }
        return lists;
    }

    public List<ChunkGroup> toChunkGroupsV2(String sentence) {
        List<ChunkGroup> lists = new ArrayList<>();

        String[] tokenize = openNLPService.getTokenizer().tokenize(sentence);
        List<String> tokens = Arrays.asList(tokenize);

        String[] tagArray = openNLPService.getPosTagger().tag(tokenize);
        List<String> tags = Arrays.asList(tagArray);
        Span[] chunks =  openNLPService.getChunker().chunkAsSpans(tokenize, tagArray);
        log.info("chunkSpan :{}", openNLPService.getChunker().chunkAsSpans(tokenize, tagArray));

        ChunkGroup chunkGroup = new ChunkGroup();
        for (int i = 0; i < chunks.length; i++) {
            String type = chunks[i].getType();
            chunkGroup = new ChunkGroup();
            lists.add(chunkGroup);
            chunkGroup.getChunks().add(type);
            StringBuilder phrase=new StringBuilder();
            StringBuilder tag=new StringBuilder();
            for( int j= chunks[i].getStart(); j < chunks[i].getEnd();j++){
                phrase.append(tokens.get(j)).append(" ");
                tag.append(tags.get(j)).append(" ");
            }
            chunkGroup.getTokens().add(new TokenTuple(phrase.toString().trim(), tag.toString().trim()));
        }
        return lists;
    }


    public String find(String sequencePart){
        List<Map<String, String>> phrases = phraseForm.getPhrases();
        String[] seq = phraseForm.getPhraseSequence().split("-");
        int i=0;
        while(i < phrases.size()){
            if(sequencePart.equals(seq[i])){
                Map<String, String> phraseMap = phrases.get(i);
                String rkey = (String) phraseMap.keySet().toArray()[0];
                return phraseMap.get(rkey);
            }
            i++;
        }
        return "";
    }
}
