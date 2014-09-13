package cec.cluster.types;

import javafx.util.Pair;

/**
 *
 * @author Krzysztof
 */
public class TypeOption extends Pair<String, Object> {

    public TypeOption(String key, Object value) {
        super(key, value);
    }

    public static TypeOption add(String key, Object value) {
        return new TypeOption(key, value);
    }
}
