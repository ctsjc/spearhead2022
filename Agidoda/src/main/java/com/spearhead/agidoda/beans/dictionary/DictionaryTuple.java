package com.spearhead.agidoda.beans.dictionary;

import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class DictionaryTuple {
    String word;
    List<String> phrase;
}
