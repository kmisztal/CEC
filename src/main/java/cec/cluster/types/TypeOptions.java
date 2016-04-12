package cec.cluster.types;

import java.util.HashMap;

/**
 *
 * @author Krzysztof
 */
public class TypeOptions extends HashMap<String, Object> {

    public TypeOptions(TypeOption[] typeOption) {
        for(TypeOption to : typeOption){
            put(to.getKey(), to.getValue());
        }
    }
}
