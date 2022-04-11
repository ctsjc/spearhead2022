package com.spearhead.agidoda.beans;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommonBag {
    String string;
    List<Map<String, String>> mapList;
    Map<String, String> map;
    List<Object> objects;
    Map<String, String> dictionaryWord2SentenceMapping;
    public CommonBag() {
        map = new HashMap<>();
        mapList = new ArrayList<>();
        mapList.add(new HashMap<>());
        objects = new ArrayList<>();
    }

    public void addToObjects(Object object) {
        objects.add(object);
    }

    public void addToMap(String key, String value) {
        map.put(key, value);
    }

    public void addToListMap(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        mapList.add(map);
    }

    @Override
    public String toString() {
        return "CommonBag{" +
                "\nString1:{'" + string + '\'' +
                "}\nMListMap:{" + mapList +
                "}\nMap:{" + printMap() +
                "}\nObjects:{" + objects +
                '}';
    }

    String printMap() {
        StringBuilder line = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            line.append("\n").append(entry.getKey()).append(":").append(StringUtils.defaultIfBlank(String.valueOf(entry.getValue()), "NOT-PRESENT"));
        }
        return line.toString();
    }
}
