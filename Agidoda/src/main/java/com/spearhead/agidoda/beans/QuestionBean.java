package com.spearhead.agidoda.beans;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class QuestionBean {
    String phrase;
    String question;
    String probability;

    public QuestionBean(String phrase, String question, String probability) {
        this.phrase = phrase;
        this.question = question;
        this.probability = probability;
    }
}
