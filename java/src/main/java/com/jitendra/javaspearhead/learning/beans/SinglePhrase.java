package com.jitendra.javaspearhead.learning.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor

/**
 * Whats the purpose of single phrase
 * multiword American reconnaissance satellite is converted to `American reconnaissance satellite `*/
public class SinglePhrase {
    String phrase;
    PhraseType type;
    String actualPhrase;
    String lemma;
    int position;

    public static SinglePhrase of(Tuple t){
        PhraseType phraseType=null;
        try {
            phraseType = PhraseType.valueOf(String.valueOf(t.getK()));
        }catch (RuntimeException e){
            phraseType = PhraseType.OTHER;
        }
        return new SinglePhrase()
            .withPhrase(String.valueOf(t.getT()))
            .withActualPhrase(String.valueOf(t.getT()))
            .withType(phraseType);
    }


}

