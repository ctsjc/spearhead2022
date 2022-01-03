package com.jitendra.javaspearhead.scratch;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;
@Slf4j
public class ScratchBook {
    @Test
    void test() throws IOException {
        String sentence= "American reconnaissance satellites have reportedly spotted Chinese ships suspected of selling oil to North Korean vessels about 30 times since October.";

        TokenizerModel tokenizerModel=new TokenizerModel(getClass().getResourceAsStream("/models/en-token.bin"));
        TokenizerME tokenizerME = new TokenizerME(tokenizerModel);

        String[] token2 = tokenizerME.tokenize(sentence);
        extracted(token2);

        POSModel posModel = new POSModel(getClass().getResourceAsStream("/models/en-pos-maxent.bin"));
        POSTaggerME posTagger = new POSTaggerME(posModel);
        String[] posTagged = posTagger.tag(token2);


        for (int i = 0; i < token2.length; i++) {
            log.info(token2[i]+" - "+posTagged[i]);
        }
        log.info("Jay Shree Ram");
    }

    private void extracted(String[] token2) {
        Stream.of(token2).forEach(token ->{
            log.info(token);
        });
    }
}
