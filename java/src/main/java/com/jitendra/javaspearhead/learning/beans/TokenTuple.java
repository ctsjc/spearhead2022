package com.jitendra.javaspearhead.learning.beans;

import lombok.Data;

@Data
public class TokenTuple{
    String token;
    String pos;
    String lemma;
    String chunk;

    public TokenTuple(String token, String pos) {
        this.token = token;
        this.pos = pos;
    }
}
