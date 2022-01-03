package com.jitendra.javaspearhead.learning.beans;

import lombok.Data;

@Data
public class Tuple<T,J, K, L>{
    T t;
    J j;
    K k;
    L l;
    public static <T,J> Tuple of(T t, J j){

        Tuple tuple = new Tuple();
        tuple.t=t;
        tuple.j=j;
        return tuple;
    }

    @Override
    public String toString() {

        return "" + t +
                "\t(" + j+")"+
                "\t(" + k+")";

    }


    public static <T,J,K> Tuple of(T t, J j, K k){
        Tuple tuple = new Tuple();
        tuple.t=t;
        tuple.j=j;
        tuple.k  =k;
        return tuple;
    }


    public static String nounSquzeer(){
        String singleWord="";
        // if NP has adj remove it,
        // if NP has group of word which is term, them squzee it.
        // example  American reconnaissance satellites
        // American is ADJ -> Romove it
        // reconnaissance satellites -> https://en.wikipedia.org/wiki/Reconnaissance_satellite

        return  singleWord;
    }

}