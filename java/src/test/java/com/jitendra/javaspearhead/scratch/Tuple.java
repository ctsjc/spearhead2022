package com.jitendra.javaspearhead.scratch;

import lombok.Data;

@Data
class Tuple<T,J, K>{
    T t;
    J j;
    K k;

    static <T,J> Tuple of(T t, J j){

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


    static <T,J,K> Tuple of(T t, J j, K k){
        Tuple tuple = new Tuple();
        tuple.t=t;
        tuple.j=j;
        tuple.k  =k;
        return tuple;
    }


}