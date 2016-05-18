/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.options;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Krzysztof
 */
public class Options {
    private static Options singleton;

    /* Static 'instance' method */
    public static Options getInstance() {
        if (singleton == null) {
            singleton = new Options();
        }
        return singleton;
    }

    private final HashMap<String, HashMap<String,String>> dictionary;

    private Options() {
        dictionary = new HashMap<>();
    }
    
    public void addKey(String key){
        dictionary.put(key, new HashMap<>());
    }
    
    public void addValue(String key, String key1, String value){
        if(!dictionary.containsKey(key))
            addKey(key);
        dictionary.get(key).put(key1, value);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");

        for(Map.Entry<String, HashMap<String,String>> e : dictionary.entrySet())
        {
            ret.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return ret.toString();
    }
    
    
}
