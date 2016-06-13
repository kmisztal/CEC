package cec.kolobokeCollections;

import com.koloboke.compile.KolobokeMap;
import com.koloboke.compile.NullKeyAllowed;

import java.util.Map;

/**
 * Created by mateusz on 13.06.2016.
 */
@KolobokeMap
@NullKeyAllowed
abstract class MyMap<K, V> implements Map<K, V> {
    public static <K, V> Map<K, V> withExpectedSize(int expectedSize) {
        return new KolobokeMyMap<K, V>(expectedSize);
    }
}
