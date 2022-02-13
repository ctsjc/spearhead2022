package com.spearhead.agidoda.beans;

import lombok.Data;
import lombok.ToString;

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

    @Override
    public String toString() {
        return "\nQuestionBean{" +
                "\nphrase='" + phrase + '\'' +
                "\tquestion='" + question + '\'' +
                "\tprobability='" + probability + '\'' +
                '}';
    }
}
