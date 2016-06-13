package cec.kolobokeCollections;

import com.koloboke.collect.map.hash.HashObjObjMap;
import com.koloboke.compile.KolobokeMap;

/**
 * Created by mateusz on 13.06.2016.
 */
@KolobokeMap
abstract class ExtendedMap<K,V> implements HashObjObjMap<K, V> {
    static <K,V> ExtendedMap<K,V> withExpectedSize(int expectedSize) {
        return new KolobokeExtendedMap<K,V>(expectedSize);
    }
}
