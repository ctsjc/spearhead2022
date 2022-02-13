package com.spearhead.agidoda.beans.chunks;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ToString
public class ChunkGroup {
    List<TokenTuple> tokens=new ArrayList<>();
    List<String> chunks=new ArrayList<>();

    public Tuple phrase(){
        String tokenPhrase = tokens.stream().map(TokenTuple::getToken).collect(Collectors.joining(" "));
        String posPhrase = tokens.stream().map(TokenTuple::getPos).collect(Collectors.joining("-"));

        return Tuple.of(tokenPhrase, posPhrase, findProminent());

    }


    String findProminent(){
        Optional<String> prominent = chunks.stream().map(s -> s.split("-")[1]).findFirst();
        return  prominent.orElse("Undetermined");
    }
}