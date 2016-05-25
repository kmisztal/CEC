package cec.cluster.types;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Krzysztof
 */
public class TypeOptions extends LinkedHashMap<String, Object> {

    public TypeOptions(TypeOption[] typeOption) {
        for(TypeOption to : typeOption){
            put(to.getKey(), to.getValue());
        }
    }
}
